package com.ensaitechnomobile.meteolocale;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ensai.appli.R;
import com.ensaitechnomobile.metier.EtatMeteo;
import com.ensaitechnomobile.metier.Localite;

public class MeteoPrincipal extends Activity implements LocationListener {

	protected static final String TAG = "AMS::Meteo";
	protected static final String APIID = "ef5e65bcdadbcc86a991779742664324";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meteo_principale);

		// TODO localisation

		String urlMeteo = urlPreparerMeteo(APIID, new Localite("Bruz"), 0, 0, false);
		syncMeteo(urlMeteo, this.getBaseContext());

	}

	/**
	 * 
	 * 
	 * 
	 * @param apid
	 *            : l'APPID de OpenWeatherMap
	 * @param loc
	 *            : la ville pour laquelle on veut la m�t�o
	 * @param coordX
	 *            : la latitude pour laquelle on veut la m�t�o
	 * @param coordY
	 *            : la longitude pour laquelle on veut la m�t�o
	 * @param previsions
	 *            : si l'on veut des pr�visions (ou implicitement la m�t�o du
	 *            jour m�me
	 * 
	 * @return
	 */

	String urlPreparerMeteo(String apid, Localite loc, int coordX, int coordY,
			boolean previsions) {
		String res = "http://api.openweathermap.org/data/2.5/";
		if (previsions) {
			res += "forecast/Daily";
		} else {
			res += "weather";
		}

		if (loc.hasVille()) {
			res += "?q=" + loc.getVille();
		} else {
			if (loc.hasLongLat()) {
				res += "?lon=" + loc.getLongitude() + "&lat="
						+ loc.getLatitude();
			} else {
				Log.w(TAG, "Ca va planter car localit� incorrecte");
			}
		}

		res += "&units=metric";
		res += "&mode=json";
		res += "&APPID=" + apid;
		Log.v("AMS::Meteo", "URL de r�cup�ration des donn�es m�t�o : " + res);
		return res;

	}

	/**
	 * Thread parall�le qui ajoute des donn�es quelquepart .. � partir des infos
	 * sur le web
	 * 
	 * Input : url : l'URL a appeler qui devrait Exemple d'URL :
	 * http://api.openweathermap.org/data/2.5/weather?q=Bruz,fr&units=metric
	 * TODO faire une fonction interm�diaire pour pouvoir saisir la ville
	 */
	private void syncMeteo(final String urlString, final Context ctx) {
		// Get all fields to be updated

		final Handler handler = new Handler();
		Runnable code = new Runnable() {
			URL url = null;

			public void run() {
				try {
					// On r�cup�re le JSON a partir de l'URL
					url = new URL(urlString);
					HttpURLConnection urlConnection;
					urlConnection = (HttpURLConnection) url.openConnection();
					BufferedInputStream in = new BufferedInputStream(
							urlConnection.getInputStream());
					String input = readStream(in);
					JSONObject json = new JSONObject(input);
					Log.i(TAG, input);
					// On transforme en m�t�o
					MeteoJSON mjson = new MeteoJSON();
					EtatMeteo em = mjson.construireEtatMeteoActuel(json);
					Log.i(TAG, em.toString());

					ajouterDansPreferences(em, ctx);

					runOnUiThread(new Runnable() {
						public void run() {
							actualiserMeteoPreferences(ctx);
						}
					});

					// TODO me d�brouiller pour maj

				} catch (MalformedURLException e) {
					Log.e(TAG, "URL malform�e");
					e.printStackTrace();
				} catch (IOException e) {
					Log.e(TAG, "Exception d'E/S");
					e.printStackTrace();
				} catch (JSONException e) {
					Log.e(TAG, "Exception JSON");
					e.printStackTrace();
				}
			}
		};
		new Thread(code).start();
	}

	/**
	 * Permet d'ajouter un �l�ment m�teo dans les pr�fe�rences
	 * 
	 * @param cxt
	 *            un contexte (Activity.getBaseContext)
	 * @param em
	 *            un �tat m�t�o
	 * 
	 */
	void ajouterDansPreferences(EtatMeteo em, Context cxt) {
		// On va chercher un objet pr�f�rences � �diter
		SharedPreferences preferences = cxt.getSharedPreferences("MeteoLocal",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		// On ajoute les valeurs
		Log.i(TAG, "Dans ADP : " + em.getLoc().toString());
		editor.putString("localite", em.getLoc().getVille());
		editor.putInt("tempMax", (int) em.getTempMax());
		editor.putInt("tempMin", (int) em.getTempMin());
		editor.putInt("rain", (int) em.getRain());
		editor.putInt("wind", (int) em.getWindSpeed());
		editor.putInt("clouds", (int) em.getClouds());
		// On committe les pr�f�rences
		editor.commit();

	}

	/**
	 * Lancez moi sur un thread UI !! R�cup�re la m�t�o stock�e dans les
	 * pr�f�rences
	 * 
	 * 
	 * 
	 */
	void actualiserMeteoPreferences(Context cxt) {

		SharedPreferences prefs = cxt.getSharedPreferences("MeteoLocal",
				Context.MODE_PRIVATE);
		// On va chercher les textbox
		TextView txt_loc, txt_temperature, txt_pluie, txt_vent, txt_nuages;
		txt_loc = (TextView) findViewById(R.id.afficher_localite_meteo);
		txt_temperature = (TextView) findViewById(R.id.info_temp);
		txt_pluie = (TextView) findViewById(R.id.info_pluie);
		txt_vent = (TextView) findViewById(R.id.info_vent);
		txt_nuages = (TextView) findViewById(R.id.info_nuages);
		// On les renseigne
		txt_loc.setText(" " + prefs.getString("localite", "Prefs Pas de loc"));
		txt_temperature.setText("Entre " + prefs.getInt("tempMin", -100)
				+ " et " + prefs.getInt("tempMax", -100) + " C");
		txt_pluie.setText(prefs.getInt("rain", -100) + " mm d'ici 3h");
		txt_vent.setText(prefs.getInt("wind", -100) * 3.6 + " km/h");
		txt_nuages.setText(prefs.getInt("clouds", -100) + "%");

	}

	/**
	 * Lit un flux de donn�es et retourne le string correspondant
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public String readStream(InputStream inputStream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));
		String ligne = null;
		String contenu = "";
		while ((ligne = reader.readLine()) != null) {
			contenu += ligne;
		}
		return contenu;
	}

	/**
	 * 
	 * Cette m�thode retourne la localisation de l'appareil.
	 * 
	 */
	public Localite recupererLocalisationAppareil(View v) {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);

		Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		double locLat = 0;
		double locLong = 0;
		if (loc != null) {
			locLat = loc.getLatitude();
			locLong = loc.getLongitude();
		}

		Localite local = new Localite(locLat, locLong);
		Log.i(TAG, "Localisation lue avec succ�s : " + local.toString());

		String urlMeteo = urlPreparerMeteo(APIID, local, 0, 0, false);
		syncMeteo(urlMeteo, this.getBaseContext());

		return local;

	}

	/**
	 * 
	 * 
	 * Impl�mentation du LocationListener Port�e: 3 m�thodes suivantes Mot
	 * d'ordre : on s'en fiche
	 * 
	 */
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.d("Latitude", "disable");
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.d("Latitude", "enable");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.d("Latitude", "status");
	}

}

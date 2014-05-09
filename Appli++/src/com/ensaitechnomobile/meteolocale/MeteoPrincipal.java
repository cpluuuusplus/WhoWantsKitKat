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

import com.ensai.appli.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ensaitechnomobile.metier.CityNotFoundException;
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

		String urlMeteo = urlPreparerMeteo(APIID, new Localite("Eindhoven"), 0,
				0, false);
		syncMeteo(urlMeteo, this.getBaseContext());

	}

	/**
	 * Permet d'ajouter un élément méteo dans les préfeérences
	 * 
	 * @param cxt
	 *            un contexte (Activity.getBaseContext)
	 * @param em
	 *            un état météo
	 * 
	 */
	void ajouterDansPreferences(EtatMeteo em, Context cxt) {
		// On va chercher un objet préférences à éditer
		SharedPreferences preferences = cxt.getSharedPreferences("MeteoLocal",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		// On ajoute les valeurs
		Log.i(TAG, "Dans ADP : " + em.getLoc().toString());
		editor.putString("localite", em.getLoc().getVille());
		editor.putInt("tempMax", (int) em.getTempMax());
		editor.putInt("tempMin", (int) em.getTempMin());
		if (em.getRain3() != 0.0) {
			editor.putInt("rain3", (int) (1000 * em.getRain3()));
		} else {
			editor.putInt("rain3", 0);
		}
		if (em.getRain1() != 0.0) {
			editor.putInt("rain1", (int) (1000 * em.getRain1()));
		} else {
			editor.putInt("rain1", 0);

		}
		editor.putInt("wind", (int) em.getWindSpeed());
		editor.putInt("clouds", (int) em.getClouds());
		// On committe les préférences
		editor.commit();

	}

	/**
	 * Lancez moi sur un thread UI !! Récupère la météo stockée dans les
	 * préférences
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
		if (prefs.getInt("rain3", 0) != 0) {
			// Il y a de la pluie à 3h
			txt_pluie.setText(prefs.getInt("rain3", -100) / 1000
					+ "mm de pluie dans les 3h");

		} else {
			if (prefs.getInt("rain1", 0) != 0) {
				// Il y a de la pluie a 1h
				txt_pluie.setText(prefs.getInt("rain1", -100) / 1000
						+ "mm de pluie dans l'heure");

			} else {
				txt_pluie.setText(0 + " mm");

			}

		}
		txt_vent.setText(prefs.getInt("wind", -100) * 3.6 + " km/h");
		txt_nuages.setText(prefs.getInt("clouds", -100) + "%");

	}

	/**
	 * Lit un flux de données et retourne le string correspondant
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
	 * Associé à un bouton Cette méthode récupère la localité
	 * 
	 */
	public void recupererLocalisationAppareil(View v) {
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
		Log.i(TAG, "Localisation lue avec succès : " + local.toString());

		recupererMeteoActuelleParLocalite(local);

	}

	/**
	 * associé à un bouton Cette méthode retourne la localité saisie (ville)
	 * 
	 * 
	 */

	public void validerChoixLocalisationSaisi(View v) {
		EditText et = (EditText) findViewById(R.id.meteo_saisie_localite);
		String villeSaisie = et.getText().toString();
		recupererMeteoActuelleParLocalite(new Localite(villeSaisie));

	}

	/**
	 * Recupère la météo dans la localité saisie et met à jour les barres
	 * 
	 * 
	 */
	public void recupererMeteoActuelleParLocalite(Localite loc) {
		String urlMeteo = urlPreparerMeteo(APIID, loc, 0, 0, false);
		syncMeteo(urlMeteo, this.getBaseContext());
	}

	/**
	 * Thread parallèle qui ajoute des données quelquepart .. à partir des infos
	 * sur le web
	 * 
	 * Input : url : l'URL a appeler qui devrait Exemple d'URL :
	 * http://api.openweathermap.org/data/2.5/weather?q=Bruz,fr&units=metric
	 * TODO faire une fonction intermédiaire pour pouvoir saisir la ville
	 */
	private void syncMeteo(final String urlString, final Context ctx) {
		// Get all fields to be updated

		Runnable code = new Runnable() {
			URL url = null;

			public void run() {
				try {
					// On récupère le JSON a partir de l'URL
					url = new URL(urlString);
					HttpURLConnection urlConnection;
					urlConnection = (HttpURLConnection) url.openConnection();
					BufferedInputStream in = new BufferedInputStream(
							urlConnection.getInputStream());
					String input = readStream(in);
					JSONObject json = new JSONObject(input);
					Log.i(TAG, input);
					// On transforme en météo
					MeteoJSON mjson = new MeteoJSON();
					EtatMeteo em = mjson.construireEtatMeteoActuel(json);
					Log.i(TAG, em.toString());

					ajouterDansPreferences(em, ctx);

					runOnUiThread(new Runnable() {
						public void run() {
							actualiserMeteoPreferences(ctx);
						}
					});

					// TODO me débrouiller pour maj

				} catch (MalformedURLException e) {
					Log.e(TAG, "URL malformée");
					e.printStackTrace();
				} catch (IOException e) {
					Log.e(TAG, "Exception d'E/S");
					e.printStackTrace();
				} catch (JSONException e) {
					Log.e(TAG, "Exception JSON");
					e.printStackTrace();
				} catch (CityNotFoundException e) {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(
									ctx,
									"La ville est invalide, veuillez saisir une ville valide",
									Toast.LENGTH_LONG).show();
						}
					});
				}
			}
		};
		new Thread(code).start();
	}

	protected void afficherMessageErreurVille() {
		// Afficher un toast d'erreur

	}

	/**
	 * 
	 * 
	 * 
	 * @param apid
	 *            : l'APPID de OpenWeatherMap
	 * @param loc
	 *            : la ville pour laquelle on veut la météo
	 * @param coordX
	 *            : la latitude pour laquelle on veut la météo
	 * @param coordY
	 *            : la longitude pour laquelle on veut la météo
	 * @param previsions
	 *            : si l'on veut des prévisions (ou implicitement la météo du
	 *            jour même
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
				Log.w(TAG, "Ca va planter car localité incorrecte");
			}
		}

		res += "&units=metric";
		res += "&mode=json";
		res += "&APPID=" + apid;
		Log.v("AMS::Meteo", "URL de récupération des données météo : " + res);
		return res;

	}

	/**
	 * 
	 * 
	 * Implémentation du LocationListener Portée: 3 méthodes suivantes Mot
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

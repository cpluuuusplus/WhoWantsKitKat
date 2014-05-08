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

import com.ensaitechnomobile.metier.EtatMeteo;
import com.ensaitechnomobile.metier.Localite;
import com.example.pamplemousse.R;

public class MeteoPrincipal extends Activity implements LocationListener {

	protected static final String TAG = "AMS::Meteo";
	protected static final String APIID = "ef5e65bcdadbcc86a991779742664324";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meteo_principale);

		// TODO localisation

		String urlMeteo = urlPreparerMeteo(APIID, "Bruz", 0, 0, false);
		syncMeteo(urlMeteo);

	}

	/**
	 * 
	 * 
	 * 
	 * @param apid
	 *            : l'APPID de OpenWeatherMap
	 * @param ville
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

	String urlPreparerMeteo(String apid, String ville, int coordX, int coordY,
			boolean previsions) {
		String res = "http://api.openweathermap.org/data/2.5/";
		if (previsions) {
			res += "forecast/Daily";
		} else {
			res += "weather";
		}
		res += "?q=" + ville;
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
	private void syncMeteo(final String urlString) {
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
					final EtatMeteo em = mjson.construireEtatMeteoActuel(json);
					Log.i(TAG, em.toString());
					
					// On va chercher
					TextView txt_loc, txt_temperature, txt_pluie, txt_vent, txt_nuages;
					txt_loc = (TextView) findViewById(R.id.afficher_localite_meteo);
					txt_temperature = (TextView) findViewById(R.id.info_temp);
					txt_pluie = (TextView) findViewById(R.id.info_pluie);
					txt_vent = (TextView) findViewById(R.id.info_vent);
					txt_nuages = (TextView) findViewById(R.id.info_nuages);
					
						// TODO me d�brouiller pour maj 
					
					txt_loc.setText(em.getTempMax()+"");

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
	 * Lire un flux de donn�es
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
		return local;
	}

	// Impl�mentation du menu

	// M�thode qui se d�clenchera lorsque vous appuierez sur le bouton menu du
	// t�l�phone
	public boolean onCreateOptionsMenu(Menu menu) {

		// Cr�ation d'un MenuInflater qui va permettre d'instancier un Menu XML
		// en un objet Menu
		MenuInflater inflater = getMenuInflater();
		// Instanciation du menu XML sp�cifier en un objet Menu
		inflater.inflate(R.layout.action_menu, menu);

		// Il n'est pas possible de modifier l'ic�ne d'ent�te du sous-menu via
		// le fichier XML on le fait donc en JAVA
		// menu.getItem(0).getSubMenu().setHeaderIcon(R.drawable.option_white);

		return true;
	}

	// M�thode qui se d�clenchera au clic sur un item
	public boolean onOptionsItemSelected(MenuItem item) {
		// On regarde quel item a �t� cliqu� gr�ce � son id et on d�clenche une
		// action
		if (item.getItemId() == R.id.action_settings)
			return true;
		if (item.getItemId() == R.id.action_ret) {
			// Pour fermer l'application il suffit de faire finish()
			finish();
			return true;
		} else
			return false;
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

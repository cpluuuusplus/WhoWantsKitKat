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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
		Log.v("AMS::Meteo", "URL de récupération des données météo : " + res);
		return res;

	}

	/**
	 * Thread parallèle qui ajoute des données quelquepart .. à partir des infos
	 * sur le web
	 * 
	 * Input : url : l'URL a appeler qui devrait Exemple d'URL :
	 * http://api.openweathermap.org/data/2.5/weather?q=Bruz,fr&units=metric
	 * TODO faire une fonction intermédiaire pour pouvoir saisir la ville
	 */
	private void syncMeteo(final String urlString) {
		Runnable code = new Runnable() {
			URL url = null;

			public void run() {
				try {
					url = new URL(urlString);
					HttpURLConnection urlConnection;
					urlConnection = (HttpURLConnection) url.openConnection();
					BufferedInputStream in = new BufferedInputStream(
							urlConnection.getInputStream());
					String input = readStream(in);
					JSONObject json = new JSONObject(input);
					Log.i(TAG, input);

					MeteoJSON mjson = new MeteoJSON();
					EtatMeteo em = mjson.construireEtatMeteoActuel(json);
					Log.i(TAG, em.toString());

				} catch (MalformedURLException e) {
					Log.e(TAG, "URL malformée");
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
	 * Lire un flux de données
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
	 * Cette méthode retourne la localisation de l'appareil.
	 * 
	 */
	public Localite recupererLocalisationAppareil(View v) {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				5000, 10, this);
		
		
		Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		double locLat=0;
		double locLong=0;
		if(loc != null){
			locLat = loc.getLatitude();
			locLong= loc.getLongitude();
		}
		
		Localite local = new Localite(locLat, locLong);
		Log.i(TAG, "Localisation lue avec succès : " + local.toString() );
		return local;
	}

	// Implémentation du menu

	// Méthode qui se déclenchera lorsque vous appuierez sur le bouton menu du
	// téléphone
	public boolean onCreateOptionsMenu(Menu menu) {

		// Création d'un MenuInflater qui va permettre d'instancier un Menu XML
		// en un objet Menu
		MenuInflater inflater = getMenuInflater();
		// Instanciation du menu XML spécifier en un objet Menu
		inflater.inflate(R.layout.menu, menu);

		// Il n'est pas possible de modifier l'icône d'entête du sous-menu via
		// le fichier XML on le fait donc en JAVA
		// menu.getItem(0).getSubMenu().setHeaderIcon(R.drawable.option_white);

		return true;
	}

	// Méthode qui se déclenchera au clic sur un item
	public boolean onOptionsItemSelected(MenuItem item) {
		// On regarde quel item a été cliqué grâce à son id et on déclenche une
		// action
		switch (item.getItemId()) {
		case R.id.option:
			Toast.makeText(this, "Option", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.favoris:
			Toast.makeText(this, "Favoris", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.stats:
			Toast.makeText(this, "Stats", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.quitter:
			// Pour fermer l'application il suffit de faire finish()
			finish();
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 
	 * Implémentation du LocationListener
	 * Portée: 3 méthodes suivantes
	 * Mot d'ordre : on s'en fiche
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

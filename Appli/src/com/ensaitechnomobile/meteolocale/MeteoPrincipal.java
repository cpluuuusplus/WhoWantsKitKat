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
import android.os.Bundle;
import android.util.Log;

import com.example.pamplemousse.R;

public class MeteoPrincipal extends Activity {
	
	protected static final String TAG = "AMS::Meteo";



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meteo_principale);

		
		String meteoURL = urlPreparerMeteo("ef5e65bcdadbcc86a991779742664324", "bruz", 0, 0, false);
		syncMeteo(meteoURL);
		
		
		
		
	}
	/**
	 * Thread parallèle qui ajoute des données quelquepart .. à partir des infos sur
	 * le web
	 * 
	 * Input :
	 * 	url : l'URL a appeler qui devrait
	 * 		Exemple d'URL : http://api.openweathermap.org/data/2.5/weather?q=Bruz,fr&units=metric
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
					//Log.i(TAG, input);
					
					
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
	 * 
	 * 
	 * @param apid : l'APPID de OpenWeatherMap
	 * @param ville : la ville pour laquelle on veut la météo
	 * @param coordX : la latitude pour laquelle on veut la météo
	 * @param coordY : la longitude pour laquelle on veut la météo
	 * @param previsions: si l'on veut des prévisions (ou implicitement la météo du jour même
	 * 
	 * @return
	 */
	
	String urlPreparerMeteo(String apid, String ville, int coordX, int coordY, boolean previsions ){
		String res = "http://api.openweathermap.org/data/2.5/weather";
		res +="?q="+ville+",fr";
		res +="&units=metric";
		res +="&?APPID="+apid;
		Log.v("AMS::Meteo", "URL de récupération des données météo : "+res);
		return res;
	}
	
}

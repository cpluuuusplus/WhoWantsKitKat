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

import com.example.pamplemousse.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MeteoPrincipal extends Activity {
	
	protected static final String TAG = "AMS::Meteo";



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meteo_principale);
		
		// TODO : cr�er probablement une classe pour r�cup�rer les donn�es de l'API
		syncMeteo("http://api.openweathermap.org/data/2.5/weather?q=Bruz,fr&units=metric");
		
		
		
		
	}
	/**
	 * Thread parall�le qui ajoute des donn�es quelquepart .. � partir des infos sur
	 * le web
	 * 
	 * Input :
	 * 	url : l'URL a appeler qui devrait
	 * 		Exemple d'URL : http://api.openweathermap.org/data/2.5/weather?q=Bruz,fr&units=metric
	 * 		TODO faire une fonction interm�diaire pour pouvoir saisir la ville
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
					
					//TODO utiliser json
					
					
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
	
}

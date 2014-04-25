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

import com.ensaitechnomobile.menuprincipal.MenuPrincipal;
import com.example.pamplemousse.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

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
	
	// Impl�mentation du menu

	// M�thode qui se d�clenchera lorsque vous appuierez sur le bouton menu du
	// t�l�phone
	public boolean onCreateOptionsMenu(Menu menu) {

		// Cr�ation d'un MenuInflater qui va permettre d'instancier un Menu XML
		// en un objet Menu
		MenuInflater inflater = getMenuInflater();
		// Instanciation du menu XML sp�cifier en un objet Menu
		inflater.inflate(R.layout.menu, menu);

		// Il n'est pas possible de modifier l'ic�ne d'ent�te du sous-menu via
		// le fichier XML on le fait donc en JAVA
		// menu.getItem(0).getSubMenu().setHeaderIcon(R.drawable.option_white);

		return true;
	}

	// M�thode qui se d�clenchera au clic sur un item
	public boolean onOptionsItemSelected(MenuItem item) {
		// On regarde quel item a �t� cliqu� gr�ce � son id et on d�clenche une
		// action
		switch (item.getItemId()) {
		case R.id.option:
			Toast.makeText(this, "Option", Toast.LENGTH_SHORT)
					.show();
			return true;
		case R.id.favoris:
			Toast.makeText(this, "Favoris", Toast.LENGTH_SHORT)
					.show();
			return true;
		case R.id.stats:
			Toast.makeText(this, "Stats", Toast.LENGTH_SHORT)
					.show();
			return true;
		case R.id.quitter:
			// Pour fermer l'application il suffit de faire finish()
			finish();
			return true;
		}
		return false;
	}
	
	
	
}

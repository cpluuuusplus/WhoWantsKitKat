package com.ensaitechnomobile.pamplemousse;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ensaitechnomobile.DAO.CoursDAO;
import com.ensaitechnomobile.SQL.MyOpenHelper;
import com.ensaitechnomobile.metier.Cours;
import com.example.pamplemousse.R;

public class PamplemousseViewer extends Activity {

	public static final String TAG = "MultiService";
	public String baseUrl;
	private String id, pass;
	private CoursDAO cdao = new CoursDAO();
	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			"EEEE, dd MMMM yyyy 'à' HH:mm", Locale.FRENCH);

	public ArrayAdapter<Cours> adapter;
	private ProgressDialog progressDialog;

	// Création de la ArrayList qui nous permettra de remplire la listView
	ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
	ListView listeView;
	JSONArray table = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pamplemousse_viewer);
		progressDialog = new ProgressDialog(this);
		show();
	}

	/**
	 * Methode permettant de mettre a jour les donnees a la suite d'un click
	 * 
	 * @param v
	 *            vue cliquee
	 */
	public void onClickMaj(View v) {
		sync();
	}

	/**
	 * Thread parallèle qui ajoute des données à la table à partir des infos sur
	 * le web
	 */
	private void sync() {
		// mise a jour de l'URL
		majURL();

		progressDialog.setMessage("Chargement en cours");
		progressDialog.setTitle("Importation des données");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setMax(10);
		progressDialog.show();
		// lancement d'un noueau thread
		Runnable code = new Runnable() {
			URL url = null;

			public void run() {
				try {
					url = new URL(baseUrl);
					HttpURLConnection urlConnection;
					urlConnection = (HttpURLConnection) url.openConnection();
					BufferedInputStream in = new BufferedInputStream(
							urlConnection.getInputStream());
					String input = readStream(in);
					JSONObject json = new JSONObject(input);
					Log.i(TAG, input);
					table = json.getJSONArray("events");
					if (table != null && table.length() > 0) {
						SQLiteOpenHelper helper = new MyOpenHelper(
								PamplemousseViewer.this);
						SQLiteDatabase db = helper.getWritableDatabase();
						removeAll(db);
						ArrayList<Cours> lsCours = coursToArray(table);
						cdao.insertAll(db, lsCours);

						// Affichage d'un toast pour dire que les donnees ont
						// ete mises a jour
						runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(
										PamplemousseViewer.this,
										getString(R.string.nb_cours,
												table.length()),
										Toast.LENGTH_LONG).show();
								show();
							}
						});
						db.close();
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				progressDialog.dismiss();
			}
		};
		new Thread(code).start();
	}

	/**
	 * Methode permettant de mettre a jour l'URL
	 */
	private void majURL() {
		baseUrl = "http://chessdiags.com/pamplemousse";
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		id = preferences.getString("login", "");
		pass = preferences.getString("password", "");
		if (id.length() == 6 && pass.length() > 0) {
			baseUrl += "?login=" + id;
			baseUrl += "&mdp=" + pass;
		}
		// Toast.makeText(this, baseUrl, Toast.LENGTH_LONG).show();
	}

	private void removeAll(SQLiteDatabase db) {
		cdao.removeAll(db);
	}

	/**
	 * Methode permettant d'afficher l'emploi du temps
	 * 
	 * @param v
	 */
	public void onClickShow(View v) {
		show();
	}

	private void show() {
		SQLiteOpenHelper helper = new MyOpenHelper(this);
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<Cours> lsCours = cdao.getAll(db);

		// *********************
		// arrayAdapter
		// Création d'un SimpleAdapter qui se chargera de mettre les items
		// présent dans notre list (listItem) dans la vue affichageitem
		SimpleAdapter mSchedule = new SimpleAdapter(this, mapping(lsCours),
				R.layout.affichage_matiere, new String[] { "debut", "fin",
						"salle", "nom" }, new int[] { R.id.debut, R.id.fin,
						R.id.salle, R.id.nom });
		listeView = (ListView) findViewById(R.id.planning);

		listeView.setAdapter(mSchedule);
		db.close();
	}

	/**
	 * Méthode permettant de récuperer un tableau de cours à partir d'un
	 * JSONArray
	 * 
	 * @param table
	 * @throws JSONException
	 */
	private ArrayList<Cours> coursToArray(JSONArray table) throws JSONException {
		ArrayList<Cours> listeCours = new ArrayList<Cours>();
		for (int i = 0; i < table.length(); i++) {
			JSONObject cours = table.getJSONObject(i);
			String nom = cours.getString("nom");
			String salle = cours.getString("salle");
			String uid = cours.getString("uid");
			long debut = cours.getLong("debut");
			long fin = cours.getLong("fin");
			listeCours.add(new Cours(debut, fin, nom, salle, uid));
		}
		return listeCours;
	}

	/**
	 * Méthode permettant l'affichage des cours par le layout afficher_matiere à
	 * partir d'un arraylist de cours
	 * 
	 * @param ls
	 *            arraylist contenant les matières
	 * @return
	 */
	public ArrayList<HashMap<String, String>> mapping(ArrayList<Cours> ls) {
		ArrayList<HashMap<String, String>> res = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map;
		for (Cours cours : ls) {
			map = new HashMap<String, String>();
			map.put("salle", cours.getSalle());
			map.put("fin",
					dateFormat.format((new java.util.Date(cours.getFin()))));
			map.put("debut",
					dateFormat.format((new java.util.Date(cours.getDebut()))));
			map.put("nom", cours.getNom());
			res.add(map);
		}
		return res;
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

	// Implémentation du menu

	/**
	 * Méthode qui se déclenchera lorsque vous appuierez sur le bouton menu du
	 * téléphone
	 */
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.action_menu, menu);
		return super.onCreateOptionsMenu(menu);

	}

	/**
	 * 
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		// On regarde quel item a été cliqué grâce à son id et on déclenche une
		// action
		switch (item.getItemId()) {
		case R.id.action_settings:
			Toast.makeText(this, "Stats", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.action_quit:
			// Pour fermer l'application il suffit de faire finish()
			finish();
			return true;
		}
		return false;
	}

}

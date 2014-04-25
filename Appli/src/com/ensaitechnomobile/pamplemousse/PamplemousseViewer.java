package com.ensaitechnomobile.pamplemousse;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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

	public ArrayAdapter<Cours> adapter;
	// Cr�ation de la ArrayList qui nous permettra de remplire la listView
	ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
	ListView listeView;
	JSONArray table = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pamplemousse_viewer);
		sync();
	}

	/**
	 * Thread parall�le qui ajoute des donn�es � la table � partir des infos sur
	 * le web
	 */
	private void sync() {
		baseUrl = "http://chessdiags.com/pamplemousse";
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		id = preferences.getString("login", "");
		baseUrl += "?login=" + id;
		pass = preferences.getString("password", "");
		baseUrl += "&mdp=" + pass;
		Toast.makeText(this, baseUrl, Toast.LENGTH_LONG).show();

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
			}
		};
		new Thread(code).start();
	}

	/**
	 * Ins�rer les donn�es dans la table
	 * 
	 * @param ls
	 * @throws JSONException
	 */
	public void maj(View v) throws JSONException {
		sync();
		if (table != null && table.length() > 0) {
			CoursDAO cdao = new CoursDAO();
			SQLiteOpenHelper helper = new MyOpenHelper(this);
			SQLiteDatabase db = helper.getWritableDatabase();
			removeAll(db);
			ArrayList<Cours> lsCours = coursToArray(table);
			cdao.insertAll(db, lsCours);
			Toast.makeText(this, getString(R.string.nb_cours, table.length()),
					Toast.LENGTH_LONG).show();
			db.close();
		}
	}

	private void removeAll(SQLiteDatabase db) {
		db.rawQuery("delete from ?", new String[] { MyOpenHelper.NOMBASE });
	}

	/**
	 * Methode permettant d'afficher l'emploi du temps
	 * 
	 * @param v
	 */
	public void show(View v) {
		CoursDAO cdao = new CoursDAO();
		SQLiteOpenHelper helper = new MyOpenHelper(this);
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<Cours> lsCours = cdao.getAll(db);

		// *********************
		// arrayAdapter
		// Cr�ation d'un SimpleAdapter qui se chargera de mettre les items
		// pr�sent dans notre list (listItem) dans la vue affichageitem
		SimpleAdapter mSchedule = new SimpleAdapter(this, mapping(lsCours),
				R.layout.affichage_matiere, new String[] { "debut", "fin",
						"salle", "nom" }, new int[] { R.id.debut, R.id.fin,
						R.id.salle, R.id.nom });

		listeView = (ListView) findViewById(R.id.planning);

		listeView.setAdapter(mSchedule);
		db.close();
	}

	/**
	 * M�thode permettant de r�cuperer un tableau de cours � partir d'un
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
			Date debut = new Date(cours.getInt("debut"));
			Date fin = new Date(cours.getInt("fin"));
			listeCours.add(new Cours(debut, fin, nom, salle, uid));
		}
		return listeCours;
	}

	/**
	 * M�thode permettant l'affichage des cours par le layout afficher_matiere �
	 * partir d'un arraylist de cours
	 * 
	 * @param ls
	 *            arraylist contenant les mati�res
	 * @return
	 */
	public ArrayList<HashMap<String, String>> mapping(ArrayList<Cours> ls) {
		ArrayList<HashMap<String, String>> res = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		for (Cours cours : ls) {
			map.put("salle", cours.getSalle());
			map.put("uid", cours.getUid());
			map.put("fin", cours.getFin().toString());
			map.put("debut", cours.getDebut().toString());
			map.put("nom", cours.getNom());
			res.add(map);
		}
		return res;
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

	/**
	 * M�thode qui se d�clenchera lorsque vous appuierez sur le bouton menu du
	 * t�l�phone
	 */
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

	/**
	 * 
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		// On regarde quel item a �t� cliqu� gr�ce � son id et on d�clenche une
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

}

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

import com.ensaitechnomobile.DAO.CoursDAO;
import com.ensaitechnomobile.SQL.MyOpenHelper;
import com.ensaitechnomobile.metier.Cours;
import com.example.pamplemousse.R;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class PamplemousseViewer extends Activity {

	public static final String TAG = "MultiService";
	
	public ArrayAdapter<Cours> adapter;
	// Création de la ArrayList qui nous permettra de remplire la listView
	ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
	ListView listeView;
	JSONArray table;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pamplemousse_viewer);
		sync();
	}
	
	/**
	 * Thread parallèle qui ajoute des données à la table à partir des infos sur
	 * le web
	 */
	private void sync() {
		Runnable code = new Runnable() {
			URL url = null;

			public void run() {
				try {
					url = new URL("http://chessdiags.com/exemple.json");
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
	 * Insérer les données dans la table
	 * 
	 * @param ls
	 * @throws JSONException
	 */
	public void maj(View v) throws JSONException {
		sync();
		if (table.length() > 0) {
			CoursDAO cdao = new CoursDAO();
			SQLiteOpenHelper helper = new MyOpenHelper(this);
			SQLiteDatabase db = helper.getWritableDatabase();
			ArrayList<Cours> lsCours = coursToArray(table);
			cdao.insertAll(db, lsCours);
			Toast.makeText(this, getString(R.string.nb_cours, table.length()),
					Toast.LENGTH_LONG).show();
			db.close();
		}
	}

	/**
	 * Methode permettant d'afficher l'emploi du temps
	 * @param v
	 */
	public void show(View v) {
		CoursDAO cdao = new CoursDAO();
		SQLiteOpenHelper helper = new MyOpenHelper(this);
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<Cours> lsCours = cdao.getAll(db);

		// *********************
		// arrayAdapter
		// Création d'un SimpleAdapter qui se chargera de mettre les items
		// présent dans notre list (listItem) dans la vue affichageitem
		SimpleAdapter mSchedule = new SimpleAdapter(this, mapping(lsCours),
				R.layout.affichage_matiere, new String[] { "debut", "fin",
						"salle", "nom", "uid" }, new int[] { R.id.debut,
						R.id.fin, R.id.salle, R.id.nom, R.id.uid });

		listeView = (ListView) findViewById(R.id.planning);

		// adapter = new ArrayAdapter<Cours>(this,
		// android.R.layout.simple_list_item_1, lsCours);

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
			Date debut = new Date(cours.getInt("debut"));
			Date fin = new Date(cours.getInt("fin"));
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

}

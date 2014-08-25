package com.ensaitechnomobile.agenda;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ensai.appli.R;
import com.ensaitechnomobile.agenda.DAO.CoursDAO;
import com.ensaitechnomobile.agenda.SQL.MyOpenHelper;
import com.ensaitechnomobile.agenda.metier.DayItem;
import com.ensaitechnomobile.agenda.metier.Item;
import com.ensaitechnomobile.agenda.metier.LessonItem;
import com.ensaitechnomobile.main.Authentification;

public class AgendaViewer extends ActionBarActivity {

	public static final String TAG = "MultiService";
	public String baseUrl;
	private String id, pass;
	private CoursDAO cdao = new CoursDAO();
	private SimpleDateFormat dFormat = new SimpleDateFormat(
			"EEEE, dd MMMM yyyy\n", Locale.FRENCH);

	public ArrayAdapter<LessonItem> adapter;
	private ProgressDialog progressDialog;

	// Création de la ArrayList qui nous permettra de remplire la listView
	ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
	ListView listeView;
	JSONArray table = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		progressDialog = new ProgressDialog(this);
		setContentView(R.layout.activity_agenda_viewer);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		show();
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
								AgendaViewer.this);
						SQLiteDatabase db = helper.getWritableDatabase();
						removeAll(db);
						ArrayList<LessonItem> lsCours = coursToArray(table);
						cdao.insertAll(db, lsCours);

						// Affichage d'un toast pour dire que les donnees ont
						// ete mises a jour
						runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(
										AgendaViewer.this,
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
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(AgendaViewer.this,
									getString(R.string.url_error),
									Toast.LENGTH_LONG).show();
						}
					});
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
	private void show() {
		SQLiteOpenHelper helper = new MyOpenHelper(this);
		SQLiteDatabase db = helper.getWritableDatabase();
		ArrayList<LessonItem> ls = cdao.getAll(db);
		if (ls.size() > 0) {
			ArrayList<Item> items = new ArrayList<Item>();
			String day = dFormat.format(new Date(ls.get(0).getDebut()));
			items.add(new DayItem(ls.get(0).getDebut()));
			for (Iterator<LessonItem> iterator = ls.iterator(); iterator
					.hasNext();) {
				LessonItem lessonItem = (LessonItem) iterator.next();
				if (day.equals(dFormat.format(new Date(lessonItem.getDebut())))) {
					items.add(lessonItem);
				} else {
					day = dFormat.format(new Date(lessonItem.getDebut()));
					items.add(new DayItem(lessonItem.getDebut()));
					items.add(lessonItem);
				}
			}
			LessonAdapter adapter = new LessonAdapter(this, items);
			listeView = (ListView) findViewById(R.id.activity_agenda_planning);
			listeView.setAdapter(adapter);
		} else {
			Toast.makeText(AgendaViewer.this, getString(R.string.agenda_null),
					Toast.LENGTH_LONG).show();
		}
		db.close();
	}

	/**
	 * Méthode permettant de récuperer un tableau de cours à partir d'un
	 * JSONArray
	 * 
	 * @param table
	 * @throws JSONException
	 */
	private ArrayList<LessonItem> coursToArray(JSONArray table)
			throws JSONException {
		ArrayList<LessonItem> listeCours = new ArrayList<LessonItem>();
		for (int i = 0; i < table.length(); i++) {
			JSONObject cours = table.getJSONObject(i);
			String nom = cours.getString("nom");
			String salle = cours.getString("salle");
			String uid = cours.getString("uid");
			long debut = cours.getLong("debut");
			long fin = cours.getLong("fin");
			listeCours.add(new LessonItem(debut, fin, nom, salle, uid));
		}
		return listeCours;
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
		inflater.inflate(R.layout.action_bar_agenda, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 
	 */
	// Méthode qui se déclenchera au clic sur un item
	public boolean onOptionsItemSelected(MenuItem item) {
		// On regarde quel item a été cliqué grâce à son id et on déclenche une
		// action
		if (item.getItemId() == R.id.action_bar_agenda_sync) {
			sync();
			return true;
		} else if (item.getItemId() == R.id.action_bar_agenda_user) {
			Intent intent = new Intent(getBaseContext(), Authentification.class);
			if (intent != null)
				startActivity(intent);
			return true;
		} else
			return false;
	}

}

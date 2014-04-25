package com.ensaitechnomobile.menuprincipal;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ensaitechnomobile.geoloc.GeolocOSM;
import com.ensaitechnomobile.meteolocale.MeteoPrincipal;
import com.ensaitechnomobile.metier.Cours;
import com.ensaitechnomobile.pamplemousse.MenuPamplemousse;
import com.example.pamplemousse.R;

public class MenuPrincipal extends Activity {

	public static final String TAG = "Menu principal";
	public ArrayAdapter<Cours> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_principal);

		// *********************
		// arrayAdapter
		// Cr�ation d'un SimpleAdapter qui se chargera de mettre les items
		// pr�sent dans notre list (listItem) dans la vue affichageitem

		ArrayList<HashMap<String, String>> ensembleActivites = new ArrayList<HashMap<String, String>>(
				2);
		HashMap<String, String> activite = new HashMap<String, String>();

		activite.put("nomActivite", getString(R.string.pamp_viewer));
		activite.put("description", getString(R.string.pampl_desc));
		activite.put("img", String.valueOf(R.drawable.pamplemousse));
		ensembleActivites.add(activite);

		activite = new HashMap<String, String>();
		activite.put("nomActivite", getString(R.string.meteo));
		activite.put("description", getString(R.string.meteo_desc));
		activite.put("img", String.valueOf(R.drawable.meteo));
		ensembleActivites.add(activite);

		activite = new HashMap<String, String>();
		activite.put("nomActivite", getString(R.string.geo));
		activite.put("description", getString(R.string.geo_desc));
		activite.put("img", String.valueOf(R.drawable.geolocalisation));
		ensembleActivites.add(activite);

		SimpleAdapter mListeActivites = new SimpleAdapter(this,
				ensembleActivites, R.layout.affichage_activite, new String[] {
						"nomActivite", "description", "img" }, new int[] {
						R.id.nomActivite, R.id.description, R.id.img });

		final ListView listeView = (ListView) findViewById(R.id.activites);

		listeView.setAdapter(mListeActivites);

		/*
		 * // Enfin on met un �couteur d'�v�nement sur notre listView
		 * listeView.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override
		 * 
		 * @SuppressWarnings("unchecked") public void onItemClick(AdapterView<?>
		 * a, View v, int position, long id) { // on r�cup�re la HashMap
		 * contenant les infos de notre item // (titre, description, img)
		 * HashMap<String, String> map = (HashMap<String, String>) listeView
		 * .getItemAtPosition(position); // on cr�er une boite de dialogue
		 * AlertDialog.Builder adb = new AlertDialog.Builder(
		 * MenuPrincipal.this); // on attribut un titre � notre boite de
		 * dialogue adb.setTitle("S�lection Item"); // on ins�re un message �
		 * notre boite de dialogue, et ici on // affiche le titre de l'item
		 * cliqu� adb.setMessage("Votre choix : " + map.get("nomActivite")); //
		 * on indique que l'on veut le bouton ok � notre boite de // dialogue
		 * adb.setPositiveButton("Ok", null); // on affiche la boite de dialogue
		 * adb.show(); } });
		 */

		// On met un �couteur d'�v�nement sur notre listView
		listeView.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {

				// on r�cup�re la HashMap contenant les infos de notre item
				// (titre, description, img)
				HashMap<String, String> map = (HashMap<String, String>) listeView
						.getItemAtPosition(position);

				Toast.makeText(MenuPrincipal.this, map.get("nomActivite"),
						Toast.LENGTH_SHORT).show();

				// Envoie des intents
				CallFunc(position);

			}
		});
	}

	/**
	 * Methode permettant de naviguer entre les activites
	 * 
	 * @param position
	 */
	private void CallFunc(int position) {
		Intent intent = null;
		switch (position) {
		case 0:
			intent = new Intent(this.getBaseContext(), MenuPamplemousse.class);
			break;
		case 1: 
			intent = new Intent(this.getBaseContext(), MeteoPrincipal.class);
			break;
		case 2:
			intent = new Intent(this.getBaseContext(), GeolocOSM.class);
			break;
		 default:
			 Toast.makeText(MenuPrincipal.this, "Clic sur option inexistante/impr�vue",
						Toast.LENGTH_SHORT).show();
			break;
		}
		// pour �viter le if tu peux faire un return sur default du switch 
		// (je n'ai pas compris)
		if (intent != null){
			startActivity(intent);
		}
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
			Toast.makeText(MenuPrincipal.this, "Option", Toast.LENGTH_SHORT)
					.show();
			return true;
		case R.id.favoris:
			Toast.makeText(MenuPrincipal.this, "Favoris", Toast.LENGTH_SHORT)
					.show();
			return true;
		case R.id.stats:
			Toast.makeText(MenuPrincipal.this, "Stats", Toast.LENGTH_SHORT)
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

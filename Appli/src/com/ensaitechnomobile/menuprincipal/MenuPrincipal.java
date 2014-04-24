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
		// Création d'un SimpleAdapter qui se chargera de mettre les items
		// présent dans notre list (listItem) dans la vue affichageitem

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
		 * // Enfin on met un écouteur d'évènement sur notre listView
		 * listeView.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override
		 * 
		 * @SuppressWarnings("unchecked") public void onItemClick(AdapterView<?>
		 * a, View v, int position, long id) { // on récupère la HashMap
		 * contenant les infos de notre item // (titre, description, img)
		 * HashMap<String, String> map = (HashMap<String, String>) listeView
		 * .getItemAtPosition(position); // on créer une boite de dialogue
		 * AlertDialog.Builder adb = new AlertDialog.Builder(
		 * MenuPrincipal.this); // on attribut un titre à notre boite de
		 * dialogue adb.setTitle("Sélection Item"); // on insère un message à
		 * notre boite de dialogue, et ici on // affiche le titre de l'item
		 * cliqué adb.setMessage("Votre choix : " + map.get("nomActivite")); //
		 * on indique que l'on veut le bouton ok à notre boite de // dialogue
		 * adb.setPositiveButton("Ok", null); // on affiche la boite de dialogue
		 * adb.show(); } });
		 */

		// On met un écouteur d'évènement sur notre listView
		listeView.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {

				// on récupère la HashMap contenant les infos de notre item
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

	private void CallFunc(int position) {
		Intent intent = null;
		switch (position) {
		case 0:
			intent = new Intent(getBaseContext(), MenuPamplemousse.class);
			break;
		}
		// pour éviter le if tu peux faire un return sur default du switch
		if (intent != null)
			startActivity(intent);
	}

	// pour éviter le if tu peux faire un return sur default du switch

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

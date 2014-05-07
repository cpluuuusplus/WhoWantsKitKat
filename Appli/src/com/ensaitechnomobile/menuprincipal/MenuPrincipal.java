package com.ensaitechnomobile.menuprincipal;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import com.ensaitechnomobile.geoloc.GeolocOSM;
import com.ensaitechnomobile.meteolocale.MeteoPrincipal;
import com.ensaitechnomobile.metier.Cours;
import com.ensaitechnomobile.pamplemousse.MenuPamplemousse;
import com.example.pamplemousse.R;

public class MenuPrincipal extends Activity {

	public static final String TAG = "Menu principal";
	public ArrayAdapter<Cours> adapter;
	Intent intent = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_principal);

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editor = preferences.edit();
		editor.putString("login", null);
		editor.putString("password", null);
	}

	public void pamplemousse(View v) {
		intent = new Intent(this.getBaseContext(), MenuPamplemousse.class);
		if (intent != null) {
			startActivity(intent);
		}
	}

	public void geolocalisation(View v) {
		intent = new Intent(this.getBaseContext(), GeolocOSM.class);
		if (intent != null) {
			startActivity(intent);
		}
	}

	public void meteo(View v) {
		intent = new Intent(this.getBaseContext(), MeteoPrincipal.class);
		if (intent != null) {
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
		inflater.inflate(R.layout.action_menu, menu);

		// Il n'est pas possible de modifier l'ic�ne d'ent�te du sous-menu via
		// le fichier XML on le fait donc en JAVA
		// menu.getItem(0).getSubMenu().setHeaderIcon(R.drawable.option_white);

		return true;
	}

	// M�thode qui se d�clenchera au clic sur un item
	public boolean onOptionsItemSelected(MenuItem item) {
		// On regarde quel item a �t� cliqu� gr�ce � son id et on d�clenche une
		// action
		if (item.getItemId() == R.id.action_settings)
			return true;
		if (item.getItemId() == R.id.action_quit) {
			// Pour fermer l'application il suffit de faire finish()
			finish();
			return true;
		} else
			return false;
	}
}

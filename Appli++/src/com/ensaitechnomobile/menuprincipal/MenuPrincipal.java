package com.ensaitechnomobile.menuprincipal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import com.ensai.appli.R;
import com.ensaitechnomobile.geoloc.GeolocOSM;
import com.ensaitechnomobile.geoloc.NewGeolocalisation;
import com.ensaitechnomobile.meteolocale.MeteoPrincipal;
import com.ensaitechnomobile.metier.Cours;
import com.ensaitechnomobile.pamplemousse.MenuPamplemousse;
import com.ensaitechnomobile.pamplemousse.PamplemousseViewer;

public class MenuPrincipal extends ActionBarActivity {

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
		// intent = new Intent(this.getBaseContext(), GeolocOSM.class);
		intent = new Intent(this.getBaseContext(), NewGeolocalisation.class);
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

	/**
	 * M�thode qui se d�clenchera lorsque vous appuierez sur le bouton menu du
	 * t�l�phone
	 */
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 
	 */
	// M�thode qui se d�clenchera au clic sur un item
	public boolean onOptionsItemSelected(MenuItem item) {
		// On regarde quel item a �t� cliqu� gr�ce � son id et on d�clenche une
		// action
		if (item.getItemId() == R.id.action_viewer) {
			Intent intent = new Intent(getBaseContext(),
					PamplemousseViewer.class);

			// pour �viter le if tu peux faire un return sur default du switch
			if (intent != null)
				startActivity(intent);
			return true;
		} else
			return false;
	}
}

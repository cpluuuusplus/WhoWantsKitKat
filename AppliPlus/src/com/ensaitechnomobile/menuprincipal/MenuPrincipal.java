package com.ensaitechnomobile.menuprincipal;

import android.annotation.SuppressLint;
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
import com.ensaitechnomobile.geolocalisation.OSM;
import com.ensaitechnomobile.meteo.locale.Meteo;
import com.ensaitechnomobile.metier.LessonItem;
import com.ensaitechnomobile.pamplemousse.AgendaViewer;
import com.ensaitechnomobile.pamplemousse.Authentification;
import com.ensaitechnomobile.pamplemousse.WebviewMail;
import com.ensaitechnomobile.pamplemousse.WebviewNotes;

public class MenuPrincipal extends ActionBarActivity {

	public static final String TAG = "Menu principal";
	public ArrayAdapter<LessonItem> adapter;
	Intent intent = null;

	@SuppressLint("CommitPrefEdits")
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

	// Implémentation des listeners bouton
	public void onClickPampViewer(View v) {
		Intent intent = new Intent(getBaseContext(), AgendaViewer.class);
		// Intent intent = new Intent(getBaseContext(),
		// SectionListExample.class);

		// pour éviter le if tu peux faire un return sur default du switch
		if (intent != null)
			startActivity(intent);
	}

	public void onClickMail(View v) {
		Intent intent = new Intent(getBaseContext(), WebviewMail.class);
		if (intent != null)
			startActivity(intent);
	}

	public void onClickNotes(View v) {
		Intent intent = new Intent(getBaseContext(), WebviewNotes.class);
		if (intent != null)
			startActivity(intent);
	}

	public void geolocalisation(View v) {
		intent = new Intent(this.getBaseContext(), OSM.class);
		if (intent != null) {
			startActivity(intent);
		}
	}

	public void meteo(View v) {
		intent = new Intent(this.getBaseContext(), Meteo.class);
		if (intent != null) {
			startActivity(intent);
		}
	}

	// Implémentation du menu

	/**
	 * Méthode qui se déclenchera lorsque vous appuierez sur le bouton menu du
	 * téléphone
	 */
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.action_bar_authentification, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 
	 */
	// Méthode qui se déclenchera au clic sur un item
	public boolean onOptionsItemSelected(MenuItem item) {
		// On regarde quel item a été cliqué grâce à son id et on déclenche une
		// action
		if (item.getItemId() == R.id.action_user) {
			Intent intent = new Intent(getBaseContext(), Authentification.class);
			if (intent != null)
				startActivity(intent);
			return true;
		} else
			return false;
	}
}

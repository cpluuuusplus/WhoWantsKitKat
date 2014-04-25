package com.ensaitechnomobile.menuprincipal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

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

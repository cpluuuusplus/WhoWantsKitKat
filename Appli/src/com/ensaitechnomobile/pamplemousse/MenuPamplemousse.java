package com.ensaitechnomobile.pamplemousse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ensaitechnomobile.menuprincipal.MenuPrincipal;
import com.example.pamplemousse.R;

public class MenuPamplemousse extends Activity {

	public static final String TAG = "MultiService";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_pamplemousse);
	}

	public void login(View v) {
		Intent intent = new Intent(getBaseContext(), Connection.class);

		// pour �viter le if tu peux faire un return sur default du switch
		if (intent != null)
			startActivity(intent);
	}

	public void pampViewer(View v) {
		Intent intent = new Intent(getBaseContext(), PamplemousseViewer.class);

		// pour �viter le if tu peux faire un return sur default du switch
		if (intent != null)
			startActivity(intent);
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
			Toast.makeText(this, "Option", Toast.LENGTH_SHORT)
					.show();
			return true;
		case R.id.favoris:
			Toast.makeText(this, "Favoris", Toast.LENGTH_SHORT)
					.show();
			return true;
		case R.id.stats:
			Toast.makeText(this, "Stats", Toast.LENGTH_SHORT)
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

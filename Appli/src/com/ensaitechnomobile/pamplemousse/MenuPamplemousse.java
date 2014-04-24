package com.ensaitechnomobile.pamplemousse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

		// pour Žviter le if tu peux faire un return sur default du switch
		if (intent != null)
			startActivity(intent);
	}

	public void pampViewer(View v) {
		Intent intent = new Intent(getBaseContext(), PamplemousseViewer.class);

		// pour Žviter le if tu peux faire un return sur default du switch
		if (intent != null)
			startActivity(intent);
	}

}

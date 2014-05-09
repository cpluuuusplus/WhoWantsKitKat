package com.ensaitechnomobile.pamplemousse;

import com.ensai.appli.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class MenuPamplemousse extends ActionBarActivity {

	public static final String TAG = "MultiService";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_pamplemousse);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	public void onClickLogin(View v) {
		Intent intent = new Intent(getBaseContext(), Connection.class);

		// pour Žviter le if tu peux faire un return sur default du switch
		if (intent != null)
			startActivity(intent);
	}

	public void onClickPampViewer(View v) {
		Intent intent = new Intent(getBaseContext(), PamplemousseViewer.class);

		// pour Žviter le if tu peux faire un return sur default du switch
		if (intent != null)
			startActivity(intent);
	}

	public void onClickMail(View v) {
		Intent intent = new Intent(getBaseContext(), WebViewMail.class);
		if (intent != null)
			startActivity(intent);
	}

	public void onClickNotes(View v) {
		Intent intent = new Intent(getBaseContext(), WebViewNotes.class);
		if (intent != null)
			startActivity(intent);
	}
}

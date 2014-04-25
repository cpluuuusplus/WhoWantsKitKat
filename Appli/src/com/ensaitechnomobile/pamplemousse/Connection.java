package com.ensaitechnomobile.pamplemousse;

import com.example.pamplemousse.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Connection extends Activity {
	protected static String id;
	protected static String pass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connection);
	}

	/**
	 * Click sur le bouton connection
	 */
	public void connect(View v) {
		EditText user_password = (EditText) findViewById(R.id.user_password);
		EditText identifiant = (EditText) findViewById(R.id.identifiant);
		id = identifiant.getText().toString();
		pass = user_password.getText().toString();
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editor = preferences.edit();
		editor.putString("login", id);
		editor.putString("password", pass);
		editor.commit();

		Toast.makeText(this, "Identifiants enregistrés", Toast.LENGTH_LONG)
				.show();
		Intent intent = new Intent(getBaseContext(), MenuPamplemousse.class);

		// pour éviter le if tu peux faire un return sur default du switch
		if (intent != null)
			startActivity(intent);
	}
}

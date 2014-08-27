package com.ensaitechnomobile.main;

import com.ensai.appli.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Authentification extends Activity {
	protected static String id;
	protected static String pass;
	EditText user_password;
	EditText identifiant;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authentification);
		user_password = (EditText) findViewById(R.id.activity_authentification_user_password);
		identifiant = (EditText) findViewById(R.id.activity_authentification_id);
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		id = preferences.getString("login", "");
		pass = preferences.getString("password", "");
		if (!id.equals("") && !pass.equals("")) {
			identifiant.setText(id);
			user_password.setText(pass);
		}
	}

	/**
	 * Click sur le bouton connection
	 */
	public void connect(View v) {
		id = identifiant.getText().toString();
		pass = user_password.getText().toString();
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editor = preferences.edit();
		editor.putString("login", id);
		editor.putString("password", pass);
		editor.commit();
		Toast.makeText(this, getString(R.string.authentification_ok),
				Toast.LENGTH_LONG).show();
		Intent intent = new Intent(getBaseContext(), Main.class);

		// pour �viter le if tu peux faire un return sur default du switch
		if (intent != null)
			startActivity(intent);
	}
}

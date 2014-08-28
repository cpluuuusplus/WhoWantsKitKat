package com.ensaitechnomobile.common;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ScrollView;

import com.ensai.appli.R;

public class Credits extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_credits);
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		ScrollView creditsBack = (ScrollView) findViewById(R.id.activity_credits_scroll_view);
		creditsBack.setBackgroundResource(preferences.getInt("MAIN_COLOR",
				R.drawable.backmotif_blue));

	}
}

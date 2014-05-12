package com.ensaitechnomobile.pamplemousse;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class WebViewNotes extends ActionBarActivity {

	@SuppressLint("SetJavaScriptEnabled")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		final String id, pass;
		// Récupération de l'ID et du mot de passe dans les préférences
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		id = preferences.getString("login", "");
		pass = preferences.getString("password", "");

		// Création de la Webview
		WebView webView = new WebView(this);
		setContentView(webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("http://pamplemousse.ensai.fr/ensai/index.php?p=1052");

		webView.setWebViewClient(new WebViewClient() {

			public void onPageFinished(WebView view, String url) {
				view.loadUrl("javascript:document.getElementsByName('username')[0].value = '"
						+ id + "'");
				view.loadUrl("javascript:document.getElementsByName('password')[0].value = '"
						+ pass + "'");
				view.loadUrl("javascript:document.forms['fm1'].submit()");
			}

		});
	}

}
// http://pamplemousse.ensai.fr/ensai/index.php?p=1052
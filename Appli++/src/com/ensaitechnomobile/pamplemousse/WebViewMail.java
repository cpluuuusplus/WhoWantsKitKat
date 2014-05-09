package com.ensaitechnomobile.pamplemousse;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewMail extends ActionBarActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Eventuellement mettre un écran pour dire que le chargement de la
		// messagerie est en cours
		// avec une petit icone qui fait comprendre à l'utilisater que quelque
		// chose se passe .. éventuellement
		// En même temps j'écris ça dans le train avec le 3G de mon tel donc je
		// suis face aux pires aléas de la data tout en sachant que je peux
		// avoir internet
		
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
		webView.loadUrl("https://webmail.ensai.fr/SOGo/so/" + id + "/Mail/view");

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

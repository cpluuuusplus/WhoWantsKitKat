package com.ensaitechnomobile.pamplemousse;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebWiewNotes extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WebView webview = new WebView(this);
		setContentView(webview);
		webview.loadUrl("https://sso-cas.ensai.fr/cas/login?service=http://ent2.ensai.fr/Login");
	}
}

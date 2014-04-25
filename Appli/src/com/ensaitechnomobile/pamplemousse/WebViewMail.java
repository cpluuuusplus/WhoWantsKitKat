package com.ensaitechnomobile.pamplemousse;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewMail extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WebView webview = new WebView(this);
		setContentView(webview);
		webview.loadUrl("https://sso-cas.ensai.fr/cas/login?service=http://ent2.ensai.fr/Login");
		
//		TODO intégrer ceci pour (espérons) saisir le mot de passe automatiquement
//		webView.getSettings().setJavaScriptEnabled(true);   
//
//	    webView.loadUrl("http://your.url");
//
//	    webView.setWebViewClient(new WebViewClient() {
//
//	        public void onPageFinished(WebView view, String url) {
//	            view.loadUrl("javascript:document.getElementsByName('school')[0].value = 'schoolname'");
//	            view.loadUrl("javascript:document.getElementsByName('j_username')[0].value = 'username'");
//	            view.loadUrl("javascript:document.getElementsByName('j_password')[0].value = 'password'");
//
//	            view.loadUrl("javascript:document.forms['login'].submit()");
//	        }
//	    });
	}
}

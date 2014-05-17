package com.ensaitechnomobile.pamplemousse;

import com.ensai.appli.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebViewNotes extends ActionBarActivity {

	protected static final String TAG = "NOTES::";
	private String id, pass;
	private WebView webview;

	@SuppressLint("SetJavaScriptEnabled")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		// R�cup�ration de l'ID et du mot de passe dans les pr�f�rences
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		id = preferences.getString("login", "");
		pass = preferences.getString("password", "");
		myClickHandler();
	}

	// When user clicks button, calls AsyncTask.
	// Before attempting to fetch the URL, makes sure that there is a network
	// connection.
	public void myClickHandler() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {

			// Cr�ation de la Webview
			webview = new WebView(this);
			webview.getSettings().setJavaScriptEnabled(true);
			webview.canGoBack();
			webview.loadUrl("http://pamplemousse.ensai.fr/ensai/");
			webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

			webview.setWebViewClient(new WebViewClient() {

				public void onPageFinished(WebView view, String url) {
					view.loadUrl("javascript:document.getElementsByName('username')[0].value = '"
							+ id + "'");
					view.loadUrl("javascript:document.getElementsByName('password')[0].value = '"
							+ pass + "'");
					view.loadUrl("javascript:document.forms['fm1'].submit()");
				}
			});
			setContentView(webview);

		} else {
			Toast.makeText(this, getString(R.string.conection_error),
					Toast.LENGTH_LONG).show();
		}
	}

	public void onResume() {
		// TODO Auto-generated method stub
		CookieSyncManager cookieSyncMngr = CookieSyncManager
				.createInstance(this);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		super.onResume();
	}
}
// http://pamplemousse.ensai.fr/ensai/index.php?p=1052
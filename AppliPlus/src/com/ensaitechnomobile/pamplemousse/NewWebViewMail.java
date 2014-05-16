package com.ensaitechnomobile.pamplemousse;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ensai.appli.R;

public class NewWebViewMail extends Activity {
	/** Called when the activity is first created. */
	// @Override
	private WebView webview;
	private String id, pass;
	private ProgressDialog progressDialog;
	protected static final String TAG = "MAIL::";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get rid of the android title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Set the XML layout
		setContentView(R.layout.activity_web_view);

		// Récupération de l'ID et du mot de passe dans les préférences
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

			// Bundle objectbundle = this.getIntent().getExtras();
			webview = (WebView) findViewById(R.id.webview01);

			final Activity activity = this;

			// Enable JavaScript and lets the browser go back
			webview.getSettings().setJavaScriptEnabled(true);
			webview.canGoBack();

			webview.setWebViewClient(new WebViewClient() {
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					view.loadUrl(url);
					return true;
				}

				public void onLoadResource(WebView view, String url) {
					// Check to see if there is a progress dialog
					if (progressDialog == null) {
						// If no progress dialog, make one and set message
						progressDialog = new ProgressDialog(activity);
						progressDialog.setMessage(getString(R.string.ENT));
						progressDialog.show();

						// Hide the webview while loading
						webview.setEnabled(false);
					}
				}

				public void onPageFinished(WebView view, String url) {
					// Page is done loading;
					// hide the progress dialog and show the webview
					// view.loadUrl("javascript:document.forms['fm1'].submit()");
					if (progressDialog.isShowing()) {
						progressDialog.dismiss();
						progressDialog = null;
						webview.setEnabled(true);
					}
					view.loadUrl("javascript:document.getElementsByName('username')[0].value = '"
							+ id + "'");
					view.loadUrl("javascript:document.getElementsByName('password')[0].value = '"
							+ pass + "'");
				}
			});
			// The URL that webview is loading
			webview.loadUrl("https://webmail.ensai.fr/SOGo/so/" + id
					+ "/Mail/view");
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
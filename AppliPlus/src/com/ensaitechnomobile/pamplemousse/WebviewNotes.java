package com.ensaitechnomobile.pamplemousse;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ensai.appli.R;

public class WebviewNotes extends ActionBarActivity {
	/** Called when the activity is first created. */
	// @Override
	private WebView webview;
	private String id, pass;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set the XML layout
		setContentView(R.layout.activity_web_view);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

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
						progressDialog = new ProgressDialog(WebviewNotes.this);
						progressDialog.setCanceledOnTouchOutside(false);
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
			webview.loadUrl("http://pamplemousse.ensai.fr/ensai/");
		} else {
			Toast.makeText(this, getString(R.string.conection_error),
					Toast.LENGTH_LONG).show();
		}
	}

	// Implémentation du menu

	/**
	 * Méthode qui se déclenchera lorsque vous appuierez sur le bouton menu du
	 * téléphone
	 */
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.action_bar_webview, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 
	 */
	// Méthode qui se déclenchera au clic sur un item
	public boolean onOptionsItemSelected(MenuItem item) {
		// On regarde quel item a été cliqué grâce à son id et on déclenche une
		// action
		if (item.getItemId() == R.id.actionbar_webview) {
			CookieManager cookieManager = CookieManager.getInstance();
			cookieManager.removeAllCookie();
			myClickHandler();
			return true;
		} else
			return false;
	}
}
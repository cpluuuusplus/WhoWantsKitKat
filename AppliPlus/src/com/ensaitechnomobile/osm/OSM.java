package com.ensaitechnomobile.osm;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.http.HttpClientFactory;
import org.osmdroid.http.IHttpClientFactory;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import com.ensai.appli.R;
import com.ensaitechnomobile.meteo.station.MeteoJSON;
import com.ensaitechnomobile.metier.CityNotFoundException;
import com.ensaitechnomobile.metier.EtatMeteo;
import com.ensaitechnomobile.metier.Localite;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

public class OSM extends ActionBarActivity {

	private MapView myOpenMapView;
	private MapController myMapController;
	protected static final String APIID = "ef5e65bcdadbcc86a991779742664324";
	protected static final String TAG = "OSM::";
	private double longitude, latitude;
	private LocationManager locationManager;
	private ArrayList<OverlayItem> overlayItemArray;
	private ProgressDialog progressDialog;
	private String country = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_osm);
		progressDialog = new ProgressDialog(this);

		HttpClientFactory.setFactoryInstance(new IHttpClientFactory() {
			public HttpClient createHttpClient() {
				final DefaultHttpClient client = new DefaultHttpClient();
				client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
						"ensaitechnomobiles.AMS");
				return client;
			}
		});

		myOpenMapView = (MapView) findViewById(R.id.mapview);
		myOpenMapView.setTileSource(TileSourceFactory.CYCLEMAP);
		// myOpenMapView.setTileSource(TileSourceFactory.MAPNIK);
		// myOpenMapView.setTileSource(TileSourceFactory.MAPQUESTOSM);
		// myOpenMapView.setTileSource(TileSourceFactory.PUBLIC_TRANSPORT);
		// myOpenMapView.setTileSource(TileSourceFactory.MAPQUESTAERIAL);

		myOpenMapView.setBuiltInZoomControls(true);
		myOpenMapView.setMultiTouchControls(true);
		myMapController = (MapController) myOpenMapView.getController();
		myMapController.setZoom(15);

		// --- Create Overlay
		overlayItemArray = new ArrayList<OverlayItem>();

		DefaultResourceProxyImpl defaultResourceProxyImpl = new DefaultResourceProxyImpl(
				this);
		MyItemizedIconOverlay myItemizedIconOverlay = new MyItemizedIconOverlay(
				overlayItemArray, null, defaultResourceProxyImpl);
		myOpenMapView.getOverlays().add(myItemizedIconOverlay);
		// ---

		localiserDevice();
	}

	/**
	 * Localiser le device
	 */
	private void localiserDevice() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		Location lastLocation = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (lastLocation != null) {
			updateLoc(lastLocation);
		} else {
			lastLocation = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (lastLocation != null) {
				updateLoc(lastLocation);
			} else {
				GeoPoint startPoint = new GeoPoint(48.13, -1.74098);
				myMapController.setCenter(startPoint);
			}
		}
	}

	private void updateLoc(Location loc) {
		GeoPoint locGeoPoint = new GeoPoint(loc.getLatitude(),
				loc.getLongitude());
		myMapController.setCenter(locGeoPoint);
		setOverlayLoc(loc);
	}

	private void setOverlayLoc(Location overlayloc) {
		GeoPoint overlocGeoPoint = new GeoPoint(overlayloc);
		// ---
		overlayItemArray.clear();
		OverlayItem newMyLocationItem = new OverlayItem("My Location",
				"My Location", overlocGeoPoint);
		overlayItemArray.add(newMyLocationItem);
		// ---
	}

	private SearchView searchView;
	private MenuItem searchItem;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.layout.action_bar_osm, menu);
		searchItem = menu.findItem(R.id.action_bar_osm_search);
		searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		searchView.setQueryHint(getString(R.string.find_city));
		searchView.setOnQueryTextListener(queryTextListener);
		return super.onCreateOptionsMenu(menu);
	}
	
	
	/**
	 * 
	 */
//	 Méthode qui se déclenchera au clic sur un item
	public boolean onOptionsItemSelected(MenuItem item) {
		// On regarde quel item a été cliqué grâce à son id et on déclenche une
		// action
		if (item.getItemId() == R.id.action_bar_osm_find) {
			localiserDevice();
			return true;
		} else if (item.getItemId() == R.id.action_bar_osm_cyclemap) {
			if (item.isChecked())
				item.setChecked(false);
			else
				item.setChecked(true);
			myOpenMapView.setTileSource(TileSourceFactory.CYCLEMAP);
			return true;
		} else if (item.getItemId() == R.id.action_bar_osm_mapnik) {
			if (item.isChecked())
				item.setChecked(false);
			else
				item.setChecked(true);
			myOpenMapView.setTileSource(TileSourceFactory.MAPNIK);
			return true;
		} else if (item.getItemId() == R.id.action_bar_osm_mapquestosm) {
			if (item.isChecked())
				item.setChecked(false);
			else
				item.setChecked(true);
			myOpenMapView.setTileSource(TileSourceFactory.MAPQUESTOSM);
			return true;
		} else if (item.getItemId() == R.id.action_bar_osm_public_transport) {
			if (item.isChecked())
				item.setChecked(false);
			else
				item.setChecked(true);
			myOpenMapView.setTileSource(TileSourceFactory.PUBLIC_TRANSPORT);
			return true;
		} else if (item.getItemId() == R.id.action_bar_osm_mapquestaerial) {
			if (item.isChecked())
				item.setChecked(false);
			else
				item.setChecked(true);
			myOpenMapView.setTileSource(TileSourceFactory.MAPQUESTAERIAL);
			return true;
		} else
			return false;
	}

	final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {

		@Override
		public boolean onQueryTextSubmit(String query) {
			String city = searchView.getQuery() + "";
			moveToNewCity(new Localite(city));
			searchView.clearFocus();
			return false;
		}

		@Override
		public boolean onQueryTextChange(String arg0) {
			// TODO Auto-generated method stub
			return false;
		}
	};

	public void moveToNewCity(Localite loc) {
		String cityURL = prepareURL(APIID, loc, 0, 0, false);
		findCity(cityURL, this.getBaseContext());
	}

	String prepareURL(String apid, Localite loc, int coordX, int coordY,
			boolean previsions) {
		String res = "http://api.openweathermap.org/data/2.5/";
		if (previsions) {
			res += "forecast/Daily";
		} else {
			res += "weather";
		}

		if (loc.hasVille()) {
			res += "?q=" + loc.getVille();
		} else {
			if (loc.hasLongLat()) {
				res += "?lon=" + loc.getLongitude() + "&lat="
						+ loc.getLatitude();
			}
		}
		res += "&units=metric";
		res += "&mode=json";
		res += "&APPID=" + apid;
		Log.v("AMS::Meteo", "URL de récupération des données météo : " + res);
		return res;
	}

	private void findCity(final String urlString, final Context ctx) {
		// Get all fields to be updated

		country = null;
		progressDialog.setTitle(getString(R.string.searching_city));
		progressDialog.setMessage(getString(R.string.move_to_city));
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();

		Runnable code = new Runnable() {
			URL url = null;

			public void run() {
				try {
					// On récupère le JSON a partir de l'URL
					url = new URL(urlString);
					HttpURLConnection urlConnection;
					urlConnection = (HttpURLConnection) url.openConnection();
					BufferedInputStream in = new BufferedInputStream(
							urlConnection.getInputStream());
					String input = readStream(in);
					JSONObject json = new JSONObject(input);
					Log.i(TAG, input);
					// On transforme en météo
					MeteoJSON mjson = new MeteoJSON();
					EtatMeteo em = mjson.construireEtatMeteoActuel(json);
					longitude = json.getJSONObject("coord").getDouble("lon");
					latitude = json.getJSONObject("coord").getDouble("lat");
					country = json.getJSONObject("sys").getString("country");
					Log.i(TAG, json.toString());
					Log.i(TAG, em.toString());

					runOnUiThread(new Runnable() {
						public void run() {
							addLocation(latitude, longitude);
						}
					});

				} catch (MalformedURLException e) {
					Log.e(TAG, "URL malformée");
					e.printStackTrace();
				} catch (IOException e) {
					Log.e(TAG, "Exception d'E/S");
					e.printStackTrace();
				} catch (JSONException e) {
					Log.e(TAG, "Exception JSON");
					e.printStackTrace();
				} catch (CityNotFoundException e) {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(
									ctx,
									"La ville est invalide, veuillez saisir une ville valide",
									Toast.LENGTH_LONG).show();
						}
					});
				}
				runOnUiThread(new Runnable() {
					public void run() {
						if (country != null) {
							String city = searchView.getQuery() + "";
							city += " (" + country + ")";
							searchView.setQuery(city, false);
						}
					}
				});
				progressDialog.dismiss();
			}
		};
		new Thread(code).start();
	}

	private void addLocation(double lat, double lng) {
		// ---Add a location marker---
		GeoPoint p = new GeoPoint(lat, lng);
		myMapController.animateTo(p);
		myMapController.setCenter(p);
		overlayItemArray = new ArrayList<OverlayItem>();

		// Put overlay icon a little way from map center
		overlayItemArray
				.add(new OverlayItem("Here u r", "SampleDescription", p));
	}

	/**
	 * Lit un flux de données et retourne le string correspondant
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public String readStream(InputStream inputStream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));
		String ligne = null;
		String contenu = "";
		while ((ligne = reader.readLine()) != null) {
			contenu += ligne;
		}
		return contenu;
	}

	public class MyItemizedIconOverlay extends ItemizedIconOverlay<OverlayItem> {

		public MyItemizedIconOverlay(
				List<OverlayItem> pList,
				org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener<OverlayItem> pOnItemGestureListener,
				ResourceProxy pResourceProxy) {
			super(pList, pOnItemGestureListener, pResourceProxy);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void draw(Canvas canvas, MapView mapview, boolean arg2)
				throws IndexOutOfBoundsException {
			// TODO Auto-generated method stub
			super.draw(canvas, mapview, arg2);

			if (overlayItemArray != null && overlayItemArray.size() != 0) {

				GeoPoint in = overlayItemArray.get(0).getPoint();

				Point out = new Point();
				mapview.getProjection().toPixels(in, out);

				Bitmap bm = BitmapFactory.decodeResource(getResources(),
						R.drawable.ic_action_location_found);
				canvas.drawBitmap(bm, out.x - bm.getWidth() / 2, // shift the
																	// bitmap
																	// center
						out.y - bm.getHeight() / 2, // shift the bitmap center
						null);
			}
		}

		@Override
		public boolean onSingleTapUp(MotionEvent event, MapView mapView) {

			return true;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent event, MapView mapView) {
			return true;

		}
	}
}

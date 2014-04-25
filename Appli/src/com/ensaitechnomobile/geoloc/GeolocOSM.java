package com.ensaitechnomobile.geoloc;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.osmdroid.http.HttpClientFactory;
import org.osmdroid.http.IHttpClientFactory;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.SimpleLocationOverlay;

import android.app.Activity;
import android.os.Bundle;

import com.example.pamplemousse.R;

public class GeolocOSM extends Activity {

	private MapView mapView;
	private MapController mapController;
	private SimpleLocationOverlay mMyLocationOverlay;
	private ScaleBarOverlay mScaleBarOverlay;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_geoloc_osm);

		HttpClientFactory.setFactoryInstance(new IHttpClientFactory() {
			public HttpClient createHttpClient() {
				final DefaultHttpClient client = new DefaultHttpClient();
				client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
						"ensaitechnomobiles.AMS");
				return client;
			}
		});

		mapView = (MapView) this.findViewById(R.id.mapview);
		mapView.setTileSource(TileSourceFactory.MAPNIK);
		mapView.setBuiltInZoomControls(true);
		mapView.setMultiTouchControls(true);

		mapController = (MapController) this.mapView.getController();
		mapController.setZoom(2);
		mapController.setCenter(new GeoPoint(52.221, 6.893)); // This point is
																// in Enschede,
																// Netherlands.
																// You should
																// select a
																// point in your
																// map or get it
																// from user's
																// location.

		this.mMyLocationOverlay = new SimpleLocationOverlay(this);
		this.mapView.getOverlays().add(mMyLocationOverlay);

		this.mScaleBarOverlay = new ScaleBarOverlay(this);
		this.mapView.getOverlays().add(mScaleBarOverlay);
	}
}
package com.ensaitechnomobile.geoloc;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.osmdroid.api.IMapController;
import org.osmdroid.api.Marker;
import org.osmdroid.http.HttpClientFactory;
import org.osmdroid.http.IHttpClientFactory;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.SimpleLocationOverlay;

import com.ensai.appli.R;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

public class GeolocOSM extends ActionBarActivity {

	private MapView mapView;
	private MapController mapController;
	private SimpleLocationOverlay mMyLocationOverlay;
	private ScaleBarOverlay mScaleBarOverlay;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_geoloc_osm);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		HttpClientFactory.setFactoryInstance(new IHttpClientFactory() {
			public HttpClient createHttpClient() {
				final DefaultHttpClient client = new DefaultHttpClient();
				client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
						"ensaitechnomobiles.AMS");
				return client;
			}
		});

		mapView = (MapView) this.findViewById(R.id.mapview);
		// mapView.setTileSource(TileSourceFactory.CYCLEMAP);
		mapView.setTileSource(TileSourceFactory.MAPNIK);
		mapView.setBuiltInZoomControls(true);
		mapView.setMultiTouchControls(true);

		mapController = (MapController) this.mapView.getController();
		GeoPoint startPoint = new GeoPoint(48.13, -1.74098);
		mapController.setZoom(10);
		mapController.setCenter(startPoint);

		// mapController.setCenter(new GeoPoint(48.0504189, -1.74098));
		this.mMyLocationOverlay = new SimpleLocationOverlay(this);
		this.mapView.getOverlays().add(mMyLocationOverlay);

		this.mScaleBarOverlay = new ScaleBarOverlay(this);
		this.mapView.getOverlays().add(mScaleBarOverlay);
	}

}

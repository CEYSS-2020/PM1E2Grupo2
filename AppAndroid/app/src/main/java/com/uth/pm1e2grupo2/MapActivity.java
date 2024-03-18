package com.uth.pm1e2grupo2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.compass.CompassOverlay;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {
	private MapView map = null;

	private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
	String strLatitud, strLongitud;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		final Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		final ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setTitle("Ubicacion del Contacto");
		}

		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if (extras == null) {
				strLatitud = null;
				strLongitud = null;
			} else {
				strLatitud = extras.getString("Latitud");
				strLongitud = extras.getString("Longitud");
			}
		} else {
			strLatitud = (String) savedInstanceState.getSerializable("Latitud");
			strLongitud = (String) savedInstanceState.getSerializable("Longitud");
		}

		Context ctx = this.getApplicationContext();
		Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

		map = findViewById(R.id.mapview);
		map.setTileSource(TileSourceFactory.MAPNIK);
		map.getController().setZoom(18.0);

		requestPermissionsIfNecessary(new String[]{
				Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.INTERNET
		});
		map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
		map.setMultiTouchControls(true);

		CompassOverlay compassOverlay = new CompassOverlay(this, map);
		compassOverlay.enableCompass();
		map.getOverlays().add(compassOverlay);

		GeoPoint point = new GeoPoint(Float.parseFloat(strLatitud), Float.parseFloat(strLongitud));
		Marker startMarker = new Marker(map);
		startMarker.setPosition(point);
		startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
		map.getOverlays().add(startMarker);

		map.getController().setCenter(point);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		ArrayList<String> permissionsToRequest = new ArrayList<>();
		for (int i = 0; i < grantResults.length; i++) {
			permissionsToRequest.add(permissions[i]);
		}
		if (permissionsToRequest.size() > 0) {
			ActivityCompat.requestPermissions(
					this,
					permissionsToRequest.toArray(new String[0]),
					REQUEST_PERMISSIONS_REQUEST_CODE);
		}
	}

	private void requestPermissionsIfNecessary(String[] permissions) {
		ArrayList<String> permissionsToRequest = new ArrayList<>();
		for (String permission : permissions) {
			if (ContextCompat.checkSelfPermission(this, permission)
					!= PackageManager.PERMISSION_GRANTED) {
				permissionsToRequest.add(permission);
			}
		}
		if (permissionsToRequest.size() > 0) {
			ActivityCompat.requestPermissions(
					this,
					permissionsToRequest.toArray(new String[0]),
					REQUEST_PERMISSIONS_REQUEST_CODE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(menuItem);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

}
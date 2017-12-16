package com.ir.hodicohiff;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MapActivity extends Activity {

	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_map);

		map = null;//((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				//.getMap();

		final CameraPosition Lebanon = new CameraPosition.Builder()
				.target(new LatLng(33.8869, 35.5131)).zoom(12).build();

		map.setOnCameraChangeListener(new OnCameraChangeListener() {

			@Override
			public void onCameraChange(CameraPosition arg0) { // Movcamera.
				// map.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),
				// 60));
				map.animateCamera(CameraUpdateFactory
						.newCameraPosition(Lebanon));

				// Remove listener to prevent position reset on camera move.
				map.setOnCameraChangeListener(null);
			}
		});

		map.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng point) {
				Intent returnIntent = new Intent();
				returnIntent.putExtra("lat", point.latitude);
				returnIntent.putExtra("long", point.longitude);
				setResult(RESULT_OK, returnIntent);
				//moveTaskToBack(true);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

}

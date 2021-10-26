package com.ir.hodicohiff;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.Window;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import Classes.Station;
import Utilities.GPSTracker;

public class MapActivity extends FragmentActivity {

	private GoogleMap map;
	private MapView mapView;
    private GPSTracker gpsTracker;
    private LatLngBounds.Builder builder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_map);


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapfragment);


        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                setUpMap(googleMap);
            }

            //Gets to GoogleMap from the MapView and does initialization stuff
            private void setUpMap(GoogleMap googleMap) {
                MapsInitializer.initialize(getApplicationContext());
                map = googleMap;

                GPSTracker gpsTracker = new GPSTracker(getApplicationContext());

                if (gpsTracker.canGetLocation()) {

                    gpsTracker.getLocation();

                    // map.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);
                }

                final CameraPosition Lebanon = new CameraPosition.Builder()
                        .target(new LatLng(33.8869, 35.5131)).zoom(12).build();

                map.animateCamera(CameraUpdateFactory.newCameraPosition(Lebanon));

                /*map.setOnCameraChangeListener(new OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(CameraPosition arg0) { // Movcamera.
                        // map.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),
                        // 60));
                        map.animateCamera(CameraUpdateFactory
                                .newCameraPosition(Lebanon));
                        // Remove listener to prevent position reset on camera move.
                        map.setOnCameraChangeListener(null);
                    }
                });*/



                map.setOnCameraChangeListener(new OnCameraChangeListener() {

                    @Override
                    public void onCameraChange(CameraPosition arg0) {
                        try {
                            // Move camera.
                            // map.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),
                            // 60));
                            map.animateCamera(CameraUpdateFactory.newLatLngBounds(
                                    builder.build(), 60));

                            // Remove listener to prevent position reset on camera move.
                            map.setOnCameraChangeListener(null);
                        }
                        catch (Exception e) {
                        }

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
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}



}

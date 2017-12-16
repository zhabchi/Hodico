package com.ir.hodicohiff;

import Classes.Station;
import Utilities.GPSTracker;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class StationsMapFragment extends SherlockFragment {

	private GoogleMap map;

	private GPSTracker gpsTracker;

	// private Tools mTools;

	private MapView mapView;
	private LatLngBounds.Builder builder;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_stations_map_fragment,
				container, false);

		// mTools = new Tools(getActivity());

		builder = new LatLngBounds.Builder();

		// Gets the MapView from the XML layout and creates it
		mapView = (MapView) v.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);

		MapsInitializer.initialize(getActivity());

		// Gets to GoogleMap from the MapView and does initialization stuff
		map = mapView.getMap();

		gpsTracker = new GPSTracker(getActivity());

		if (gpsTracker.canGetLocation()) {

			gpsTracker.getLocation();

			// map.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);
		}

		for (Station s : StationsPage.stations) {
			Marker stationMarker = map.addMarker(new MarkerOptions().position(
					s.getPosition()).title(s.getName()));

			builder.include(stationMarker.getPosition());

		}

		map.setOnCameraChangeListener(new OnCameraChangeListener() {

			@Override
			public void onCameraChange(CameraPosition arg0) {
				// Move camera.
				// map.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),
				// 60));
				map.animateCamera(CameraUpdateFactory.newLatLngBounds(
						builder.build(), 60));

				// Remove listener to prevent position reset on camera move.
				map.setOnCameraChangeListener(null);
			}
		});

		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {

				double latitude = marker.getPosition().latitude;
				double longitude = marker.getPosition().longitude;
				String label = marker.getTitle();
				String uriBegin = "geo:" + latitude + "," + longitude;
				String query = latitude + "," + longitude + "(" + label + ")";
				String encodedQuery = Uri.encode(query);
				String uriString = uriBegin + "?q=" + encodedQuery + "&z=12";
				Uri uri = Uri.parse(uriString);
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
						uri);
				startActivity(intent);

				// For Navigation
				/*
				 * startActivity(new Intent(Intent.ACTION_VIEW, Uri
				 * .parse("google.navigation:ll=" +
				 * marker.getPosition().latitude + "," +
				 * marker.getPosition().longitude + "&mode=c")));
				 */

			}
		});

		map.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				if (marker.isInfoWindowShown()) {
					marker.hideInfoWindow();
				} else {
					marker.showInfoWindow();
				}
				return false;
			}
		});

		return v;
	}

	@Override
	public void onResume() {
		mapView.onResume();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}
}

/*
 * @Override public void onActivityCreated(Bundle savedInstanceState) {
 * super.onActivityCreated(savedInstanceState);
 * 
 * // mapView = getMap();
 * 
 * MapView mapView = (MapView) frag.findViewById(R.id.map);
 * 
 * map = mapView.getMap();
 * 
 * gpsTracker = new GPSTracker(getActivity());
 * 
 * if (gpsTracker.canGetLocation()) {
 * 
 * gpsTracker.getLocation();
 * 
 * // Move the camera instantly to hamburg with a zoom of 15.
 * //map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
 * //gpsTracker.getLatitude(), gpsTracker.getLongitude()), 12));
 * 
 * // Zoom in, animating the camera.
 * map.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null); }
 * 
 * // Get Data // mTools.showLoadingDialog(); mWeb = new
 * WebHttpRequest(getActivity(), WebHttpRequest.WEB_STATIONS, new
 * OnTaskCompleted() {
 * 
 * @Override public void onTaskError(VolleyError arg0) {
 * mTools.displayAlert("Error", "Please connect to the internet",
 * android.R.string.ok); mTools.hideLoadingDialog(); }
 * 
 * @Override public void onTaskCompleted(JSONArray arg0) {
 * 
 * int id; String name = ""; String description = ""; String address = "";
 * double longitude; double latitude; int type;
 * 
 * Station s; stations = new ArrayList<Station>();
 * 
 * List<JSONObject> results; results = mTools.parseJson(arg0);
 * 
 * try { for (int i = 0; i < results.size(); i++) { s = new Station(); id =
 * results.get(i).getInt("station_id"); name =
 * results.get(i).getString("station_name"); address = results.get(i).getString(
 * "station_address"); description = results.get(i).getString(
 * "station_description"); longitude = results.get(i).getDouble(
 * "station_long"); latitude = results.get(i).getDouble( "station_lat"); type =
 * results.get(i).getInt("station_type");
 * 
 * s.setId(id); s.setName(name); s.setAddress(address);
 * s.setDescription(description); s.setPosition(latitude, longitude);
 * s.setType(type); stations.add(s);
 * 
 * @SuppressWarnings("unused") Marker stationMarker = map .addMarker(new
 * MarkerOptions() .position(s.getPosition()) .title(s.getName()));
 * 
 * } mTools.hideLoadingDialog(); } catch (JSONException e) {
 * e.printStackTrace(); } }
 * 
 * @Override public void onTaskCompleted(String arg0) { method stub
 * 
 * } });
 * 
 * // Get Data at start mTools.showLoadingDialog(); mWeb.getJson();
 * 
 * map.setOnMarkerClickListener(new OnMarkerClickListener() {
 * 
 * @Override public boolean onMarkerClick(Marker marker) {
 * 
 * double latitude = marker.getPosition().latitude; double longitude =
 * marker.getPosition().longitude; String label = marker.getTitle(); String
 * uriBegin = "geo:" + latitude + "," + longitude; String query = latitude + ","
 * + longitude + "(" + label + ")"; String encodedQuery = Uri.encode(query);
 * String uriString = uriBegin + "?q=" + encodedQuery + "&z=12"; Uri uri =
 * Uri.parse(uriString); Intent intent = new
 * Intent(android.content.Intent.ACTION_VIEW, uri); startActivity(intent);
 * 
 * // For Navigation /* startActivity(new Intent(Intent.ACTION_VIEW, Uri
 * .parse("google.navigation:ll=" + marker.getPosition().latitude + "," +
 * marker.getPosition().longitude + "&mode=c")));
 * 
 * return false; } });
 * 
 * }
 */


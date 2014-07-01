package com.ir.hodicohiff;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Classes.Station;
import Utilities.OnTaskCompleted;
import Utilities.TabListener;
import Utilities.Tools;
import Utilities.WebHttpRequest;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.android.volley.VolleyError;

public class StationsPage extends SherlockFragmentActivity {

	public static ArrayList<Station> stations;
	private WebHttpRequest mWeb;
	private Tools mTools;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTools = new Tools(StationsPage.this, this);
		mTools.setHeader(R.drawable.ma7atatakhd);
		setContentView(R.layout.activity_stations_page);

		mTools = new Tools(this);

		mWeb = new WebHttpRequest(this, WebHttpRequest.WEB_STATIONS,
				new OnTaskCompleted() {

					@Override
					public void onTaskError(VolleyError arg0) {
						mTools.displayAlert("Error",
								"Please connect to the internet",
								android.R.string.ok, true);
						mTools.hideLoadingDialog();
					}

					@Override
					public void onTaskCompleted(JSONArray arg0) {

						int id;
						String name = "";
						String description = "";
						String address = "";
						double longitude;
						double latitude;
						int type;

						Station s;
						stations = new ArrayList<Station>();

						List<JSONObject> results;
						results = mTools.parseJson(arg0);

						try {
							for (int i = 0; i < results.size(); i++) {
								s = new Station();
								id = results.get(i).getInt("station_id");
								name = results.get(i).getString("station_name");
								address = results.get(i).getString(
										"station_address");
								description = results.get(i).getString(
										"station_description");
								longitude = results.get(i).getDouble(
										"station_long");
								latitude = results.get(i).getDouble(
										"station_lat");
								type = results.get(i).getInt("station_type");

								s.setId(id);
								s.setName(name);
								s.setAddress(address);
								s.setDescription(description);
								s.setPosition(latitude, longitude);
								s.setType(type);
								stations.add(s);

							}
							createTabs();
							mTools.hideLoadingDialog();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onTaskCompleted(String arg0) {

					}
				});

		// Get Data at start
		mTools.showLoadingDialog();
		mWeb.getJson();

	}

	@Override
	public void onBackPressed() {
		MenuPage.i = MenuPage.Menu.size();
		finish();
	}

	public void createTabs() {

		// ActionBar gets initiated
		ActionBar actionbar = getSupportActionBar();
		actionbar.setStackedBackgroundDrawable(new ColorDrawable(
				android.R.color.transparent));
		// Tell the ActionBar we want to use Tabs.
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// initiating both tabs and set text to it.
		ActionBar.Tab MapTab = actionbar.newTab().setText("Map");
		ActionBar.Tab StationsTab = actionbar.newTab().setText("Search");
		
		View tabView = getLayoutInflater().inflate(R.layout.tabs, null);
		TextView tabText = (TextView) tabView.findViewById(R.id.tab_title);
		tabText.setText("Map");
		Drawable image = getResources().getDrawable(R.drawable.map);
		tabText.setCompoundDrawablePadding(5);
		tabText.setCompoundDrawablesWithIntrinsicBounds(null, image, null, null);
		MapTab.setCustomView(tabView);
		
		
		tabView = getLayoutInflater().inflate(R.layout.tabs, null);
		tabText = (TextView) tabView.findViewById(R.id.tab_title);
		tabText.setText("Search");
		image = getResources().getDrawable(R.drawable.search);
		tabText.setCompoundDrawablePadding(5);
		tabText.setCompoundDrawablesWithIntrinsicBounds(null, image, null, null);
		StationsTab.setCustomView(tabView);

		// create the two fragments we want to use for display content
		Fragment StationsMapFragment = new StationsMapFragment();
		Fragment StationsSearchFragment = new SearchStationsFragment();

		// set the Tab listener. Now we can listen for clicks.
		MapTab.setTabListener(new TabListener(StationsMapFragment));
		StationsTab.setTabListener(new TabListener(StationsSearchFragment));

		// add the two tabs to the actionbar
		actionbar.addTab(MapTab);
		actionbar.addTab(StationsTab);
	}

}
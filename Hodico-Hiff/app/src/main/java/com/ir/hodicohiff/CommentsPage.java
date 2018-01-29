package com.ir.hodicohiff;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Adapters.StationsAdapterDropDown;
import Classes.Station;
import Utilities.OnTaskCompleted;
import Utilities.Tools;
import Utilities.WebHttpRequest;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.actionbarsherlock.app.SherlockActivity;
import com.android.volley.VolleyError;

public class CommentsPage extends Activity {

	public static ArrayList<Station> stations;
	private WebHttpRequest mWeb;
	private Tools mTools;

	private StationsAdapterDropDown adapter;

	private String url;

	private Spinner spStations;
	private Button btnSend;
	private RatingBar nazafa, estekbal, khedma, tawafor, khousoumat, bida3a;
	private TextView tvurl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		mTools = new Tools(CommentsPage.this);
		mTools.setHeader(R.drawable.ta3li2akhd);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_comments_page);


		spStations = (Spinner) findViewById(R.id.spStations);

		nazafa = (RatingBar) findViewById(R.id.Nazafa);
		estekbal = (RatingBar) findViewById(R.id.Estekbal);
		khedma = (RatingBar) findViewById(R.id.Khedma);
		tawafor = (RatingBar) findViewById(R.id.Tawafor);
		khousoumat = (RatingBar) findViewById(R.id.Khousoumat);
		bida3a = (RatingBar) findViewById(R.id.Bida3a);

		btnSend = (Button) findViewById(R.id.btnSend);
		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				url = CommentsPage.this
						.getString(com.ir.hodicohiff.R.string.Feedback)
						+ "?insert=true&email="
						+ mTools.getPrimaryEmail()
						+ "&phone="
						+ mTools.getPhoneNumber()
						+ "&recp="
						+ estekbal.getRating()
						+ "&clean="
						+ nazafa.getRating()
						+ "&serv="
						+ khedma.getRating()
						+ "&disc="
						+ khousoumat.getRating()
						+ "&avail="
						+ tawafor.getRating()
						+ "&cond="
						+ bida3a.getRating()
						+ "&stationId="
						+ ((Station) spStations.getSelectedItem()).getId();

				mWeb = new WebHttpRequest(CommentsPage.this, url,
						new OnTaskCompleted() {

							@Override
							public void onTaskError(VolleyError arg0) {
								// TODO Auto-generated method stub
								mTools.displayAlert("Error",
										"Please connect to the internet",
										android.R.string.ok, false);
								mTools.hideLoadingDialog();
							}

							@Override
							public void onTaskCompleted(JSONArray arg0) {

							}

							@Override
							public void onTaskCompleted(String arg0) {
								if (!arg0.equals("0")) {
									mTools.displayToast(
											"Thank you for your feedback!",
											Toast.LENGTH_SHORT);
								} else {
									mTools.displayToast(
											"Something went wrong! Your feedback wasn't sent!",
											Toast.LENGTH_SHORT);
								}
								mTools.hideLoadingDialog();
							}
						});
				mWeb.getString();
			}

		});

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

							adapter = new StationsAdapterDropDown(
									CommentsPage.this, stations);
							spStations.setAdapter(adapter);
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

}

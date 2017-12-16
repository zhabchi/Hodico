package com.ir.hodicohiff;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Classes.Product;
import Classes.StringWithTag;
import Utilities.OnTaskCompleted;
import Utilities.TabListener;
import Utilities.Tools;
import Utilities.WebHttpRequest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.android.volley.VolleyError;

public class CarPage extends SherlockFragmentActivity {

	private Tools mTools;
	
	private TextView tvurl;

	public static List<StringWithTag> CarTypes;
	public static List<StringWithTag> CarMakes;
	public static List<StringWithTag> HorsePowers;
	public static List<StringWithTag> Symbols;
	public static List<Product> Products;

	private WebHttpRequest mWeb1;
	private WebHttpRequest mWeb2;
	private WebHttpRequest mWeb3;
	private WebHttpRequest mWeb4;
	private WebHttpRequest mWeb5;

	private static int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTools = new Tools(CarPage.this, this);
		mTools.setHeader(R.drawable.sayaratakhd);
		setContentView(R.layout.activity_car_page);
		
		tvurl = (TextView)findViewById(R.id.tvurl);
		Linkify.addLinks(tvurl, Linkify.ALL);
		tvurl.setLinkTextColor(Color.WHITE);

		count = 0;

		mWeb1 = new WebHttpRequest(getApplicationContext(),
				WebHttpRequest.WEB_TAX_CARTYPE, new OnTaskCompleted() {

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
						String descr = "";

						StringWithTag type;
						CarTypes = new ArrayList<StringWithTag>();

						List<JSONObject> results;
						results = mTools.parseJson(arg0);

						try {
							for (int i = 0; i < results.size(); i++) {
								type = new StringWithTag();
								id = results.get(i).getInt("car_type_id");
								descr = results.get(i).getString(
										"car_type_description");
								type.setTag(id);
								type.setString(descr);
								CarTypes.add(type);

							}
							count++;
							if (count == 5) {
								mTools.hideLoadingDialog();
								createTabs();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onTaskCompleted(String arg0) {
						// TODO Auto-generated method stub

					}
				});

		mWeb2 = new WebHttpRequest(getApplicationContext(),
				WebHttpRequest.WEB_TAX_CARYEARMAKE, new OnTaskCompleted() {

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
						String descr = "";

						StringWithTag make;
						CarMakes = new ArrayList<StringWithTag>();

						List<JSONObject> results;
						results = mTools.parseJson(arg0);

						try {
							for (int i = 0; i < results.size(); i++) {
								make = new StringWithTag();
								id = results.get(i).getInt("car_yearmake_id");
								descr = results.get(i).getString(
										"car_yearmake_description");
								make.setTag(id);
								make.setString(descr);
								CarMakes.add(make);

							}
							count++;
							if (count == 5) {
								mTools.hideLoadingDialog();
								createTabs();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onTaskCompleted(String arg0) {
						// TODO Auto-generated method stub

					}
				});

		mWeb3 = new WebHttpRequest(getApplicationContext(),
				WebHttpRequest.WEB_TAX_CARHORSEPOWER, new OnTaskCompleted() {

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
						String descr = "";

						StringWithTag horsepower;
						HorsePowers = new ArrayList<StringWithTag>();

						List<JSONObject> results;
						results = mTools.parseJson(arg0);

						try {
							for (int i = 0; i < results.size(); i++) {
								horsepower = new StringWithTag();
								id = results.get(i).getInt("car_horsepower_id");
								descr = results.get(i).getString(
										"car_horsepower_description");
								horsepower.setTag(id);
								horsepower.setString(descr);
								HorsePowers.add(horsepower);

							}
							count++;
							if (count == 5) {
								mTools.hideLoadingDialog();
								createTabs();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onTaskCompleted(String arg0) {
						// TODO Auto-generated method stub

					}
				});

		mWeb4 = new WebHttpRequest(getApplicationContext(),
				WebHttpRequest.WEB_TAX_CARSYMBOL, new OnTaskCompleted() {

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
						String descr = "";

						StringWithTag symbol;
						Symbols = new ArrayList<StringWithTag>();

						List<JSONObject> results;
						results = mTools.parseJson(arg0);

						try {
							for (int i = 0; i < results.size(); i++) {
								symbol = new StringWithTag();
								id = results.get(i).getInt("car_symbol_id");
								descr = results.get(i).getString(
										"car_symbol_desc");
								symbol.setTag(id);
								symbol.setString(descr);
								Symbols.add(symbol);

							}
							count++;
							if (count == 5) {
								mTools.hideLoadingDialog();
								createTabs();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onTaskCompleted(String arg0) {
						// TODO Auto-generated method stub

					}
				});

		mWeb5 = new WebHttpRequest(getApplicationContext(),
				WebHttpRequest.WEB_PRICESTRIP, new OnTaskCompleted() {

					@Override
					public void onTaskError(VolleyError arg0) {
						// TODO Auto-generated method stub
						mTools.displayAlert("Error",
								"Please connect to the internet",
								android.R.string.ok, true);
						mTools.hideLoadingDialog();
					}

					@Override
					public void onTaskCompleted(JSONArray arg0) {

						int id;
						String product = "";
						int price = 0;
						Product p;
						Products = new ArrayList<Product>();

						List<JSONObject> results;
						results = mTools.parseJson(arg0);

						try {
							for (int i = 0; i < results.size(); i++) {
								p = new Product();
								id = results.get(i).getInt("pp_product_id");
								product = results.get(i).getString(
										"product_description");
								price = results.get(i).getInt(
										"pp_product_price");
								p.setProduct(product);
								p.setPrice(price);
								p.setId(id);
								Products.add(p);
							}
							count++;
							if (count == 5) {
								mTools.hideLoadingDialog();
								createTabs();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onTaskCompleted(String arg0) {
						// TODO Auto-generated method stub

					}
				});

		mTools.showLoadingDialog();
		mWeb1.getJson();
		mWeb2.getJson();
		mWeb3.getJson();
		mWeb4.getJson();
		mWeb5.getJson();

	}

	public void createTabs() {

		// ActionBar gets initiated
		ActionBar actionbar = getSupportActionBar();
		actionbar.setStackedBackgroundDrawable(new ColorDrawable(
				android.R.color.transparent));
		// Tell the ActionBar we want to use Tabs.
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// initiating both tabs and set text to it.
		ActionBar.Tab TaxTab = actionbar.newTab();
		ActionBar.Tab OilTab = actionbar.newTab();
		ActionBar.Tab TripTab = actionbar.newTab();
		
		View tabView = getLayoutInflater().inflate(R.layout.tabs, null);
		TextView tabText = (TextView) tabView.findViewById(R.id.tab_title);
		tabText.setText("Mechanic");
		Drawable image = getResources().getDrawable(R.drawable.tax);
		tabText.setCompoundDrawablePadding(5);
		tabText.setCompoundDrawablesWithIntrinsicBounds(null, image, null, null);
		TaxTab.setCustomView(tabView);
		
		
		tabView = getLayoutInflater().inflate(R.layout.tabs, null);
		tabText = (TextView) tabView.findViewById(R.id.tab_title);
		tabText.setText("Oil");
		image = getResources().getDrawable(R.drawable.oil);
		tabText.setCompoundDrawablePadding(5);
		tabText.setCompoundDrawablesWithIntrinsicBounds(null, image, null, null);
		OilTab.setCustomView(tabView);
		
		tabView = getLayoutInflater().inflate(R.layout.tabs, null);
		tabText = (TextView) tabView.findViewById(R.id.tab_title);
		tabText.setText("Trip");
		image = getResources().getDrawable(R.drawable.trip);
		tabText.setCompoundDrawablePadding(5);
		tabText.setCompoundDrawablesWithIntrinsicBounds(null, image, null, null);
		TripTab.setCustomView(tabView);
		
		// create the two fragments we want to use for display content
		Fragment TaxFragment = new TaxFragment();
		Fragment OilFragment = new OilFragment();
		Fragment TripCostFragment = new TripCostFragment();

		// set the Tab listener. Now we can listen for clicks.
		TaxTab.setTabListener(new TabListener(TaxFragment));
		OilTab.setTabListener(new TabListener(OilFragment));
		TripTab.setTabListener(new TabListener(TripCostFragment));

		// add the two tabs to the actionbar
		actionbar.addTab(TaxTab);
		actionbar.addTab(OilTab);
		actionbar.addTab(TripTab);
	}

	@Override
	public void onBackPressed() {
		MenuPage.i = MenuPage.Menu.size();
		finish();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case OilFragment.DATE_PICKER_ID:

			// open datepicker dialog.
			// set date picker for current date
			// add pickerListener listner to date picker
			return new DatePickerDialog(this, pickerListener, OilFragment.year,
					OilFragment.month, OilFragment.day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		@Override
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			OilFragment.year = selectedYear;
			OilFragment.month = selectedMonth;
			OilFragment.day = selectedDay;

			// Show selected date
			OilFragment.txtDate.setText(new StringBuilder()
					.append(OilFragment.day).append("/")
					.append(OilFragment.month + 1).append("/")
					.append(OilFragment.year).append(" "));

		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

}

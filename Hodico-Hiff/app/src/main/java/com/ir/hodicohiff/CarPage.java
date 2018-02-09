package com.ir.hodicohiff;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import Adapters.CarsSectionsPagerAdapter;
import Classes.Product;
import Classes.StringWithTag;
import Utilities.OnTaskCompleted;

import Utilities.Tools;
import Utilities.WebHttpRequest;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import android.view.Window;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.VolleyError;

public class CarPage extends Activity implements ActionBar.TabListener {

	private Tools mTools;
	
	private TextView tvurl;




	public static List<StringWithTag> Symbols;




	private WebHttpRequest mWeb4;


	private static int count = 0;
    private CarsSectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTools = new Tools(CarPage.this);
		mTools.setHeader(R.drawable.sayaratakhd);

		setContentView(R.layout.activity_car_page);

		//PopulateAllData();

		//tvurl = (TextView)findViewById(R.id.tvurl);
		//Linkify.addLinks(tvurl, Linkify.ALL);
		//tvurl.setLinkTextColor(Color.WHITE);

		setContentView(R.layout.activity_car_page);
		getActionBar().setDisplayHomeAsUpEnabled(false); //disabled action bar

        mSectionsPagerAdapter = new CarsSectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.Carcontainer);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(
					actionBar.newTab()
							.setText(mSectionsPagerAdapter.getPageTitle(i))
							.setTabListener(this)
							.setIcon(mSectionsPagerAdapter.getPageIcon(i)));

		}
		actionBar.setDisplayShowTitleEnabled(false);

	}

	/*public void createTabs() {

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
*/
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


	private void PopulateAllData(){

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
						//createTabs();
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



		mTools.showLoadingDialog();



		mWeb4.getJson();

	}

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}

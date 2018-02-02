package com.ir.hodicohiff;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import Adapters.ProductsAdapter;
import Classes.Product;
import Utilities.GPSTracker;
import Utilities.Tools;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.actionbarsherlock.app.SherlockFragment;

public class TripCostFragment extends Fragment {

	private EditText etSource, etDestination, etAvgKm;
	private TextView txtCost, txtLiters;
	private Spinner spFuelTypes;

	private ImageButton btnToMyLocation, btnFromMyLocation;

	private Button btnSubmit;

	private GPSTracker gpsTracker;

	private Tools mTools;

	// private int RESULT_OK = 1;
	// private int RESULT_CANCELED = 0;

	//@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_trip_cost_fragment,
				container, false);

		/*mTools = new Tools(getContext());

		btnFromMyLocation = (ImageButton) v.findViewById(R.id.FromMyLocation);
		btnToMyLocation = (ImageButton) v.findViewById(R.id.ToMyLocation);

		btnSubmit = (Button) v.findViewById(R.id.btnSubmit);

		txtCost = (TextView) v.findViewById(R.id.txtCostRes);
		txtLiters = (TextView) v.findViewById(R.id.txtLitersRes);

		etSource = (EditText) v.findViewById(R.id.etSource);
		etDestination = (EditText) v.findViewById(R.id.etDestination);
		etAvgKm = (EditText) v.findViewById(R.id.etAvgKm);
		spFuelTypes = (Spinner) v.findViewById(R.id.spFuelType);

		ProductsAdapter dataAdapter = new ProductsAdapter(getActivity(), CarPage.Products);
		spFuelTypes.setAdapter(dataAdapter);

		etSource.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					Intent i = new Intent(getContext(), MapActivity.class);
					// i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					etSource.setInputType(InputType.TYPE_NULL); // disable soft
																// input
					// etSource.onTouchEvent(event); // call native handler
					etSource.setFocusable(false);
					startActivityForResult(i, 1);
				}
				return true;
			}
		});


		 // etSource.setOnClickListener(new OnClickListener() {

		//  @Override public void onClick(View v) {


		//  } });


		etDestination.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					Intent i = new Intent(getContext(), MapActivity.class);
					// i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					etDestination.setInputType(InputType.TYPE_NULL); // disable
																		// soft
					// input
					// etSource.onTouchEvent(event); // call native handler
					etDestination.setFocusable(false);
					startActivityForResult(i, 2);
				}
				return true;
			}
		});
		*/
		/*
		 * etDestination.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * 
		 * Intent i = new Intent(getActivity(), MapActivity.class);
		 * i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		 * startActivityForResult(i, 2); } });
		 */

		/*gpsTracker = new GPSTracker(getContext());

		btnFromMyLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (gpsTracker.canGetLocation()) {

					Location current = gpsTracker.getLocation();
					String location = String.valueOf(current.getLatitude())
							+ ", " + String.valueOf(current.getLongitude());
					etSource.setText(location);
				} else {
					mTools.displayToast("Location Not Available!",
							Toast.LENGTH_SHORT);
				}

			}
		});

		btnToMyLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (gpsTracker.canGetLocation()) {

					Location current = gpsTracker.getLocation();
					String location = String.valueOf(current.getLatitude())
							+ ", " + String.valueOf(current.getLongitude());
					etDestination.setText(location);
				} else {
					mTools.displayToast("Location Not Available!",
							Toast.LENGTH_SHORT);
				}

			}
		});

		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mTools.hideSoftInput();

				if (!etAvgKm.getText().toString().equals(null)
						&& !etAvgKm.getText().toString().equals("")
						&& !etDestination.getText().toString().equals(null)
						&& !etDestination.getText().toString().equals("")
						&& !etSource.getText().toString().equals(null)
						&& !etSource.getText().toString().equals("")) {
					Location locationA = new Location("point A");

					String latA = etSource.getText().toString().split(",")[0];
					String lngA = etSource.getText().toString().split(",")[1];

					locationA.setLatitude(Double.parseDouble(latA));
					locationA.setLongitude(Double.parseDouble(lngA));

					Location locationB = new Location("point B");

					String latB = etDestination.getText().toString().split(",")[0];
					String lngB = etDestination.getText().toString().split(",")[1];

					locationB.setLatitude(Double.parseDouble(latB));
					locationB.setLongitude(Double.parseDouble(lngB));

					float distance = locationA.distanceTo(locationB);
					double distancekm = distance * 0.001;

					// float liters = (float) (distancekm * 20 / Double
					// .parseDouble(etAvgKm.getText().toString()));

					float liters = (float) (distancekm * 20 / Double
							.parseDouble(etAvgKm.getText().toString()));
					double price = ((Product) spFuelTypes.getSelectedItem())
							.getPrice();
					float cost = (float) (price * (liters / 20));

					DecimalFormatSymbols symbols = new DecimalFormatSymbols();
					symbols.setGroupingSeparator(',');
					symbols.setDecimalSeparator('.');

					DecimalFormat decimalFormat = new DecimalFormat(
							"#,### LL", symbols);
					DecimalFormat litersFormat = new DecimalFormat(
							"#,##0.00 Liters", symbols);
					String tripcost = decimalFormat.format(cost);
					String tripliters = litersFormat.format(liters);
					txtLiters.setText(tripliters);
					txtCost.setText(tripcost);
				} else {
					mTools.displayToast("Please fill all fields correctly!",
							Toast.LENGTH_SHORT);
				}
			}
		});*/

		return v;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {

			if (resultCode == Activity.RESULT_OK) {
				double lat = data.getDoubleExtra("lat", 33.8869);
				double lng = data.getDoubleExtra("long", 35.5131);
				etSource.setText(lat + ", " + lng);
			}
			if (resultCode == Activity.RESULT_CANCELED) {
				mTools.displayToast("No Location Chosen", Toast.LENGTH_SHORT);
			}
		}

		if (requestCode == 2) {

			if (resultCode == Activity.RESULT_OK) {
				double lat = data.getDoubleExtra("lat", 33.8869);
				double lng = data.getDoubleExtra("long", 35.5131);
				etDestination.setText(lat + ", " + lng);
			}
			if (resultCode == Activity.RESULT_CANCELED) {
				mTools.displayToast("No Location Chosen", Toast.LENGTH_SHORT);
			}
		}
	}// onActivityResult

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
}

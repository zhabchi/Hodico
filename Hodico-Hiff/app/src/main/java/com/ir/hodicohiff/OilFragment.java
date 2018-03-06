package com.ir.hodicohiff;

import java.util.Calendar;

import Utilities.Tools;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class OilFragment extends Fragment {

	public static TextView txtDate;
	private Button btnChangeDate, btnSubmit;
	private EditText etAvgKm, etAfter;

	public static int year;
	public static int month;
	public static int day;

	static final int DATE_PICKER_ID = 1111;

	Tools mTools;

	//@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_oil_fragement, container,
				false);

		mTools = new Tools(getContext());

		etAvgKm = (EditText) v.findViewById(R.id.etAvgKmDay);
		etAfter = (EditText) v.findViewById(R.id.etAfterKm);
		txtDate = (TextView) v.findViewById(R.id.txtDate);
		btnChangeDate = (Button) v.findViewById(R.id.changeDate);
		btnSubmit = (Button) v.findViewById(R.id.btnReminder);

		// Get current date by calender

		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		// Show current date

		txtDate.setText(new StringBuilder()
				// Month is 0 based, just add 1
				.append(day).append("/").append(month + 1).append("/")
				.append(year).append(" "));

		// Button listener to show date picker dialog

		btnChangeDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mTools.hideSoftInput();
				// On button click show datepicker dialog
				getActivity().showDialog(DATE_PICKER_ID);

			}

		});

		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//getActivity().getWindow().setSoftInputMode(
				//		WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

				if (!etAvgKm.getText().equals(null)
						&& !etAvgKm.getText().toString().equals("")
						&& !etAfter.getText().toString().equals(null)
						&& !etAfter.getText().toString().equals("")) {
					int kmday = Integer.parseInt(etAvgKm.getText().toString());
					int afterkm = Integer
							.parseInt(etAfter.getText().toString());
					int daystoAdd = afterkm / kmday;
					mTools.setReminder(txtDate.getText().toString(), daystoAdd);
				} else {
					mTools.displayToast("Please fill all fields correctly!",
							Toast.LENGTH_SHORT);
				}
			}
		});

		return v;
	}

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
}

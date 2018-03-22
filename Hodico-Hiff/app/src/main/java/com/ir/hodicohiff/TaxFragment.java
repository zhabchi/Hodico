package com.ir.hodicohiff;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Classes.StringWithTag;
import Utilities.OnTaskCompleted;
import Utilities.Tools;
import Utilities.WebHttpRequest;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;

public class TaxFragment extends Fragment {

	private Spinner spCarType, spCarMake, spHorsePower; //spSymbol;

	private Button btnSubmit;

	//private EditText etPlateNb;

	Tools mTools;

	private WebHttpRequest mWeb;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_tax_fragment, container,
				false);

		//mTools = new Tools(getActivity());
        mTools = new Tools(getContext());
        //mTools.setHeader(R.drawable.sayaratakhd);
		btnSubmit = (Button) v.findViewById(R.id.btnSubmit);

		//etPlateNb = (EditText) v.findViewById(R.id.etCarPlate);

		spCarType = (Spinner) v.findViewById(R.id.spCarType);

		spCarMake = (Spinner) v.findViewById(R.id.spCarMake);

		spHorsePower = (Spinner) v.findViewById(R.id.spCarHorsePower);

		//spSymbol = (Spinner) v.findViewById(R.id.spCarSymbol);

        populateCarType();

        populateCarMake();

        populateCarHorsePower();



		ArrayAdapter<StringWithTag> CarSymbolAdap = new ArrayAdapter<StringWithTag>(
				getActivity(), R.layout.spin_layout, CarPage.Symbols);

		//spSymbol.setAdapter(CarSymbolAdap);

		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mTools.hideSoftInput();				if (Validate()) {

					String cartype = ((StringWithTag) (spCarType
							.getSelectedItem())).getTag().toString();
					String carmake = ((StringWithTag) (spCarMake
							.getSelectedItem())).getTag().toString();
					String carhorse = ((StringWithTag) (spHorsePower
							.getSelectedItem())).getTag().toString();
					//String carsymbol = ((StringWithTag) (spSymbol
							//.getSelectedItem())).getTag().toString();
					//String platenb = etPlateNb.getText().toString();

					String url = getActivity().getResources().getString(
							R.string.Tax)
							+ "?getdata=true&cartype="
							+ cartype
							+ "&carmake="
							+ carmake
							+ "&carhorsepower="
							+ carhorse;
							//+ "&carplatenb="
							//+ platenb
							//+ "&carsymbol="
							//+ carsymbol;

					mWeb = new WebHttpRequest(getActivity(), url,
							new OnTaskCompleted() {

								@Override
								public void onTaskError(VolleyError arg0) {
									mTools.displayAlert("Error",
											"Please connect to the internet",
											android.R.string.ok, false);
									mTools.hideLoadingDialog();
								}

								@Override
								public void onTaskCompleted(JSONArray arg0) {
									String amount = "";
									String period = "";

									List<JSONObject> results;
									results = mTools.parseJson(arg0);

									try {
										for (int i = 0; i < results.size(); i++) {
											amount = results.get(i).getString(
													"tax_amount_due")
													+ " LL";
											period = results.get(i).getString(
													"tax_due_period");
										}


										mTools.displayAlert("Tax",
												"Amount due: " + amount,
														//+ "\nPeriod due: "
														//+ period,
												android.R.string.ok, false);

									} catch (JSONException e) {
										e.printStackTrace();
									}
                                    mTools.hideLoadingDialog();
								}

								@Override
								public void onTaskCompleted(String arg0) {
									mTools.hideLoadingDialog();
								}
							});

					mTools.showLoadingDialog();
					mWeb.getJson();

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

	public boolean Validate() {
		boolean result = true;
		//if (etPlateNb.getText().toString().equals("")
			//	|| etPlateNb.getText() == null) {
			//result = false;
		//}
		return result;
	}

	private void populateCarType()
    {

        WebHttpRequest mWeb1 = new WebHttpRequest(this.getContext(),
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

                List<StringWithTag> CarTypes = new ArrayList<StringWithTag>();

                StringWithTag type;
                CarTypes.clear();

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

                    if(CarTypes != null) {
                        ArrayAdapter<StringWithTag> CarTypeAdap = new ArrayAdapter<StringWithTag>(
                                getActivity(), R.layout.spin_layout, CarTypes);

                        spCarType.setAdapter(CarTypeAdap);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mTools.hideLoadingDialog();

            }

            @Override
            public void onTaskCompleted(String arg0) {
                // TODO Auto-generated method stub

            }
        });

        if(mTools.isOnline())
            mTools.showLoadingDialog();
        mWeb1.getJson();
    }

    private void populateCarMake()
    {
         WebHttpRequest mWeb2 = new WebHttpRequest(getContext(),
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
                List<StringWithTag> CarMakes = new ArrayList<StringWithTag>();

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

                    if(CarMakes != null) {
                        ArrayAdapter<StringWithTag> CarMakeAdap = new ArrayAdapter<StringWithTag>(
                                getActivity(), R.layout.spin_layout, CarMakes);

                        spCarMake.setAdapter(CarMakeAdap);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mTools.hideLoadingDialog();
            }

            @Override
            public void onTaskCompleted(String arg0) {
                // TODO Auto-generated method stub

            }
        });

        if(!mTools.isOnline())
            mTools.showLoadingDialog();
        mWeb2.getJson();
    }

    private void populateCarHorsePower() {

        WebHttpRequest mWeb3 = new WebHttpRequest(getContext(),
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
                List<StringWithTag> HorsePowers = new ArrayList<StringWithTag>();

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

                    if(HorsePowers!= null) {
                        ArrayAdapter<StringWithTag> CarHorsePowerAdap = new ArrayAdapter<StringWithTag>(
                                getActivity(), R.layout.spin_layout, HorsePowers);

                        spHorsePower.setAdapter(CarHorsePowerAdap);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mTools.hideLoadingDialog();
            }

            @Override
            public void onTaskCompleted(String arg0) {
                // TODO Auto-generated method stub

            }
        });

        if(!mTools.isOnline())
            mTools.showLoadingDialog();
	    mWeb3.getJson();
    }
}

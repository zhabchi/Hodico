package com.ir.hodicohiff;

import Adapters.StationsAdapter;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


public class SearchStationsFragment extends Fragment {

	private ListView lv;
	private EditText inputSearch;
	private TextView tvurl;

	StationsAdapter adapter;

	//@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_search_stations_fragment,
				container, false);
		lv = (ListView) v.findViewById(R.id.list_view);
		inputSearch = (EditText) v.findViewById(R.id.inputSearch);


		// Adding items to listview
		adapter = new StationsAdapter(this.getActivity(), StationsPage.stations);
		lv.setAdapter(adapter);

		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				adapter.setStationsHidden(StationsPage.stations);
				adapter.getFilter().filter(cs);
				// if (cs.length() == 0 || arg3 > arg3-arg2) {
				 adapter.setStationsHidden(StationsPage.stations);
				// }
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}
		});

		return v;
	}

	/*@Override
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
	}*/
}
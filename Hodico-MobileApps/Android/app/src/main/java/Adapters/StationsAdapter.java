package Adapters;

import java.util.ArrayList;
import java.util.List;

import Classes.Station;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.ir.hodicohiff.R;

public class StationsAdapter extends BaseAdapter {
	private Activity activity;
	private List<Station> stations;

	public StationsAdapter(Activity activity, List<Station> stations) {
		this.activity = activity;
		this.stations = stations;
	}

	public int getCount() {
		if(stations!= null)
		    return stations.size();
		else
		    return 0;
	}

	public Object getItem(int position) {
		return stations.get(position);
	}

	public long getItemId(int position) {
		return stations.get(position).getId();
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View itemLayout;
		if (convertView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			itemLayout = inflater.inflate(R.layout.stationitemlayout, null);
		} else {
			itemLayout = convertView;
		}
		TextView t1 = (TextView) itemLayout.findViewById(R.id.station_name);
		TextView t2 = (TextView) itemLayout.findViewById(R.id.station_address);
		t2.setText(String.valueOf(stations.get(position).getAddress()));
		t1.setText(String.valueOf(stations.get(position).getName()));
		t1.setTag(stations.get(position).getPosition());
		itemLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				TextView text = (TextView) v.findViewById(R.id.station_name);
				LatLng latlng = (LatLng) text.getTag();
				double latitude = latlng.latitude;
				double longitude = latlng.longitude;

				String label = (String) text.getText();
				String uriBegin = "geo:" + latitude + "," + longitude;
				String query = latitude + "," + longitude + "(" + label + ")";
				String encodedQuery = Uri.encode(query);
				String uriString = uriBegin + "?q=" + encodedQuery + "&z=12";
				Uri uri = Uri.parse(uriString);
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
						uri);
				activity.startActivity(intent);
			}
		});
		return itemLayout;
	}

	public void setStations(List<Station> stations) {
		this.stations = stations;
		notifyDataSetChanged();
	}

	public void setStationsHidden(List<Station> stations) {
		this.stations = stations;
	}

	public Filter getFilter() {
		return new Filter() {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				final FilterResults oReturn = new FilterResults();
				final ArrayList<Station> results = new ArrayList<Station>();
				List<Station> orig = null;
				if (orig == null)
					orig = stations;
				if (constraint != null) {
					if (stations != null && stations.size() > 0) {
						for (final Station s : stations) {
							if (s.getName().toLowerCase()
									.contains(constraint.toString())
									|| s.getAddress().toLowerCase()
											.contains(constraint.toString()))
								results.add(s);
						}
					}
					oReturn.values = results;
				}
				return oReturn;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				stations = (ArrayList<Station>) results.values;
				notifyDataSetChanged();
			}
		};
	}

	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
}
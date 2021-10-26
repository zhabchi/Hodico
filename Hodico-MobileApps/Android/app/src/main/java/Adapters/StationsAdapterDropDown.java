package Adapters;

import java.util.List;

import Classes.Station;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.ir.hodicohiff.R;

public class StationsAdapterDropDown extends BaseAdapter implements
		SpinnerAdapter {
	private Activity activity;
	private List<Station> stations;

	public StationsAdapterDropDown(Activity activity, List<Station> stations) {
		this.activity = activity;
		this.stations = stations;
	}

	public int getCount() {
		return stations.size();
	}

	public Object getItem(int position) {
		return stations.get(position);
	}

	public long getItemId(int position) {
		return stations.get(position).getId();
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View spinView;
		if (convertView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			spinView = inflater.inflate(R.layout.spin_layout, null);
		} else {
			spinView = convertView;
		}
		TextView t1 = (TextView) spinView.findViewById(R.id.Product);
		t1.setText(String.valueOf(stations.get(position).getName()));
		return spinView;
	}
}
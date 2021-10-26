package Adapters;

import java.util.List;

import Classes.Product;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.ir.hodicohiff.R;

public class ProductsAdapter extends BaseAdapter implements SpinnerAdapter {
	private Activity activity;
	private List<Product> products;

	public ProductsAdapter(Activity activity, List<Product> products) {
		this.activity = activity;
		this.products = products;
	}

	public int getCount() {
		return products.size();
	}

	public Object getItem(int position) {
		return products.get(position);
	}

	public long getItemId(int position) {
		return products.get(position).getId();
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
		t1.setText(String.valueOf(products.get(position).getProduct()));
		return spinView;
	}
}
package Adapters;

import java.util.List;

import Classes.Product;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.ir.hodicohiff.R;

public class PricesAdapter extends BaseAdapter implements SpinnerAdapter {
	private Activity activity;
	private List<Product> products;

	public PricesAdapter(Activity activity, List<Product> products) {
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
			spinView = inflater.inflate(R.layout.priceslistrow, null);
		} else {
			spinView = convertView;
		}
		TextView product = (TextView) spinView.findViewById(R.id.txtProduct);

		TextView price = (TextView) spinView.findViewById(R.id.txtPrice);

		TextView curr = (TextView) spinView.findViewById(R.id.txtCur);

		product.setText(String.valueOf(products.get(position).getProduct()));

		if (products.get(position).getProduct().equalsIgnoreCase("fuel oil")) {
			curr.setText("$");
			curr.setBackgroundColor(Color.parseColor("#000000"));
			curr.setTextColor(Color.parseColor("#cccc00"));
		} else {
			curr.setText("LL");
		}
		price.setText(String.valueOf(products.get(position).getPrice())+" ");
		Typeface font = Typeface.createFromAsset(activity.getAssets(),
				"7LED.ttf");
		price.setTypeface(font);
		curr.setTypeface(font);

		return spinView;
	}
}
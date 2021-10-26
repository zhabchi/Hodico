package Utilities;

import org.json.JSONArray;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class WebHttpRequest {

	private Context context;
	public static int WEB_TIPS = com.ir.hodicohiff.R.string.Tips;
	public static int WEB_NEWS = com.ir.hodicohiff.R.string.News;
	public static int WEB_PRICES = com.ir.hodicohiff.R.string.Prices;
	public static int WEB_PRICESDATE = com.ir.hodicohiff.R.string.PricesDate;
	public static int WEB_PRICESTRIP = com.ir.hodicohiff.R.string.PricesTrip;
	public static int WEB_PRICESDEL = com.ir.hodicohiff.R.string.PricesDel;
	public static int WEB_PRODUCTS = com.ir.hodicohiff.R.string.Products;
	public static int WEB_STATIONS = com.ir.hodicohiff.R.string.Stations;
	public static int WEB_ORDERS = com.ir.hodicohiff.R.string.Orders;
	public static int WEB_FEEDBACK = com.ir.hodicohiff.R.string.Feedback;
	public static int WEB_TAX = com.ir.hodicohiff.R.string.Tax;

	public static int WEB_TAX_CARHORSEPOWER = com.ir.hodicohiff.R.string.TaxCarHorsePower;
	public static int WEB_TAX_CARYEARMAKE = com.ir.hodicohiff.R.string.TaxCarYearMake;
	public static int WEB_TAX_CARTYPE = com.ir.hodicohiff.R.string.TaxCarType;
	public static int WEB_TAX_CARSYMBOL= com.ir.hodicohiff.R.string.TaxCarSymbol;
	
	private String url;
	private OnTaskCompleted mOnTaskCompleted;

	public WebHttpRequest(Context context, int type,
			OnTaskCompleted onTaskCompleted) {
		this.context = context;
		setUrl(context.getString(type));
		this.mOnTaskCompleted = onTaskCompleted;
	}

	public WebHttpRequest(Context context, String url,
			OnTaskCompleted onTaskCompleted) {
		this.context = context;
		setUrl(url);
		this.mOnTaskCompleted = onTaskCompleted;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void getJson() {
		RequestQueue queue = Volley.newRequestQueue(context);

		JsonArrayRequest sr = new JsonArrayRequest(url,
				new Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray arg0) {
						mOnTaskCompleted.onTaskCompleted(arg0);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						mOnTaskCompleted.onTaskError(arg0);
					}
				});
		sr.setShouldCache(false);
		sr.setRetryPolicy(new DefaultRetryPolicy(5000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		queue.add(sr);

	}

	public void getString() {
		RequestQueue queue = Volley.newRequestQueue(context);

		StringRequest sr = new StringRequest(url, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				mOnTaskCompleted.onTaskCompleted(arg0);
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				mOnTaskCompleted.onTaskError(arg0);

			}
		});
		sr.setShouldCache(false);
		sr.setRetryPolicy(new DefaultRetryPolicy(5000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		queue.add(sr);

	}

}

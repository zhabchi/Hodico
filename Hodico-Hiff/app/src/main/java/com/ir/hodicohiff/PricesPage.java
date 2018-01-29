package com.ir.hodicohiff;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Adapters.PricesAdapter;
import Classes.Product;
import Utilities.OnTaskCompleted;
import Utilities.Tools;
import Utilities.WebHttpRequest;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;


import com.android.volley.VolleyError;


public class PricesPage extends Activity {

	private WebHttpRequest mWeb;
	private WebHttpRequest mWeb2;
	private Tools mTools;
	private PricesAdapter adapter;
	private ArrayList<Product> products;

	private ListView actualListView;
	//private PullToRefreshListView mPullRefreshListView;
	private TextView tvurl;

	private View mHeaderView;
	private View mFooterView;
    private SwipeRefreshLayout mySwipeRefreshLayout;
	private String date;

	private boolean push = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_prices_page);

		actualListView = (ListView) findViewById(R.id.pricelist);
        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_price_page);

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        getPricesData();
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
		if (getIntent().hasExtra("push")) {
			push = getIntent().getExtras().getBoolean("push", false);
		}

		mTools = new Tools(this);

		/*tvurl = (TextView) findViewById(R.id.tvurl);
		Linkify.addLinks(tvurl, Linkify.ALL);
		tvurl.setLinkTextColor(Color.WHITE);*/

		//adapter = (PricesAdapter) getLastNonConfigurationInstance();

		// Pull To Refresh
		//mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);

		//actualListView = mPullRefreshListView.getRefreshableView();
		actualListView.setDividerHeight(0);

		registerForContextMenu(actualListView);
		mHeaderView = LayoutInflater.from(this).inflate(R.layout.priceshdr,
				null);
		actualListView.addHeaderView(mHeaderView);

		mFooterView = LayoutInflater.from(this).inflate(R.layout.pricesftr,
				null);

		actualListView.addFooterView(mFooterView);
		
		/*mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						mWeb.getJson();
					}
				});

		mPullRefreshListView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {

					}
				});*/

		getPricesData();
	/*
	 * @Override public void onBackPressed() { if (!push) { MenuPage.i =
	 * MenuPage.Menu.size(); } finish(); }
	 */
	}

	private void getPricesData() {
		mWeb = new WebHttpRequest(PricesPage.this, WebHttpRequest.WEB_PRICES,
				new OnTaskCompleted() {

					@Override
					public void onTaskError(VolleyError arg0) {
						// TODO Auto-generated method stub
						mTools.displayAlert("Error",
								"Please connect to the internet",
								android.R.string.ok, true);
						mTools.hideLoadingDialog();
					}

					@Override
					public void onTaskCompleted(JSONArray arg0) {

						int id;
						String product = "";
						int price = 0;
						Product p;
						products = new ArrayList<Product>();

						List<JSONObject> results;
						results = mTools.parseJson(arg0);

						try {
							for (int i = 0; i < results.size(); i++) {
								p = new Product();
								id = results.get(i).getInt("pp_product_id");
								product = results.get(i).getString(
										"product_description");
								price = results.get(i).getInt(
										"pp_product_price");
								p.setProduct(product);
								p.setPrice(price);
								p.setId(id);
								products.add(p);
							}
							adapter = new PricesAdapter(PricesPage.this,
									products);
							// setListAdapter(adapter);
							actualListView.setAdapter(adapter);
							//mPullRefreshListView.onRefreshComplete();
							mTools.hideLoadingDialog();
							actualListView.requestLayout();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onTaskCompleted(String arg0) {
						// TODO Auto-generated method stub

					}
				});


		mWeb2 = new WebHttpRequest(PricesPage.this, WebHttpRequest.WEB_PRICESDATE,
				new OnTaskCompleted() {

					@Override
					public void onTaskError(VolleyError arg0) {
						// TODO Auto-generated method stub
						mTools.displayAlert("Error",
								"Please connect to the internet",
								android.R.string.ok, true);
						mTools.hideLoadingDialog();
					}

					@Override
					public void onTaskCompleted(JSONArray arg0) {
						List<JSONObject> results;
						results = mTools.parseJson(arg0);

						try {
							for (int i = 0; i < results.size(); i++) {
								date = results.get(i).getString("Date");
							}
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
							Date datep = sdf.parse(date);
							String day = new SimpleDateFormat("EEE").format(datep);
							sdf.applyPattern("dd/MM/yyyy");
							date = sdf.format(datep);
							TextView txtDate = (TextView) mFooterView.findViewById(R.id.txtDate);
							txtDate.setText(day + " " + date);
							//mPullRefreshListView.onRefreshComplete();
							mTools.hideLoadingDialog();
							actualListView.requestLayout();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onTaskCompleted(String arg0) {
						// TODO Auto-generated method stub

					}
				});

		// Get Data at start
		mTools.showLoadingDialog();
		//mPullRefreshListView.setRefreshing(true);
		mWeb2.getJson();
		mWeb.getJson();

	}
}

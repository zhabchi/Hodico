package com.ir.hodicohiff;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Adapters.ProductsAdapter;
import Classes.Product;
import Utilities.OnTaskCompleted;
import Utilities.Tools;
import Utilities.WebHttpRequest;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

public class ProductsPage extends Activity {

	private WebHttpRequest mWeb;
	private Tools mTools;
	private List<Product> products;

	private Spinner spProducts;
	private EditText etQty, etPhone;
	private TextView tvTotalLeb, tvTotalDol, tvurl, tvPhone;
	private Button btnsend;

	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTools = new Tools(ProductsPage.this);
		mTools.setHeader(R.drawable.wasselhd);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_products_page);

		etQty = (EditText) findViewById(R.id.etQty);
		etPhone = (EditText) findViewById(R.id.etPhone);
		tvTotalLeb = (TextView) findViewById(R.id.tvTotalPriceLeb);
		tvTotalDol = (TextView) findViewById(R.id.tvTotalPriceDol);
		btnsend = (Button) findViewById(R.id.btnSend);
		spProducts = (Spinner) findViewById(R.id.spProducts);

		mTools = new Tools(this);

		etPhone.setText(mTools.getPhoneNumber());

		//tvurl = (TextView) findViewById(R.id.tvurl);
		//Linkify.addLinks(tvurl, Linkify.ALL);
		//tvurl.setLinkTextColor(Color.WHITE);

		mWeb = new WebHttpRequest(ProductsPage.this,
				WebHttpRequest.WEB_PRICESDEL, new OnTaskCompleted() {

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
							ProductsAdapter dataAdapter = new ProductsAdapter(
									ProductsPage.this, products);
							spProducts.setAdapter(dataAdapter);
							mTools.hideLoadingDialog();
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

		// Get Data at start
		mTools.showLoadingDialog();
		mWeb.getJson();

		etQty.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				float qty = 0;
				String totalpriceleb = null;
				String totalpricedol = null;
				if (etQty.getText().toString().equals("")) {
					qty = 0;
					totalpriceleb = "0.00 LL";
					totalpricedol = "0.00 $";
				} else {
					qty = Float.parseFloat(etQty.getText().toString());
					Product p = (Product) spProducts
							.getItemAtPosition(spProducts
									.getSelectedItemPosition());
					float price = (qty / 20) * p.getPrice();

					DecimalFormatSymbols symbols = new DecimalFormatSymbols();
					symbols.setGroupingSeparator(' ');
					symbols.setDecimalSeparator('.');

					DecimalFormat decimalFormatLeb = new DecimalFormat(
							"#,###.00 LL", symbols);

					DecimalFormat decimalFormatDol = new DecimalFormat(
							"#,###.00 $", symbols);
					if (price != 0) {
						totalpriceleb = decimalFormatLeb.format(price);
						totalpricedol = decimalFormatDol.format(price/1500);
					} else if (price == 0) {
						totalpriceleb = "0.00 LL";
						totalpricedol = "0.00 $";
					}
				}

				tvTotalLeb.setText(totalpriceleb);
				tvTotalDol.setText(totalpricedol);

			}
		});

		btnsend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mTools.hideSoftInput();

				if (!etQty.getText().toString().trim().equals("0")
						&& !etQty.getText().toString().trim().equals("")) {

					url = ProductsPage.this.getString(R.string.Orders)
							+ "?insert=true&email=" + mTools.getPrimaryEmail()
							+ "&phone=" + etPhone.getText() + "&productid="
							+ spProducts.getSelectedItemId() + "&quantity="
							+ etQty.getText();

					mWeb = new WebHttpRequest(ProductsPage.this, url,
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

								}

								@Override
								public void onTaskCompleted(String arg0) {
									if (!arg0.equals("0")) {
										mTools.displayToast(
												"Thank you for your order!",
												Toast.LENGTH_SHORT);
									} else {
										mTools.displayToast(
												"Something went wrong! Your order wasn't sent!",
												Toast.LENGTH_SHORT);
									}
									mTools.hideLoadingDialog();
								}
							});
					mWeb.getString();
				} else {
					mTools.displayToast("Please enter a valid quantity",
							Toast.LENGTH_SHORT);
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		MenuPage.i = MenuPage.Menu.size();
		finish();
	}

}

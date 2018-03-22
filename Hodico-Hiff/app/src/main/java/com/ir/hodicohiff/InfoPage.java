package com.ir.hodicohiff;

import Utilities.Tools;

import android.app.Activity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.Window;
import android.widget.TextView;

//import com.actionbarsherlock.app.SherlockActivity;

//public class InfoPage extends SherlockActivity {
public class InfoPage extends Activity {

	TextView tvT1, tvT2, tvT3, tvT4, tvE, tvM;

	private Tools mTools;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTools = new Tools(InfoPage.this);
		//mTools.setHeader(R.drawable.ettisalhd);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_info_page);

		tvT1 = (TextView) findViewById(R.id.tvT1);
		tvT2 = (TextView) findViewById(R.id.tvT2);
		tvT3 = (TextView) findViewById(R.id.tvT3);
		tvT4 = (TextView) findViewById(R.id.tvT4);
		tvE = (TextView) findViewById(R.id.tvE);
		tvM = (TextView) findViewById(R.id.tvM);

		Linkify.addLinks(tvT1, Linkify.ALL);
		Linkify.addLinks(tvT2, Linkify.ALL);
		Linkify.addLinks(tvT3, Linkify.ALL);
		Linkify.addLinks(tvT4, Linkify.ALL);
		Linkify.addLinks(tvE, Linkify.ALL);
		Linkify.addLinks(tvM, Linkify.ALL);

	}

	@Override
	public void onBackPressed() {
		MenuPage.i = MenuPage.Menu.size();
		finish();
	}
	
}

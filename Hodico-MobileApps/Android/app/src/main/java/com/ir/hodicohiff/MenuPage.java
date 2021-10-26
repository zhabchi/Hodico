package com.ir.hodicohiff;

import java.util.HashMap;
import java.util.Map;

import Listeners.RegistrationIntentService;
import Utilities.MenuAnimator;
import Utilities.Tools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

//import com.actionbarsherlock.app.ActionBar;
//import com.actionbarsherlock.app.ActionBar.LayoutParams;
//import com.actionbarsherlock.app.SherlockActivity;

public class MenuPage extends Activity {

	private ImageView imgPoster1, imgPoster2, imgPoster3, imgPoster4,
			imgPoster5, imgPoster6, imgPoster7,hodicologo;
	private Button btnStart;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	//private TextView tvurl;

	public static Map<Integer, ImageView> Menu;

	// Animation
	Animation animZoomIn;

	Tools tools;
	MenuAnimator ma;

	public static int i = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//
		super.onCreate(savedInstanceState);
		//Remove title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_menu_page);

		tools = new Tools(this);

		//tvurl = (TextView) findViewById(R.id.tvurl);
		//Linkify.addLinks(tvurl, Linkify.ALL);

		//tvurl.setLinkTextColor(Color.WHITE);

		Menu = new HashMap<Integer, ImageView>();

		imgPoster1 = (ImageView) findViewById(R.id.imageView1);
		imgPoster2 = (ImageView) findViewById(R.id.imageView2);
		imgPoster3 = (ImageView) findViewById(R.id.imageView3);
		imgPoster4 = (ImageView) findViewById(R.id.imageView4);
		imgPoster5 = (ImageView) findViewById(R.id.imageView5);
		imgPoster6 = (ImageView) findViewById(R.id.imageView6);
		imgPoster7 = (ImageView) findViewById(R.id.imageView7);

		// Stations
		imgPoster1.setClickable(true);
		imgPoster1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MenuPage.this, PricesPage.class);
				startActivityForResult(i, 1);
			}
		});

		// Car
		imgPoster2.setClickable(true);
		imgPoster2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MenuPage.this, NewsPage.class);
				startActivityForResult(i, 1);
			}
		});

		// Info
		imgPoster3.setClickable(true);
		imgPoster3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MenuPage.this, StationsPage.class);
				startActivityForResult(i, 1);
			}
		});

		// News
		imgPoster4.setClickable(true);
		imgPoster4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MenuPage.this, CommentsPage.class);
				startActivityForResult(i, 1);
			}
		});

		// Comments
		imgPoster5.setClickable(true);
		imgPoster5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MenuPage.this, CarPage.class);
				startActivityForResult(i, 1);
			}
		});

		// Deliver
		imgPoster6.setClickable(true);
		imgPoster6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MenuPage.this, ProductsPage.class);
				startActivityForResult(i, 1);
			}
		});

		// Deliver
		imgPoster7.setClickable(true);
		imgPoster7.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MenuPage.this, InfoPage.class);
				startActivityForResult(i, 1);
			}
		});

		Menu.put(0, imgPoster1);
		Menu.put(1, imgPoster2);
		Menu.put(2, imgPoster3);
		Menu.put(3, imgPoster4);
		Menu.put(4, imgPoster5);
		Menu.put(5, imgPoster6);
		Menu.put(6, imgPoster7);

		ma = new MenuAnimator(Menu, this);
		if (i == 0) {
			ma.animateMenu(i);
		}

		RegisterGCM();

	}

    public void RegisterGCM() {

        //email = mTools.getPrimaryEmail();
        //name = mTools.getDeviceID();

        if (checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("First Screen", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {
			if (i == Menu.size()) {
				ma.showMenu();
			}
		}
	}

	@Override
	public void onBackPressed() {
		MenuPage.i = 0;
		finish();
	}

}

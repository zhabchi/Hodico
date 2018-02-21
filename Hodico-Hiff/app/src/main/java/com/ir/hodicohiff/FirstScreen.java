package com.ir.hodicohiff;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Listeners.MyInstanceIDListenerService;
import Listeners.QuickstartPreferences;
import Listeners.RegistrationIntentService;
import Listeners.ShakeListener;
import Listeners.ShakeListener.OnShakeListener;
import Utilities.CommonUtilities;
import Utilities.OnTaskCompleted;

import Utilities.Tools;
import Utilities.WebHttpRequest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager.WakeLock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.iid.InstanceIDListenerService;
import com.google.android.gms.gcm.GcmReceiver;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class FirstScreen extends Activity {

	private FrameLayout mainframe;

	private ShakeListener mShaker;
	private OnShakeListener mOnShake;

	private Tools mTools;

	private WebHttpRequest mWeb;

	private String tipoftheday;
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	private WakeLock mWakeLock;
	private AsyncTask<Void, Void, Void> mRegisterTask;
	public static String email, name;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first_screen);

		mTools = new Tools(this);

		RegisterGCM();

		mainframe = (FrameLayout) findViewById(R.id.MainFrame);

		mainframe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// mTools.displayToast("Next Page", Toast.LENGTH_SHORT);
				Intent i = new Intent(FirstScreen.this, MenuPage.class);
				startActivity(i);
				FirstScreen.this.finish();
				mShaker.setOnShakeListener(null);
			}
		});

		mOnShake = new OnShakeListener() {

			@Override
			public void onShake() {

				mTools.dialogVibrate(100);

				if (tipoftheday != null) {
					mTools.displayAlert("Tip Of The Day", tipoftheday,
							android.R.string.ok, MenuPage.class);
				} else {
					mTools.showLoadingDialog();
					mWeb = new WebHttpRequest(FirstScreen.this,
							WebHttpRequest.WEB_TIPS, new OnTaskCompleted() {

								@Override
								public void onTaskError(VolleyError arg0) {

									mTools.displayAlert("Error",
											"Please connect to the internet",
											android.R.string.ok, false);
									mTools.hideLoadingDialog();
								}

								@Override
								public void onTaskCompleted(JSONArray arg0) {

									String tip = "";
									List<JSONObject> results;
									results = mTools.parseJson(arg0);
									try {
										tip = results.get(0).getString(
												"tip_Desc");
										tipoftheday = tip;
									} catch (JSONException e) {
										e.printStackTrace();
									}

									mTools.displayAlert("Tip Of The Day", tip,
											android.R.string.ok, MenuPage.class);
									mTools.hideLoadingDialog();
								}

								@Override
								public void onTaskCompleted(String arg0) {

								}
							});

					mWeb.getJson();
				}

			}
		};

		mShaker = new ShakeListener(FirstScreen.this);
		mShaker.setOnShakeListener(mOnShake);

		mWeb = new WebHttpRequest(FirstScreen.this, WebHttpRequest.WEB_TIPS,
				new OnTaskCompleted() {

					@Override
					public void onTaskError(VolleyError arg0) {

					}

					@Override
					public void onTaskCompleted(JSONArray arg0) {

						String tip = "";
						List<JSONObject> results;
						results = mTools.parseJson(arg0);
						try {
							tip = results.get(0).getString("tip_Desc");
							tipoftheday = tip;
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onTaskCompleted(String arg0) {

					}
				});

		mWeb.getJson();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mShaker.setOnShakeListener(mOnShake);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mShaker.setOnShakeListener(null);
	}

	public void RegisterGCM() {

		//email = mTools.getPrimaryEmail();
		//name = mTools.getDeviceID();

		if (checkPlayServices()) {
			Intent intent = new Intent(this, RegistrationIntentService.class);
			startService(intent);
		}

		// Make sure the device has the proper dependencies.
		/*GCMRegistrar.checkDevice(this);

		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);

		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				CommonUtilities.DISPLAY_MESSAGE_ACTION));

		// Get GCM registration id
		final String regId = GCMRegistrar.getRegistrationId(this);

		// Check if regid already presents
		if (regId.equals("")) {
			// Registration is not present, register now with GCM
			GCMRegistrar.register(this, CommonUtilities.SENDER_ID);
		} else {
			// Device is already registered on GCM
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.
			} else {
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						// Register on our server
						// On server creates a new user
						ServerUtilities.register(context, name, email, regId);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};

				mRegisterTask.execute(null, null, null);
			}
		}*/
	}

	/**
	 * Receiving push messages
	 * */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(
					CommonUtilities.EXTRA_MESSAGE);
			// Waking up mobile if it is sleeping
			mWakeLock.acquire();

			/**
			 * Take appropriate action on this message depending upon your app
			 * requirement For now i am just displaying it on the screen
			 * */

			Toast.makeText(getApplicationContext(),
					"New Message: " + newMessage, Toast.LENGTH_LONG).show();

			// Releasing wake lock
			if (mWakeLock.isHeld()) {
				mWakeLock.release();
			}
		}
	};

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

}

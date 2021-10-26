package Utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.ir.hodicohiff.R;

public class Tools {

	private Context context;
	int counter;
	private static int dialogOpened = 0;
	private ProgressDialog pdialog;
	/*private SherlockActivity activity;
	private SherlockListActivity listactivity;
	private SherlockFragmentActivity fragmentactivity;*/

	public Tools(Context context) {
		this.context = context;
	}

	/*public Tools(SherlockActivity activity, Context context) {
		this.activity = activity;
		this.context = context;
	}

	public Tools(SherlockListActivity listactivity, Context context) {
		this.listactivity = listactivity;
		this.context = context;
	}

	public Tools(SherlockFragmentActivity fragmentactivity, Context context) {
		this.fragmentactivity = fragmentactivity;
		this.context = context;
	}*/

	public void displayToast(String message, int duration) {
		Toast.makeText(context, message, duration).show();
	}

	public void displayAlert(String title, String message, int button,
			final boolean goBack) {

		try {
			if (dialogOpened == 0) {
				new AlertDialog.Builder(context).setTitle(title)
						.setPositiveButton(button, new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialogOpened = 0;
								if (goBack) {
									((Activity) context).finish();
								}
							}
						}).setMessage(message).show();
				dialogOpened = 1;
			}
		} catch (Exception ex) {

			String msg = ex.getMessage();
			Log.e("MEEE", msg);
		}
	}

	public void displayAlert(String title, String message, int button,
			final Class nextClass) {

		try {
			if (dialogOpened == 0) {
				new AlertDialog.Builder(context).setTitle(title)
						.setPositiveButton(button, new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialogOpened = 0;
								((Activity) context).finish();
								Intent nextClassIntent = new Intent(context,
										nextClass);
								((Activity) context)
										.startActivity(nextClassIntent);
							}
						}).setMessage(StringEscapeUtils.unescapeHtml3(message)).show();
				dialogOpened = 1;
			}
		} catch (Exception ex) {

			String msg = ex.getMessage();
			Log.e("MEEE", msg);
		}
	}

	public void vibrate(long time) {
		final Vibrator vibe = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		vibe.vibrate(time);
	}

	public void dialogVibrate(long time) {
		final Vibrator vibe = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		if (dialogOpened == 0) {
			vibe.vibrate(time);
		}
	}

	public void showLoadingDialog() {
		pdialog = new ProgressDialog(context);
		pdialog.setCancelable(false);
		pdialog.setMessage("Loading ....");
		if (dialogOpened == 0) {
			pdialog.show();
		}
	}

	public void hideLoadingDialog() {
		if (pdialog != null) {
			pdialog.hide();
		}
	}

	public List<JSONObject> parseJson(JSONArray array) {

		List<JSONObject> results = new ArrayList<JSONObject>();

		try {
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				results.add(object);
			}
		} catch (Exception ex) {

		}
		return results;
	}

	public String getPrimaryEmail() {
		String possibleEmail = "";
		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(context).getAccounts();
		for (Account account : accounts) {
			if (emailPattern.matcher(account.name).matches()) {
				if (account.name.endsWith("gmail.com")
						|| account.name.endsWith("googlemail.com"))
					possibleEmail = account.name;
			}
		}
		return possibleEmail;

	}

	public String getDeviceID() {
		String deviceId = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);
		return deviceId;
	}

	public String getPhoneNumber() {
		TelephonyManager tMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		//String mPhoneNumber = tMgr.getLine1Number();
		String mPhoneNumber = "";
		return mPhoneNumber;
	}

	public void setHeader(int drawable) {
		/*ActionBar actionBar = null;
		View actionBarView = null;
		if (activity != null) {
			actionBar = activity.getSupportActionBar();
			actionBarView = activity.getLayoutInflater().inflate(
					R.layout.actionbar3, null);
		} else if (listactivity != null) {
			actionBar = listactivity.getSupportActionBar();
			actionBarView = listactivity.getLayoutInflater().inflate(
					R.layout.actionbar3, null);
		} else if (fragmentactivity != null) {
			actionBar = fragmentactivity.getSupportActionBar();
			actionBarView = fragmentactivity.getLayoutInflater().inflate(
					R.layout.actionbar3, null);
		}*/
		/*actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);*/

		/*ImageView header = (ImageView) actionBarView.findViewById(R.id.header);
		header.setBackgroundResource(drawable);*/
		/*ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		actionBar.setCustomView(actionBarView, params);*/
	}

	public void setReminder(String date, int days) {
		// Calendar cal = Calendar.getInstance();
		Intent intent = new Intent(Intent.ACTION_EDIT);

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		formatter.setLenient(false);

		Date oldDate = null;
		try {
			oldDate = formatter.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long datemilli = oldDate.getTime();

		long daysToAdd = TimeUnit.MILLISECONDS.convert(days, TimeUnit.DAYS);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra("beginTime", datemilli + daysToAdd);
		intent.putExtra("allDay", true);
		intent.putExtra("title", "Hodico - Oil Change Reminder");
		context.startActivity(intent);
	}


	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
	
	public void hideSoftInput()
	{
	   
	  	    InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
	  	    if(((Activity)context).getCurrentFocus()!=null)
	  	    {
	  	    	inputManager.hideSoftInputFromWindow(((Activity)context).getCurrentFocus().getWindowToken(), 0);
	  	    }
	  	    
	      
	}
}

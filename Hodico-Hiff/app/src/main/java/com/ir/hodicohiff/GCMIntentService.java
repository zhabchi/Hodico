package com.ir.hodicohiff;

import static Utilities.CommonUtilities.SENDER_ID;
import Utilities.ServerUtilities;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

//import com.google.android.gcm.GCMBaseIntentService;
//import com.ir.hodicohiff.R;

//public class GCMIntentService extends GCMBaseIntentService {
public class GCMIntentService extends Service {

	private static final String TAG = "GCMIntentService";

	public GCMIntentService() {
	//	super(SENDER_ID);
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * Method called on device registered
	 **/
//	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
		ServerUtilities.register(context, FirstScreen.name, FirstScreen.email,
				registrationId);
	}

	/**
	 * Method called on device un registred
	 * */
//	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
		ServerUtilities.unregister(context, registrationId);
	}

	/**
	 * Method called on Receiving a new message
	 * */
//	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i(TAG, "Received message");
		//
		String type = intent.getExtras().getString("type");
		String message = intent.getExtras().getString("message");
		generateNotification(context, type, message);
	}

	/**
	 * Method called on receiving a deleted message
	 * */
	//@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
	}

	/**
	 * Method called on Error
	 * */
	//@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
	}

	//@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		//return super.onRecoverableError(context, errorId);
		return  false;
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	private static void generateNotification(Context context, String type,
			String message) {
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, message, when);

		String title = context.getString(R.string.app_name);

		if (type.equals("1")) {

			Intent notificationIntent = new Intent(context, NewsPage.class);
			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);

			notificationIntent.putExtra("push", true);
			PendingIntent intent = PendingIntent.getActivity(context, 0,
					notificationIntent, 0);
			//notification.setLatestEventInfo(context, title, message, intent);
		}
		else if(type.equals("2"))
		{
			Intent notificationIntent = new Intent(context, PricesPage.class);
			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			notificationIntent.putExtra("push", true);
			PendingIntent intent = PendingIntent.getActivity(context, 0,
					notificationIntent, 0);
			//notification.setLatestEventInfo(context, title, message, intent);
		}
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// Play default notification sound
		notification.defaults |= Notification.DEFAULT_SOUND;

		// Vibrate if vibrate is enabled
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notificationManager.notify(0, notification);

	}

}
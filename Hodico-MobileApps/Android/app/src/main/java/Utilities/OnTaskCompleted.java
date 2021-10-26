package Utilities;

import org.json.JSONArray;

import com.android.volley.VolleyError;

public interface OnTaskCompleted {
	public static int SUCCESS = 1;
	public static int ERROR = -1;

	void onTaskCompleted(JSONArray arg0);
	
	void onTaskCompleted(String arg0);

	void onTaskError(VolleyError arg0);

}
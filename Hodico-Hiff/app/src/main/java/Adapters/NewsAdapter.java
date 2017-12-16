package Adapters;

import java.util.ArrayList;

import org.apache.commons.lang3.StringEscapeUtils;

import Classes.News;
import Utilities.BitmapLruCache;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ir.hodicohiff.R;

public class NewsAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<News> news;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;
	public RequestQueue mRequestQueue;

	public NewsAdapter(Activity activity, ArrayList<News> news) {
		this.activity = activity;
		this.news = news;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mRequestQueue = Volley
				.newRequestQueue(activity.getApplicationContext());
		imageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache());

	}

	public int getCount() {
		return news.size();
	}

	public News getItem(int position) {
		return news.get(position);
	}

	public long getItemId(int position) {
		return news.get(position).getId();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.listrow, null);

		TextView title = (TextView) vi.findViewById(R.id.title); // title
		TextView data = (TextView) vi.findViewById(R.id.data); // duration
		ImageView thumb_image = (ImageView) vi.findViewById(R.id.list_image); // thumb
																				// image

		News n = getItem(position);

		final String link = n.getLink();
		title.setText(StringEscapeUtils.unescapeHtml3(n.getTitle()));
		data.setText(StringEscapeUtils.unescapeHtml3(n.getData()));
		if (n.getImg() != null && !(n.getImg()).equals("")
				&& !(n.getImg()).equals("0")) {
			imageLoader.get(n.getImg(), ImageLoader
					.getImageListener(thumb_image, R.drawable.ic_launcher,
							R.drawable.ic_launcher));
		}

		vi.setClickable(true);
		vi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (link != null && !link.equals("") && !link.equals("0")) {
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					// i.setData(Uri.parse(link));
					// Intent chooser = Intent.createChooser(i, "Open with");
					// chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					activity.getApplicationContext().startActivity(i);
				}
			}
		});

		return vi;
	}
}
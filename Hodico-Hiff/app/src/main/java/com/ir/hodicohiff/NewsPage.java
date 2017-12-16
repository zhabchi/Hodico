package com.ir.hodicohiff;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Adapters.NewsAdapter;
import Classes.News;
import Utilities.OnTaskCompleted;
import Utilities.Tools;
import Utilities.WebHttpRequest;
import android.graphics.Color;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListActivity;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class NewsPage extends SherlockListActivity {

	private WebHttpRequest mWeb;
	private Tools mTools;
	private NewsAdapter adapter;
	private ArrayList<News> news;

	private ListView actualListView;
	private PullToRefreshListView mPullRefreshListView;
	private TextView tvurl;

	private boolean push = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mTools = new Tools(NewsPage.this, this);
		mTools.setHeader(R.drawable.akhbarhd);
		setContentView(R.layout.activity_news_page);

		if (getIntent().hasExtra("push")) {
			push = getIntent().getExtras().getBoolean("push", false);
		}

		// mTools = new Tools(this);

		tvurl = (TextView) findViewById(R.id.tvurl);
		Linkify.addLinks(tvurl, Linkify.ALL);

		tvurl.setLinkTextColor(Color.WHITE);
		
		adapter = (NewsAdapter) getLastNonConfigurationInstance();

		// Pull To Refresh
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);

		actualListView = mPullRefreshListView.getRefreshableView();

		registerForContextMenu(actualListView);

		mPullRefreshListView
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
				});

		// Get Data
		// mTools.showLoadingDialog();
		mWeb = new WebHttpRequest(NewsPage.this, WebHttpRequest.WEB_NEWS,
				new OnTaskCompleted() {

					@Override
					public void onTaskError(VolleyError arg0) {
						// TODO Auto-generated method stub
						mTools.displayAlert("Error",
								"Please connect to the internet",
								android.R.string.ok, true);
						mTools.hideLoadingDialog();
						mPullRefreshListView.onRefreshComplete();
					}

					@Override
					public void onTaskCompleted(JSONArray arg0) {

						int id;
						String title = "";
						String data = "";
						String img = "";
						String link = "";
						News n;
						news = new ArrayList<News>();

						List<JSONObject> results;
						results = mTools.parseJson(arg0);

						try {
							for (int i = 0; i < results.size(); i++) {
								n = new News();
								id = results.get(i).getInt("news_id");
								title = results.get(i).getString("news_title");
								data = results.get(i).getString("news_data");
								img = NewsPage.this.getString(R.string.Website)
										+ results.get(i).getString(
												"news_Image_path");
								link = results.get(i).getString("news_Link");
								n.setData(data);
								n.setTitle(title);
								n.setId(id);
								n.setImg(img);
								n.setLink(link);
								news.add(n);
							}
							adapter = new NewsAdapter(NewsPage.this, news);
							// setListAdapter(adapter);
							actualListView.setAdapter(adapter);
							mPullRefreshListView.onRefreshComplete();
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
		mPullRefreshListView.setRefreshing(true);
		mWeb.getJson();

	}

	@Override
	public Object getLastNonConfigurationInstance() {
		return (getListAdapter());
	}

	/*
	 * @Override public void onBackPressed() { if (!push) { MenuPage.i =
	 * MenuPage.Menu.size(); } finish(); }
	 */

}

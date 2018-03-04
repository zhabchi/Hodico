package com.ir.hodicohiff;


import android.app.Activity;
import android.app.ActionBar;
import android.app.FragmentTransaction;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.widget.TabWidget;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Classes.Station;
import Utilities.OnTaskCompleted;
import Utilities.Tools;
import Utilities.WebHttpRequest;

public class StationsPage extends Activity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    public static ArrayList<Station> stations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_stations_page);

        //first get list of stations from server
        PopulateStationsList();


    }

    private void PopulateStationsList() {

        WebHttpRequest mWeb;
        final Tools mTools;
        TabLayout mTabs;
        mTools = new Tools(StationsPage.this);
        mTools.setHeader(R.drawable.ma7atatakhd);
        mWeb = new WebHttpRequest(StationsPage.this, WebHttpRequest.WEB_STATIONS,
                new OnTaskCompleted() {

                    @Override
                    public void onTaskError(VolleyError arg0) {
                        mTools.displayAlert("Error",
                                "Please connect to the internet",
                                android.R.string.ok, true);
                        mTools.hideLoadingDialog();
                    }

                    @Override
                    public void onTaskCompleted(JSONArray arg0) {

                        int id;
                        String name = "";
                        String description = "";
                        String address = "";
                        double longitude;
                        double latitude;
                        int type;

                        Station s;
                        stations = new ArrayList<Station>();

                        List<JSONObject> results;
                        results = mTools.parseJson(arg0);

                        try {
                            for (int i = 0; i < results.size(); i++) {
                                s = new Station();
                                id = results.get(i).getInt("station_id");
                                name = results.get(i).getString("station_name");
                                address = results.get(i).getString(
                                        "station_address");
                                description = results.get(i).getString(
                                        "station_description");
                                longitude = results.get(i).getDouble(
                                        "station_long");
                                latitude = results.get(i).getDouble(
                                        "station_lat");
                                type = results.get(i).getInt("station_type");

                                s.setId(id);
                                s.setName(name);
                                s.setAddress(address);
                                s.setDescription(description);
                                s.setPosition(latitude, longitude);
                                s.setType(type);
                                stations.add(s);

                            }

                            mTools.hideLoadingDialog();

                            CreateTabs();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onTaskCompleted(String arg0) {

                    }
                });

        // Get Data at start
        mTools.showLoadingDialog();
        mWeb.getJson();
    }



    private void CreateTabs()
    {
        //Check Permissions
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[] {
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION },
                    1);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabWidget = (TabLayout) findViewById(R.id.activity_stations_tabs);
        tabWidget.setTabTextColors(R.color.Navy,R.color.Gray);//set text color
        tabWidget.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        TabLayout tabWidget = (TabLayout) findViewById(R.id.activity_stations_tabs);
                        tabWidget.getTabAt(position).select();
                    }
                });
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_stations_page, container, false);

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position == 0)
            {
                StationsMapFragment mapFragment = new StationsMapFragment();
                return mapFragment;
            }
            else
            {
                SearchStationsFragment searchFragment = new SearchStationsFragment();
                return searchFragment;
            }
            //return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Map";
                case 1:
                    return "Search";

            }
            return null;
        }

        public int getPageIcon(int i) {
            if(i == 0)
                return R.drawable.ic_search_map;
            else
                return R.drawable.ic_search_stations;
        }
    }
}

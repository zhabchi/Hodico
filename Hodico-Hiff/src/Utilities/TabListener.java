package Utilities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.ir.hodicohiff.R;

public class TabListener implements ActionBar.TabListener {
	public Fragment fragment;

	public TabListener(Fragment fragment) {
		this.fragment = fragment;
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction ft) {
		ft.replace(R.id.fragment_container, fragment);

	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction ft) {
		ft.remove(fragment);

	}

}
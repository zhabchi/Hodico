package Adapters;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;


import com.ir.hodicohiff.OilFragment;
import com.ir.hodicohiff.R;
import com.ir.hodicohiff.TaxFragment;
import com.ir.hodicohiff.TripCostFragment;


public class CarsSectionsPagerAdapter extends FragmentPagerAdapter {

    public CarsSectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if (position == 0) {
            TaxFragment taxFragment = new TaxFragment();
            return taxFragment;
        } else if (position == 1) {
            OilFragment oilFragment = new OilFragment();
            return oilFragment;
        }
        else
        {
            TripCostFragment tripCostFragment = new TripCostFragment();
            return tripCostFragment;
        }
        //return PlaceholderFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Tax";
            case 1:
                return "Oil";
            case 2:
                return "Trip Cost";

        }
        return "";
    }

    public int getPageIcon(int i) {
        if (i == 0)
            return R.drawable.ic_mechanic;
        else if (i == 1)
            return R.drawable.ic_oil;
        else
            return R.drawable.ic_trip;
    }
}

package io.akwa.frigdoor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import io.akwa.frigdoor.fragment.BluGraphFragment;
import io.akwa.frigdoor.fragment.BluTagOverViewFragment;


public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
 
    @Override
    public Fragment getItem(int position) {
 
        switch (position) {
            case 0:
                BluTagOverViewFragment tab1 = new BluTagOverViewFragment();
                return tab1;
            case 1:
                BluGraphFragment tab2 = new BluGraphFragment();
                return tab2;
            case 2:
                BluTagOverViewFragment tab3 = new BluTagOverViewFragment();
                return tab3;
//            case 3:
//                BluProdScanHistoryFragment tab4 = new BluProdScanHistoryFragment();
//                return tab4;
            default:
                return null;
        }
    }
 
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}

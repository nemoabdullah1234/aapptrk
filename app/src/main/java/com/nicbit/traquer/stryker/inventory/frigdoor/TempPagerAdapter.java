package com.nicbit.traquer.stryker.inventory.frigdoor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nicbit.traquer.stryker.inventory.frigdoor.fragment.TempGraphFragment;
import com.nicbit.traquer.stryker.inventory.frigdoor.fragment.TempOverViewFragment;


public class TempPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public TempPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TempOverViewFragment tab1 = new TempOverViewFragment();
                return tab1;
            case 1:
                TempGraphFragment tab2 = new TempGraphFragment();
                return tab2;
//            case 2:
//                BluTagOverViewFragment tab3 = new BluTagOverViewFragment();
//                return tab3;
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

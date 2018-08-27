package com.nicbit.traquer.stryker.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nicbit.traquer.stryker.Models.CaseDetails;
import com.nicbit.traquer.stryker.Models.Shipment;
import com.nicbit.traquer.stryker.caseDetail.MapFragment;

import java.util.List;

public class MapPagerAdapter extends FragmentPagerAdapter {

    List<Shipment> shipments;
    CaseDetails caseDetails;

    public MapPagerAdapter(FragmentManager fragmentManager, List<Shipment> shipments, CaseDetails caseDetails) {
        super(fragmentManager);
        this.shipments = shipments;
        this.caseDetails = caseDetails;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return shipments.size();
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        return MapFragment.newInstance(shipments.get(position), caseDetails);
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}


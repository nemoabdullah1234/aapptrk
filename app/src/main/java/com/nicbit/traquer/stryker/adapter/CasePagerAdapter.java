package com.nicbit.traquer.stryker.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nicbit.traquer.stryker.Models.CaseDetails;
import com.nicbit.traquer.stryker.Models.Shipment;
import com.nicbit.traquer.stryker.caseDetail.ShipmentViewFragment;

import java.util.List;

public class CasePagerAdapter extends FragmentPagerAdapter {

    List<Shipment> shipments;
    CaseDetails caseDetails;
    String mCaseNumber;
    boolean isDashBoard;
    String caseId;

    public CasePagerAdapter(FragmentManager fragmentManager, List<Shipment> shipments, CaseDetails caseDetails, String caseNumber, boolean isDashBoard, String caseId) {
        super(fragmentManager);
        this.shipments = shipments;
        this.caseDetails = caseDetails;
        this.mCaseNumber = caseNumber;
        this.isDashBoard = isDashBoard;
        this.caseId = caseId;
    }

    @Override
    public int getCount() {
        return shipments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return ShipmentViewFragment.newInstance(shipments.get(position), caseDetails, mCaseNumber, isDashBoard, caseId);


    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}


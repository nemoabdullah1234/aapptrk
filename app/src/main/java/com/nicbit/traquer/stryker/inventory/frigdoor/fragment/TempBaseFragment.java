package com.nicbit.traquer.stryker.inventory.frigdoor.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.nicbit.traquer.stryker.inventory.frigdoor.ApiTempModel;
import com.nicbit.traquer.stryker.inventory.frigdoor.TemperatureInfoActivity;

/**
 * Created by niteshgoel on 4/6/16.
 */
public abstract class TempBaseFragment extends Fragment {

    public TemperatureInfoActivity baseActivity;
    public ApiTempModel tempData;

    protected abstract void updateViews();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.baseActivity = (TemperatureInfoActivity) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tempData = ((TemperatureInfoActivity) getActivity()).getTempData();
        if (tempData != null) {
            updateViews();
        }
    }

    public void onReportIssueClick() {
//        if (baseActivity.isLogin()) {
//            if(baseActivity.launchReport()){
//                if (tempData.requestID != null) {
//                    baseActivity.launchReportScreen();
//                } else {
//                    baseActivity.setIsReport(true);
//                    baseActivity.createScanRequest();
//                }
//            }
//
//        } else {
//            baseActivity.signInConfirmation("Report");
//        }
    }
}

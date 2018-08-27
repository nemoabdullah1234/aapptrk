package io.akwa.frigdoor.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import io.akwa.frigdoor.FrigdorHomeActivity;
import io.akwa.frigdoor.TagData;


/**
 * Created by niteshgoel on 4/6/16.
 */
public abstract class BluBaseFragment extends Fragment {

    public FrigdorHomeActivity baseActivity;
    public TagData bluTagData;

    protected abstract void updateViews();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.baseActivity = (FrigdorHomeActivity) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bluTagData = ((FrigdorHomeActivity) getActivity()).getTagData();
        if (bluTagData != null) {
            updateViews();
        }
    }

    public void onReportIssueClick() {
//        if (baseActivity.isLogin()) {
//            if(baseActivity.launchReport()){
//                if (bluTagData.requestID != null) {
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

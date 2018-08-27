package com.nicbit.traquer.stryker.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.nicbit.traquer.common.login.DefaultLoginActivity;
import com.nicbit.traquer.stryker.BuildConfig;
import com.nicbit.traquer.stryker.Models.ReaderGetSettingsResponse;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.home.StrykerHomeActivity;
import com.nicbit.traquer.stryker.util.PrefUtils;

public class StrykerLoginActivity extends DefaultLoginActivity {
    public static final String TAG = "WHLoginActivity";


    @Override
    public void setUpView() {
        if (!BuildConfig.IS_PROD) {
         /*   mEmail.setText("ankit.vaish+1@nicbit.com");
            mEmail.setText("sharma.niharika74@yahoo.in");*/
        }
    }

    @Override
    public void setAppName() {
        mAppName.setVisibility(View.VISIBLE);
        mAppName.setImageResource(R.drawable.rep_logo);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setCheckHomePagePermissions(this);
        //checkHomePagePermission();

    }

    public void setSetting(ReaderGetSettingsResponse readerGetSettingsResponse) {
        PrefUtils.setDefaultView(readerGetSettingsResponse.getDashboardDefaultView());
        PrefUtils.setSortBy(readerGetSettingsResponse.getDashboardSortBy());
        PrefUtils.setSortOrder(readerGetSettingsResponse.getDashboardSortOrder());
        PrefUtils.setNotification(readerGetSettingsResponse.getNotifications());
        PrefUtils.setVibration(readerGetSettingsResponse.getVibration());
        PrefUtils.setSound(readerGetSettingsResponse.getSound());
        PrefUtils.setLed(readerGetSettingsResponse.getLed());
        PrefUtils.setBeaconStatus(readerGetSettingsResponse.getBeaconServiceStatus());
    }

    @Override
    protected Intent getHomeIntent() {
        return new Intent(this, StrykerHomeActivity.class);
    }

    @Override
    protected Intent getForgotPasswordIntent() {
        return new Intent(this, StrykerForgotPasswordActivity.class);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }


}

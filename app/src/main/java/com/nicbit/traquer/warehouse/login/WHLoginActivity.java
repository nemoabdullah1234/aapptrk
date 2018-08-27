package com.nicbit.traquer.warehouse.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.nicbit.traquer.common.login.DefaultLoginActivity;
import com.nicbit.traquer.stryker.BuildConfig;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.warehouse.WarehouseHomeActivity;

public class WHLoginActivity extends DefaultLoginActivity {
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
        mAppName.setImageResource(R.drawable.wh_logo);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    protected Intent getHomeIntent() {
        return new Intent(this, WarehouseHomeActivity.class);
    }

    @Override
    protected Intent getForgotPasswordIntent() {
        return new Intent(this, WHForgotPasswordActivity.class);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }


}

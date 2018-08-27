package com.nicbit.traquer.stryker.splash;

import android.content.Intent;
import android.os.Bundle;

import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.home.StrykerHomeActivity;
import com.nicbit.traquer.stryker.login.StrykerLoginActivity;

public class StrykerSplashActivity extends BaseSplashActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void launchLoginActivity() {
        startActivity(new Intent(this, StrykerLoginActivity.class));
        finish();
    }

    @Override
    public void setAppName() {

        imgLogo.setImageResource(R.drawable.rep_splash);

    }


    @Override
    public void launchHomeActivity() {
        startActivity(new Intent(this, StrykerHomeActivity.class));
        finish();
    }
}

package com.nicbit.traquer.stryker.forgotPassword;

import android.content.Intent;
import android.view.View;

import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.login.StrykerLoginActivity;

public class StrykerResetPasswordActivity extends DefaultResetPasswordActivity {
    @Override
    public void setAppName() {
        mAppName.setVisibility(View.VISIBLE);
        mAppName.setImageResource(R.drawable.rep_logo);
    }

    @Override
    public Intent getLoginIntent() {
        return new Intent(this, StrykerLoginActivity.class);
    }

}

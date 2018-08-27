package com.nicbit.traquer.warehouse.login;

import android.content.Intent;
import android.view.View;

import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.forgotPassword.DefaultForgotPasswordActivity;
import com.nicbit.traquer.stryker.forgotPassword.StrykerResetPasswordActivity;

public class WHForgotPasswordActivity extends DefaultForgotPasswordActivity {
    @Override
    public void setAppName() {
        mAppName.setVisibility(View.VISIBLE);
        mAppName.setImageResource(R.drawable.wh_logo);
    }

    @Override
    protected Intent getResetPasswordIntent() {
        return new Intent(this, StrykerResetPasswordActivity.class);
    }
}

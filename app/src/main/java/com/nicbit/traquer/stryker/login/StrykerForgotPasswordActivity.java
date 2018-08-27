package com.nicbit.traquer.stryker.login;

import android.content.Intent;
import android.view.View;

import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.forgotPassword.DefaultForgotPasswordActivity;
import com.nicbit.traquer.stryker.forgotPassword.StrykerResetPasswordActivity;

public class StrykerForgotPasswordActivity extends DefaultForgotPasswordActivity {
    @Override
    public void setAppName() {
        mAppName.setVisibility(View.VISIBLE);
        mAppName.setImageResource(R.drawable.rep_logo);
    }

    @Override
    protected Intent getResetPasswordIntent() {
        return new Intent(this, StrykerResetPasswordActivity.class);
    }
}

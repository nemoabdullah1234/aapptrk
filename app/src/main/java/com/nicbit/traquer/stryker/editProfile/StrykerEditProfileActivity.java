package com.nicbit.traquer.stryker.editProfile;

import android.content.Intent;

import com.nicbit.traquer.stryker.login.StrykerLoginActivity;

public class StrykerEditProfileActivity extends DefaultEditProfileActivity {

    @Override
    public Intent getLoginIntent() {
        return new Intent(this, StrykerLoginActivity.class);
    }
}

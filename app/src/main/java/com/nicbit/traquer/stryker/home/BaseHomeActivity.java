package com.nicbit.traquer.stryker.home;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.nicbit.traquer.common.BaseActivity;
import com.nicbit.traquer.stryker.util.PrefUtils;

public abstract class BaseHomeActivity extends BaseActivity {


    public FragmentManager fragmentManager;
    public boolean isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        isLogin = PrefUtils.isUserLogin();
    }

}

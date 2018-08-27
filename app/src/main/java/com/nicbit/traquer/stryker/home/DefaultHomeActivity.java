package com.nicbit.traquer.stryker.home;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.nicbit.traquer.common.newInventory.response.ApiBaseResponse;
import com.nicbit.traquer.common.tracking.Tracker;
import com.nicbit.traquer.common.updatedevice.DeviceInfo;
import com.nicbit.traquer.stryker.BaseApplication;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.util.PrefUtils;
import com.nicbit.traquer.warehouse.updatedevice.RegisterDeviceApiHandler;
import com.nicbit.traquer.warehouse.updatedevice.RegisterUpdateDevicePresneter;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class DefaultHomeActivity extends BaseHomeActivity implements BaseFragmentDrawer.FragmentDrawerListener, RegisterUpdateDevicePresneter.View {

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    public TextView mToolbarTitle;
    public BaseFragmentDrawer mDrawerFragment;

    public abstract BaseFragmentDrawer getDrawerFragment();

    public abstract int getView();

    public abstract void displayView(int position);

    protected abstract void updateTitle(Fragment f);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getView());
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        mDrawerFragment = getDrawerFragment();
        if (mDrawerFragment != null) {
            mDrawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
            mDrawerFragment.setDrawerListener(this);
        }
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.container_body);
                if (f != null) {
                    updateTitle(f);
                }
            }
        });
        updateCurrentLocation();
        DeviceInfo deviceInfo = BaseApplication.getDeviceInfo();
        updateDeviceStatus(deviceInfo);
    }


    public void updateProfile() {
        mDrawerFragment.setUserProfileInfo();
    }

    public void setTitle(String title) {
        mToolbarTitle.setText(title);
    }


    public void startTracking() {
        if (!PrefUtils.getCode().equals("") && PrefUtils.getBeaconStatus()) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Tracker.startTracking();
                }
            } else {
                Tracker.startTracking();
            }
        }
    }

    public void updateDeviceStatus(DeviceInfo deviceInfo) {
        RegisterDeviceApiHandler deviceApiHandler = new RegisterDeviceApiHandler(this);
        deviceApiHandler.deviceUpdate(deviceInfo);

    }

    @Override
    public void onDeviceUpdate(ApiBaseResponse response, NicbitException e) {
        if (response != null && response.getCode() == 200 || response.getCode() == 201) {
            PrefUtils.setProjectId(response.getData().getClient().getProjectId());
            PrefUtils.setClientId(response.getData().getClient().getClientId());
            PrefUtils.setCode(response.getData().getCode());
            PrefUtils.setDeviceId(response.getData().getDeviceId());
            PrefUtils.setUuid(response.getData().getUuid());
            PrefUtils.setMajor(response.getData().getMajor());
            PrefUtils.setMinor(response.getData().getMinor());

        }
    }


}

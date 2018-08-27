package com.nicbit.traquer.warehouse;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.nicbit.traquer.common.BaseActivity;
import com.nicbit.traquer.common.newInventory.floor.FloorListFragment;
import com.nicbit.traquer.common.tracking.Tracker;
import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.about.AboutActivity;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.help.HelpActivity;
import com.nicbit.traquer.stryker.home.BaseFragmentDrawer;
import com.nicbit.traquer.stryker.home.DefaultHomeActivity;
import com.nicbit.traquer.stryker.home.HomeContract;
import com.nicbit.traquer.stryker.home.HomePresenter;
import com.nicbit.traquer.stryker.login.StrykerLoginActivity;
import com.nicbit.traquer.stryker.search.SearchActivity;
import com.nicbit.traquer.stryker.util.DialogClass;
import com.nicbit.traquer.stryker.util.LocationBluetoothPermissionUtility;
import com.nicbit.traquer.stryker.util.PrefUtils;
import com.nicbit.traquer.stryker.util.StringUtils;
import com.nicbit.traquer.warehouse.login.WHLoginActivity;

import java.util.ArrayList;

import butterknife.ButterKnife;
import io.akwa.aklogs.NBLogger;

public class WarehouseHomeActivity extends DefaultHomeActivity implements BaseActivity.CheckHomePagePermission, HomeContract.View {

    HomeContract.UserActionsListener mActionsListener;
    private LocationBluetoothPermissionUtility locationBluetoothPermissionUtility;
    FloorListFragment mInventoryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setCheckHomePagePermissions(this);
        checkHomePagePermission();
        Tracker.startTracking();
        mActionsListener = new HomePresenter(this);
        addFragment();
//        LocationUpdateService.getConfig(this);
        createReceiver();
    }

    @Override
    public void onBluetoothChange(boolean isOn) {
        // mInventoryFragment.onBluetoothChange(isOn);

    }

    private void addFragment() {
        mInventoryFragment = new FloorListFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_body, mInventoryFragment, "inventory");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle("Inventory");
    }

    public void onBackPressed() {
        if (mDrawerFragment.isDrawerOpen()) {
            mDrawerFragment.closeDrawer();
        } else {
            Fragment fragment = fragmentManager.findFragmentByTag("inventory");
            if (fragment != null && fragment.isVisible()) {
                this.finish();
            } else
                super.onBackPressed();
        }
    }

    @Override
    public BaseFragmentDrawer getDrawerFragment() {
        return (WarehouseFragmentDrawer) fragmentManager.findFragmentById(R.id.fragment_navigation_drawer_warehouse);
    }

    public void sendDiagnostic() {

        ArrayList<Uri> fileList = NBLogger.getUriList();

        if (fileList.size() > 0) {
            Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);
            i.setType("text/plain");
            i.putParcelableArrayListExtra(Intent.EXTRA_STREAM, fileList);
            i.putExtra(Intent.EXTRA_EMAIL, "sujoy@nicbit.com");
            startActivity(i);
        } else {
            Toast.makeText(this, "No Log file available", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public int getView() {
        return R.layout.activity_warehouse_home;
    }

    public void displayView(int position) {
        switch (position) {
            case 0: {
                Intent intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                break;
            }
            case 1: {
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            }
            case 2: {
                sendDiagnostic();
                break;
            }
            case 3: {
                onSignOutClicked();
                break;
            }

        }


    }

    @Override
    protected void updateTitle(Fragment f) {

    }

    @Override
    public Intent getLoginIntent() {
        return new Intent(this, StrykerLoginActivity.class);
    }


    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        int id = item.getItemId();
        switch (id) {
            case R.id.menu_search:
                Intent intent = new Intent(WarehouseHomeActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }


    @Override
    public void onPermissionGranted(boolean isGranted) {
        if (!isGranted) {


        } else {
            locationBluetoothPermissionUtility = new LocationBluetoothPermissionUtility(this);
            locationBluetoothPermissionUtility.checkLocationOnOff();
            locationBluetoothPermissionUtility.setLocationListener(new LocationBluetoothPermissionUtility.LocationBluetoothListener() {
                @Override
                public void onLocationON() {
                    locationBluetoothPermissionUtility.checkBluetoothOnOff();
                }

                @Override
                public void onLocationOFF() {
                    locationBluetoothPermissionUtility.checkBluetoothOnOff();
                }

                @Override
                public void onBluetoothON() {
                    showSessionOutDialog();

                }

                @Override
                public void onBluetoothOFF() {
                    showSessionOutDialog();
                }
            });
        }
    }

    public void showSessionOutDialog() {
        if (getIntent().getStringExtra(StringUtils.IntentKey.SESSION_MESSAGE) != null && !TextUtils.isEmpty(getIntent().getStringExtra(StringUtils.IntentKey.SESSION_MESSAGE))) {
            new Handler().postDelayed(new Runnable() {

                public void run() {
                    DialogClass.alerDialog(WarehouseHomeActivity.this, getIntent().getStringExtra(StringUtils.IntentKey.SESSION_MESSAGE));
                }

            }, 2000);
        }
    }


    void onSignOutClicked() {
        logout();
        // DialogClass.showDialog(this, this.getString(R.string.please_wait));
        // mActionsListener.doLogout();
    }

    @Override
    public void onLogoutDone(ApiResponseModel loginResponse, NicbitException e) {
        DialogClass.dismissDialog(this);
        if (e == null) {
            logout();
        } else {
            DialogClass.alerDialog(this, getResources().getString(R.string.check_internet_connection));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void logout() {
        PrefUtils.clearDataOnLogout();
        Intent intent = new Intent(this, WHLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        this.finish();
    }
}

package com.nicbit.traquer.stryker.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.google.gson.JsonObject;
import com.nicbit.traquer.common.BaseActivity;
import com.nicbit.traquer.common.EventsLog;
import com.nicbit.traquer.common.linkdevice.LinkUnlinkDeviceContract;
import com.nicbit.traquer.common.linkdevice.LinkUnlinkDevicePresenter;
import com.nicbit.traquer.common.tracking.Tracker;
import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.ReaderGetSettingsResponse;
import com.nicbit.traquer.stryker.Models.UpdateSettingsRequest;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.about.AboutActivity;
import com.nicbit.traquer.stryker.completed.CompletedCaseActivity;
import com.nicbit.traquer.stryker.enums.CaseSortBy;
import com.nicbit.traquer.stryker.enums.CaseSortOrder;
import com.nicbit.traquer.stryker.enums.DefaultView;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.help.HelpActivity;
import com.nicbit.traquer.stryker.home.BaseFragmentDrawer;
import com.nicbit.traquer.stryker.home.CaseListingFragment;
import com.nicbit.traquer.stryker.home.DefaultHomeActivity;
import com.nicbit.traquer.stryker.home.HomeContract;
import com.nicbit.traquer.stryker.home.HomePresenter;
import com.nicbit.traquer.stryker.home.TabClass;
import com.nicbit.traquer.stryker.home.TraquerFragmentDrawer;
import com.nicbit.traquer.stryker.inventory.details.InventoryDetailActivity;
import com.nicbit.traquer.stryker.inventory.list.InventoryListActivity;
import com.nicbit.traquer.stryker.login.StrykerLoginActivity;
import com.nicbit.traquer.stryker.network.ApiHandler;
import com.nicbit.traquer.stryker.notification.NotificationActivity;
import com.nicbit.traquer.stryker.search.SearchActivity;
import com.nicbit.traquer.stryker.setting.SettingActivity;
import com.nicbit.traquer.stryker.setting.SettingContract;
import com.nicbit.traquer.stryker.setting.SettingPresenter;
import com.nicbit.traquer.stryker.util.DialogClass;
import com.nicbit.traquer.stryker.util.LocationBluetoothPermissionUtility;
import com.nicbit.traquer.stryker.util.PrefUtils;
import com.nicbit.traquer.stryker.util.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.akwa.aklogs.NBLogger;
import io.akwa.frigdoor.FrigdorHomeActivity;
import io.akwa.nfc.NFCActivity;

public class StrykerHomeActivity extends DefaultHomeActivity implements BaseActivity.CheckHomePagePermission, HomeContract.View, LinkUnlinkDeviceContract.View, SettingContract.View {

    public CaseListingFragment caseListingFragment;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.container_body)
    FrameLayout mFrameLayout;
    private int REQUEST_SETTING = 1301;
    HomeContract.UserActionsListener mActionsListener;
    LinkUnlinkDeviceContract.UserActionsListener actionsListener;
    private TabClass tabClass;
    public SettingContract.UserActionsListener settingActionListener;


    String TAG = "StrykerHomeActivity";
    private LocationBluetoothPermissionUtility locationBluetoothPermissionUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        settingActionListener = new SettingPresenter(this);
        getSettings();
        mActionsListener = new HomePresenter(this);
        actionsListener = new LinkUnlinkDevicePresenter(this);
        tabClass = new TabClass(this);
        int tabPostion = setPagerAdapter();
        caseListingFragment = new CaseListingFragment();
        caseListingFragment.currentSelectedTab = tabPostion;


        if (!PrefUtils.getSettingFetched()) {
            DialogClass.showDialog(this, getString(R.string.please_wait));
            getSettings();
        } else {

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, caseListingFragment, "caseList");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }


        startTracking();
        //call for Device Link

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("appCode", PrefUtils.getCode());
        actionsListener.linkDevice(jsonObject);


    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(getString(R.string.dashboard));


    }

    public void onBackPressed() {
        if (mDrawerFragment.isDrawerOpen()) {
            mDrawerFragment.closeDrawer();
        } else {
            Fragment fragment = fragmentManager.findFragmentByTag("caseList");
            if (fragment != null && fragment.isVisible()) {
                this.finish();
            } else
                super.onBackPressed();
        }
    }

    @Override
    public BaseFragmentDrawer getDrawerFragment() {
        return (TraquerFragmentDrawer) fragmentManager.findFragmentById(R.id.fragment_navigation_drawer_traquer);
    }


    @Override
    public int getView() {
        return R.layout.activity_stryker_home;
    }

    public void displayView(int position) {
        switch (position) {
            case 0: {
                Intent intent = new Intent(this, CompletedCaseActivity.class);
                startActivity(intent);
                break;
            }
            case 1: {
                Intent intent = new Intent(this, InventoryListActivity.class);
                startActivity(intent);
                break;
            }
            case 2: {
                Intent intent = new Intent(this, NotificationActivity.class);
                startActivity(intent);
                break;
            }

            case 3: {
                EventsLog.customEvent("MENU", "SETTING", "CLICK");
                Intent intent = new Intent(this, SettingActivity.class);
                startActivityForResult(intent, REQUEST_SETTING);
                break;
            }

            case 4: {
                EventsLog.customEvent("MENU", "HELP", "CLICK");
                Intent intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                break;
            }
            case 5: {
                EventsLog.customEvent("MENU", "ABOUT", "CLICK");
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            }
            case 6: {
                com.nicbit.traquer.stryker.util.Util.sendDiagnostic(this);
                break;
            }
            case 7: {
                DialogClass.alerDialog(this, PrefUtils.getLogStatus() ? "Log Disabled" : "Log Enabled");
                getDrawerFragment().switchLogStatus();
                NBLogger.getLoger().setLogStatus(PrefUtils.getLogStatus());
                break;
            }

            case 8: {

                EventsLog.customEvent("MENU", "LOGOUT", "CLICK");

                onSignOutClicked();
                break;
            }
            case 9: {
                Intent intent = new Intent(this, FrigdorHomeActivity.class);
                startActivity(intent);
                break;
            }
            case 10: {
                Intent intent = new Intent(this, NFCActivity.class);
                startActivityForResult(intent, 2);
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

    private int setPagerAdapter() {
        for (int i = 0; i < 3; i++) {
            mTabLayout.addTab(mTabLayout.newTab());
            mTabLayout.getTabAt(i).setCustomView(tabClass.getTabView(i));
        }
        int selectDefaultTab = selectDefaultTab();

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                caseListingFragment.filterList(tab.getPosition());
                //caseListingFragment.scrollToPosition(0);
                tabClass.getSelectedTabView(tab.getPosition(), tab);
                EventsLog.customEvent("REP_HOME", "TAB", tabClass.tabValues[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabClass.getUnSelectedTabView(tab.getPosition(), tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return selectDefaultTab;
    }

    private int selectDefaultTab() {
        String defaultView = PrefUtils.getDefaultView();
        DefaultView tabByName = DefaultView.getTabByName(defaultView);
        TabLayout.Tab tab = mTabLayout.getTabAt(tabByName.getPosition());
        tab.select();
        tabClass.getSelectedTabView(tabByName.getPosition(), tab);
        return tabByName.getPosition();
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
                Intent intent = new Intent(StrykerHomeActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_sort:
                openfilterDialog();
                break;
        }

        return true;
    }

    private void openfilterDialog() {
// custom dialog
        final AppCompatDialog dialog = new AppCompatDialog(this);
        dialog.setContentView(R.layout.filter_layout);
        dialog.setTitle("Sort");
        final RadioGroup sortByRadioGroup = (RadioGroup) dialog.findViewById(R.id.sortByRadioGroup);
        CheckBox orderByCheckBox = (CheckBox) dialog.findViewById(R.id.orderByCheckBox);


        String sortBy = PrefUtils.getSortBy();
        if (sortBy.equalsIgnoreCase(CaseSortBy.ETD.getName())) {
            int id = sortByRadioGroup.getChildAt(CaseSortBy.ETD.getPosition()).getId();
            sortByRadioGroup.check(id);
        } else if (sortBy.equalsIgnoreCase(CaseSortBy.CASENO.getName())) {
            int id = sortByRadioGroup.getChildAt(CaseSortBy.CASENO.getPosition()).getId();
            sortByRadioGroup.check(id);
        } else if (sortBy.equalsIgnoreCase(CaseSortBy.HOSPITAL.getName())) {
            int id = sortByRadioGroup.getChildAt(CaseSortBy.HOSPITAL.getPosition()).getId();
            sortByRadioGroup.check(id);
        } else if (sortBy.equalsIgnoreCase(CaseSortBy.DOCTOR.getName())) {
            int id = sortByRadioGroup.getChildAt(CaseSortBy.DOCTOR.getPosition()).getId();
            sortByRadioGroup.check(id);
        } else if (sortBy.equalsIgnoreCase(CaseSortBy.SURGERYTYPE.getName())) {
            int id = sortByRadioGroup.getChildAt(CaseSortBy.SURGERYTYPE.getPosition()).getId();
            sortByRadioGroup.check(id);
        } else if (sortBy.equalsIgnoreCase(CaseSortBy.SURGERYDATE.getName())) {
            int id = sortByRadioGroup.getChildAt(CaseSortBy.SURGERYDATE.getPosition()).getId();
            sortByRadioGroup.check(id);
        }


        if (PrefUtils.getSortOrder().equalsIgnoreCase(CaseSortOrder.ASC.getName())) {
            orderByCheckBox.setChecked(true);
        } else {
            orderByCheckBox.setChecked(false);
        }


        orderByCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    String name = CaseSortOrder.getNameByPosition(0);
                    PrefUtils.setSortOrder(name);
                } else {
                    String name = CaseSortOrder.getNameByPosition(1);
                    PrefUtils.setSortOrder(name);
                }

            }
        });


        sortByRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                if (checkedId != -1) {
                    View radioButton = sortByRadioGroup.findViewById(checkedId);
                    int position = sortByRadioGroup.indexOfChild(radioButton);
                    String name = CaseSortBy.getNameByPosition(position);
                    PrefUtils.setSortBy(name);
                }
            }
        });
        Button buttonDone = (Button) dialog.findViewById(R.id.buttonDone);
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caseListingFragment.getCases();
                updateSetting();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void updateSetting() {
        UpdateSettingsRequest request = new UpdateSettingsRequest();
        request.setDashboardSortOrder(PrefUtils.getSortOrder());
        request.setDashboardSortBy(PrefUtils.getSortBy());
        request.setBeaconServiceStatus(PrefUtils.getBeaconStatus());
        request.setNotifications(PrefUtils.getNotification());
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.updateSettings(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SETTING && resultCode == RESULT_OK) {
            selectDefaultTab();
            mDrawerFragment.setUserProfileInfo();
            caseListingFragment.getCases();
        }
        if (locationBluetoothPermissionUtility != null) {
            locationBluetoothPermissionUtility.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == 2 && resultCode == RESULT_OK) {
            String uid = data.getStringExtra(StringUtils.IntentKey.UID);
            Intent intent = new Intent(this, InventoryDetailActivity.class);
            intent.putExtra(StringUtils.IntentKey.UID, uid);
            intent.putExtra(StringUtils.IntentKey.IS_EDIT, false);
            intent.putExtra(StringUtils.INTENT_SOURCE, "Inventory");
            startActivity(intent);
        }


        super.onActivityResult(requestCode, resultCode, data);
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
                    DialogClass.alerDialog(StrykerHomeActivity.this, getIntent().getStringExtra(StringUtils.IntentKey.SESSION_MESSAGE));
                }

            }, 2000);
        }
    }


    void onSignOutClicked() {
        DialogClass.showDialog(this, this.getString(R.string.please_wait));
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("appCode", PrefUtils.getCode());

        actionsListener.unLinkDevice(jsonObject);
    }

    @Override
    public void onLogoutDone(ApiResponseModel loginResponse, NicbitException e) {
        DialogClass.dismissDialog(this);
        if (e == null) {
            PrefUtils.clearDataOnLogout();
            Intent intent = new Intent(this, StrykerLoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            this.finish();
        } else {
            DialogClass.alerDialog(this, getResources().getString(R.string.check_internet_connection));
        }

    }

    @Override
    public void onGetSettingsResponseReceive(ApiResponseModel response, NicbitException e) {
        DialogClass.dismissDialog(this);
        if (e == null) {
            if (response.getStatus() == StringUtils.SUCCESS_STATUS) {
                ReaderGetSettingsResponse readerGetSettingsResponse = response.getData().getReaderGetSettingsResponse();
                PrefUtils.setSettingFetched(true);
                if (readerGetSettingsResponse != null) {
                    setSetting(readerGetSettingsResponse);
                    if (!PrefUtils.getBeaconStatus())
                        Tracker.stopTracking();
                }

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, caseListingFragment, "caseList");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                selectDefaultTab();
            }

        }
    }

    void getSettings() {
        if (!PrefUtils.getSettingFetched())
            settingActionListener.getSettings();
    }


    @Override
    public void onUpdateSettingsResponseReceive(ApiResponseModel response, NicbitException e) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onUnLinkDevice(ApiResponseModel loginResponse, NicbitException e) {
        DialogClass.dismissDialog(this);

        PrefUtils.clearDataOnLogout();
        Intent intent = new Intent(this, StrykerLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        this.finish();

    }

    @Override
    public void onLinkDevice(ApiResponseModel loginResponse, NicbitException e) {
        Log.i("Device Link", "Device Linked");


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

}

package com.nicbit.traquer.stryker;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.cloudinary.android.MediaManager;
import com.crashlytics.android.Crashlytics;
import com.nicbit.traquer.common.cognito.AppHelper;
import com.nicbit.traquer.common.newInventory.response.ApiBaseResponse;
import com.nicbit.traquer.common.tracking.TraquerScanUtil;
import com.nicbit.traquer.common.updatedevice.DeviceInfo;
import com.nicbit.traquer.common.updatedevice.UpdateDevicePresneter;
import com.nicbit.traquer.common.utils.Constant;
import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.util.PrefUtils;
import com.nicbit.traquer.warehouse.data.Client;
import com.nicbit.traquer.warehouse.updatedevice.RegisterDeviceApiHandler;
import com.nicbit.traquer.warehouse.updatedevice.RegisterUpdateDevicePresneter;
import com.urbanairship.Logger;
import com.urbanairship.UAirship;

import io.akwa.aklogs.NBLogger;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class BaseApplication extends MultiDexApplication implements UpdateDevicePresneter.View, RegisterUpdateDevicePresneter.View {
    public static String deviceId;
    public static String appName;
    public static String appType;
    public static Context context;
    static BaseApplication baseApplication;

    @Override
    public void onCreate() {
        MultiDex.install(this);
        MediaManager.init(this);
        context = this;
        AppHelper.init(getApplicationContext());
        super.onCreate();
        context = this;
        Realm.init(this);
        Fabric.with(this, new Crashlytics());
        deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        appType = "traquer";
        appName = "salesRep";
        new PrefUtils(this);
        airShipSetup();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/source_sans_pro_regular.ttf")
                .build()
        );
        baseApplication = this;
        //  if (PrefUtils.getCode() == null || PrefUtils.getCode().equals("")) {
        DeviceInfo deviceInfo = getDeviceInfo();
        updateDeviceStatus(deviceInfo);

        //  }

        NBLogger.delete3DaysBackProfile();

        //    to solve: android.os.FileUriExposedException: file.jpg exposed beyond app through ClipData.Item.getUri()
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

    }

    public static Context getContext() {
        return context;
    }

    public static BaseApplication getAppInstance() {
        return baseApplication;
    }

    @Override
    public void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        super.registerActivityLifecycleCallbacks(callback);

    }

    private void airShipSetup() {


        UAirship.takeOff(this, new UAirship.OnReadyCallback() {
            @Override
            public void onAirshipReady(UAirship airship) {

                // Enable user notifications
                airship.getPushManager().setUserNotificationsEnabled(true);
                String channelId = UAirship.shared().getPushManager().getChannelId();
                DeviceInfo deviceInfo = getDeviceInfo();
                updateDeviceStatus(deviceInfo);
                Logger.info("My Application Channel ID0000: " + channelId);
            }
        });
        UAirship.shared().getPushManager().setUserNotificationsEnabled(true);
    }


    @Override
    public void onDeviceUpdate(ApiResponseModel response, NicbitException e) {
        if (response != null && response.getStatus() == 1) {
            PrefUtils.setIsDeviceUpdatedToServer(true);
        }


    }

    public static DeviceInfo getDeviceInfo() {

        String channelId = UAirship.shared().getPushManager().getChannelId();
        boolean isBluetooth = TraquerScanUtil.isBluetoothEnabled();
        boolean isLocation = TraquerScanUtil.isLocationServiceEnable(context);
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;


        DeviceInfo deviceInfo = new DeviceInfo();
        int version = android.os.Build.VERSION.SDK_INT;
        deviceInfo.setDeviceId(deviceId);
        deviceInfo.setManufacturer(manufacturer);
        if (BuildConfig.IS_PROD)
            deviceInfo.setAppVersion(getAppVersion() + "(P)");
        else
            deviceInfo.setAppVersion(getAppVersion() + "(Q)");
        deviceInfo.setModel(model);
        deviceInfo.setVersion("" + version);
        deviceInfo.setOs("android");
        deviceInfo.setAppName("salesRep");
        deviceInfo.setChannelId(channelId);
        deviceInfo.setName(getLocalBluetoothName());
        Client client = new Client();
        client.setClientId(Constant.CLIENT_ID);
        client.setProjectId(Constant.PROJECT_ID);
        deviceInfo.setClient(client);
        deviceInfo.setBluetoothStatus(isBluetooth ? 1 : 0);
        deviceInfo.setLocationStatus(isLocation ? 1 : 0);
        return deviceInfo;

    }

    public static String getAppVersion() {
        String versionCode = "1.0";
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public void updateDeviceStatus(DeviceInfo deviceInfo) {
        RegisterDeviceApiHandler deviceApiHandler = new RegisterDeviceApiHandler(this);
        deviceApiHandler.deviceUpdate(deviceInfo);

    }

    @Override
    public void onDeviceUpdate(ApiBaseResponse response, NicbitException e) {
        if (response != null && (response.getCode() == 200 || response.getCode() == 201)) {
            PrefUtils.setProjectId(response.getData().getClient().getProjectId());
            PrefUtils.setClientId(response.getData().getClient().getClientId());
            PrefUtils.setCode(response.getData().getCode());
            PrefUtils.setDeviceId(response.getData().getDeviceId());
            PrefUtils.setUuid(response.getData().getUuid());
            PrefUtils.setMajor(response.getData().getMajor());
            PrefUtils.setMinor(response.getData().getMinor());
        }
    }

    public static String getLocalBluetoothName() {

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String name = mBluetoothAdapter.getName();
        if (name == null) {
            name = mBluetoothAdapter.getAddress();
        }
        return name;
    }


}

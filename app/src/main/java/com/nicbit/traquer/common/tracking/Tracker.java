package com.nicbit.traquer.common.tracking;

import android.content.Intent;
import android.widget.Toast;

import com.nicbit.traquer.common.utils.Constant;
import com.nicbit.traquer.common.utils.Key;
import com.nicbit.traquer.stryker.BaseApplication;
import com.nicbit.traquer.stryker.NBService;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.util.AppLog;
import com.nicbit.traquer.stryker.util.PrefUtils;
import com.nicbit.traquer.stryker.util.StringUtils;

import io.akwa.aklogs.NBLogger;

public class Tracker {

    public static void startTracking() {
        if (!Tracker.isServiceRunning()
                && TraquerScanUtil.isBluetoothEnabled()
                && TraquerScanUtil.isLocationServiceEnable(BaseApplication.getContext())
                && PrefUtils.getBeaconStatus()) {
            Intent intent = new Intent(BaseApplication.getContext(), NBService.class);
            NBLogger.getLoger().writeLog(BaseApplication.getContext(), null, "--- In Tracker.startTracking() ---");
            intent.putExtra(Key.IntentKey.TOKEN, PrefUtils.getSessionToken());
            intent.putExtra(Key.IntentKey.DEVICE_ID, BaseApplication.deviceId);
            intent.putExtra(Key.IntentKey.CLIENT_ID, Constant.CLIENT_ID);
            intent.putExtra(Key.IntentKey.PROJECT_ID, Constant.PROJECT_ID);
            intent.putExtra(Key.IntentKey.CODE, PrefUtils.getCode());
            BaseApplication.getContext().startService(intent);
            AppLog.i("start service activit-----");
            Toast.makeText(BaseApplication.getContext(), BaseApplication.getContext().getString(R.string.app_name) + " service started", Toast.LENGTH_LONG).show();
        }
    }

    public static void stopTracking() {
        if (Tracker.isServiceRunning()) {
            Intent intent = new Intent(BaseApplication.getContext(), NBService.class);
            BaseApplication.getContext().stopService(intent);
            NBLogger.getLoger().writeLog(BaseApplication.getContext(), null, "--- In Tracker.stopTracking.......() ---");
            NBService.setServiceSource("");
            Toast.makeText(BaseApplication.getContext(), BaseApplication.getContext().getString(R.string.app_name) + " service stopped", Toast.LENGTH_LONG).show();
        }
    }

    public static boolean isServiceRunning() {
        return (NBService.getServiceSource() != null && NBService.getServiceSource().equals(StringUtils.APP_NAME));

    }
}

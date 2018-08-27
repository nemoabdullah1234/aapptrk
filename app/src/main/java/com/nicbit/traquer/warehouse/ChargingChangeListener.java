package com.nicbit.traquer.warehouse;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import io.akwa.aklogs.NBLogger;

public class ChargingChangeListener extends BroadcastReceiver {

    private NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (intent.getAction().contains("android.intent.action.ACTION_POWER_CONNECTED")) {
            NBLogger.getLoger().writeLog(context, null, "--Charging Connected --");
            sendNotification(context, "Gateway Charging Status Change", "Charging On " + formattedDate.toString());
        } else {
            NBLogger.getLoger().writeLog(context, null, "--Charging Disconnected --");
            sendNotification(context, "Gateway Charging Status Change", "Charging Off " + formattedDate.toString());

        }
    }

    private void sendNotification(Context context, String title, String message) {

        Notification notification = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(android.R.drawable.ic_lock_idle_low_battery)
                .build();

        notificationManager.notify((new Random()).nextInt(100) + 1, notification);
    }
}

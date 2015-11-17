package com.shhutapp.services;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.shhutapp.MainActivity;

@SuppressLint("NewApi")
public class NLService extends NotificationListenerService {

    private String TAG = "NLService";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "**********  onNotificationStart: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public IBinder onBind(Intent mIntent) {
        IBinder mIBinder = super.onBind(mIntent);
        Log.i(TAG, "Bind");
        return mIBinder;
    }
    @Override
    public boolean onUnbind(Intent mIntent) {
        boolean mOnUnbind = super.onUnbind(mIntent);
        return mOnUnbind;
    }
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i(TAG, "**********  onNotificationPosted: " + sbn.getPackageName());
        if(MainActivity.isDream()){
            this.cancelNotification(sbn.getKey());
        }
        Intent intent = new Intent("app");
        intent.putExtra("type",2);
        sendBroadcast(intent);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG, "********** onNOtificationRemoved");
    }
}

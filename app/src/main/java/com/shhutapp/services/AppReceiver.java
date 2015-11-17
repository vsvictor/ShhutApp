package com.shhutapp.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by victor on 10.06.15.
 */
public class AppReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Notify","Received!!!!!!!!!!!!!!!!!!");
        Intent res = new Intent("app");
        res.putExtra("type",2);
        context.sendBroadcast(intent);
    }
}

package com.shhutapp.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by victor on 10.06.15.
 */
public class MessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.i("SMS", "Bundle: "+bundle.toString());
        if(bundle != null){
            Log.i("Message", "Received");
            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] msgs = new SmsMessage[pdus.length];
            ArrayList<String> numbers = new ArrayList<String>();
            ArrayList<String> messages = new ArrayList<String>();
            for(int i = 0; i<msgs.length;i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                numbers.add(msgs[i].getOriginatingAddress());
                messages.add(msgs[i].getMessageBody().toString());
            }
            if(messages.size()>0){
                Intent res = new Intent("msg");
                res.putExtra("messages",messages);
                res.putExtra("type",3);
                context.sendBroadcast(res);
                Log.i("Message",messages.toString());
            }
        }
        abortBroadcast();
    }
}

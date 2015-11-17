package com.shhutapp.services;

import java.util.ArrayList;
import java.util.Date;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class NoficationService extends AccessibilityService {

	private final String tag = "NotificationService";

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		Log.d(tag, "Inside onAccessibilityEvent:"+event.toString());
		if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED){
			Intent intent = new Intent("app");
			intent.putExtra("type",2);
			sendBroadcast(intent);
			Log.i(tag,"Sended");
		}
	}

	@Override
	public void onInterrupt() {
		isInit = false;
	}
	
	private boolean isInit = false;
	
	@Override
	public void onServiceConnected(){
		Log.d(tag, "Service connected");
	    if (isInit) {
	        return;
	    }
	    AccessibilityServiceInfo info = new AccessibilityServiceInfo();
	    info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
		info.notificationTimeout = 100;
	    info.feedbackType = AccessibilityEvent.TYPES_ALL_MASK;
	    setServiceInfo(info);
	    isInit = true;
	}
}

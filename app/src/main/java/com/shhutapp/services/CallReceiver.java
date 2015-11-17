package com.shhutapp.services;

import java.lang.reflect.Method;
import java.util.ArrayList;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.AppSettings;
import com.shhutapp.data.BaseObjectList;
import com.shhutapp.data.Card;
import com.shhutapp.data.CardType;
import com.android.internal.telephony.ITelephony;
import com.shhutapp.data.Incomming;
import com.shhutapp.data.SMSCard;
import com.shhutapp.data.StringStringPair;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.provider.CallLog;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallReceiver extends BroadcastReceiver {
	private static final String TAG = "Phone call";
	private Context vcontext;
	private static boolean incomingCall = false;
	private ArrayList<Card> activeCards;
	private static AudioManager auManager;
	private static TelephonyManager phManager;
	private boolean sended;
	private static int volume;
	private static int vibrateState;

	/*
	public CallReceiver(){
		super();
	}
	*/
	public CallReceiver(Context context){
		super();
		this.vcontext = context;
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		//sended = false;
		phManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		auManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		//auManager.setStreamVolume(AudioManager.STREAM_RING, 0, 0);
		String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
		AppSettings set = new AppSettings(context);
		incomingCall = true;
		if(MainActivity.getMainActivity().isDream()){
			boolean call = false;
			if(MainActivity.getMainActivity().getWhiteList() == null) {
				Log.i(TAG, "White list is null");
				call = false;
			}
			else{
				if(Carder.inContacts(phoneNumber)){
					Log.i(TAG, "White list not null");
					call = MainActivity.getMainActivity().getWhiteList().inList(phoneNumber);
					Log.i(TAG, "Call is list? "+String.valueOf(call));
				}
				else{
					Log.i(TAG, "White list not null");
					call = MainActivity.getMainActivity().getWhiteList().getUnknown();
					Log.i(TAG, "Call is unknown?"+String.valueOf(call));
				}
			}
			if(call){Log.i(TAG, "Called geo");}
			else{
				disableCall(context);
				if(!sended) {
					sendToCall(context);
					addMissing(context, phoneNumber);
				}
				sended = !sended;
				try{
					SMSCard c = MainActivity.getMainActivity().getSMS();
					if(c != null){
						String sms = MainActivity.getMainActivity().getSMS().getText();
						if(sms != null) {
							if (!sended) sendSMS(phoneNumber, sms, c.getTime());
						}
					}
				}catch(Exception e){
					Log.i(TAG, e.getMessage());
					Log.i(TAG, "Disable call: dream, sms is null");
				}
			}
			return;
		}
		
		activeCards = Carder.getActiveCards();
		if((activeCards == null || activeCards.isEmpty()) && !MainActivity.isDream()){} 
		else {
			for(int i = 0; i< activeCards.size();i++){
				Card c = activeCards.get(i);
				boolean call = false;
				if(c.wl == null) {Log.i(TAG, "White list is null");call = false;}
				else{
					if(Carder.inContacts(phoneNumber)){
						Log.i(TAG, "White list not null");
						call = c.inWhiteList(phoneNumber);
						Log.i(TAG, "Call is list? "+String.valueOf(call));
					}
					else{
						Log.i(TAG, "White list not null");
						call = c.wl.getUnknown();
						Log.i(TAG, "Call is unknown?"+String.valueOf(call));
					}
				}
				if(c.getType() == CardType.Geo){
					if(call){Log.i(TAG, "Called geo");}
					else{
						disableCall(context);
						if(!sended) {
							sendToCall(context);
							addMissing(context, phoneNumber);
						}
						sended = !sended;
						try{
							String sms = c.buildSMSText();
							if(!sended) sendSMS(phoneNumber,sms, c.getTime());
						} catch(Exception e){
							Log.i(TAG, e.getMessage());
							Log.i(TAG, "Disable call: geo");							
						}

					}
					break;
				}
				String sms = "";
				if(c.getType() == CardType.Dream){
					if(call){Log.i(TAG, "Called dream");}
					else {
						disableCall(context);
						if(sended) {
							sendToCall(context);
							addMissing(context, phoneNumber);
						}
						sended = !sended;
						try{
							sms = c.getSMSText();
							if(!sended) sendSMS(phoneNumber,sms, c.getTime());
						} catch(Exception e){
							Log.i(TAG, e.getMessage());
							Log.i(TAG, "Disable call: dream card");							
						}
					}
					break;
				}
				
			}
		}
		//setVolumeDefault(context);
		Log.v(TAG, "Receving " + phoneNumber);
		//auManager.setStreamVolume(AudioManager.STREAM_RING, 100, 0);
	}
	
	//@SuppressWarnings("deprecation")
	private static void disableCall(Context context){
		ITelephony telephonyService;

		//volume = auManager.getStreamVolume(AudioManager.STREAM_RING);
		//vibrateState = auManager.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER);
		try {
			Class c = Class.forName(phManager.getClass().getName());
			Method m = c.getDeclaredMethod("getITelephony");
			m.setAccessible(true);
			telephonyService = (ITelephony) m.invoke(phManager);
			//telephonyService.silenceRinger();
			telephonyService.endCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void sendSMS(String ph, String text, int time){
		BaseObjectList list = MainActivity.getMainActivity().getIncomming();
		Incomming f = (Incomming) list.find(ph);
		if(f == null) {
			Incomming in = new Incomming(list.size(),ph,System.currentTimeMillis());
			MainActivity.getMainActivity().getIncomming().add(in);
			SmsManager.getDefault().sendTextMessage(ph, null, text, null, null);
			sended = true;
		}
		else{
			if (((System.currentTimeMillis()-f.getTime())/1000)>time){
				SmsManager.getDefault().sendTextMessage(ph, null, text, null, null);
				sended = true;
			}
		}
	}
	private static void sendToCall(Context context){
		Intent intent = new Intent("call");
		intent.putExtra("type",1);
		context.sendBroadcast(intent);
	}
	private void addMissing(Context context, String number){
		Long l = System.currentTimeMillis();
		Uri call_log = CallLog.Calls.CONTENT_URI;
		ContentValues values = new ContentValues();
		values.put(CallLog.Calls.NUMBER, number);
		values.put(CallLog.Calls.DATE, l);
		values.put(CallLog.Calls.DURATION, 0);
		values.put(CallLog.Calls.TYPE, CallLog.Calls.INCOMING_TYPE);
		values.put(CallLog.Calls.NEW, 1);
		values.put(CallLog.Calls.CACHED_NAME, "");
		values.put(CallLog.Calls.CACHED_NUMBER_TYPE, 0);
		values.put(CallLog.Calls.CACHED_NUMBER_LABEL, "");
		Log.d(TAG, "Inserting call log placeholder for " + number);
		String[] cols = {CallLog.Calls.NUMBER};
/*		Cursor c = context.getContentResolver().query(
				CallLog.Calls.CONTENT_URI,cols,CallLog.Calls.NUMBER+"=? AND "+CallLog.Calls.DATE+"=?",
				new String[]{number, String.valueOf(l)},null);
		if(!c.moveToFirst())*/

			context.getContentResolver().insert(CallLog.Calls.CONTENT_URI, values);
	}
}

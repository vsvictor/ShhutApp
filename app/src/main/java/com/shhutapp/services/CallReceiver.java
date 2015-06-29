package com.shhutapp.services;

import java.lang.reflect.Method;
import java.util.ArrayList;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.AppSettings;
import com.shhutapp.data.Card;
import com.shhutapp.data.CardType;
import com.android.internal.telephony.ITelephony;
import com.shhutapp.data.SMSCard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
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
	public CallReceiver(){
		super();
	}
	
	public CallReceiver(Context context){
		super();
		this.vcontext = context;
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		sended = false;
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
				sendToCall(context);
				try{
					SMSCard c = MainActivity.getMainActivity().getSMS();
					if(c != null){
						String sms = MainActivity.getMainActivity().getSMS().getText();
						if(sms != null) {
							if (!sended) sendSMS(phoneNumber, sms);
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
						sendToCall(context);
						try{
							String sms = c.buildSMSText();
							if(!sended) sendSMS(phoneNumber,sms);
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
						sendToCall(context);
						try{
							sms = c.getSMSText();
							if(!sended) sendSMS(phoneNumber,sms);
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
	private void sendSMS(String ph, String text){
		SmsManager.getDefault().sendTextMessage(ph, null, text, null, null);
		sended = true;
	}
	private static void sendToCall(Context context){
		Intent intent = new Intent("call");
		intent.putExtra("type",1);
		context.sendBroadcast(intent);
	}
}

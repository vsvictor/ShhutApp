package com.shhutapp.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;
import com.shhutapp.Actions;
import com.shhutapp.Constants;
import com.shhutapp.JSONParser;
import com.shhutapp.LocaleStaticData;

import de.mindpipe.android.logging.log4j.LogConfigurator;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class Finder extends Service{
	private static final String TAG = "Finder";
	private Logger logger;
	private JSONParser parser;
	@Override
	public void onCreate(){
		super.onCreate();
		logger = createLogger();
		parser = new JSONParser();
	}
	@Override
	public int onStartCommand(final Intent intent, int flags, int startID) {
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		if(!wifiManager.isWifiEnabled()) return Service.START_NOT_STICKY;
		try {
			String findText = intent.getExtras().getString("find");
			Questor qq = new Questor();
			qq.execute(findText);
		}catch (Exception e){

		}
		logger.info(TAG+": intent sended");
		return Service.START_REDELIVER_INTENT;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	public Logger createLogger(){
        LogConfigurator logConfigurator = new LogConfigurator();
        logConfigurator.setFileName(Environment.getExternalStorageDirectory()
                        + File.separator + "Logs"
                        + File.separator + "ShhutApp.log");
        logConfigurator.setRootLevel(Level.DEBUG);
        logConfigurator.setLevel("org.apache", Level.ERROR);
        logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
        logConfigurator.setMaxFileSize(1024 * 1024 * 5);
        logConfigurator.setImmediateFlush(true);
        logConfigurator.configure();
        Logger logger = Logger.getLogger("Locator");
        return logger;
	}
	private class Questor extends AsyncTask<String, Void, JSONObject>{

		@Override
		protected JSONObject doInBackground(String... params) {
			String findText = params[0];
			JSONObject rObj = null;
			ArrayList<NameValuePair> pair = new ArrayList<NameValuePair>();
			BasicNameValuePair p1 = new BasicNameValuePair("sensor", "true");
			BasicNameValuePair p2 = new BasicNameValuePair("address", findText);
			BasicNameValuePair p3 = new BasicNameValuePair("language", Locale.getDefault().getLanguage());
			BasicNameValuePair p4 = new BasicNameValuePair("key", Constants.GeoAPIKey);
			BasicNameValuePair p5 = new BasicNameValuePair("client", Constants.GeoAPIKey);
			pair.add(p1);
			pair.add(p2);
			pair.add(p3);
			try {
				rObj = parser.makeHttpRequest(Constants.geoCode, "GET", pair);
			} catch (IOException e1) {
				rObj = null;
				e1.printStackTrace();
			}
			return rObj;
		}

		@Override
		protected void onPostExecute(JSONObject res) {
			super.onPostExecute(res);
			Intent result = new Intent(Actions.Finder);
			JSONObject obj = new JSONObject();
			try {
				obj.put("status_code", 1);
				obj.put("name", Actions.Finder);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			try {
				String sRes = res.getString("status");
				if(sRes.equals("OK")){
					logger.info(TAG+": result obj: "+res.toString());
					JSONArray in = res.getJSONArray("results");
					JSONArray out = new JSONArray();
					for(int i = 0; i<in.length();i++){
						JSONObject inRow = in.getJSONObject(i);
						String s = inRow.getString("formatted_address");
						JSONObject geom = inRow.getJSONObject("geometry");
						JSONObject loc = geom.getJSONObject("location");
						double lat = loc.getDouble("lat");
						double lon = loc.getDouble("lng");
						JSONObject row = new JSONObject();
						row.put("address", s);
						row.put("lat", lat);
						row.put("lon", lon);
						out.put(row);
					}
					obj.put("name", Actions.Finder);
					obj.put("result", out);
					obj.put("status_code", 1);
				}
				else{
					obj.put("result", new JSONArray());
				}
			} catch (JSONException e) {
				logger.info(TAG+": result obj excaption: "+e.getMessage());
			}
			result.putExtra("name", Actions.Finder);
			result.putExtra("command", obj.toString());
			sendBroadcast(result);
			stopSelf();
		}
	}
}

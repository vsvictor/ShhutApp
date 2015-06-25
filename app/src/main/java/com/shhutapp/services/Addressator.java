package com.shhutapp.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.model.LatLng;
import com.shhutapp.Actions;
import com.shhutapp.Constants;
import com.shhutapp.JSONParser;
import com.shhutapp.data.DBHelper;

import de.mindpipe.android.logging.log4j.LogConfigurator;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;

public class Addressator extends Service{
	private static final String TAG = "Addressator";
	private JSONParser parser;
	//private Logger logger;
	private String name;
	private double lat;
	private double lon;
	private double radius;
	private DBHelper helper;
	private SQLiteDatabase db;
	public void onCreate(){
		super.onCreate();
		//logger = createLogger();
		parser = new JSONParser();
		helper = new DBHelper(getApplicationContext());
		db = helper.getWritableDatabase();
	}
	@Override
	public int onStartCommand(final Intent intent, int flags, int startID) {
		//WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		//if(!wifiManager.isWifiEnabled()) return Service.START_NOT_STICKY;
		name = intent.getExtras().getString("name");
		lat = intent.getExtras().getDouble("lat");
		lon = intent.getExtras().getDouble("long");
		radius = Math.abs(intent.getExtras().getDouble("radius"));
		Questor qq = new Questor();
		qq.execute(new LatLng(lat, lon));
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
	private class Questor extends AsyncTask<LatLng, Void, JSONObject>{

		@Override
		protected JSONObject doInBackground(LatLng... params) {
			LatLng loc = params[0];
			JSONObject rObj = null;
			ArrayList<NameValuePair> pair = new ArrayList<NameValuePair>();
			BasicNameValuePair p1 = new BasicNameValuePair("sensor", "true");
			BasicNameValuePair p2 = new BasicNameValuePair("latlng", String.valueOf(loc.latitude)+","+String.valueOf(loc.longitude));
			BasicNameValuePair p3 = new BasicNameValuePair("language", Locale.getDefault().getLanguage());
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
			String city = " ";
			String street = " ";
			String number = " ";
			Intent result = new Intent(Actions.Addressator);
			JSONObject obj = new JSONObject();
			try {
				JSONArray all = res.getJSONArray("results");
				JSONObject r = all.getJSONObject(0);
				JSONArray components = r.getJSONArray("address_components");
				for(int i = 0; i<components.length();i++){
					JSONObject curr = components.getJSONObject(i);
					JSONArray types = curr.getJSONArray("types");
					for(int j = 0; j< types.length(); j++){
						String s = types.getString(j);
						if(s.equals("route")) street = curr.getString("long_name");
						if(s.equals("locality")) city = curr.getString("short_name");
						if(s.equals("street_number")) number = curr.getString("short_name");
					}
				}
				if(!number.equals(" ")){
					street += ", ";
					street += number;
				}
				obj.put("name", name);
				obj.put("lat", lat);
				obj.put("long", lon);
				obj.put("city", city);
				obj.put("radius", Math.abs(radius));
				obj.put("street", street);
				obj.put("status_code", 1);
			}
			catch (NullPointerException e1){
				e1.printStackTrace();
				try {
					if(obj == null) obj = new JSONObject();
					obj.put("name", name);
					obj.put("lat", lat);
					obj.put("long", lon);
					obj.put("city", " ");
					obj.put("radius", Math.abs(radius));
					obj.put("street", " ");
					obj.put("status_code", -1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			catch (JSONException e1) {
				e1.printStackTrace();
				try {
					if(obj == null) obj = new JSONObject();
					obj.put("name", name);
					obj.put("lat", lat);
					obj.put("long", lon);
					obj.put("city", " ");
					obj.put("radius", Math.abs(radius));
					obj.put("street", " ");
					obj.put("status_code", 1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			result.putExtra("name", Actions.Addressator);
			result.putExtra("command", obj.toString());
			sendBroadcast(result);
			stopSelf();
		}
	}
}

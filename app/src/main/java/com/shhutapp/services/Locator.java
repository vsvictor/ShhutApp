package com.shhutapp.services;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;
import com.shhutapp.Actions;
import com.shhutapp.MainActivity;

import de.mindpipe.android.logging.log4j.LogConfigurator;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;

public class Locator extends Service{
	private static final String TAG = "Locator";
	private LocationManager locationManager;
	private String provider;
	private Criteria criteria;
	private Location loc;
	private Logger logger;
	@Override
	public void onCreate(){
		super.onCreate();
		logger = createLogger();
		if(!Locator.isLocationEnabled(MainActivity.getMainActivity())) return;
    	locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
    	criteria = new Criteria();
    	provider = locationManager.getBestProvider(criteria, true);
    	logger.info(TAG+": provider: "+provider);
    	loc = locationManager.getLastKnownLocation(provider);
    	if(loc == null){
    		loc = new Location(provider);
    		loc.setLatitude(0);
    		loc.setLongitude(0);
    	}
    	logger.info(TAG+": prev location: ("+String.valueOf(loc.getLatitude())+":"+String.valueOf(loc.getLatitude())+")");
    	locationManager.requestLocationUpdates(provider, 1000*5, 10, listener);
    	logger.info(TAG+": listener is set ");
	}
	@Override
	public int onStartCommand(final Intent intent, int flags, int startID) {
		if(!Locator.isLocationEnabled(MainActivity.getMainActivity())) return Service.START_NOT_STICKY;
    	criteria = new Criteria();
    	provider = locationManager.getBestProvider(criteria, true);
    	logger.info(TAG+": provider onStartComment: "+provider);
    	loc = locationManager.getLastKnownLocation(provider);
    	if(loc == null){
    		loc = new Location(provider);
    		loc.setLatitude(0);
    		loc.setLongitude(0);
    	}
    	logger.info(TAG+": OnStartCommand location: ("+String.valueOf(loc.getLatitude())+":"+String.valueOf(loc.getLatitude())+")");
		Intent res = new Intent(Actions.Location);
		JSONObject obj = new JSONObject();
		try {
			obj.put("lat", loc.getLatitude());
			obj.put("lon", loc.getLongitude());
			obj.put("status_code", 1);
			logger.info(TAG+": result obj: "+obj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
			logger.info(TAG+": result obj excaption: "+e.getMessage());
		}
		res.putExtra("name", Actions.Location);
		res.putExtra("command", obj.toString());
		sendBroadcast(res);
		stopSelf();
		logger.info(TAG+": intent sended");
		return Service.START_REDELIVER_INTENT;
	}
	private LocationListener listener = new LocationListener(){
		@Override
		public void onLocationChanged(Location location){
		    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
		    locationManager.removeUpdates(this);
		    loc = location;
		    logger.info(TAG+": in listener onLocationChanged location: ("+String.valueOf(loc.getLatitude())+":"+String.valueOf(loc.getLatitude())+")");
		}
		@Override
		public void onProviderDisabled(String provider) {
		}
		@Override
		public void onProviderEnabled(String provider) {
		}
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};
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
	public static boolean isLocationEnabled(Context context) {
	    int locationMode = 0;
	    String locationProviders;

	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
	        try {
	            locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

	        } catch (SettingNotFoundException e) {
	            e.printStackTrace();
	        }

	        return locationMode != Settings.Secure.LOCATION_MODE_OFF;

	    }else{
	        locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
	        return !TextUtils.isEmpty(locationProviders);
	    }
	} 
}

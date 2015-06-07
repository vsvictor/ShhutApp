package com.shhutapp;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.model.LatLng;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

public class LocaleStaticData {

	@SuppressLint("NewApi")
	public static LatLng CoodinateFromName(Context context, String country, String city){
		LatLng result = null;
		String sFind = "";
		if(country != null && !country.isEmpty()) {sFind += country;sFind+=",";}
		if(city != null && !city.isEmpty()){sFind += city;}
		Geocoder geocoder = new Geocoder(context, Locale.getDefault());
		try {
			List<Address> addressed = geocoder.getFromLocationName(sFind, 1);
			if(addressed != null && !addressed.isEmpty()){
				Address ad = addressed.get(0);
				result = new LatLng(ad.getLatitude(), ad.getLongitude());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static LatLng CoodinateFromName(Context context, String data){
		LatLng result = null;
		Geocoder geocoder = new Geocoder(context, Locale.getDefault());
		try {
			List<Address> addressed = geocoder.getFromLocationName(data, 30);
			if(addressed != null && !addressed.isEmpty()){
				Address ad = addressed.get(0);
				result = new LatLng(ad.getLatitude(), ad.getLongitude());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}

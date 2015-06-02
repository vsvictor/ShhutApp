package com.shhutapp;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {
	private Context context;
	private SharedPreferences sh;
	public Settings(Context context){
		this.context = context;
		sh = context.getSharedPreferences("shhutapp", context.MODE_PRIVATE);
	}
	public void setFirst(boolean first){
		SharedPreferences.Editor ed = sh.edit();
		ed.putBoolean("first", first);
		ed.commit();
	}
	public boolean isFirst(){
		return sh.getBoolean("first", true);
	}
	public void setMinAreaRadius(int min){
		SharedPreferences.Editor ed = sh.edit();
		ed.putInt("minAreaRadius", min);
		ed.commit();
	}
	public int getMinAreaRadius(){
		return sh.getInt("minAreaRadius", 5);
	}
	public void setDefaultAreaRadius(int min){
		SharedPreferences.Editor ed = sh.edit();
		ed.putInt("defaultAreaRadius", min);
		ed.commit();
	}
	public int getDefaultAreaRadius(){
		return sh.getInt("defaultAreaRadius", 100);
	}
	public void setDefaultCallVolume(int vol){
		SharedPreferences.Editor ed = sh.edit();
		ed.putInt("volume", vol);
		ed.commit();
	}
	public int getDefaultCallVolume(){
		return sh.getInt("volume", 80);
	}
	public void setDefaultVibrate(boolean b){
		SharedPreferences.Editor ed = sh.edit();
		ed.putBoolean("virate", b);
		ed.commit();
	}
	public boolean getDefaultVibtare(){
		return sh.getBoolean("vibrate", true);
	}
	public SharedPreferences getSharedPreferences(){return sh;}
}

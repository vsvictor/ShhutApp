package com.shhutapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;

import com.shhutapp.data.IntStringPair;
import com.shhutapp.data.StringStringPair;

public class AppSettings {
	private Context context;
	private SharedPreferences sh;
	public AppSettings(Context context){
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
	public void setFirstQueit(boolean first){
		SharedPreferences.Editor ed = sh.edit();
		ed.putBoolean("first_q", first);
		ed.commit();
	}
	public boolean isFirstQueit(){
		return sh.getBoolean("first_q", true);
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
	public boolean getDefaultOnVolumeButton(){
		return sh.getBoolean("onVolumeButton", true);
	}
	public void setOnVolumeButton(boolean onSet){
		SharedPreferences.Editor ed = sh.edit();
		ed.putBoolean("onVolumeButton",onSet);
		ed.commit();
	}
	public boolean getVibtateOnActivate(){
		return sh.getBoolean("onVibrate", true);
	}
	public void setVibrateOnActivate(boolean onSet){
		SharedPreferences.Editor ed = sh.edit();
		ed.putBoolean("onVibrate",onSet);
		ed.commit();
	}

	public void setDefaultVibrate(boolean b){
		SharedPreferences.Editor ed = sh.edit();
		ed.putBoolean("virate", b);
		ed.commit();
	}
	public boolean getDefaultVibtare(){
		return sh.getBoolean("vibrate", true);
	}
	public StringStringPair getLanguage(){
		String s1 = sh.getString("shortLang","def");
		String s2 = sh.getString("longLang",context.getResources().getString(R.string.use_default_lang));
		return new StringStringPair(s1,s2);
	}
	public void setLanguage(StringStringPair pair){
		SharedPreferences.Editor ed = sh.edit();
		ed.putString("shortLang",pair.getID());
		ed.putString("longLang",pair.getName());
		ed.commit();
	}
	public IntStringPair getTeam(){
		int s1 = sh.getInt("idTeam", 0);
		String s2 = sh.getString("nameTeam","Material Cyan");
		return new IntStringPair(s1,s2);
	}
	public void setTeam(IntStringPair pair){
		SharedPreferences.Editor ed = sh.edit();
		ed.putInt("idTeam", pair.getID());
		ed.putString("nameTeam",pair.getName());
		ed.commit();
	}

	public int getDefaultAccuracy(){ return  sh.getInt("accuracy", Criteria.ACCURACY_MEDIUM);}
	public SharedPreferences getSharedPreferences(){return sh;}
	public boolean isPurchased(){
		return sh.getBoolean("purchased", false);
		//return true;
	}
	public void setPurchased(boolean purchased){
		SharedPreferences.Editor ed = sh.edit();
		ed.putBoolean("purchased", purchased);
		ed.commit();
	}
}

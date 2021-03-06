package com.shhutapp.data;

import com.shhutapp.R;
import com.shhutapp.utils.DateTimeOperator;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import java.util.Date;

public class GeoCard extends IntStringPair{
	private Context context;
	private CardType type;
	private String actName;
	private String address;
	private int actType;
	private long actTime;
	private double lat;
	private double lon;
	private double radius;
	private Bitmap background;
	private int minutes;
	private boolean onoff;
	public GeoCard(){
		super(-1, "");
		this.type = CardType.Geo;
		context = null;
		onoff = true;
	}
	public GeoCard(Context context){
		super(-1, "");
		this.type = CardType.Geo;
		this.context = context;
		onoff = true;
	}

	//public void setActivationName(String actName){this.actName = actName;}
	//public String getActivationName(){return this.actName;}
	public String getActivationName(){
		String result = null;
		if(actType == 1){
			result = context.getResources().getString(R.string.here);
		}
		else if(actType == 2){
			result = context.getResources().getString(R.string.quiet_hours);
		}
		else if(actType == 4){
			result = context.getResources().getString(R.string.first_from_grand)+" "+DateTimeOperator.toDateTime(actTime, "HH:mm")+"+"+context.getResources().getString(R.string.quiet_hours);
		}
		else if(actType == 3){
			//result = context.getResources().getString(R.string.first_from_grand)+" "+DateTimeOperator.toDateTime(actTime, "HH:mm");
			result = context.getResources().getString(R.string.first_from_grand)+" "+DateTimeOperator.dateToTimeString(DateTimeOperator.toDateTime(minutes*60*1000, "HH:mm"));
			//result = context.getResources().getString(R.string.first_from_grand)+DateTimeOperator.minutesToTimeString(minutes);
		}
		else{
			result = " ";
		}
		return result;
	}
	public void setActivationTime(long actTime){this.actTime = actTime;}
	public long getActivationTime(){return this.actTime;}
	public void setCardType(CardType aType){type = aType;}
	public CardType getCardType(){return this.type;}
	public void setAddress(String address){this.address = address;}
	public String getAddress(){return this.address;}
	public void setTypeActivation(int actType){this.actType = actType;}
	public int getTypeActivation(){return this.actType;}
	public void setTimeActivation(long actTime){this.actTime = actTime;}
	public long getTimeActivation(){return this.actTime;}
	public int getWhiteListID(){return -1;}
	public void setLantitude(double lat){this.lat = lat;}
	public double getLantitude(){return this.lat;}
	public void setLongitude(double lon){this.lon = lon;}
	public double getLongitude(){return this.lon;}
	public void setRadius(double radius){this.radius = radius;}
	public double getRadius(){return this.radius;}
	public void setBackground(Bitmap b){
		this.background = b;
	}
	public Bitmap getBackground(){
		return this.background;
	}
	public void setMinutes(int iMin){
		minutes = iMin;
	}
	public int getMinutes(){
		return this.minutes;

	}
	public void setOnoff(boolean val){
		this.onoff = val;
	}
	public boolean isOn(){
		return onoff;
	}
	public void updateOnOff(SQLiteDatabase db){
		ContentValues row = new ContentValues();
		row.put("onoff", this.onoff?1:0);
		db.update("cards", row,"id=?",new String[]{String.valueOf(getID())});
	}
}

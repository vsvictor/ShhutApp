package com.shhutapp.data;

import com.shhutapp.R;
import com.shhutapp.utils.DateTimeOperator;

import android.content.Context;

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
	public GeoCard(){
		super(-1, "");
		this.type = CardType.Geo;
		context = null;
	}
	public GeoCard(Context context){
		super(-1, "");
		this.type = CardType.Geo;
		this.context = context;
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
			result = context.getResources().getString(R.string.first_from_grand)+" "+DateTimeOperator.dateToTimeString(DateTimeOperator.toDateTime(actTime, "HH:mm"));
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
}

package com.shhutapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SMSCard extends IntStringPair {
	private String text;
	private int time;
	private boolean OnOff = false;
	public SMSCard(){
		super(-1,"");
		this.text = "";
		OnOff = false;
		time = -1;
	}
	public SMSCard(int id, String name){
		super(id, name);
		this.text = "";
		OnOff = false;
		time = -1;
	}
	public SMSCard(int id, String name, String text){
		super(id, name);
		this.text = text;
		OnOff = false;
		time = -1;
	}
	
	public void setText(String text){this.text = text;}
	public String getText(){return this.text;}
	public void save(SQLiteDatabase db){
		String s = getText();
		int beg = s.indexOf("      ");
		if(beg > 0) {
			String res = s.substring(0, beg) + "{Loc}" + s.substring(beg + 6, s.length());
			setText(res);
		}
		ContentValues row = new ContentValues();
		row.put("name", getName());
		row.put("sms", getText());
		row.put("time",getTime());
		if(this.getID() > 0){
			String[] args = {String.valueOf(getID())};
			db.update("sms",row,"id=?",args);
		}
		else {
			db.insert("sms", null, row);
		}
	}
	public void setState(boolean b){ this.OnOff = b;}
	public boolean getState(){return this.OnOff;}
	public void setTime(int time){
		this.time = time;
	}
	public int getTime(){return this.time;}
}

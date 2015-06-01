package com.shhutapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SMSCard extends IntStringPair {
	private String text;
	private boolean OnOff = false;
	public SMSCard(){
		super(-1,"");
		this.text = "";
		OnOff = false;
	}
	public SMSCard(int id, String name){
		super(id, name);
		this.text = "";
		OnOff = false;
	}
	public SMSCard(int id, String name, String text){
		super(id, name);
		this.text = text;
		OnOff = false;
	}
	
	public void setText(String text){this.text = text;}
	public String getText(){return this.text;}
	public void save(SQLiteDatabase db){
		ContentValues row = new ContentValues();
		row.put("name", getName());
		row.put("sms", getText());
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
}

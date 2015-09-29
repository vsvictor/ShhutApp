package com.shhutapp.data;

import com.shhutapp.MainActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class Card extends IntStringPair {
	private CardType type;
	private long idLoc;
	private long idQuiet;
	private long idMessage;
	private long idWhiteList;
	
	public GeoCard geo;
	public QueitCard dream;
	public SMSCard sms;
	public WhiteListCard wl;

	private int minutes;
	private boolean onoff;
	private int time;

	public Card(){
		super(-1,"");
		this.idLoc = -1;
		this.idQuiet = -1;
		this.idMessage = -1;
		this.idWhiteList = -1;
		geo = null;
		dream = null;
		sms = null;
		wl = null;
		onoff = true;
	}
	public void setType(CardType type){this.type = type;}
	public CardType getType(){return this.type;}
	public void setLocation(long idLoc){this.idLoc = idLoc;}
	public long getLocation(){return this.idLoc;}
	public void setQuiet(long idQuiet){this.idQuiet = idQuiet;}
	public long getQuiet(){return this.idQuiet;}
	public void setSMS(long idSMS){this.idMessage = idSMS;}
	public long getSMS(){ return this.idMessage;}
	public void setWhiteList(long idWhiteList){this.idWhiteList = idWhiteList;}
	public long getWhiteList(){return this.idWhiteList;}
	public static int cardTypeToId(CardType c){
		int res = -1;
		if(c == CardType.Geo) res = 1;
		else if(c == CardType.Dream) res = 2;
		else if(c == CardType.Message) res = 3;
		else if(c == CardType.WhiteList) res = 4;
		return res;
	}
	public static CardType idToCardType(int i){
		CardType res = CardType.Error;
		switch(i){
			case 1:{res = CardType.Geo;break;}
			case 2:{res = CardType.Dream;break;}
			case 3:{res = CardType.Message;break;}
			case 4:{res = CardType.WhiteList;break;}
			default:{res = CardType.Error;break;}
		}
		return res;
	}
	public String getSMSText(){
		if(sms != null)	return sms.getText();
		else return " ";
	}
	public String buildSMSText(){
		String s = "";
		String r = "";
		StringBuilder b = new StringBuilder();
		if(sms != null){
			s = sms.getText();
			int beg = s.indexOf("{Location}");
			r = s.substring(0, beg-1);
			r += this.geo.getName();
			String sEnd = s.substring(beg+10);
			r +=sEnd;
		}
		return r;
	}
	public void save(SQLiteDatabase db){
		ContentValues cv = new ContentValues();
		cv.put("type", cardTypeToId(this.type));
		cv.put("name", " ");
		cv.put("idActivate", 0);
		cv.put("idGeo", this.idLoc);
		cv.put("idDream", this.idQuiet);
		cv.put("idWhiteList", this.idWhiteList);
		cv.put("idMessage", this.idMessage);
		cv.put("onoff", this.onoff?1:0);
		db.insert("cards", null, cv);
	}
	public void save(SQLiteDatabase db, boolean isUpdate){
		ContentValues cv = new ContentValues();
		if(isUpdate){
			if(this.idLoc != -1) cv.put("idGeo", this.idLoc);
			if(this.idQuiet != -1)cv.put("idDream", this.idQuiet);
			if(this.idWhiteList != -1)cv.put("idWhiteList", this.idWhiteList);
			if(this.idMessage != -1)cv.put("idMessage", this.idMessage);
			cv.put("onoff", this.onoff ? 1 : 0);
		}
		else {
			cv.put("type", cardTypeToId(this.type));
			cv.put("name", " ");
			cv.put("idActivate", 0);
			cv.put("idGeo", this.idLoc);
			cv.put("idDream", this.idQuiet);
			cv.put("idWhiteList", this.idWhiteList);
			cv.put("idMessage", this.idMessage);
			cv.put("onoff", this.onoff ? 1 : 0);
		}
		if(isUpdate){
			int r = db.update("cards", cv,"id=?",new String[]{String.valueOf(getID())});
			//Toast.makeText(MainActivity.getMainActivity(),"Updated: "+String.valueOf(r), Toast.LENGTH_LONG).show();
		}
		else db.insert("cards", null, cv);
	}

	public boolean inWhiteList(String number){
		if(this.wl == null) return false;
		else return this.wl.inList(number);
	}
	public void setMinutes(int min){
		this.minutes = min;
	}
	public int getMinutes(){
		return this.minutes;
	}
	public void setOnOff(boolean val){
		this.onoff = val;
	}
	public boolean isOn(){
		return onoff;
	}
	public int getTime(){
		return sms.getTime();
	}
}

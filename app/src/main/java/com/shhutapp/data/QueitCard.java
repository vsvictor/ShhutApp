package com.shhutapp.data;

import java.util.Comparator;
import java.util.Date;

import com.shhutapp.MainActivity;
import com.shhutapp.utils.DateTimeOperator;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;
import android.widget.Toast;

public class QueitCard extends IntStringPair{
	private Date beg;
	private Date end;
	private boolean[] days;
	private boolean isOn;
	private boolean isMessage;
	
	public QueitCard(){
		super(-1,"");
		this.beg = new Date();
		this.end = new Date();
		this.days = new boolean[7];
		for(int i = 0;i<7; i++) this.days[i] = false;
		isOn = true;
		isMessage = false;
	}
	public QueitCard(int id, String name){
		super(id, name);
		this.beg = new Date();
		this.end = new Date();
		this.days = new boolean[7];
		for(int i = 0;i<7; i++) this.days[i] = false;
		isMessage = false;
	}
	public QueitCard(int id, String name, Date beg, Date end){
		super(id, name);
		this.beg = beg;
		this.end = end;
		this.days = new boolean[7];
		for(int i = 0;i<7; i++) this.days[i] = false;
		isMessage = false;
	}
	public QueitCard(int id, String name, Date beg, Date end, boolean[] days){
		super(id, name);
		this.beg = beg;
		this.end = end;
		this.days = new boolean[7];
		for(int i = 0;i<days.length; i++) this.days[i] = days[i];
		for(int i = days.length;i<7; i++) this.days[i] = false;
		isMessage = false;
	}
	public void setBegin(Date d){this.beg = d;}
	public Date getBegin(){return beg;}
	public void setEnd(Date d){this.end = d;}
	public Date getEnd(){return this.end;}
	public void setDays(boolean[] days){this.days = days.clone();}
	public void setDays(boolean d1,boolean d2,boolean d3,boolean d4,boolean d5,boolean d6,boolean d7){
		this.days = new boolean[7];
		this.days[0] = d1;
		this.days[1] = d2;
		this.days[2] = d3;
		this.days[3] = d4;
		this.days[4] = d5;
		this.days[5] = d6;
		this.days[6] = d7;
	}
	public boolean[] getDays(){return this.days.clone();}
	public boolean isAllDays(){
		boolean res = true;
		for(int i = 0; i<7; i++) res = res && !this.days[i];
		return res;
	}
	public boolean isDay(int d){
		return days[d];
	}
	public void save(SQLiteDatabase db){
		Card card = new Card();
		ContentValues cv = new ContentValues();
		cv.put("begtime", this.beg.getTime());
		cv.put("endtime", this.end.getTime());
		cv.put("is1", this.days[0]?1:0);
		cv.put("is2", this.days[1]?1:0);
		cv.put("is3", this.days[2]?1:0);
		cv.put("is4", this.days[3]?1:0);
		cv.put("is5", this.days[4]?1:0);
		cv.put("is6", this.days[5]?1:0);
		cv.put("is7", this.days[6]?1:0);
		long l = db.insert("quieties", null, cv);
		card.setType(CardType.Dream);
		card.setQuiet(l);
		card.save(db);
	}
	public void save(SQLiteDatabase db, long idMessage){
		Card card = new Card();
		ContentValues cv = new ContentValues();
		cv.put("begtime", this.beg.getTime());
		cv.put("endtime", this.end.getTime());
		cv.put("is1", this.days[0]?1:0);
		cv.put("is2", this.days[1]?1:0);
		cv.put("is3", this.days[2]?1:0);
		cv.put("is4", this.days[3]?1:0);
		cv.put("is5", this.days[4]?1:0);
		cv.put("is6", this.days[5]?1:0);
		cv.put("is7", this.days[6]?1:0);
		long l = db.insert("quieties", null, cv);
		card.setType(CardType.Dream);
		card.setQuiet(l);
		card.setSMS(idMessage);
		card.save(db);
	}
	public void save(SQLiteDatabase db, long idMessage, long idWhiteList){
		Card card = new Card();
		ContentValues cv = new ContentValues();
		cv.put("begtime", this.beg.getTime());
		cv.put("endtime", this.end.getTime());
		cv.put("is1", this.days[0]?1:0);
		cv.put("is2", this.days[1]?1:0);
		cv.put("is3", this.days[2]?1:0);
		cv.put("is4", this.days[3]?1:0);
		cv.put("is5", this.days[4]?1:0);
		cv.put("is6", this.days[5]?1:0);
		cv.put("is7", this.days[6]?1:0);
		long l = db.insert("quieties", null, cv);
		card.setType(CardType.Dream);
		card.setQuiet(l);
		card.setSMS(idMessage);
		card.setWhiteList(idWhiteList);
		card.save(db);
	}

	public void setOnOff(boolean b){
		isOn = b;
	}
	public boolean isOn(){return isOn;}
	public String timeToText(){
		String s1 = DateTimeOperator.dateToTimeString(beg);
		String s2 = DateTimeOperator.dateToTimeString(end);
		return s1+" - "+s2;
	}
	public String daysToText(){
		StringBuilder b = new StringBuilder();
		for(int i = 0; i<7;i++){
			if(this.days[i]) {
				b.append(DateTimeOperator.numberDayToNameDay(MainActivity.getMainActivity(), i));
				b.append(" ");
			}
		}
		return b.toString().trim();
	}
	public void setSMS(boolean b){isMessage = b;}
	public boolean isSMS(){return isMessage;}
	public boolean isWhiteList(SQLiteDatabase db){
		String[] cols = {"idWhiteList"};
		String[] args = {"1", String.valueOf(getID())};
		Cursor c = db.query("cards", cols,"type=? and idDream=?", args, null, null, null);
		if(!c.moveToFirst()) return false;
		int id = c.getInt(0);
		Toast.makeText(MainActivity.getMainActivity(),String.valueOf(id),Toast.LENGTH_LONG).show();
		return id>0;
	}
	public boolean isMessage(SQLiteDatabase db){
		String[] cols = {"idMessage"};
		String[] args = {"1", String.valueOf(getID())};
		Cursor c = db.query("cards", cols,"type=? and idDream=?", args, null, null, null);
		if(!c.moveToFirst()) return false;
		int id = c.getInt(0);
		Toast.makeText(MainActivity.getMainActivity(),String.valueOf(id),Toast.LENGTH_LONG).show();
		return id>0;
	}
	public static class CompareBegin implements Comparable<QueitCard>, Comparator<BaseObject> {

		private Date dateTime;

		public Date getBegin() {
			return getBegin();
		}

		public void setDateTime(Date datetime) {
			this.dateTime = datetime;
		}

		@Override
		public int compareTo(QueitCard o) {
			return getBegin().compareTo(o.getBegin());
		}

		@Override
		public int compare(BaseObject lhs, BaseObject rhs) {
			QueitCard q1 = (QueitCard)lhs;
			QueitCard q2 = (QueitCard)rhs;
			return (int)(q2.getBegin().getTime()-q1.getBegin().getTime());
		}
	}
}

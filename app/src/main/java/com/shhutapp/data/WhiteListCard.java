package com.shhutapp.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shhutapp.utils.Convertor;

public class WhiteListCard extends IntStringPair{
	private boolean unknown;
	private boolean org;
	private boolean urgent;
	private boolean OnOff = false;
	private BaseObjectList cont;
	private BaseObjectList apps;
	public WhiteListCard() {
		super(-1, "");
		this.unknown = false;
		this.org = false;
		this.urgent = false;
		cont = null;
		apps = null;
	}
	public WhiteListCard(int id) {
		super(id, "");
		this.unknown = false;
		this.org = false;
		this.urgent = false;
		cont = null;
		apps = null;
	}
	public WhiteListCard(int id, String name) {
		super(id, name);
		this.unknown = false;
		this.org = false;
		this.urgent = false;
		cont = null;
		apps = null;
	}
	public void save(SQLiteDatabase db){
		ContentValues row = new ContentValues();
		row.put("name", getName());
		row.put("unk",getUnknown()?1:0);
		row.put("org",getOrganizations()?1:0);
		row.put("urg",getUrgent()?1:0);
		if(getID() == -1) db.insert("white_list", null, row);
		else{
			String[] args = {String.valueOf(getID())};
			db.update("white_list", row,"id=?", args);
		}
	}
	public int newlist(SQLiteDatabase db){
		ContentValues row = new ContentValues();
		row.put("name", getName());
		row.put("unk",getUnknown()?1:0);
		row.put("org",getOrganizations()?1:0);
		row.put("urg",getUrgent()?1:0);
		Long l = db.insert("white_list", null, row);
		return l.intValue();
	}
	public void setUnknown(boolean unk){this.unknown = unk;}
	public boolean getUnknown(){return this.unknown;}
	public void setOrganizations(boolean org){ this.org = org;}
	public boolean getOrganizations(){return this.org;}
	public void setUrgent(boolean urg){this.urgent = urg;}
	public boolean getUrgent(){return this.urgent;}
	public void setState(boolean b){ this.OnOff = b;}
	public boolean getState(){return this.OnOff;}
	public void load(SQLiteDatabase db){
		String[] args1 = {String.valueOf(id), "0"};
		String[] args2 = {String.valueOf(id), "1"};
		String[] args3 = {String.valueOf(getID())};
		Cursor w = db.query("white_list",null,"id=?",args3,null,null,null);
		if(w.moveToFirst()){
			this.setName(w.getString(1));
			this.setUnknown(w.getInt(2) != 0 ? true : false);
			this.setOrganizations(w.getInt(3) != 0 ? true : false);
			this.setUrgent(w.getInt(4)!=0?true:false);
			int i = 0;
		}
		else{
			this.setName("");
			this.setUnknown(false);
			this.setOrganizations(false);
			this.setUrgent(false);
			int i = 0;
		}
		cont = new BaseObjectList();
		//Cursor c = db.query("white_list_contacts", null, "idlist=? and type=?", args1, null, null, null);
		Cursor c = db.query("white_list_contacts", null, null, null, null, null, null);
		if(c.moveToFirst()){
			do{
				ContactCard card = new ContactCard();
				Long l = c.getLong(0);
				card.setID(l.intValue());
				card.setPhone(c.getString(4));
				cont.add(card);
			}while(c.moveToNext());
		}
		apps = new BaseObjectList();
		Cursor a = db.query("white_list_contacts", null, "idlist=? and type=?", args2, null, null, null);
		if(a.moveToFirst()){
			do{
				ApplicationCard card = new ApplicationCard();
				Long l = c.getLong(0);
				card.setID(l.intValue());
				card.setPackage(c.getString(4));
				cont.add(card);
			}while(c.moveToNext());
		}
	}
	public boolean inList(String phNumber){
		boolean res = false;
		if(cont == null) return false;
		for(int i = 0; i<cont.size(); i++){
			ContactCard cc = (ContactCard) cont.get(i);
			if(cc.getPhone().equals(phNumber)||cc.getPhone().contains(phNumber)||phNumber.contains(cc.getPhone())){
				res = true;
				break;
			}
		}
		return res;
	}
	public long insert(SQLiteDatabase db, ContactCard card){
		ContentValues row = new ContentValues();
		row.put("idlist", getID());
		row.put("type", 0);
		row.put("name", card.getName());
		row.put("detail", card.getPhone());
		if(card.getAvatar() != null) row.put("avatar", Convertor.BitmapToBase64(card.getAvatar()));
		row.put("idcard", card.getID());
		return db.insert("white_list_contacts", null, row);
	}
}

package com.shhutapp.data;

import com.shhutapp.MainActivity;
import com.shhutapp.utils.Convertor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

public class ContactCard extends IntStringPair{
	private String phone;
	private Bitmap avatar;
	private boolean OnOff = false;
	private int section = 0;
	public ContactCard(){
		super(-1,"");
	}
	public ContactCard(int id, String name){
		super(id,name);
	}
	public ContactCard(int id, String name, String phone){
		super(id,name);
		this.phone = phone;
	}
	public ContactCard(int id, String name, String phone, Bitmap avatar){
		super(id,name);
		this.phone = phone;
		this.avatar = Bitmap.createBitmap(avatar);
	}
	
	public void setPhone(String phone){ this.phone = phone;}
	public String getPhone(){
		String s = this.phone.replaceAll(" ", "");
		return s;
	}
	public void setAvatar(Context context, Bitmap avatar){
		int f = (int) Convertor.convertDpToPixel(40, context);
		if(avatar != null) this.avatar = Bitmap.createScaledBitmap(avatar, f, f, false);
		else this.avatar = null;
	}
	public Bitmap getAvatar(){ return this.avatar;}
	public void setState(boolean b){ this.OnOff = b;}
	public boolean getState(){return this.OnOff;}
	public void saveToList(SQLiteDatabase db, int id){
		String[] args = {String.valueOf(id),String.valueOf(this.getID()), "0"};
		Cursor c = db.query("white_list_contacts", null, "idlist=? and id=? and type=?", args, null, null, null);
		if(!c.moveToFirst()) {
			ContentValues row = new ContentValues();
			row.put("idcard", this.getID());
			row.put("idlist", id);
			row.put("type", 0);
			row.put("name", this.getName());
			row.put("detail", this.getPhone());
			if (this.getAvatar() != null) {
				row.put("avatar", Convertor.BitmapToBase64(this.getAvatar()));
			}
			db.insert("white_list_contacts", null, row);
		}
	}
	public void deleteFromList(SQLiteDatabase db, int id){
		String[] args = {String.valueOf(id), String.valueOf(this.getID()), "0"};
		db.delete("white_list_contacts", "idlist=? and idcard=? and type=?", args);
	}
    public void setSection(int section){
        this.section = section;
    }
    public int getSection(){
        return this.section;
    }
    public ContactCard clone(){
        ContactCard c = new ContactCard(getID(),getName(),phone);
        c.avatar = getAvatar();
        c.OnOff = OnOff;
        c.section = section;
        return c;
    }
}

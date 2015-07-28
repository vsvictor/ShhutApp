package com.shhutapp.data;

import com.shhutapp.MainActivity;
import com.shhutapp.utils.Convertor;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

public class ApplicationCard extends IntStringPair{
	private boolean OnOff = false;
	private Bitmap icon;
	private String packageName;
	private int section = 0;
	public ApplicationCard(){
		super(-1,"");
	}
	public ApplicationCard(int id, String name){
		super(id,name);
	}
	public ApplicationCard(int id, String name, boolean state){
		super(id,name);
		this.OnOff = state;
	}

	public void setState(boolean b){ this.OnOff = b;}
	public boolean getState(){return this.OnOff;}
	public void setPackage(String p){this.packageName = p;}
	public String getPackage(){return this.packageName;}
	public void setIcon(Bitmap icon){this.icon = Bitmap.createBitmap(icon);}
	public Bitmap getIcon(){return this.icon;}
	public void saveToList(SQLiteDatabase db, int id){
		String[] args = {String.valueOf(id),String.valueOf(this.getID()), "1"};
		Cursor c = db.query("white_list_contacts", null, "idlist=? and id=? and type=?", args, null, null, null);
		if(!c.moveToFirst()) {
			ContentValues row = new ContentValues();
			row.put("idcard", this.getID());
			row.put("idlist", id);
			row.put("type", 1);
			row.put("name", this.getName());
			row.put("detail", this.getPackage());
			if (this.getIcon() != null) {
				row.put("avatar", Convertor.BitmapToBase64(this.getIcon()));
			}
			db.insert("white_list_contacts", null, row);
		}
	}
	public void deleteFromList(SQLiteDatabase db, int id){
		String[] args = {String.valueOf(id), String.valueOf(this.getID()), "1"};
		db.delete("white_list_contacts", "idlist=? and idcard=? and type=?", args);
	}
    public void setSection(int section){
        this.section = section;
    }
    public int getSection(){
        return this.section;
    }
}

package com.shhutapp.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.shhutapp.MainActivity;
import com.shhutapp.utils.Convertor;
import com.shhutapp.utils.DateTimeOperator;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{
	private MainActivity act;
    private static GeoCard deletedGeo = null;
    private static long deletedIdWhiteList;
    private static long deletedIdQueitCard;
    private static long deletedIdMessage;
	public DBHelper(MainActivity act) {
		super(act, "shutt", null, 1);
		this.act = act;
	}
	public DBHelper(Context context) {
		super(context, "shutt", null, 1);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		String command = "create table locations ("
				+ "id integer primary key autoincrement,"
				+ "name text,"
				+ "city text,"
				+ "street text,"
				+ "background text,"
				+ "lat real(3,16)," 
				+ "long real(3,16)," 
				+ "radius real" + ");";
		db.execSQL(command);
		command = "create table quieties ("
				+ "id integer primary key autoincrement," 
				+ "name text,"
				+ "begtime long,"
				+ "endtime long,"
				+ "is1 int default 0,"
				+ "is2 int default 0,"
				+ "is3 int default 0,"
				+ "is4 int default 0,"
				+ "is5 int default 0,"
				+ "is6 int default 0,"
				+ "is7 int default 0" + ");";
		db.execSQL(command);
		command = "create table sms ("
				+ "id integer primary key autoincrement," 
				+ "name text,"
				+ "sms text" + ");";
		db.execSQL(command);
		command = "create table white_list ("
				+ "id integer primary key autoincrement,"
				+ "name text, "
				+ "unk integer default 0, "
				+ "org integer default 0, "
				+ "urg integer default 0" + ");";
		db.execSQL(command);
		//type 0 - contacts, 1 - application
		command = "create table white_list_contacts ("
				+ "id integer primary key autoincrement,"
				+ "idlist integer, "
				+ "type integer default 0, "
                + "name text, "
				+ "detail text, "
                + "avatar text, "
				+ "idcard integer" + ");";
		db.execSQL(command);
		
		//type: 0-Не указано, 1-Пока вы тут, 2-Тихие часы, 3-Время,4-Время+Тихие часы
		command =  "create table activations ("
				+ "id integer primary key autoincrement," 
				+ "type integer,"
				+ "time long" + ");";
		db.execSQL(command);
		ContentValues values = new ContentValues();
		values.put("time", 0);values.put("type", 1);db.insert("activations", null, values);
		values.clear();values.put("time", 0);values.put("type", 2);db.insert("activations", null, values);
		values.clear();values.put("time", DateTimeOperator.toDateTime("01:30", "HH:mm").getTime());values.put("type", 3);db.insert("activations", null, values);
		values.clear();values.put("time", DateTimeOperator.toDateTime("01:30", "HH:mm").getTime());values.put("type", 4);db.insert("activations", null, values);

		//type: 0-geo,1-dream,2-white list,3-messages,4-error
		command =  "create table cards ("
				+ "id integer primary key autoincrement," 
				+ "type integer,"
				+ "name text,"
				+ "idActivate integer,"
				+ "idGeo integer,"
				+ "idDream integer,"
				+ "idWhiteList integer,"
				+ "idMessage integer,"
				+ "time integer,"
				+ "onoff integer"+");";
		db.execSQL(command);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	public BaseObjectList loadGeoCards(){
		BaseObjectList list = new BaseObjectList();
		Cursor cur = act.getDB().rawQuery("Select cards.id, locations.name , locations.street,  cards.idActivate,  activations.time, locations.background, locations.lat, locations.long, locations.radius, cards.time, cards.onoff From cards, locations, activations Where cards.idGeo=locations.id and cards.idActivate=activations.id", null);
		if(cur.moveToFirst()){
			do{
				GeoCard card = new GeoCard(this.act);
				card.setID(cur.getInt(0));
				card.setName(cur.getString(1));
				card.setAddress(cur.getString(2));
				card.setTypeActivation(cur.getInt(3));
				card.setTimeActivation(cur.getLong(4));
				card.setLantitude(cur.getDouble(6));
				card.setLongitude(cur.getDouble(7));
				card.setRadius(cur.getDouble(8));
				card.setMinutes(cur.getInt(9));
				String s = cur.getString(5);
				Bitmap b = Convertor.Base64ToBitmap(s);
				card.setBackground(b);
				card.setOnoff(cur.getInt(10)==1?true:false);
				list.add(card);
			}while(cur.moveToNext());
		}
		return list;
	}
	public BaseObjectList loadQueitCard(){
		BaseObjectList list = new BaseObjectList();
		Cursor cur = act.getDB().rawQuery("Select cards.id, quieties.begtime , quieties.endtime,  quieties.is1,  quieties.is2, quieties.is3, quieties.is4, quieties.is5, quieties.is6, quieties.is7, cards.idMessage From cards, quieties Where cards.idDream=quieties.id", null);
		if(cur.moveToFirst()){
			do{
				QueitCard card = new QueitCard(); 
				card.setID(cur.getInt(0));
				card.setBegin(new Date(cur.getLong(1)));
				card.setEnd(new Date(cur.getLong(2)));
				boolean[] days = new boolean[7];
				days[0] = (cur.getInt(3)==0?false:true);
				days[1] = (cur.getInt(4)==0?false:true);
				days[2] = (cur.getInt(5)==0?false:true);
				days[3] = (cur.getInt(6)==0?false:true);
				days[4] = (cur.getInt(7)==0?false:true);
				days[5] = (cur.getInt(8)==0?false:true);
				days[6] = (cur.getInt(9)==0?false:true);
				card.setDays(days);
				long idMessage = cur.getLong(10);
				if(idMessage >= 0) card.setSMS(true);
				else card.setSMS(false);
				list.add(card);
			}while(cur.moveToNext());
		}
		return list;
	}
	public BaseObjectList loadQueitCardsOrderByTime(){
		BaseObjectList list = new BaseObjectList();
		Cursor cur = act.getDB().rawQuery("Select cards.id, quieties.begtime , quieties.endtime,  quieties.is1,  quieties.is2, quieties.is3, quieties.is4, quieties.is5, quieties.is6, quieties.is7, cards.idMessage From cards, quieties Where cards.idDream=quieties.id ORDER by quieties.begtime", null);
		if(cur.moveToFirst()){
			do{
				QueitCard card = new QueitCard();
				card.setID(cur.getInt(0));
				card.setBegin(new Date(cur.getLong(1)));
				card.setEnd(new Date(cur.getLong(2)));
				boolean[] days = new boolean[7];
				days[0] = (cur.getInt(3)==0?false:true);
				days[1] = (cur.getInt(4)==0?false:true);
				days[2] = (cur.getInt(5)==0?false:true);
				days[3] = (cur.getInt(6)==0?false:true);
				days[4] = (cur.getInt(7)==0?false:true);
				days[5] = (cur.getInt(8)==0?false:true);
				days[6] = (cur.getInt(9)==0?false:true);
				card.setDays(days);
				long idMessage = cur.getLong(10);
				if(idMessage >= 0) card.setSMS(true);
				else card.setSMS(false);
				list.add(card);
			}while(cur.moveToNext());
		}
		return list;
	}

	public BaseObjectList loadMessages(){
		BaseObjectList list = new BaseObjectList();
		Cursor c = act.getDB().query("sms", null, null, null, null, null, null);
		if(c.moveToFirst()){
			do{
				int id = c.getInt(0);
				String name = c.getString(1);
				String text = c.getString(2);
				SMSCard card = new SMSCard(id, name, text);
				list.add(card);
			}while(c.moveToNext());
		}
		return list;
	}
	public void deleteGeoCard(long loc){
        long l;
        Cursor cur = act.getDB().rawQuery("Select cards.id, locations.name , locations.street,  cards.idActivate,  activations.time, locations.background, locations.lat, locations.long, locations.radius, cards.time, cards.idGeo, cards.idDream, cards.idWhiteList, cards.idMessage From cards, locations, activations Where cards.idGeo=locations.id and cards.idActivate=activations.id and cards.id=" + String.valueOf(loc), null);
        if(cur.moveToFirst()){
            deletedGeo = new GeoCard(this.act);
            deletedGeo.setID(cur.getInt(0));
            deletedGeo.setName(cur.getString(1));
            deletedGeo.setAddress(cur.getString(2));
            deletedGeo.setTypeActivation(cur.getInt(3));
            deletedGeo.setTimeActivation(cur.getLong(4));
            deletedGeo.setLantitude(cur.getDouble(6));
            deletedGeo.setLongitude(cur.getDouble(7));
            deletedGeo.setRadius(cur.getDouble(8));
            deletedGeo.setMinutes(cur.getInt(9));
            String s = cur.getString(5);
            Bitmap b = Convertor.Base64ToBitmap(s);
            deletedGeo.setBackground(b);
            l = cur.getLong(10);
            deletedIdQueitCard = cur.getLong(11);
            deletedIdWhiteList = cur.getLong(12);
            deletedIdMessage = cur.getLong(13);

            act.getDB().delete("cards","id=?", new String[]{String.valueOf(loc)});
            act.getDB().delete("locations","id=?", new String[]{String.valueOf(l)});
        }
	}
    public void unDelete(){
        ContentValues row = new ContentValues();
        row.put("name", deletedGeo.getName());
        row.put("city", "__");
        row.put("street", deletedGeo.getAddress());
        row.put("background", Convertor.BitmapToBase64(deletedGeo.getBackground()));
        row.put("lat", deletedGeo.getLantitude());
        row.put("long", deletedGeo.getLongitude());
        row.put("radius", deletedGeo.getRadius());
        long l = act.getDB().insert("locations",null, row);
        row.clear();
        row.put("type", 0);
        row.put("name", deletedGeo.getName());
        row.put("idActivate", deletedGeo.getTypeActivation());
        row.put("idGeo",l);
        row.put("idDream", deletedIdQueitCard);
        row.put("idWhiteList", deletedIdWhiteList);
        row.put("idMessage", deletedIdMessage);

        row.put("time",deletedGeo.getTimeActivation());
        act.getDB().insert("cards", null, row);
    }
	public void deleteMessage(long id){
		String[] args = {String.valueOf(id)};
		act.getDB().delete("sms", "id=?", args);
	}
	public void deleteWhiteList(long id){
		String[] args = {String.valueOf(id)};
		act.getDB().delete("white_list", "id=?", args);
		act.getDB().delete("white_list_contacts", "idlist=?", args);
	}
	public void deleteQueitCard(long id){
		String[] args = {String.valueOf(id)};
		Cursor c = act.getDB().query("cards", null, "id=?", new String[]{String.valueOf(id)},null, null, null);
		if(c.moveToFirst()) act.getDB().delete("quieties", "id=?", new String[]{String.valueOf(c.getInt(5))});
		act.getDB().delete("cards", "id=?", args);
		//act.getDB().delete("quieties", "id=?", args);
	}

	public BaseObjectList loadWhiteLists(){
		BaseObjectList res = new BaseObjectList();
		Cursor c  = act.getDB().query("white_list", null, null, null, null, null, null);
		if(c.moveToFirst()){
			do{
				int id = c.getInt(0);
				String name = c.getString(1);
				WhiteListCard card = new WhiteListCard(id, name);
				card.setUnknown(c.getInt(2)==1);
				card.setOrganizations(c.getInt(3)==1);
				card.setUrgent(c.getInt(4)==1);
				res.add(card);
			}while(c.moveToNext());
		}
		return res;
	}
	public BaseObjectList loadContacts(int listID){
		BaseObjectList res = new BaseObjectList();
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		String[] data = {ContactsContract.CommonDataKinds.Phone._ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.PHOTO_URI};
		Cursor cur = act.getContentResolver().query(uri, data, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
		if(cur.moveToFirst()){
			do{
				Bitmap b = null;
				ContactCard card = new ContactCard();
				card.setID(Integer.parseInt(cur.getString(0)));
				card.setName(cur.getString(1));
				card.setPhone(cur.getString(2));
				String ur = cur.getString(3);
				if(ur != null){
					Uri ph = Uri.parse(ur);
					try {
						b = MediaStore.Images.Media.getBitmap(act.getContentResolver(), ph);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				card.setAvatar(act, b);
				res.add(card);
			}while(cur.moveToNext());
		}
		String[] args = {String.valueOf(listID), "0"};
		Cursor c = act.getDB().query("white_list_contacts", null, "idlist=? and type=?", args, null, null, null);
		if(c.moveToFirst()){
			do{
				String s = c.getString(4);
				for(int i = 0; i<res.size();i++){
					ContactCard p = (ContactCard ) res.get(i);
					if(p.getPhone().equals(s)) {
						p.setState(true);
						break;
					}
				}
			}while(c.moveToNext());
		}
		return res;
	}
	public BaseObjectList loadContactsSelected(int listID){
		BaseObjectList res = new BaseObjectList();
		String[] args = {String.valueOf(listID), "0"};
		Cursor c = act.getDB().query("white_list_contacts",null,"idlist=? and type=?",args,null,null,null);
		if(c.moveToFirst()){
			do{
				ContactCard card = new ContactCard();
                card.setID(c.getInt(6));
                card.setName(c.getString(3));
                card.setPhone(c.getString(4));
                if(c.getString(5)!= null && !c.getString(5).isEmpty()) {
                    card.setAvatar(act, Convertor.Base64ToBitmap(c.getString(5)));
                }
                card.setState(true);
                res.add(card);
			}while (c.moveToNext());
		}
		return res;
	}
	public BaseObjectList loadApplications(int listID){
		BaseObjectList res = new BaseObjectList();
		final PackageManager pm = act.getPackageManager();
		List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
		for (ApplicationInfo packageInfo : packages) {
			ApplicationCard card = new ApplicationCard();
			int f = (int) Convertor.convertDpToPixel(32, act);
			Bitmap icon = Bitmap.createScaledBitmap(Convertor.drawableToBitmap(packageInfo.loadIcon(pm)), f, f, false);
			card.setName(packageInfo.loadLabel(pm) == null?packageInfo.packageName:packageInfo.loadLabel(pm));
			card.setIcon(icon);
			card.setPackage(packageInfo.packageName);
			res.add(card);
		}
		String[] args = {String.valueOf(listID), "1"};
		Cursor c = act.getDB().query("white_list_contacts", null, "idlist=? and type=?", args, null, null, null);
		if(c.moveToFirst()){
			do{
				String s = c.getString(3);
				for(int i = 0; i<res.size();i++){
					ApplicationCard p = (ApplicationCard) res.get(i); 
					if(p.getPackage().equals(s)) {
						p.setState(true);
						break;
					}
				}
			}while(c.moveToNext());
		}
		return res;
	}
	public BaseObjectList loadApplicationsSelected(int listID){
		BaseObjectList res = new BaseObjectList();
		String[] args = {String.valueOf(listID), "1"};
		Cursor c = act.getDB().query("white_list_contacts",null,"idlist=? and type=?",args,null,null,null);
		if(c.moveToFirst()){
			do{
				ApplicationCard card = new ApplicationCard();
				card.setID(c.getInt(6));
				card.setName(c.getString(3));
				card.setPackage(c.getString(4));
				if(c.getString(5)!= null && !c.getString(5).isEmpty()) {
					card.setIcon(Convertor.Base64ToBitmap(c.getString(5)));
				}
				card.setState(true);
				res.add(card);
			}while (c.moveToNext());
		}
		return res;
	}
	public QueitCard findNear(){
		QueitCard res = new QueitCard();
		QueitCard pr = null;
		int i = 0;
		BaseObjectList list = this.loadQueitCardsOrderByTime();
		for(BaseObject q : list){
			QueitCard card = (QueitCard) q;
			Date curr = Calendar.getInstance().getTime();
			String s = DateTimeOperator.dateToTimeString(curr);
			Date dd = DateTimeOperator.toDateTime(s,"HH:ss");
			Date dbeg = card.getBegin();
			Log.i("Begin",dbeg.toString());
			Log.i("Current",dd.toString());
			if(dbeg.after(dd)) return card;
			//else break;
		}
		return null;
	}
}

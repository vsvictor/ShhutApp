package com.shhutapp.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.provider.ContactsContract;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.shhutapp.data.BaseObjectList;
import com.shhutapp.data.Card;
import com.shhutapp.data.CardType;
import com.shhutapp.data.ContactCard;
import com.shhutapp.data.DBHelper;
import com.shhutapp.data.GeoCard;
import com.shhutapp.data.QueitCard;
import com.shhutapp.data.SMSCard;
import com.shhutapp.data.WhiteListCard;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import de.mindpipe.android.logging.log4j.LogConfigurator;

public class Carder extends Service{
	private DBHelper dbHelper;
	private static SQLiteDatabase db;
	private static ArrayList<Card> activeCards;
	private static boolean isStarted = false;
	private Logger logger;
	public static BaseObjectList cont;
	@Override
	public void onCreate(){
		super.onCreate();
		activeCards = new ArrayList<Card>();
		isStarted = true;
		dbHelper = new DBHelper(this);
		db = dbHelper.getReadableDatabase();
		//logger = createLogger();
		//logger.info("Carder created");
		cont = loadContacts();
	}
	@Override
	public int onStartCommand(final Intent intent, int flags, int startID) {
		final Thread th = new Thread(new Runnable(){
			@Override
			public void run() {
				//logger.info("Carder state:"+String.valueOf(isStarted));
				
				while(isStarted){
					activeCards.clear();
					String[] cols = {"id", "type", "name","idActivate","idGeo","idDream","idWhiteList","idMessage"};
					String[] args = {"1"};
					//Cursor c = db.query("cards", cols, "type=?", args, null, null, null);
					Cursor c = db.query("cards", cols, null, null, null, null, "type");
					if(c.moveToFirst()){
						do{
							Card card= new Card();
							long id = c.getLong(5);
							long lid = c.getLong(0);
							long wl = c.getLong(6);
							if(wl>0){
								card.wl = oneWhiteList(wl);
							}
							CardType type = Card.idToCardType(c.getInt(1));
							if(type == CardType.Geo){
								card.setType(CardType.Geo);
								card.geo = oneGeoCard(lid);
								if(checkCardGeo(card.geo)){
									if(c.getLong(7)>=0){
										card.sms = oneSMSCard(c.getLong(7));
									}
									activeCards.add(card);
								}
							}
							else if(type == CardType.Dream) {
								card.setType(CardType.Dream);
								card.dream = oneQueitCard(id);
								if(checkCardDream(card.dream)){
									if(c.getLong(7)>=0){
										card.sms = oneSMSCard(c.getLong(7));
									}
									activeCards.add(card);
								}
							}
						}while(c.moveToNext());
					}
					try {
						Thread.sleep(1000*20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}});
		th.start();
		return Service.START_REDELIVER_INTENT;
	}
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	public static boolean isStarted(){return isStarted;}
	public static void stop(){isStarted = false;}
	public static ArrayList<Card> getActiveCards(){return activeCards;}
	private boolean checkCardDream(QueitCard q){
		boolean res = false;
		if(q == null) return res;
		Date curr = Calendar.getInstance(TimeZone.getDefault()).getTime();
		int h = curr.getHours();
		int m = curr.getMinutes();
		int s = curr.getSeconds();
		int currsec = s+(m*60)+(h*60*60);
		int begsec = q.getBegin().getSeconds()+(q.getBegin().getMinutes()*60)+(q.getBegin().getHours()*60*60);
		int endsec = q.getEnd().getSeconds()+(q.getEnd().getMinutes()*60)+(q.getEnd().getHours()*60*60);
		if((currsec>begsec) && (currsec<endsec)){
			if(q.isAllDays()) {
				res = true;
			}
			else{
				int d = curr.getDay();
				res = q.isDay(d);
			}
		}
		return res;
	}

	private boolean checkCardGeo(GeoCard c){
		boolean res = false;
		if(c == null) return res;
		LatLng loc = getMyLocation();
		LatLng cen = new LatLng(c.getLantitude(), c.getLongitude());
		//double dist = Math.abs(MapAreaManager.distance(cen, loc));
		double dist = Math.abs(SphericalUtil.computeDistanceBetween(cen, loc));
		return c.getRadius()>=dist;
	}
	public Logger createLogger(){
        LogConfigurator logConfigurator = new LogConfigurator();
        logConfigurator.setFileName(Environment.getExternalStorageDirectory()
                        + File.separator + "Logs"
                        + File.separator + "ShhutApp.log");
        logConfigurator.setRootLevel(Level.DEBUG);
        logConfigurator.setLevel("org.apache", Level.ERROR);
        logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
        logConfigurator.setMaxFileSize(1024 * 1024 * 5);
        logConfigurator.setImmediateFlush(true);
        logConfigurator.configure();
        Logger logger = Logger.getLogger("Locator");
        return logger;
	}
	public static GeoCard oneGeoCard(long id){
		GeoCard card = new GeoCard();
		Cursor cur = db.rawQuery("Select cards.id,  locations.name, locations.lat, locations.long, locations.radius From cards, locations Where cards.id="+String.valueOf(id)+" and cards.idGeo=locations.id", null);
		if(cur.moveToFirst()){
			Long l = cur.getLong(0);
			card.setID(l.intValue());
			card.setName(cur.getString(1));
			card.setLantitude(cur.getDouble(2));
			card.setLongitude(cur.getDouble(3));
			card.setRadius(cur.getDouble(4));
		}
		else card = null;
		return card;
	}
	public static QueitCard oneQueitCard(long id){
		QueitCard card = new QueitCard();
		Cursor cur = db.rawQuery("Select cards.id, quieties.begtime , quieties.endtime,  quieties.is1,  quieties.is2, quieties.is3, quieties.is4, quieties.is5, quieties.is6, quieties.is7 From cards, quieties Where quieties.id="+String.valueOf(id)+" and cards.idDream=quieties.id", null);
		if(cur.moveToFirst()){
			Long l = cur.getLong(0);
			card.setID(l.intValue());
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
		}
		else card = null;
		return card;
	}
	public static SMSCard oneSMSCard(long id){
		SMSCard card = new SMSCard();
		Cursor cur = db.rawQuery("Select cards.id, sms.sms From cards, sms Where sms.id="+String.valueOf(id)+" and cards.idMessage=sms.id", null);
		if(cur.moveToFirst()){
			Long l = cur.getLong(0);
			card.setID(l.intValue());
			card.setName("");
			card.setText(cur.getString(1));
		}
		else card = null;
		return card;
	}
	public static WhiteListCard oneWhiteList(long id){
		WhiteListCard card = new WhiteListCard();
		Cursor cur = db.rawQuery("Select cards.id, white_list.unk, white_list.org, white_list.urg From cards, white_list Where white_list.id="+String.valueOf(id)+" and cards.idWhiteList=white_list.id", null);
		if(cur.moveToFirst()){
			Long l = cur.getLong(0);
			card.setID(l.intValue());
			card.setUnknown(cur.getInt(1)==1);
			card.setOrganizations(cur.getInt(2)==1);
			card.setUrgent(cur.getInt(3)==1);
			card.load(db);
		}
		else card = null;
		return card;
	}
	private LatLng getMyLocation(){
		LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = locationManager.getBestProvider(criteria, true);
		Location loc = locationManager.getLastKnownLocation(provider);
		return new LatLng(loc.getLatitude(), loc.getLongitude());
	}
	private BaseObjectList loadContacts(){
		BaseObjectList res = new BaseObjectList();
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		String[] data = {ContactsContract.CommonDataKinds.Phone._ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.PHOTO_URI};
		Cursor cur = getContentResolver().query(uri, data, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
		if(cur.moveToFirst()){
			do{
				Bitmap b = null;
				ContactCard card = new ContactCard();
				card.setID(Integer.parseInt(cur.getString(0)));
				card.setName(cur.getString(1));
				card.setPhone(cur.getString(2));
				res.add(card);
			}while(cur.moveToNext());
		}
		return res;
	}
	public static boolean inContacts(String number){
		boolean res = false;
		for(int i = 0; i<cont.size();i++){
			ContactCard c = (ContactCard) cont.get(i);
			if(c.getPhone().equals(number)||c.getPhone().contains(number)||number.contains(c.getPhone())){
				res = true;
				break;
			}
		}
		return res;
	}
}

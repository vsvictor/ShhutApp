package com.shhutapp.geo;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.data.BaseObject;
import com.shhutapp.data.BaseObjectList;
import com.shhutapp.data.DBHelper;
import com.shhutapp.data.GeoCard;

//import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by victor on 04.06.15.
 */
public class AreaManager {
    private ArrayList<Area> areas;
    //private LinkedBlockingDeque<Area> areas;
    private GoogleMap map;

    public AreaManager(){
        //this.areas = new LinkedBlockingDeque<Area>();
        this.areas = new ArrayList<Area>();
        this.map = null;
    }
    public AreaManager(GoogleMap map){
        //areas = new LinkedBlockingDeque<Area>();
        this.areas = new ArrayList<Area>();
        this.map = map;
    }
    public void setMap(GoogleMap map){
        this.map = map;
    }
    public GoogleMap getMap(){
        return this.map;
    }
    public void addArea(Area ar){
        areas.add(ar);
    }
    public void removeArea(Area ar){
        areas.remove(ar);
    }
    public Area find(Area ar){
        for(Area a: areas){
            if(ar.equals(a)) return a;
        }
        return null;
    }
    public Area find(LatLng l){
        for(Area a: areas){
            if(a.inArea(l)) return a;
        }
        return null;
    }

    //public LinkedBlockingDeque<Area> getAreas(){
    public ArrayList<Area> getAreas(){
        return areas;
    }
    public void draw(Area ar){
        ar.draw(map);
    }
    public void clear(Area ar){
        ar.clear();
    }
    public void drawAll(){
        for(Area ar: areas){
            Area a = ar;
            Log.i("AreaDraw", "Area: "+a.getName());
            a.draw(map);
        }
        //draw(areas.get(0));
        //Area ar = areas.get(1);
        //Log.i("AreaDraw", "Area: "+ar.getName());
        //draw(areas.get(1));

    }
    public void clearAll(){
        for(Area ar: areas){
            ar.clear();
        }
    }
    public Area getFirst(){
        if(areas.size() == 0) return null;
        else return areas.get(0);//areas.getFirst();
    }
    public Area getLast(){
        if(areas.size() ==0) return null;
        else return areas.get(areas.size()-1);//areas.getLast();
    }
    public void load(DBHelper helper){
        BaseObjectList list = helper.loadGeoCards();
        Drawable dr = MainActivity.getMainActivity().getResources().getDrawable(R.drawable.address_back);
        for(Object it : list){
            GeoCard card = (GeoCard)it;
            LatLng center = new LatLng(card.getLantitude(),card.getLongitude());
            double radius = card.getRadius();
            String name = card.getName();
            //Log.i("Area", "Area: " + center + " radius: " + radius + " name:" + name);
            Area ar = new Area(MainActivity.getMainActivity(), center,radius,name);
            ar.setStrokeWidth(2);
            ar.setStrokeColor(Color.argb(255, 197, 17, 98));
            ar.setFillColor(Color.argb(64, 197, 17, 98));
            ar.draw(getMap());
            ar.addName(getMap(),dr,Color.BLACK);
            //ar.reDraw(map);
            addArea(ar);
        }
    }
    public void reDrawAll(){
        for(Area ar : areas){
            ar.reDraw(getMap());
            ar.rebuildName(getMap());
        }
    }
}

package com.shhutapp.geo;

import com.google.android.gms.maps.GoogleMap;

//import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by victor on 04.06.15.
 */
public class AreaManager {
    //private ArrayList<Area> areas;
    private LinkedBlockingDeque<Area> areas;
    private GoogleMap map;

    public AreaManager(){
        this.areas = new LinkedBlockingDeque<Area>();
        this.map = null;
    }
    public AreaManager(GoogleMap map){
        areas = new LinkedBlockingDeque<Area>();
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
    public LinkedBlockingDeque<Area> getAreas(){
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
            ar.draw(map);
        }
    }
    public void clearAll(){
        for(Area ar: areas){
            ar.clear();
        }
    }
    public Area getFirst(){
        if(areas.size() == 0) return null;
        else return areas.getFirst();
    }
    public Area getLast(){
        if(areas.size() ==0) return null;
        else return areas.getLast();
    }
}

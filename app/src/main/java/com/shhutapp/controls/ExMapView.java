package com.shhutapp.controls;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.geo.Area;
import com.shhutapp.geo.AreaManager;
import com.shhutapp.services.Addressator;

import java.util.Iterator;
import java.util.logging.LogRecord;

/**
 * Created by victor on 04.06.15.
 */
public class ExMapView extends GesturesMapView implements OnMapReadyCallback{
    private Context context;
    private AreaManager manager;
    private boolean enableAdd;
    public ExMapView(Context context) {
        super(context);
        this.context = context;
        manager = new AreaManager(this.getMap());
        enableAdd = true;
        this.getMapAsync(this);
    }
    public ExMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        manager = new AreaManager(this.getMap());
        enableAdd = true;
        this.getMapAsync(this);
    }
    public ExMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        manager = new AreaManager(this.getMap());
        enableAdd = true;
        this.getMapAsync(this);
    }
    public ExMapView(Context context, GoogleMapOptions options) {
        super(context, options);
        this.context = context;
        manager = new AreaManager(this.getMap());
        enableAdd = true;
        this.getMapAsync(this);
    }
    public AreaManager getManager(){
        return manager;
    }
    public Point getPointPressed(){
        return getMap().getProjection().toScreenLocation(manager.getLast().getCenter());
    }
    @Override
    public void onPostScroll(LatLng source, LatLng target, LatLng begin, LatLng end) {
        double new_radius;
        if(manager.getLast()!= null && manager.getLast().inArea(begin)){
            double dist = (Math.abs(SphericalUtil.computeDistanceBetween(begin,end)))/10;
            double old = manager.getLast().getRadius();
            double dist1 = Math.abs(SphericalUtil.computeDistanceBetween(manager.getLast().getCenter(), begin));
            double dist2 = Math.abs(SphericalUtil.computeDistanceBetween(manager.getLast().getCenter(), end));
            if(dist1<dist2) new_radius = old + dist;
            else new_radius = old - dist;
            double recom = manager.checkRadius(manager.getLast(), new_radius);
            manager.getLast().setRadius(recom);
            manager.getLast().reDraw(getMap());
            if(manager.getLast().getRadius()<=5) {manager.clear(manager.getLast());manager.removeArea(manager.getLast(), MainActivity.getMainActivity().getDB());}
       }
        else{
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(target, getMap().getCameraPosition().zoom);
            tryUpdateCamera(update, 0);
        }
    }
    @Override
    public void onPostFling(LatLng source, LatLng target, int animateTime) {
        for(Area ar: manager.getAreas()){
            if(ar.inArea(source)){
                manager.clear(ar);
                manager.removeArea(ar, MainActivity.getMainActivity().getDB());
                enableAdd=true;
                context.sendBroadcast(new Intent("fling"));
                return;
            }
        }

        //CameraUpdate update = CameraUpdateFactory.newLatLngZoom(target,getMap().getCameraPosition().zoom);
        //tryUpdateCamera(update,animateTime);
    }
    @Override
    public void onPostScale(LatLng source, double sourceZoom, LatLng target, double targetZoom) {
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(target, (float) targetZoom);
        tryUpdateCamera(update, 0);
        getManager().reDrawAll();
    }
    @Override
    public void onPostLongPress(LatLng targer){
        if(!enableAdd) return;
        Log.i("Post", "onPostLongPress");
        for(Area ar : manager.getAreas()){
            if(ar.inArea(targer)) return;;
        }
        Area ar = new Area(context, targer);
        for(Area area : manager.getAreas()){
            double dist = Math.abs(SphericalUtil.computeDistanceBetween(ar.getCenter(),area.getCenter()));
            if(dist<ar.getRadius()+area.getRadius()){
                ar.setRadius(dist-area.getRadius());
            }
        }
        if(ar.getRadius()<=5) return;
        Drawable dr = context.getResources().getDrawable(R.drawable.address_back);
        manager.addArea(ar);
        ar.addCenter(getMap(), BitmapFactory.decodeResource(context.getResources(), R.drawable.logo), 1f / 6f);
        ar.addRadius(getMap(), BitmapFactory.decodeResource(context.getResources(), R.drawable.drag_circle));
        ar.addArrow(getMap());
        ar.addSize(getMap(), dr, Color.MAGENTA);
        ar.draw(getMap());
        ar.setName(String.valueOf(manager.getAreas().size()));
        enableAdd = false;
        Intent intent = new Intent(MainActivity.getMainActivity(), Addressator.class);
        intent.putExtra("name", ar.getName());
        intent.putExtra("lat", ar.getCenter().latitude);
        intent.putExtra("long", ar.getCenter().longitude);
        intent.putExtra("radius", ar.getRadius());
        MainActivity.getMainActivity().startService(intent);
    }
    @Override
    public void onPostDown(LatLng target) {
    }
    @Override
    public void onPostShowPress(LatLng target) {
    }
    @Override
    public void onPostSingleTapUp(LatLng target) {
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        getManager().setMap(googleMap);
        getManager().load(MainActivity.getMainActivity().getDBHelper());
    }
}

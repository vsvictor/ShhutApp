package com.shhutapp.controls;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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

/**
 * Created by victor on 04.06.15.
 */
public class ExMapView extends GesturesMapView {
    private Context context;
    private AreaManager manager;
    private boolean enableAdd;
    public ExMapView(Context context) {
        super(context);
        this.context = context;
        manager = new AreaManager(this.getMap());
        enableAdd = true;
    }
    public ExMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        manager = new AreaManager(this.getMap());
        enableAdd = true;
    }
    public ExMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        manager = new AreaManager(this.getMap());
        enableAdd = true;
    }
    public ExMapView(Context context, GoogleMapOptions options) {
        super(context, options);
        this.context = context;
        manager = new AreaManager(this.getMap());
        enableAdd = true;
    }
    public AreaManager getManager(){
        return manager;
    }
    public Point getPointPressed(){
        return getMap().getProjection().toScreenLocation(manager.getLast().getCenter());
    }
    @Override
    public void onPostScroll(LatLng source, LatLng target) {
        if(manager.getLast()!= null && manager.getLast().inArea(source)){

            double dist = Math.abs(SphericalUtil.computeDistanceBetween(source,target));
            double old = manager.getLast().getRadius();
            double dist1 = Math.abs(SphericalUtil.computeDistanceBetween(manager.getLast().getCenter(), source));
            double dist2 = Math.abs(SphericalUtil.computeDistanceBetween(manager.getLast().getCenter(), target));
            if(dist1<dist2) manager.getLast().setRadius(old + dist);
            else manager.getLast().setRadius(old - dist);
            if(manager.getLast().getRadius()<=5) {manager.clear(manager.getLast());manager.removeArea(manager.getLast());}
            else manager.getLast().reDraw(getMap());

/*
            Log.i("Radius", "Old radius:m " + manager.getLast().getRadius());
            double dist = Math.abs(SphericalUtil.computeDistanceBetween(manager.getLast().getCenter(), target));
            manager.getLast().setRadius(dist);
            Log.i("Radius", "New radius:m " + manager.getLast().getRadius());
            manager.getLast().reDraw(getMap());
*/

            //Log.i("Radius", "Old radius:m " + old + " New radius:m " + manager.getLast().getRadius()+"Distance:m "+dist);
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
                manager.removeArea(ar);
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
}

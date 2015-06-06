package com.shhutapp.geo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import com.nineoldandroids.animation.ObjectAnimator;
import com.shhutapp.controls.ExIconGenerator;
import com.shhutapp.utils.Convertor;

/**
 * Created by victor on 04.06.15.
 */
public class Area {

    private int DEFAULT_STROKE_COLOR = Color.argb(255,0,188,212);
    private float DEFAULT_STROKE_WIDTH = 1;
    private int DEFAULT_FILL_COLOR = Color.argb(64,0,188,212);
    private double DEFAULT_RADIUS = 100;

    private static int EARTH_RADIUS = 6371009;



    private Context context;
    private double radius; //in meters!!!!!
    private float strokeWidth;
    private int strokeColor;
    private int fillColor;
    private int sizeColor;
    private Drawable sizeBackground;
    private Bitmap centerBitmap;
    private Bitmap radiusBitmap;
    private int hCenter;
    private int wCenter;
    private float fCenter;

    private LatLng center;
    private Circle circle;
    private String name;
    private Marker centerMarker;
    private Marker radiusMarker;
    private Polyline arrowMarker;
    private Marker sizeMarker;
    private Marker addressMarker;

    public boolean isDrawed;



    public Area(Context context){
        this.context = context;
        this.center = new LatLng(0,0);
        this.radius = DEFAULT_RADIUS;
        this.strokeWidth = DEFAULT_STROKE_WIDTH;
        this.strokeColor = DEFAULT_STROKE_COLOR;
        this.fillColor = DEFAULT_FILL_COLOR;
        this.centerMarker = null;
        this.radiusMarker = null;
        this.arrowMarker = null;
        this.sizeMarker = null;
        this.addressMarker = null;
        this.isDrawed = false;
    }
    public Area(Context context, LatLng center){
        this(context);
        this.center = new LatLng(center.latitude,center.longitude);
    }
    public Area(Context context, LatLng center, double radius){ //radius in meters!!!!!;
        this(context, center);
        this.radius = radius;
    }
    public Area(Context context, LatLng center, double radius, String name){ //radius in meters!!!!!;
        this(context, center, radius);
        this.name = new String(name);
    }

    @Override
    public boolean equals(Object obj){
        Area ar = (Area) obj;
        return center.equals(ar.center) && this.radius == ar.radius;
    }
    public void draw(GoogleMap map){
        circle = map.addCircle(new CircleOptions()
                .center(center)
                .radius(radius)
                .strokeWidth(this.strokeWidth)
                .strokeColor(this.strokeColor)
                .fillColor(this.fillColor)
        );
        isDrawed = true;
    }
    public void reDraw(GoogleMap map){
        clear();
        draw(map);
        if(centerMarker != null){
            removeCenter();
            //Log.i("Values","Vals hCenter: "+hCenter+", wCenter: "+wCenter+" fCenter: "+fCenter);
            if(hCenter == -1 && wCenter == -1 && fCenter == -1) addCenter(map,centerBitmap);
            else if(hCenter != -1 && wCenter != -1 && fCenter == -1) addCenter(map, centerBitmap, wCenter, hCenter);
            else if(hCenter == -1 && wCenter == -1 && fCenter != -1) addCenter(map,centerBitmap,fCenter);
        }
        if(radiusMarker != null){
            removeRadius();
            addRadius(map,radiusBitmap);
        }
        if(arrowMarker != null){
            removeArrow();
            addArrow(map);
        }
        if(sizeMarker !=null){
            removeSize();
            addSize(map, sizeBackground, sizeColor);
        }
    }
    public void clear(){
        if(circle != null) circle.remove();
        if(centerMarker !=null) removeCenter();
        if(radiusMarker != null) removeRadius();
        if(arrowMarker != null) removeArrow();
        if(sizeMarker != null) removeSize();
        isDrawed = false;
    }
    public boolean isDrawed(){
        return isDrawed;
    }
    public LatLng getCenter(){
        return center;
    }
    public void setStrokeWidth(float width){
        this.strokeWidth = width;
    }
    public void setStrokeColor(int color){
        this.strokeColor = color;
    }
    public void setFillColor(int color){
        this.fillColor = color;
    }
    public void setRadius(double radius){
        this.radius = radius;
    }
    public double getRadius(){
        return this.radius;
    }
    public int getRadiusInPixel(GoogleMap map){
        Point p1 = map.getProjection().toScreenLocation(getCenter());
        Point p2 = map.getProjection().toScreenLocation(toRadiusLatLng());
        return (int)Math.abs(Convertor.distance(p1, p2));
    }
    public void setName(String name){
        this.name = new String(name);
    }
    public String getName(){
        return this.name;
    }
    public LatLng toRadiusLatLng() {
        double radiusAngle = Math.toDegrees(radius / EARTH_RADIUS) / Math.cos(Math.toRadians(center.latitude));
        return new LatLng(center.latitude, center.longitude + radiusAngle);
    }
    public boolean inArea(LatLng geo){
        /*Log.i("Distance","Distance: "+
                Math.abs(SphericalUtil.computeDistanceBetween(center, toRadiusLatLng())-
                        SphericalUtil.computeDistanceBetween(center,geo))+
        " Radius:"+getRadius()+" Center: ("+getCenter().latitude+":"+getCenter().longitude+")");*/
        double dist1 = Math.abs(SphericalUtil.computeDistanceBetween(center, toRadiusLatLng()));
        double dist2 = Math.abs(SphericalUtil.computeDistanceBetween(center,geo));
        //Log.i("Distance","Radius: "+dist1+" center-point: "+dist2);
        return (dist1>dist2);
    }
    public void addCenter(GoogleMap map, Bitmap bitmap){
        if(bitmap == null) return;
        hCenter = -1;
        wCenter = -1;
        fCenter = -1;
        centerBitmap = bitmap;
        centerMarker = map.addMarker(new MarkerOptions()
                .position(getCenter())
                .anchor(0.5f, 1f)
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                .draggable(false));
    }
    public void addCenter(GoogleMap map, Bitmap bitmap, int width, int height){
        fCenter = -1;
        if(bitmap == null || width<=0 || height <= 0) return;
        Bitmap bit = Bitmap.createScaledBitmap(bitmap,width,height,false);
        addCenter(map,bit);
        centerBitmap = bitmap;
        hCenter = height;
        wCenter = width;
    }
    public void addCenter(GoogleMap map, Bitmap bitmap, float unit){ //in radius units, example: 1/2, 1/3, 1/6...
        hCenter = -1;
        wCenter = -1;
        int r = getRadiusInPixel(map);
        if(bitmap == null || unit <= 0) return;
        int height = (int)(r*unit);
        int width = (int)(height*0.6785714f);
        if(height<=0 || width<=0 ) return;
        //Log.i("Bitmap:", "R=" + r + " H=" + height + " W=" + width);
        Bitmap bit = Bitmap.createScaledBitmap(bitmap,2*width,2*height,false);
        addCenter(map, bit);
        centerBitmap = bitmap;
        fCenter = unit;
    }
    public void removeCenter(){
        if(centerMarker != null) centerMarker.remove();
    }
    public void rebuildCenter(GoogleMap map){
        if(centerMarker != null){
            removeCenter();
            addCenter(map,centerBitmap);
        }
    }

    public void addArrow(GoogleMap map){
        arrowMarker = map.addPolyline(new PolylineOptions()
                .add(getCenter(),toRadiusLatLng())
                .width(strokeWidth)
                .color(strokeColor));
    }
    public void removeArrow(){
        if(arrowMarker != null) arrowMarker.remove();
    }
    public void rebuildArrow(GoogleMap map){
        if(arrowMarker != null) {
            removeArrow();
            addArrow(map);
        }
    }
    public void addRadius(GoogleMap map, Bitmap bitmap){
        radiusBitmap = bitmap;
        if(bitmap == null) return;
        int size = (int)(getRadiusInPixel(map)*0.05);
        if(size<1) size = 1;
        Bitmap bit = Bitmap.createScaledBitmap(bitmap,2*size,2*size,false);
        radiusMarker = map.addMarker(new MarkerOptions()
                .position(toRadiusLatLng())
                .icon(BitmapDescriptorFactory.fromBitmap(bit))
                .anchor(0.5f, 0.5f).flat(true)
                .draggable(false));
    }
    public void removeRadius(){
        if(radiusMarker != null) radiusMarker.remove();;
    }
    public void rebuildRadius(GoogleMap map){
        if(radiusMarker != null){
            removeRadius();
            addRadius(map, radiusBitmap);
        }
    }
    public void addSize(GoogleMap map, Drawable background, int color){
        sizeColor = color;
        sizeBackground = background;
        LatLng pos = SphericalUtil.computeOffset(getCenter(), getRadius()/2, 90);
        ExIconGenerator gen = new ExIconGenerator(context);
        gen.setBackground(background);
        String sValue = String.valueOf((int)getRadius());
        Bitmap bitmap = gen.makeIcon(sValue+" m", color, 16);

        if(bitmap.getWidth()<getRadiusInPixel(map)) {
            sizeMarker = map.addMarker(new MarkerOptions()
                    .position(pos)
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                    .draggable(false));
        }
    }
    public void removeSize(){
        if(sizeMarker != null) sizeMarker.remove();
    }
    public void rebuildSize(GoogleMap map){
        if(sizeMarker != null) {
            removeSize();
            addSize(map, sizeBackground, sizeColor);
        }
    }
}

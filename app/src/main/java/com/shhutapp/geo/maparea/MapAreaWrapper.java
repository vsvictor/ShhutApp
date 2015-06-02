package com.shhutapp.geo.maparea;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

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
import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.controls.ExIconGenerator;
import com.shhutapp.utils.Convertor;
import com.shhutapp.utils.Geo;

public class MapAreaWrapper {
    private double radiusInPixels;
    private float moveDrawableAnchorU;
    private float moveDrawableAnchorV;

    public static enum MarkerMoveResult {moved, radiusChange, minRadius, maxRadius, none};
    public static enum MarkerType {move, resize, none}
    private Context context;
    private String address;
	private Marker centerMarker;
    private Marker radiusMarker;
    private Marker sizeMarker;
    private Polyline arrow;
    private Circle circle;
    private double radiusMeters;
    private int minRadiusMeters = -1;
    private int maxRadiusMeters = -1;
    private GoogleMap map;
    private float resizeU;
    private float resizeV;
    private String name;
    public MapAreaWrapper(GoogleMap map, LatLng center, double radiusMeters, String name, float strokeWidth, int strokeColor, int fillColor, int minRadiusMeters, int maxRadiusMeters,
    		int centerDrawableId, int radiusDrawableId, float moveDrawableAnchorU, float moveDrawableAnchorV, float resizeDrawableAnchorU, float resizeDrawableAnchorV,
            String address, Context context) {
        this.context = context;
        this.address = address;
        this.radiusMeters = radiusMeters;
        this.minRadiusMeters = minRadiusMeters;
        this.maxRadiusMeters = maxRadiusMeters;
        this.map = map;
        this.resizeU = resizeDrawableAnchorU;
        this.resizeV = resizeDrawableAnchorV;
        this.moveDrawableAnchorU = moveDrawableAnchorU;
        this.moveDrawableAnchorV = moveDrawableAnchorV;
        this.name = name;
        centerMarker = map.addMarker(new MarkerOptions()
                .position(center)
                .anchor(moveDrawableAnchorU, moveDrawableAnchorV)
                .draggable(true));
        radiusMarker = map.addMarker(new MarkerOptions()
	        .position(MapAreasUtils.toRadiusLatLng(center, radiusMeters))
	        .anchor(resizeDrawableAnchorU, resizeDrawableAnchorV)
	        .draggable(true));

        Point pCenter = map.getProjection().toScreenLocation(center);
        Point pRadius = map.getProjection().toScreenLocation(MapAreasUtils.toRadiusLatLng(center, radiusMeters));

        radiusInPixels = Geo.distance(pCenter, pRadius);


        if (centerDrawableId != -1) {
            Bitmap bitmap = BitmapFactory.decodeResource(MainActivity.getMainActivity().getResources(),R.drawable.logo);
            int pHeight = (int)Convertor.convertDpToPixel((int)(radiusInPixels/6), MainActivity.getMainActivity());
            int pWidth  = (int)Convertor.convertDpToPixel((int)((38*radiusInPixels/6)/56), MainActivity.getMainActivity());
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, pWidth, pHeight, false);
            centerMarker.setIcon(BitmapDescriptorFactory.fromBitmap(scaled));
        }
        if (radiusDrawableId != -1) {
            Bitmap bitmap = BitmapFactory.decodeResource(MainActivity.getMainActivity().getResources(),R.drawable.radius);
            int pHeight = (int)Convertor.convertDpToPixel((int)(18*(radiusInPixels/6)/56), MainActivity.getMainActivity());
            int pWidth  = (int)Convertor.convertDpToPixel((int)(18*(radiusInPixels/6)/56), MainActivity.getMainActivity());
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, pWidth, pHeight, false);
        	radiusMarker.setIcon(BitmapDescriptorFactory.fromBitmap(scaled));
        }

        LatLng c = SphericalUtil.computeOffset(getCenter(), getRadius()/2, 90);
        Point p = map.getProjection().toScreenLocation(c);
        Point p1 = map.getProjection().toScreenLocation(getCenter());
        Point p2 = map.getProjection().toScreenLocation(radiusMarker.getPosition());

        ExIconGenerator gen = new ExIconGenerator(context);
        Drawable shape = context.getResources().getDrawable(R.drawable.address_back);
        gen.setBackground(shape);
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
        Bitmap b = gen.makeIcon(String.valueOf(Math.round(getRadius()))+" m", Color.MAGENTA, 16, tf);
        sizeMarker = map.addMarker(new MarkerOptions()
                .position(c)
                .icon(BitmapDescriptorFactory.fromBitmap(b))
                .draggable(false));

        arrow = map.addPolyline(new PolylineOptions()
                .add(getCenter(), radiusMarker.getPosition())
                .width(2)
                .color(0xFF00ACC1));

        circle = map.addCircle(new CircleOptions()
                .center(center)
                .radius(radiusMeters)
                .strokeWidth(strokeWidth)
                .strokeColor(strokeColor)
                .fillColor(fillColor));
    }
    public MapAreaWrapper(GoogleMap map, LatLng center, double radiusMeters, String name, float strokeWidth, int strokeColor, int fillColor, int minRadius, int maxRadius, String address, Context context) {
    	this(map, center, radiusMeters, name, strokeWidth, strokeColor, fillColor, minRadius, maxRadius, -1, -1, address, context);
    }
    public MapAreaWrapper(GoogleMap map, LatLng center, double radiusMeters, String name, float strokeWidth, int strokeColor, int fillColor, int minRadius, int maxRadius,
    		int centerDrawableId, int radiusDrawableId, String address, Context context) {
    	this(map, center, radiusMeters, name, strokeWidth, strokeColor, fillColor, minRadius, maxRadius, centerDrawableId, radiusDrawableId, 0.5f, 1f, 0.5f, 1f, address, context);
    }
    public LatLng getCenter() {
    	return centerMarker.getPosition();
    }
    public double getRadius() {
    	return radiusMeters;
    }
    public void setStokeWidth(float strokeWidth) {
    	circle.setStrokeWidth(strokeWidth);
    }
    public void setStokeColor(int strokeColor) {
    	circle.setStrokeColor(strokeColor);
    }
    public void setFillColor(int fillColor) {
    	circle.setFillColor(fillColor);
    }
    public void setCenter(LatLng center) {
    	centerMarker.setPosition(center);
        onCenterUpdated(center);
    }
    public MarkerMoveResult onMarkerMoved(Marker marker) {
        if (marker.equals(centerMarker)) {
        	onCenterUpdated(marker.getPosition());
            return MarkerMoveResult.moved;
        }
        if (marker.equals(radiusMarker)) {
        	 double newRadius = MapAreasUtils.toRadiusMeters(centerMarker.getPosition(), marker.getPosition());
        	 if (minRadiusMeters != -1 && newRadius < minRadiusMeters) {
        		 return MarkerMoveResult.minRadius;
        	 } else if (maxRadiusMeters != -1 && newRadius > maxRadiusMeters) {
        		 return MarkerMoveResult.maxRadius;
        	 } else {
        		 setRadius(newRadius);
        		 return MarkerMoveResult.radiusChange;
        	 }
        }
        return MarkerMoveResult.none;
    }
    public void onCenterUpdated(LatLng center) {
    	circle.setCenter(center);
        radiusMarker.setPosition(MapAreasUtils.toRadiusLatLng(center, radiusMeters));
    }
    public void setRadius(double radiusMeters) {
    	this.radiusMeters = radiusMeters;
    	circle.setRadius(radiusMeters);
    }
    @Override
    public String toString() {
    	return "center: " + getCenter() + " radius: " + getRadius();
    }
    public void remove(){
        circle.remove();
        centerMarker.remove();
        radiusMarker.remove();
        sizeMarker.remove();
        arrow.remove();
    }
    public void rebuildCenterMarker(){
        Point pCenter = map.getProjection().toScreenLocation(getCenter());
        Point pRadius = map.getProjection().toScreenLocation(MapAreasUtils.toRadiusLatLng(getCenter(), getRadius()));
        radiusInPixels = Geo.distance(pCenter, pRadius);
        Bitmap bitmap = BitmapFactory.decodeResource(MainActivity.getMainActivity().getResources(),R.drawable.logo);
        int pHeight = (int)Convertor.convertDpToPixel((int)(radiusInPixels/6), MainActivity.getMainActivity());
        int pWidth  = (int)Convertor.convertDpToPixel((int)((38*radiusInPixels/6)/56), MainActivity.getMainActivity());
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, pWidth, pHeight, false);

        if(centerMarker != null) centerMarker.remove();
        centerMarker = map.addMarker(new MarkerOptions()
                .position(getCenter())
                .anchor(moveDrawableAnchorU, moveDrawableAnchorV)
                .flat(true)
                .icon(BitmapDescriptorFactory.fromBitmap(scaled))
                .draggable(false));

        //centerMarker.setIcon(BitmapDescriptorFactory.fromBitmap(scaled));
    }

    public void rebuildRadiusMarker(){
        Point pCenter = map.getProjection().toScreenLocation(getCenter());
        Point pRadius = map.getProjection().toScreenLocation(MapAreasUtils.toRadiusLatLng(getCenter(), getRadius()));
        radiusInPixels = Geo.distance(pCenter, pRadius);
        Bitmap bitmap = BitmapFactory.decodeResource(MainActivity.getMainActivity().getResources(),R.drawable.radius);
        int pHeight = (int)Convertor.convertDpToPixel((int)(18*(radiusInPixels/6)/56), MainActivity.getMainActivity());
        int pWidth  = (int)Convertor.convertDpToPixel((int)(18*(radiusInPixels/6)/56), MainActivity.getMainActivity());
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, pWidth, pHeight, false);

        if(radiusMarker != null) radiusMarker.remove();
        radiusMarker = map.addMarker(new MarkerOptions()
                .position(MapAreasUtils.toRadiusLatLng(getCenter(), radiusMeters))
                .anchor(this.resizeU, this.resizeV)
                .flat(true)
                .icon(BitmapDescriptorFactory.fromBitmap(scaled))
                .draggable(false));
        //radiusMarker.setIcon(BitmapDescriptorFactory.fromBitmap(scaled));
    }
    public void rebuildSizeMarker(){
        if(sizeMarker != null) sizeMarker.remove();
        LatLng c = SphericalUtil.computeOffset(getCenter(), getRadius() / 2, 90);
        ExIconGenerator gen = new ExIconGenerator(context);
        Drawable shape = context.getResources().getDrawable(R.drawable.address_back);
        gen.setBackground(shape);
        //gen.setColor(Color.WHITE);
        Bitmap b = gen.makeIcon(String.valueOf(Math.round(getRadius()))+" m", Color.MAGENTA);
        sizeMarker = map.addMarker(new MarkerOptions()
                .position(c)
                .icon(BitmapDescriptorFactory.fromBitmap(b))
                .draggable(false));

        if(arrow != null) arrow.remove();
        arrow = map.addPolyline(new PolylineOptions()
                .add(getCenter(), radiusMarker.getPosition())
                .width(2)
                .color(0xFF00ACC1));
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){return name;}
}
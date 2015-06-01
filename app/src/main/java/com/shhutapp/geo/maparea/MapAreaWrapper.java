package com.shhutapp.geo.maparea;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.utils.Convertor;
import com.shhutapp.utils.Geo;

public class MapAreaWrapper {
	public static enum MarkerMoveResult {moved, radiusChange, minRadius, maxRadius, none};
    public static enum MarkerType {move, resize, none}
	private Marker centerMarker;
    private Marker radiusMarker;
    private Circle circle;
    private double radiusMeters;
    private int minRadiusMeters = -1;
    private int maxRadiusMeters = -1;
    public MapAreaWrapper(GoogleMap map, LatLng center, double radiusMeters, float strokeWidth, int strokeColor, int fillColor, int minRadiusMeters, int maxRadiusMeters,
    		int centerDrawableId, int radiusDrawableId, float moveDrawableAnchorU, float moveDrawableAnchorV, float resizeDrawableAnchorU, float resizeDrawableAnchorV) {
        this.radiusMeters = radiusMeters;
        this.minRadiusMeters = minRadiusMeters;
        this.maxRadiusMeters = maxRadiusMeters;
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
        double pixels = Geo.distance(pCenter, pRadius);
        if (centerDrawableId != -1) {
            Bitmap bitmap = BitmapFactory.decodeResource(MainActivity.getMainActivity().getResources(),R.drawable.logo);
            int pHeight = (int)Convertor.convertDpToPixel(50, MainActivity.getMainActivity());
            int pWidth  = (int)Convertor.convertDpToPixel(38, MainActivity.getMainActivity());
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, pWidth, pHeight, false);
            centerMarker.setIcon(BitmapDescriptorFactory.fromBitmap(scaled));
        }
        if (radiusDrawableId != -1) {
            Bitmap bitmap = BitmapFactory.decodeResource(MainActivity.getMainActivity().getResources(),R.drawable.radius);
            int pHeight = (int)Convertor.convertDpToPixel(18, MainActivity.getMainActivity());
            int pWidth  = (int)Convertor.convertDpToPixel(18, MainActivity.getMainActivity());
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, pWidth, pHeight, false);
        	radiusMarker.setIcon(BitmapDescriptorFactory.fromResource(radiusDrawableId));
        }
        circle = map.addCircle(new CircleOptions()
                .center(center)
                .radius(radiusMeters)
                .strokeWidth(strokeWidth)
                .strokeColor(strokeColor)
                .fillColor(fillColor));
    }
    public MapAreaWrapper(GoogleMap map, LatLng center, double radiusMeters, float strokeWidth, int strokeColor, int fillColor, int minRadius, int maxRadius) {
    	this(map, center, radiusMeters, strokeWidth, strokeColor, fillColor, minRadius, maxRadius, -1, -1);
    }
    public MapAreaWrapper(GoogleMap map, LatLng center, double radiusMeters, float strokeWidth, int strokeColor, int fillColor, int minRadius, int maxRadius,
    		int centerDrawableId, int radiusDrawableId) {
    	this(map, center, radiusMeters, strokeWidth, strokeColor, fillColor, minRadius, maxRadius, centerDrawableId, radiusDrawableId, 0.5f, 1f, 0.5f, 1f);
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
    }
}
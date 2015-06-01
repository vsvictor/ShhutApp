package com.shhutapp.controls;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.shhutapp.R;

public class ZoneView extends MapView{
	private ArrayList<Zone> zones;
	private Context context;
	private Zone z;
	public ZoneView(Context context) {
		super(context);
		this.context = context;
		zones = new ArrayList<Zone>();
	}
	public ZoneView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;		
		zones = new ArrayList<Zone>();
	}
	public ZoneView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;		
		zones = new ArrayList<Zone>();
	}
	public ZoneView(Context context, GoogleMapOptions options) {
		super(context, options);
		this.context = context;		
		zones = new ArrayList<Zone>();
	}
	public void addZone(LatLng center, int radius){
		GoogleMap map = this.getMap();
        Bitmap mBitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mBitmap);
        Drawable shape = getResources().getDrawable(R.drawable.circle);
        shape.setBounds(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        shape.draw(canvas);
		GroundOverlayOptions grOpt =  new GroundOverlayOptions();
		grOpt.image(BitmapDescriptorFactory.fromBitmap(mBitmap));
		grOpt.position(center, 50,50);
		GroundOverlay ov = map.addGroundOverlay(grOpt);
		Zone newZone = new Zone(center, radius);
		newZone.setOverlay(ov);
		zones.add(newZone);
	}
	private Zone findZone(Point p){
		Zone result = null;
		GoogleMap map = this.getMap();
		Projection proj = map.getProjection();
		for(Zone z : zones){
			Point c = proj.toScreenLocation(z.getCenter());
			int radius = z.getRadius();
			double d = (c.x-p.x)*(c.x-p.x)+(c.y-p.y)*(c.y-p.y);
			double dist = Math.sqrt(Math.abs(d));
			if(dist<radius) {result = new Zone(z.getCenter(), z.getRadius());break;}
		}
		return result;
	}
	public class Zone{
		private LatLng center;
		private int radius;
		private GroundOverlay ov;
		public Zone(){
		}
		public Zone(LatLng center){
			this.center = center;
		}
		public Zone(LatLng center, int radius){
			this.center = center;
			this.radius = radius;
		}
		public LatLng getCenter(){return center;}
		public void setRadius(int radius){this.radius = radius;}
		public int getRadius(){return radius;}
		public void setOverlay(GroundOverlay gr){ov = gr;}
		public GroundOverlay getOverlay(){return ov;}
	}
}

package com.shhutapp.controls;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.shhutapp.MainActivity;
import com.shhutapp.geo.maparea.MapAreaManager;
import com.shhutapp.geo.maparea.MapAreaWrapper;
import com.shhutapp.utils.Geo;

/**
 * Created by victor on 01.06.15.
 */
public class Clickator extends View {
    private boolean downed;
    private Point lastPoint;
    private GoogleMap map;
    private MapAreaManager manager;

    public Clickator(Context context) {
        super(context);
    }

    public Clickator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Clickator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Clickator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public void setMap(GoogleMap map){
        this.map = map;
    }
    public void setAreaManager(MapAreaManager manager){
        this.manager = manager;
    }
    private GestureDetector dt = new GestureDetector(new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }
        @Override
        public void onShowPress(MotionEvent e) {
        }
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            LatLng llast = map.getProjection().fromScreenLocation(lastPoint);
            MapAreaWrapper wr;
            float x1 = e1.getX();
            float y1 = e1.getY();
            float x2 = e2.getX();
            float y2 = e2.getY();
            if(downed && (wr=manager.inArea(llast))!=null){
                Point pEnd = new Point((int)x2, (int)y2);
                Point pCenter = map.getProjection().toScreenLocation(wr.getCenter());
                wr.setRadius(Geo.distance(pCenter, pEnd));
            }
            else {
                CameraPosition oldpos = map.getCameraPosition();
                Point p = map.getProjection().toScreenLocation(oldpos.target);
                p.x += -((x2 - x1) / 20);
                p.y += -((y2 - y1) / 20);
                LatLng l = map.getProjection().fromScreenLocation(p);
                CameraPosition newpos = new CameraPosition.Builder().target(l).zoom(oldpos.zoom).build();
                map.moveCamera(CameraUpdateFactory.newCameraPosition(newpos));
            }
            return true;
        }
        @Override
        public void onLongPress(MotionEvent e) {
            if(!manager.isFound()) {
                Point p = new Point((int) e.getX(), (int) e.getY());
                LatLng l = map.getProjection().fromScreenLocation(p);
                manager.onMapLongClick(l);
                manager.setFound(true);
            }
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    });
    private ScaleGestureDetector sg = new ScaleGestureDetector(MainActivity.getMainActivity(), new ScaleGestureDetector.OnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scale = detector.getScaleFactor();
            CameraPosition oldpos = map.getCameraPosition();
            CameraPosition newpos = new CameraPosition.Builder().target(oldpos.target).zoom(scale*oldpos.zoom).build();
            map.moveCamera(CameraUpdateFactory.newCameraPosition(newpos));
            return true;
        }
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }
        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
        }
    });
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_UP){
            downed = false;
        }
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            lastPoint = new Point((int)event.getX(),(int)event.getY());
            downed = true;
        }
        sg.onTouchEvent(event);
        dt.onTouchEvent(event);
        return true;
    }
}

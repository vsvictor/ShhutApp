package com.shhutapp.fragments.area;

import android.content.Context;
import android.graphics.Point;
import android.os.SystemClock;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.shhutapp.MainActivity;
import com.shhutapp.geo.maparea.MapAreaManager;
import com.shhutapp.geo.maparea.MapAreaWrapper;
import com.shhutapp.utils.Geo;

/**
 * Created by victor on 31.05.15.
 */
public  class TouchableWrapper extends FrameLayout {

    private long lastTouched = 0;
    private static final long SCROLL_TIME = 200L; // 200 Milliseconds, but you can adjust that to your liking
    private static TouchableWrapper inst;
    private MotionEvent event;
    private GoogleMap map;
    private Context context;
    private MapAreaManager manager;
    private Point lastPoint;
    private boolean downed;
    public TouchableWrapper(Context context) {
        super(context);
        this.context = context;
        inst = this;
    }
    public void setMap(GoogleMap map){
        this.map = map;
    }
    public void setAreaManager(MapAreaManager man){
        this.manager = man;
    }
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
                wr.setRadius(Geo.distance(pCenter,pEnd));
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
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_UP){
            downed = false;
        }
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            lastPoint = new Point((int)ev.getX(),(int)ev.getY());
            downed = true;
        }
        sg.onTouchEvent(ev);
        dt.onTouchEvent(ev);
        //return super.onTouchEvent(ev);
        return true;
    }
}

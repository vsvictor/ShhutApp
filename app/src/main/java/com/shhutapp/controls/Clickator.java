package com.shhutapp.controls;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.geo.maparea.MapAreaManager;
import com.shhutapp.geo.maparea.MapAreaWrapper;
import com.shhutapp.services.Addressator;
import com.shhutapp.utils.Geo;

/**
 * Created by victor on 01.06.15.
 */
public class Clickator extends View {
    private boolean downed;
    private long downed_time = 0;
    private long down_beg = 0;
    private long down_end = 0;
    private boolean partDelete = false;
    private Point lastPoint;
    private GoogleMap map;
    private MapAreaManager manager;
    private MotionEvent currevent;
    private boolean isMenu;

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
    public void setMenuVisible(boolean isMenu){
        this.isMenu = isMenu;
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
                wr.rebuildCenterMarker();
                wr.rebuildRadiusMarker();
                wr.rebuildSizeMarker();
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
                MapAreaWrapper wr = manager.findByName(manager.getCurrentName());
                if(wr != null) {
                    Intent intent = new Intent(MainActivity.getMainActivity(), Addressator.class);
                    intent.putExtra("name", wr.getName());
                    intent.putExtra("lat", wr.getCenter().latitude);
                    intent.putExtra("long", wr.getCenter().longitude);
                    intent.putExtra("radius", Math.abs(wr.getRadius()));
                    MainActivity.getMainActivity().startService(intent);
                }
            }
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.i("Gesture", "onFling");
            MapAreaWrapper wr;
            Point p = new Point((int)e1.getX(), (int)e1.getY());
            Log.i("Gesture", "Coords" + p.x + ":" + p.y);
            LatLng llast = map.getProjection().fromScreenLocation(p);
            if(((wr=manager.inArea(llast))!=null)&& partDelete){
                Log.i("Gesture", "Deleted");
                manager.delete(wr);
            }
            return true;
        }
    });
    private ScaleGestureDetector sg = new ScaleGestureDetector(MainActivity.getMainActivity(), new ScaleGestureDetector.OnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scale = detector.getScaleFactor();
            //Log.i("Scale", String.valueOf(scale));
            CameraPosition oldpos = map.getCameraPosition();
            float zoom = scale*oldpos.zoom;
            if(zoom > 18) zoom = 18;
            int width = Clickator.this.getWidth();
            int height = Clickator.this.getHeight();
            Point p = new Point(width/2, height/2);
            LatLng l = map.getProjection().fromScreenLocation(p);
            CameraPosition newpos = new CameraPosition.Builder().target(oldpos.target).zoom(zoom).build();
            map.moveCamera(CameraUpdateFactory.newCameraPosition(newpos));
            for(MapAreaWrapper wr:manager.getCircles()){
                wr.rebuildCenterMarker();
                wr.rebuildRadiusMarker();
            }
            return true;
        }
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            float scale = detector.getScaleFactor();
            Log.i("Scale", "Begin:"+String.valueOf(scale));
            CameraPosition oldpos = map.getCameraPosition();
            Point p = map.getProjection().toScreenLocation(oldpos.target);
            Log.i("Scale", "Posinion:" + p.x + ":" + p.y);
            int i = (int) MainActivity.getMainActivity().getResources().getDimension(R.dimen.abc_action_bar_default_height_material);
            Log.i("Scale", "Action bar:" + i);
            return true;
        }
        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            float scale = detector.getScaleFactor();
            Log.i("Scale", "End"+String.valueOf(scale));
        }
    });
    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.currevent = event;
        if(event.getAction() == MotionEvent.ACTION_UP){
            downed = false;
            down_end = event.getEventTime();
            partDelete = ((down_end-down_beg)<500);
        }
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            lastPoint = new Point((int)event.getX(),(int)event.getY());
            downed = true;
            partDelete = false;
            down_beg = event.getDownTime();
        }
        if(event.getPointerCount()>1)sg.onTouchEvent(event);
        else dt.onTouchEvent(event);
        return true;
    }
}

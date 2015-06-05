package com.shhutapp.controls;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;

/**
 * Created by victor on 04.06.15.
 */
public abstract class GesturesMapView extends MapView {

    public static float MAX_ZOOM = 20;
    public static float MIN_ZOOM = 1;
    public static float MIN_ZOOM_FOR_FLING = 7;
    private boolean mIsInAnimation = false;
    public GesturesMapView(Context context) {
        super(context);
        mGestureDetector = new GestureDetector(context, mGestudeListener);
        mScaleGestureDetector = new ScaleGestureDetector(context, mScaleGestudeListener);
    }
    public GesturesMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, mGestudeListener);
        mScaleGestureDetector = new ScaleGestureDetector(context, mScaleGestudeListener);
    }
    public GesturesMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mGestureDetector = new GestureDetector(context, mGestudeListener);
        mScaleGestureDetector = new ScaleGestureDetector(context, mScaleGestudeListener);
    }
    public GesturesMapView(Context context, GoogleMapOptions options) {
        super(context, options);
        mGestureDetector = new GestureDetector(context, mGestudeListener);
        mScaleGestureDetector = new ScaleGestureDetector(context, mScaleGestudeListener);
    }
    private GestureDetector mGestureDetector = null;
    private GestureDetector.SimpleOnGestureListener mGestudeListener =
            new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    if (mIsInAnimation) return false;
                    GoogleMap map = getMap();
                    LatLng target = map.getCameraPosition().target;
                    Point screenPoint = map.getProjection().toScreenLocation(target);
                    Point newPoint = new Point(screenPoint.x + (int)distanceX, screenPoint.y + (int)distanceY);
                    LatLng mapNewTarget = map.getProjection().fromScreenLocation(newPoint);

                    //CameraUpdate update = CameraUpdateFactory.newLatLngZoom(mapNewTarget, map.getCameraPosition().zoom);
                    //tryUpdateCamera(update, 0);

                    onPostScroll(target, mapNewTarget);
                    return true;
                }

                @Override
                public boolean onFling (MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    if(e1.getPointerCount()>1 || e2.getPointerCount()>1) return false;
                    if (mIsInAnimation) return false;
                    GoogleMap map = getMap();
                    double zoom = map.getCameraPosition().zoom;
                    if (zoom < MIN_ZOOM_FOR_FLING)
                        return false;
                    int velocity = (int) Math.sqrt(velocityX * velocityX + velocityY * velocityY);
                    if (velocity < 500) return false;
                    double k1 = 0.002d; /*exipemental*/
                    double k2 = 0.002d;/*exipemental*/

                    LatLng target = map.getCameraPosition().target;
                    Point screenPoint = map.getProjection().toScreenLocation(target);
                    Point newPoint = new Point(screenPoint.x - (int)(velocityX * k1 * zoom * zoom/*exipemental*/),
                            screenPoint.y - (int)(velocityY * k1 * zoom * zoom/*exipemental*/));
                    LatLng mapNewTarget = map.getProjection().fromScreenLocation(newPoint);
                    //CameraUpdate update = CameraUpdateFactory.newLatLngZoom(
                    //        mapNewTarget,map.getCameraPosition().zoom);
                    //tryUpdateCamera(update, (int) (velocity * k2 * zoom * zoom) /*exipemental*/);
                    onPostFling(target,mapNewTarget, (int) (velocity * k2 * zoom * zoom));
                    return true;
                }
                @Override
                public void onLongPress(MotionEvent e) {
                    if(e.getPointerCount()>1) return;
                    GoogleMap map = getMap();
                    Point p = new Point((int) e.getX(), (int) e.getY());
                    LatLng l = map.getProjection().fromScreenLocation(p);
                    onPostLongPress(l);
                }
                @Override
                public boolean onDown(MotionEvent e) {
                    if(e.getPointerCount()>1) return false;
                    GoogleMap map = getMap();
                    Point p = new Point((int) e.getX(), (int) e.getY());
                    LatLng l = map.getProjection().fromScreenLocation(p);
                    onPostDown(l);
                    return true;
                }
                @Override
                public void onShowPress(MotionEvent e) {
                    if(e.getPointerCount()>1) return;
                    GoogleMap map = getMap();
                    Point p = new Point((int) e.getX(), (int) e.getY());
                    LatLng l = map.getProjection().fromScreenLocation(p);
                    onPostShowPress(l);
                }
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    if(e.getPointerCount()>1) return false;
                    GoogleMap map = getMap();
                    Point p = new Point((int) e.getX(), (int) e.getY());
                    LatLng l = map.getProjection().fromScreenLocation(p);
                    onPostSingleTapUp(l);
                    return true;
                }

            };
    private ScaleGestureDetector mScaleGestureDetector = null;
    private ScaleGestureDetector.SimpleOnScaleGestureListener mScaleGestudeListener =
            new ScaleGestureDetector.SimpleOnScaleGestureListener() {
                @Override
                public boolean onScale (ScaleGestureDetector detector) {
                    if (mIsInAnimation) return false;

                    GoogleMap map = getMap();
                    double oldZoom = map.getCameraPosition().zoom;
                    double zoom = map.getCameraPosition().zoom;

                    double k = 1d / detector.getScaleFactor();
                    int x = (int) detector.getFocusX();
                    int y = (int) detector.getFocusY();
                    LatLng mapFocus = map.getProjection().
                            fromScreenLocation(new Point(x, y));
                    LatLng target = map.getCameraPosition().target;

                    zoom = zoom + Math.log(detector.getScaleFactor()) / Math.log(2d);
                    if (zoom < MIN_ZOOM)
                        if (zoom == MIN_ZOOM) return false;
                        else zoom = MIN_ZOOM;
                    if (zoom > MAX_ZOOM)
                        if (zoom == MAX_ZOOM) return false;
                        else zoom = MAX_ZOOM;

                    double dx = norm(mapFocus.longitude) - norm(target.longitude);
                    double dy = mapFocus.latitude - target.latitude;
                    double dk = 1d - 1d / k;
                    LatLng newTarget = new LatLng(target.latitude - dy * dk,
                            norm(target.longitude) - dx * dk);

                    onPostScale(target, oldZoom, newTarget, zoom);
                    return true;
                }
            };
    public void tryUpdateCamera(CameraUpdate update, int animateTime) {
        GoogleMap map = getMap();
        final VisibleRegion reg = map.getProjection().getVisibleRegion();
        if (animateTime <= 0) {
            map.moveCamera(update);
        } else {
            mIsInAnimation = true;
            map.animateCamera(update, animateTime, new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    mIsInAnimation = false;
                }
                @Override
                public void onCancel() {
                    mIsInAnimation = false;
                }
            });
        }
    }
    private double norm(double lonVal) {
        while (lonVal > 360d) lonVal -= 360d;
        while (lonVal < -360d) lonVal += 360d;
        if (lonVal < 0) lonVal = 360d + lonVal;
        return lonVal;
    }

    private double denorm(double lonVal) {
        if (lonVal > 180d) lonVal = -360d + lonVal;
        return lonVal;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mScaleGestureDetector != null) mScaleGestureDetector.onTouchEvent(event);
        if (mGestureDetector != null) mGestureDetector.onTouchEvent(event);
        return super.onInterceptTouchEvent(event);
/*        if(mScaleGestureDetector != null && event.getPointerCount()>1) return mScaleGestureDetector.onTouchEvent(event);
        else if(mGestureDetector != null) return mGestureDetector.onTouchEvent(event);
        else return super.onInterceptTouchEvent(event);*/
    }

    public abstract void onPostScroll(LatLng source, LatLng target);
    public abstract void onPostFling(LatLng source, LatLng target, int animateTime);
    public abstract void onPostScale(LatLng source, double sourceZoom, LatLng target, double targetZoom);
    public abstract void onPostLongPress(LatLng targer);
    public abstract void onPostDown(LatLng target);
    public abstract void onPostShowPress(LatLng target);
    public abstract void onPostSingleTapUp(LatLng target);
}

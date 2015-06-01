package com.shhutapp.fragments.area;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.shhutapp.Actions;
import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.controls.ZoneView;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.geo.maparea.MapAreaManager;
import com.shhutapp.geo.maparea.MapAreaManager.CircleManagerListener;
import com.shhutapp.geo.maparea.MapAreaMeasure;
import com.shhutapp.geo.maparea.MapAreaWrapper;
import com.shhutapp.pages.BasePage;
import com.shhutapp.services.Locator;
import com.shhutapp.social.twitter.extpack.winterwell.json.JSONObject;
import com.shhutapp.utils.Geo;

/**
 * Created by victor on 28.05.15.
 */
public class MapArea  extends SupportMapFragment{
    public View view;
    public TouchableWrapper toucher;
    private GoogleMap map;
    private MapAreaManager manager;
    private MainActivity act;
    private BasePage page;
    private View rView;
    private double lat;
    private double lon;

    public MapArea(){
        super();
        this.act = null;
        this.page = null;
    }

    public MapArea(MainActivity act, BasePage page){
        super();
        this.act = act;
        this.page = page;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        //rView = inf.inflate(R.layout.maparea, container, false);
        view = super.onCreateView(inf, container, savedInstanceState);
        toucher = new TouchableWrapper(getActivity());
        //toucher.addView(rView);
        toucher.addView(view);
        map = getMap();
        if(map != null){
            createManager();
            toucher.setMap(getMap());
            toucher.setAreaManager(manager);
        }
        return toucher;
    }
    @Override
    public View getView() {
        return view;
    }
    public void invalidate(){
        toucher.invalidate();
        view.invalidate();
    }

    private void createManager(){
        manager = new MapAreaManager(map, 4, Color.RED, Color.HSVToColor(70, new float[] {1, 1, 200}),
                R.drawable.logo, R.drawable.drag_circle, 0.5f, 1.0f, 0.5f, 0.5f,
                new MapAreaMeasure(act.getSettings().getDefaultAreaRadius(), MapAreaMeasure.Unit.meters),
                new CircleManagerListener() { //listener for all circle events
                    @Override
                    public void onResizeCircleEnd(MapAreaWrapper draggableCircle) {
                        double newRadius = draggableCircle.getRadius();
                        for(MapAreaWrapper wr : manager.getCircles()){
                            if(wr.equals(draggableCircle)) continue;
                            if(Geo.distance(wr.getCenter(), draggableCircle.getCenter())<=(draggableCircle.getRadius()+wr.getRadius())){
                                newRadius = Geo.distance(wr.getCenter(), draggableCircle.getCenter())-wr.getRadius();
                                draggableCircle.setRadius(newRadius);
                                //draggableCircle.rebuildSizeMarker();
                            }
                        }
                        /*ContentValues row = new ContentValues();
                        row.put("radius", Math.abs(newRadius));
                        String[] args = {draggableCircle.getName()};
                        getDB().update("locations", row, "name=?", args);*/

                    }
                    @Override
                    public void onCreateCircle(MapAreaWrapper draggableCircle) {
                    }
                    @Override
                    public void onMoveCircleEnd(MapAreaWrapper draggableCircle) {
                    }
                    @Override
                    public void onMoveCircleStart(MapAreaWrapper draggableCircle) {
                    }
                    @Override
                    public void onResizeCircleStart(MapAreaWrapper draggableCircle) {
                    }
                    @Override
                    public void onMinRadius(MapAreaWrapper draggableCircle) {
                    }
                    @Override
                    public void onMaxRadius(MapAreaWrapper draggableCircle) {
                    }
                });
        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon),18)); ;
    }
    @Override
    public void onResume(){
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Actions.Location);
        act.registerReceiver(br, filter);
        act.startService(new Intent(act, Locator.class));
    }
    public void setActivity(MainActivity act){
        this.act = act;
    }
    public void setPage(BasePage page){
        this.page = page;
    }
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String command = intent.getExtras().getString("command");
            if(command != null) {
                JSONObject obj = new JSONObject(command);
                lat = obj.getDouble("lat");
                lon = obj.getDouble("lon");
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon),18)); ;
            }
        }
    };
}

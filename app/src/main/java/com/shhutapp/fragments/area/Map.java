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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.shhutapp.Actions;
import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.controls.Clickator;
import com.shhutapp.controls.ZoneView;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.geo.maparea.MapAreaManager;
import com.shhutapp.geo.maparea.MapAreaMeasure;
import com.shhutapp.geo.maparea.MapAreaWrapper;
import com.shhutapp.pages.BasePage;
import com.shhutapp.services.Locator;
import com.shhutapp.social.twitter.extpack.winterwell.json.JSONObject;
import com.shhutapp.utils.Geo;

/**
 * Created by victor on 01.06.15.
 */
public class Map extends BaseFragments{
    private BasePage page;
    private ZoneView zv;
    private Clickator clickator;
    private MapAreaManager manager;
    private Bundle saved;
    private RelativeLayout rlMyLocation;
    private double lat;
    private double lon;
    public Map(){
        super(MainActivity.getMainActivity());
        lat = 0;
        lon = 0;
    }
    public Map(MainActivity act){
        super(act);
        lat = 0;
        lon = 0;
    }
    public Map(MainActivity act, BasePage page){
        super(act);
        this.page = page;
        lat = 0;
        lon = 0;
    }
    @Override
    public   void onCreate(Bundle saved){
        super.onCreate(saved);
        this.saved = saved;
    }
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.map, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        zv = (ZoneView)rView.findViewById(R.id.map);
        MapsInitializer.initialize(act);
        zv.onCreate(saved);
        zv.onResume();
        clickator = (Clickator) rView.findViewById(R.id.clickator);
        clickator.setMap(zv.getMap());
        clickator.setAreaManager(createManager());
        rlMyLocation = (RelativeLayout) rView.findViewById(R.id.rlMyLocation);
        rlMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act.startService(new Intent(MainActivity.getMainActivity(), Locator.class));
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Actions.Location);
        act.registerReceiver(brMyLocation, filter);
    }
    @Override
    public void onPause(){
        super.onPause();
        act.unregisterReceiver(brMyLocation);
    }
    private MapAreaManager createManager(){
        return new MapAreaManager(zv.getMap(), 4, Color.RED, Color.HSVToColor(70, new float[] {1, 1, 200}),
                R.drawable.logo, R.drawable.drag_circle, 0.5f, 1.0f, 0.5f, 0.5f,
                new MapAreaMeasure(act.getSettings().getDefaultAreaRadius(), MapAreaMeasure.Unit.meters),
                new MapAreaManager.CircleManagerListener() { //listener for all circle events
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
    private BroadcastReceiver brMyLocation = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String command = intent.getExtras().getString("command");
            if(command != null) {
                JSONObject obj = new JSONObject(command);
                lat = obj.getDouble("lat");
                lon = obj.getDouble("lon");
                CameraPosition pos = new CameraPosition.Builder().target(new LatLng(lat,lon)).zoom(18).build();
                zv.getMap().animateCamera(CameraUpdateFactory.newCameraPosition(pos));
            }
        }
    };
}

package com.shhutapp.fragments.area;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shhutapp.Actions;
import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.controls.ExMapView;
import com.shhutapp.data.Address;
import com.shhutapp.data.BaseObjectList;
import com.shhutapp.data.FilteredAdapter;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.fragments.OnNextListener;
import com.shhutapp.geo.AreaManager;
import com.shhutapp.pages.BasePage;
import com.shhutapp.services.Finder;
import com.shhutapp.services.Locator;
import com.shhutapp.utils.Convertor;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.concurrent.TimeUnit;

/**
 * Created by victor on 01.06.15.
 */
public class Map extends BaseFragments{
    private BasePage page;
    private ExMapView zv;
    private Bundle saved;
    private RelativeLayout rlMyLocation;
    private double lat;
    private double lon;
    private String newName;
    private TextView tvMapAddress;
    private RelativeLayout rlMapAddress;
    private ImageView ivSearchMenu;
    private ImageView ivSearchCancel;
    private EditText edSearch;
    private RelativeLayout rlMapMenu;
/*
    private RelativeLayout rlItem1;
    private RelativeLayout rlItem2;
    private RelativeLayout rlItem3;
    private RelativeLayout rlItem4;
    */
    private RelativeLayout[] rlItems;
    private TextView[] tvItems;
    private boolean isMenu;
    private int selectedItem;
    private boolean isFind = true;
    private BaseObjectList listAddress;
    private MapAddressAdapter add;
    private ListView lvAddress;
    private ImageView ivMapUpdate;
    private RelativeLayout rlMapUpdare;
    private int centerX;
    private int centerY;
    private Point pCenter;
    private TextView tvMapError;
    private RelativeLayout rlMapError;
    private TextView tvMapErrorClose;
    private TextView tvMapErrorSettings;
    private Marker my;

    public Map(){
        super(MainActivity.getMainActivity());
        lat = 0;
        lon = 0;
        isMenu = false;
    }
    @SuppressLint("ValidFragment")
    public Map(MainActivity act){
        super(act);
        lat = 0;
        lon = 0;
        isMenu = false;
    }
    @SuppressLint("ValidFragment")
    public Map(MainActivity act, BasePage page){
        super(act);
        this.page = page;
        lat = 0;
        lon = 0;
        isMenu = false;
    }
    @Override
    public   void onCreate(Bundle saved){
        super.onCreate(saved);
        this.saved = saved;
        rlItems = new RelativeLayout[4];
        tvItems = new TextView[4];
    }
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.map, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, final Bundle saved) {
        super.onViewCreated(view, saved);
        getMainActivity().getHeader().setOnNextListener(new OnNextListener() {
            @Override
            public void onNext() {
                Bitmap bp = Bitmap.createBitmap(zv.getWidth(), zv.getHeight(), Bitmap.Config.ARGB_8888);
                final GoogleMap.SnapshotReadyCallback cb = new GoogleMap.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(Bitmap bitmap) {
                        int wInPic = (int) Convertor.convertDpToPixel(350, getMainActivity());
                        int hInPic = (int) getMainActivity().getResources().getDimension(R.dimen.map_card_view_height);
                        pCenter = zv.getPointPressed();
                        Bitmap bPrev = Bitmap.createBitmap(bitmap, 0, pCenter.y - hInPic / 2, wInPic, hInPic);
                        Bitmap bp = makeTransparent(bPrev, 72);
                        ContentValues row = new ContentValues();
                        row.put("background", Convertor.BitmapToBase64(bp));
                        //row.put("lat", zv.getManager().getLast().getCenter().latitude);
                        //row.put("long", zv.getManager().getLast().getCenter().latitude);
                        row.put("radius",zv.getManager().getLast().getRadius());
                        getMainActivity().getDB().update("locations", row, "name=?", new String[]{zv.getManager().getLast().getName()});
                        AreaName ar = new AreaName(getMainActivity(), page);
                        Bundle b = new Bundle();
                        b.putString("number", zv.getManager().getLast().getName());
                        b.putString("photo", Convertor.BitmapToBase64(bp));
                        b.putString("address", tvMapAddress.getText().toString());
                        ar.setArguments(b);
                        getMainActivity().getSupportFragmentManager().beginTransaction().
                                addToBackStack(null).
                                replace(R.id.areaPage, ar).
                                commit();
                    }
                };
                zv.getManager().clearAll();
                zv.getMap().clear();
                zv.invalidate();
                zv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        zv.getMap().snapshot(cb);
                    }
                },100);

            }
        });

        zv = (ExMapView)rView.findViewById(R.id.map);
        MapsInitializer.initialize(act);
        zv.onCreate(saved);
        zv.onResume();
        zv.getMap().setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        zv.getMap().getUiSettings().setAllGesturesEnabled(false);

        //zv.getManager().load(getMainActivity().getDBHelper());
        rlMyLocation = (RelativeLayout) rView.findViewById(R.id.rlMyLocation);
        rlMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.getMainActivity(), Locator.class);
                intent.putExtra("accuracy", getMainActivity().getSettings().getDefaultAccuracy());
                act.startService(intent);
            }
        });
        ivMapUpdate = (ImageView) rView.findViewById(R.id.ivUpdateMap);
        ivMapUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("VMap", "Refresh...");
                MapsInitializer.initialize(act);
                zv.onCreate(saved);
                zv.onResume();
                Log.i("VMap", "Refreshed");
            }
        });
        rlMapAddress = (RelativeLayout) rView.findViewById(R.id.rlMapAddress);
        rlMapAddress.setVisibility(View.INVISIBLE);
        tvMapAddress = (TextView) rView.findViewById(R.id.tvMapAddress);
        ivSearchMenu = (ImageView) rView.findViewById(R.id.ivSearchMenu);
        ivSearchMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMenu = true;
                upDateMenu();
            }
        });
        ivSearchCancel = (ImageView)rView.findViewById(R.id.ivSearchCancel);
        ivSearchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edSearch.setText("");
            }
        });
        edSearch = (EditText) rView.findViewById(R.id.edMapTextSearch);
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count >= 3 && isFind) {
                    Intent intent = new Intent(getMainActivity(), Finder.class);
                    intent.putExtra("find", String.valueOf(s));
                    getMainActivity().startService(intent);
                    isFind = (edSearch.getText().toString().length() > 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        createMenu();
        lvAddress = (ListView) rView.findViewById(R.id.lvAddress);
        listAddress = new BaseObjectList();
        add = new MapAddressAdapter(getMainActivity(),listAddress);
        lvAddress.setAdapter(add);
        lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isFind = false;
                Address newAd = (Address) listAddress.get(position);
                edSearch.setText(newAd.getName().toString());
                LatLng coordinate = new LatLng(newAd.getLatitude(), newAd.getLongitude());
                CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(coordinate, 15);
                zv.getMap().animateCamera(camera);
                add.setData(new BaseObjectList());
                add.notifyDataSetChanged();
                //rlMapUpdate.setVisibility(View.VISIBLE);
                isFind = true;
                InputMethodManager imm = (InputMethodManager) getMainActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edSearch.getWindowToken(), 0);
            }
        });
        rlMapUpdare = (RelativeLayout) rView.findViewById(R.id.rlMapUpdate);
        ivMapUpdate = (ImageView) rView.findViewById(R.id.ivUpdateMap);
        ivMapUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //zv.getMap().
            }
        });
        tvMapError = (TextView) rView.findViewById(R.id.tvMapErrorText);
        rlMapError = (RelativeLayout) rView.findViewById(R.id.rlMapError);
        rlMapError.setVisibility(View.INVISIBLE);
        tvMapErrorClose = (TextView) rView.findViewById(R.id.tvMapErrorClose);
        tvMapErrorClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlMapError.setVisibility(View.INVISIBLE);
            }
        });
        tvMapErrorSettings = (TextView) rView.findViewById(R.id.tvMapErrorSettings);
        tvMapErrorSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BasePage sett = getMainActivity().createPageFromID(BasePage.Pages.settingsPage);
                Bundle b = new Bundle();
                b.putInt("prevID", BasePage.Pages.areaPage);
                sett.setArguments(b);
                getMainActivity().getSupportFragmentManager().beginTransaction().remove(page).commit();
                getMainActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, sett).commit();
            }
        });
    }

    private void createMenu() {
        rlMapMenu = (RelativeLayout) rView.findViewById(R.id.rlMapMenu);
        rlMapMenu.setVisibility(isMenu?View.VISIBLE:View.INVISIBLE);
        tvItems[0] = (TextView) rView.findViewById(R.id.tvItem1);
        tvItems[1] = (TextView) rView.findViewById(R.id.tvItem2);
        tvItems[2] = (TextView) rView.findViewById(R.id.tvItem3);
        tvItems[3] = (TextView) rView.findViewById(R.id.tvItem4);
        rlItems[0] = (RelativeLayout) rView.findViewById(R.id.item1);
        rlItems[1] = (RelativeLayout) rView.findViewById(R.id.item2);
        rlItems[2] = (RelativeLayout) rView.findViewById(R.id.item3);
        rlItems[3] = (RelativeLayout) rView.findViewById(R.id.item4);

        rlItems[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zv.getMap().setMapType(GoogleMap.MAP_TYPE_HYBRID);
                isMenu = false;
                selectedItem = 0;
                upDateMenu();
            }
        });
        rlItems[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zv.getMap().setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                isMenu = false;
                selectedItem = 1;
                upDateMenu();
            }
        });
        rlItems[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                isMenu = false;
                selectedItem = 2;
                upDateMenu();
            }
        });
        rlItems[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMenu = false;
                selectedItem = 3;
                upDateMenu();
            }
        });


    }
    private void upDateMenu() {
        for(int i = 0; i<4;i++){
            rlItems[i].setBackground(getMainActivity().getResources().getDrawable(R.drawable.rect_map_search));
            tvItems[i].setTextColor(Color.argb(255, 0,0,0));
        }
        rlMapMenu.setVisibility(isMenu?View.VISIBLE:View.INVISIBLE);
        if(zv.getMap().getMapType() == GoogleMap.MAP_TYPE_HYBRID) tvItems[0].setTextColor(Color.argb(255, 197, 17, 98));
        else if(zv.getMap().getMapType() == GoogleMap.MAP_TYPE_TERRAIN)tvItems[1].setTextColor(Color.argb(255, 197, 17, 98));
    }

    @Override
    public void onResume(){
        super.onResume();
        IntentFilter filterMyLocation = new IntentFilter();
        filterMyLocation.addAction(Actions.Location);
        act.registerReceiver(brMyLocation, filterMyLocation);
        IntentFilter filterAddressator = new IntentFilter();
        filterAddressator.addAction(Actions.Addressator);
        act.registerReceiver(addressator, filterAddressator);
        IntentFilter filterFinder = new IntentFilter();
        filterFinder.addAction(Actions.Finder);
        act.registerReceiver(finder, filterFinder);

        IntentFilter filterFling = new IntentFilter();
        filterFling.addAction("fling");
        act.registerReceiver(fling, filterFling);

    }
    @Override
    public void onPause(){
        super.onPause();
        act.unregisterReceiver(brMyLocation);
        act.unregisterReceiver(addressator);
        act.unregisterReceiver(finder);
        act.unregisterReceiver(fling);
    }
    private BroadcastReceiver brMyLocation = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int status = 0;
            String command = intent.getExtras().getString("command");
            if(command != null) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(command);
                    lat = obj.getDouble("lat");
                    lon = obj.getDouble("lon");
                    status = obj.getInt("status_code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(status == 1) {
                    Log.i("Location", "Lantitude: " + lat + " Longitude: " + lon);
                    CameraPosition pos = new CameraPosition.Builder().target(new LatLng(lat, lon)).zoom(16).build();
                    zv.getMap().animateCamera(CameraUpdateFactory.newCameraPosition(pos));
                    if(my != null) my.remove();
                    Bitmap b = BitmapFactory.decodeResource(getMainActivity().getResources(), R.drawable.my);
                    int r = (int)Convertor.convertDpToPixel(20,getMainActivity());
                    Bitmap bb = Bitmap.createScaledBitmap(b, r, r, false);
                    my = zv.getMap().addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bb)).draggable(false).position(new LatLng(lat,lon)));
                }
                else if(status == -1){
                    //tvMapError.setText(getMainActivity().getResources().getString(R.string.not_location));
                    rlMapError.setVisibility(View.VISIBLE);
                }
                else if(status == -2){
                    tvMapError.setText(getMainActivity().getResources().getString(R.string.not_source));
                    rlMapError.setVisibility(View.VISIBLE);
                }
            }
        }
    };
    private BroadcastReceiver addressator = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int status = 0;
            String command = intent.getExtras().getString("command");
            if(command == null) return;
            JSONObject result = null;
            try {
                result = new JSONObject(command);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ContentValues row = new ContentValues();
            String name = null, city = null, street = null;
            double lat=0, lon=0, radius;
            try {
                name = result.getString("name");
                city = result.getString("city");
                if(city == null) city = " ";
                street = result.getString("street");
                if(street == null) street = " ";
                lat = result.getDouble("lat");
                lon = result.getDouble("long");
                radius = result.getDouble("radius");
                status = result.getInt("status_code");
                row.put("name", name);
                row.put("lat", lat);
                row.put("long", lon);
                row.put("city", city);
                row.put("street", street);
                row.put("radius", radius);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //String[] cols = {"name","lat","long","city","street","radius"};
            if(status == 1) {
                String[] args = {name};
                Cursor c = act.getDB().query("locations", null, "name=?", args, null, null, null);
                if (c.moveToFirst()) act.getDB().update("locations", row, "name=?", args);
                else act.getDB().insert("locations", null, row);
                tvMapAddress.setText(street);
                rlMapAddress.setVisibility(View.VISIBLE);
            }
            else if(status == -1){
                //tvMapError.setText(getMainActivity().getResources().getString(R.string.not_address));
                //rlMapError.setVisibility(View.VISIBLE);
            }
            getMainActivity().getHeader().setVisibleNext(true);
        }
    };
    BroadcastReceiver finder = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String command = intent.getExtras().getString("command");
            JSONObject result = null;
            try {
                result = new JSONObject(command);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listAddress = new BaseObjectList();
            JSONArray arr = null;
            try {
                arr = result.getJSONArray("result");
                for(int i = 0; i<arr.length();i++){
                    JSONObject o = arr.getJSONObject(i);
                    String s = o.getString("address");
                    double lat = o.getDouble("lat");
                    double lon = o.getDouble("lon");
                    Address p = new Address(1,s,lat, lon);
                    listAddress.add(p);
                }
                add.setData(listAddress);
                add.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
    BroadcastReceiver fling= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            rlMapAddress.setVisibility(View.INVISIBLE);
            getMainActivity().getHeader().setVisibleNext(false);
        }
    };
    private class MapAddressAdapter extends FilteredAdapter {

        public MapAddressAdapter(Context context, BaseObjectList list) {
            super(context, list);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getInflater().inflate(R.layout.map_search_item, null);
            TextView tvMenuItemText = (TextView) view.findViewById(R.id.tvMenuItemText);
            String text = (String) getData().get(position).getName();
            tvMenuItemText.setText(text);
            return view;
        }
        public void setData(BaseObjectList newData){
            this.list = newData;
            this.data = newData;
        }
    }
    public Bitmap makeTransparent(Bitmap src, int value) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap transBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(transBitmap);
        canvas.drawARGB(0, 0, 0, 0);
        // config paint
        final Paint paint = new Paint();
        paint.setAlpha(value);
        canvas.drawBitmap(src, 0, 0, paint);
        return transBitmap;
    }
}

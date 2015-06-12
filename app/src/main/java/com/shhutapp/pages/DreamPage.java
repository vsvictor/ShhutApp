package com.shhutapp.pages;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.controls.TransportedLayoutCircle;
import com.shhutapp.fragments.dream.DreamMain;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by victor on 22.05.15.
 */
public class DreamPage extends BasePage {
    private DreamPage page;
    private DreamMain main;
    private int callCounterl;
    private int appCounter;
    private int msgCounter;
    public DreamPage() {
        super(MainActivity.getMainActivity());
    }
    public DreamPage(MainActivity act) {
        super(act);
    }
    public DreamPage(MainActivity act, DreamPage page) {
        super(act);
        this.page = page;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        prevID = getArguments().getInt("back");
        main = new DreamMain(getMainActivity(), page);
    }
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        callCounterl = 0;
        appCounter = 0;
        msgCounter = 0;
        rootView = inf.inflate(R.layout.dream_page, container, false);
        return rootView;
    }
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        final int h = view.getMeasuredHeight();
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final TransportedLayoutCircle tr = (TransportedLayoutCircle) rootView.findViewById(R.id.ivDreamBack);
                tr.setMaxRadius(800);
                tr.setCenter(new Point((int) event.getX(), (int) event.getY()));
                final Timer t = new Timer();
/*                tr.setOnStopListener(new OnStopLister() {
                    @Override
                    public void onStop() {
                        t.cancel();
                        getMainActivity().setDream(false);
                        MainActivity.mpage = new MainPage(getMainActivity());
                        getMainActivity().getSupportFragmentManager().beginTransaction().show(getMainActivity().getHeader()).commitAllowingStateLoss();
                        getMainActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, getMainActivity().getMainPage()).commitAllowingStateLoss();
                    }
                });*/
                t.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        getMainActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tr.addRadius();
                                if(tr.getRadius()>tr.getMaxRadius()){
                                    t.cancel();
                                    getMainActivity().setDream(false);
                                    getMainActivity().getSupportFragmentManager().beginTransaction().show(getMainActivity().getHeader()).commitAllowingStateLoss();
                                    getMainActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, getMainActivity().getMainPage()).commitAllowingStateLoss();
                                }
                                else tr.invalidate();
                            }
                        });
                    }
                },0,5);

                return true;
            }
        });
        getMainActivity().setDream(true);
    }
    @Override
    public void onResume(){
        super.onResume();
        getMainActivity().getHeader().setHeight(0);
        if(!main.isAdded()) {
            fragmentManager().beginTransaction().add(R.id.dreamPage, main).commit();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction("call");
        filter.addAction("app");
        filter.addAction("msg");
        getMainActivity().registerReceiver(receiver, filter);
    }
    @Override
    public void onPause(){
        super.onPause();
        //receiver.abortBroadcast();
        //getMainActivity().unregisterReceiver(receiver);
    }
    @Override
    public int getID() {
        return Pages.dreamPage;
    }
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int type = intent.getExtras().getInt("type");
            switch (type){
                case 1: {
                    callCounterl++;
                    break;
                }
                case 2: {
                    appCounter++;
                    break;
                }
                case 3: {
                    int count = intent.getExtras().getStringArrayList("messages").size();
                    msgCounter += count;
                    break;
                }
            }
            main.updateData(callCounterl/2, appCounter, msgCounter);
        }
    };
}

package com.shhutapp.pages;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;

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
    private boolean reges = false;

    public DreamPage() {
        super(MainActivity.getMainActivity());
    }
    @SuppressLint("ValidFragment")
    public DreamPage(MainActivity act) {
        super(act);
    }
    @SuppressLint("ValidFragment")
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
        getMainActivity().resumeStatusBarColor();
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
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        getMainActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tr.addRadius(3);
                                if(tr.getRadius()>tr.getMaxRadius()){
                                    t.cancel();
                                    getMainActivity().replaceStatusBarColor();
                                    getMainActivity().setDream(false);
                                    getMainActivity().getSupportFragmentManager().beginTransaction().show(getMainActivity().getHeader()).commitAllowingStateLoss();
                                    getMainActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, getMainActivity().getMainPage()).commitAllowingStateLoss();
                                }
                                else tr.reDraw();//tr.invalidate();
                            }
                        });
                    }
                },25,25);
                //Animation an = AnimationUtils.loadAnimation(getMainActivity(), R.anim.tear);
                //tr.startAnimation(an);

                return true;
            }
        });
        getMainActivity().setDream(true);
    }
    @Override
    public void onResume(){
        super.onResume();
        getMainActivity().resumeStatusBarColor();
        getMainActivity().setTransparedNav();
        getMainActivity().getHeader().setHeight(0);
        if(!main.isAdded()) {
            fragmentManager().beginTransaction().add(R.id.dreamPage, main).commit();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction("call");
        filter.addAction("app");
        filter.addAction("msg");
        if(!reges) {
            getMainActivity().registerReceiver(receiver, filter);
            reges =true;
        }
        IntentFilter ifil = new IntentFilter();
        ifil.addAction("com.google.android.c2dm.intent.RECEIVE");
        getMainActivity().registerReceiver(res, ifil);
    }
    @Override
    public void onPause(){
        super.onPause();
        if(reges) {
            //receiver.abortBroadcast();
            //getMainActivity().unregisterReceiver(receiver);
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
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

            main.updateData(callCounterl, appCounter, msgCounter);
            //NotificationManager man = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //man.cancelAll();
            //abortBroadcast();
        }
    };
    private BroadcastReceiver res = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("Broad", "!!!!!!!!!!YES!!!!!!!!!!!!!!");
        }
    };
}

package com.shhutapp.pages;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.fragments.CardList;
import com.shhutapp.fragments.MainControlPanel;
import com.shhutapp.fragments.OnBackListener;
import com.shhutapp.fragments.OnCancelListener;
import com.shhutapp.fragments.Scale;
import com.shhutapp.fragments.dream.DreamMain;
import com.shhutapp.fragments.dream.OnExitListener;
import com.shhutapp.fragments.messages.MessageEmpty;
import com.shhutapp.fragments.messages.MessageList;
import com.shhutapp.fragments.messages.MessageListListener;
import com.shhutapp.fragments.messages.MessageNew;
import com.shhutapp.fragments.messages.MessageScale;

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
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().setDream(false);
                BasePage page = getMainActivity().createPageFromID(prevID);
                getMainActivity().getSupportFragmentManager().beginTransaction().remove(getCurrent()).commit();
                getMainActivity().getSupportFragmentManager().beginTransaction().add(R.id.header, getMainActivity().getHeader()).replace(R.id.container, page).commit();
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
                    msgCounter++;
                    break;
                }
            }
            main.updateData(callCounterl/2, appCounter, msgCounter);
        }
    };
}

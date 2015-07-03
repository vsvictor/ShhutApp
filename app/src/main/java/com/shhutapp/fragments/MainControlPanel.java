package com.shhutapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.shhutapp.MainActivity;
import com.shhutapp.R;

public class MainControlPanel extends BaseFragments {
    private RelativeLayout rlDreamMode;
    private RelativeLayout rlQuietHours;
    private RelativeLayout rlWhiteList;
    private RelativeLayout rlMessages;
    private MainControlPanelListener listener;
    public MainControlPanel(){
        super();
        rView = null;
    }
    @SuppressLint("ValidFragment")
    public  MainControlPanel(MainActivity act){
        super(act);
        rView = null;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState){
        rView = inf.inflate(R.layout.control_panel_main, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved){
        super.onViewCreated(view,saved);
        rlDreamMode = (RelativeLayout)rView.findViewById(R.id.rlDreamMode);
        rlDreamMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDreamMode();
            }
        });
        rlQuietHours = (RelativeLayout)rView.findViewById(R.id.rlQuietHours);
        rlQuietHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onQuietHours();
            }
        });
        rlWhiteList = (RelativeLayout)rView.findViewById(R.id.rlWhiteList);
        rlWhiteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onWhiteList();
            }
        });
        rlMessages = (RelativeLayout)rView.findViewById(R.id.rlMessages);
        rlMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMessages();
            }
        });
    }
    public void setOnMainControlPanelListener(final MainControlPanelListener actions){
        listener = actions;
    }
}

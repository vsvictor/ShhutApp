package com.shhutapp.fragments.settings;

import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.pages.BasePage;

/**
 * Created by victor on 26.05.15.
 */
public class SettingsSN extends BaseFragments {
    private BasePage page;
    private OnNetworks listener;
    private RelativeLayout rlFacebook;
    private RelativeLayout rlTwitter;
    private RelativeLayout rlVK;
    private RelativeLayout rlSMS;

    public SettingsSN(){
        super(MainActivity.getMainActivity());
    }
    public SettingsSN(MainActivity act){
        super(act);
    }
    public SettingsSN(MainActivity act, BasePage page){
        super(act);
        this.page = page;
    }
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.sn_networks, container, false);
        return rView;
    }
    public void onViewCreated(View view, Bundle saved){
        super.onViewCreated(view, saved);
        rlFacebook = (RelativeLayout) rView.findViewById(R.id.rlFacebook);
        rlTwitter = (RelativeLayout) rView.findViewById(R.id.rlTwitter);
        rlVK = (RelativeLayout) rView.findViewById(R.id.rlVK);
        rlSMS = (RelativeLayout) rView.findViewById(R.id.rlSMS);
        rlFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onFacebook();
                }
            }
        });
        rlTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onTwitter();
                }
            }
        });
        rlVK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onVK();
                }
            }
        });
        rlSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onSMS();
                }
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
    }
    public void setOnNetworkListener(OnNetworks list){
        listener = list;
    }
}

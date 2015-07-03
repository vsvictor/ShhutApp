package com.shhutapp.fragments.queittime;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.fragments.MainControlPanelListener;
import com.shhutapp.pages.QueitTimePage;

public class QueitTimeControlPanel extends BaseFragments {
    private QueitTimePage page;
    private RelativeLayout rlQueitTimeDays;
    private RelativeLayout rlQuietTimeWL;
    private RelativeLayout rlQueitTimeMsg;
    private QueitTimeControlPanelListener listener;
    private ImageView ivQueitTimeOn;
    private ImageView ivQueitTimeOff;
    private ImageView ivQueitTimeDaysOn;
    private ImageView ivQueitTimeDaysOff;
    private ImageView ivQueitTimeMsgOn;
    private ImageView ivQueitTimeMsgOff;
    private boolean isWhiteList;
    private boolean isMessage;
    private boolean isDays;
    public QueitTimeControlPanel(){
        super();
        rView = null;
    }
    @SuppressLint("ValidFragment")
    public  QueitTimeControlPanel(MainActivity act){
        super(act);
        rView = null;
    }
    @SuppressLint("ValidFragment")
    public  QueitTimeControlPanel(MainActivity act, QueitTimePage page){
        super(act);
        rView = null;
        this.page = page;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        try {
            isWhiteList = getArguments().getBoolean("isWhiteList");
            isMessage = getArguments().getBoolean("isMessage");
            isDays = getArguments().getBoolean("isDays");
        }catch (Exception e){
            isWhiteList = false;
            isMessage = false;
            isDays = false;
            getMainActivity().clearWhiteList();
            getMainActivity().clearSMS();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState){
        rView = inf.inflate(R.layout.control_panel_queittime, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved){
        super.onViewCreated(view, saved);
        rlQueitTimeDays = (RelativeLayout)rView.findViewById(R.id.rlQueitTimeDays);
        rlQueitTimeDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDays();
            }
        });
        rlQuietTimeWL = (RelativeLayout)rView.findViewById(R.id.rlQuietTimesWhiteList);
        rlQuietTimeWL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onWhiteList();
            }
        });
        rlQueitTimeMsg = (RelativeLayout)rView.findViewById(R.id.rlQueitTimeMsg);
        rlQueitTimeMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMessage();
            }
        });

        ivQueitTimeDaysOn = (ImageView) rView.findViewById(R.id.ivDaysOn);
        ivQueitTimeDaysOff = (ImageView) rView.findViewById(R.id.ivDaysOff);
        ivQueitTimeDaysOn.setVisibility(isDays?View.VISIBLE:View.INVISIBLE);
        ivQueitTimeDaysOff.setVisibility(isDays?View.INVISIBLE:View.VISIBLE);

        ivQueitTimeOn = (ImageView) rView.findViewById(R.id.ivQuietTimeWLOn);
        ivQueitTimeOff = (ImageView) rView.findViewById(R.id.ivQuietTimeWLOff);
        ivQueitTimeOn.setVisibility(isWhiteList?View.VISIBLE:View.INVISIBLE);
        ivQueitTimeOff.setVisibility(isWhiteList?View.INVISIBLE:View.VISIBLE);

        ivQueitTimeMsgOn = (ImageView) rView.findViewById(R.id.ivQueitTimeMsgOn);
        ivQueitTimeMsgOff = (ImageView) rView.findViewById(R.id.ivQueitTimeMsgOff);
        ivQueitTimeMsgOn.setVisibility(isMessage?View.VISIBLE:View.INVISIBLE);
        ivQueitTimeMsgOff.setVisibility(isMessage?View.INVISIBLE:View.VISIBLE);
    }
    public void setOnQueitTimeControlPanelListener(final QueitTimeControlPanelListener actions){
        listener = actions;
    }
    @Override
    public void onHiddenChanged(boolean hidden){
        super.onHiddenChanged(hidden);
        try {
            if(getMainActivity().getWhiteList() != null || isWhiteList){
                ivQueitTimeOn.setVisibility(View.VISIBLE);
                ivQueitTimeOff.setVisibility(View.INVISIBLE);
            }
            else{
                ivQueitTimeOn.setVisibility(View.INVISIBLE);
                ivQueitTimeOff.setVisibility(View.VISIBLE);
            }
        }
        catch (NullPointerException e){
        }
        try {
            if(page.isDays()){
                ivQueitTimeDaysOn.setVisibility(View.VISIBLE);
                ivQueitTimeDaysOff.setVisibility(View.INVISIBLE);
            }
            else{
                ivQueitTimeDaysOn.setVisibility(View.INVISIBLE);
                ivQueitTimeDaysOff.setVisibility(View.VISIBLE);
            }
        }
        catch (NullPointerException e){
        }
        try {
            if(getMainActivity().getSMS() != null || isMessage){
                ivQueitTimeMsgOn.setVisibility(View.VISIBLE);
                ivQueitTimeMsgOff.setVisibility(View.INVISIBLE);
            }
            else{
                ivQueitTimeMsgOn.setVisibility(View.INVISIBLE);
                ivQueitTimeMsgOff.setVisibility(View.VISIBLE);
            }
        }
        catch (NullPointerException e){
        }
    }
}

package com.shhutapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.shhutapp.MainActivity;
import com.shhutapp.OkCancelListener;
import com.shhutapp.R;

/**
 * Created by victor on 23.05.15.
 */
public class OkCancel extends BaseFragments {
    private OkCancelListener listener;
    private RelativeLayout rlOk;
    private RelativeLayout rlCancel;

    public OkCancel(){
        super();
        rView = null;
        act = null;
    }
    @SuppressLint("ValidFragment")
    public OkCancel(MainActivity act){
        super(act);
        rView = null;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState){
        rView = inf.inflate(R.layout.ok_cancel, container, false);
        return rView;
    }
    public void onViewCreated(View view, Bundle saved){
        super.onViewCreated(view, saved);
        rlOk = (RelativeLayout) rView.findViewById(R.id.rlOkCancelOK);
        rlCancel = (RelativeLayout) rView.findViewById(R.id.rlOkCancelCancel);
        rlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    listener.ok();
            }
        });
        rlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    listener.cancel();
            }
        });
    }
    public void setOnOkCancelListener(OkCancelListener list){
        listener =list;
    }
}

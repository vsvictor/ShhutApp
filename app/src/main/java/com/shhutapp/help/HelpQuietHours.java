package com.shhutapp.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.fragments.BaseFragments;

public class HelpQuietHours extends BaseFragments {
    private TextView textNext;
    HelpWhiteList hwl;
    public HelpQuietHours(){
        super();
        rView = null;
        act = null;
    }
    public HelpQuietHours(MainActivity act){
        super(act);
        rView = null;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState){
        rView = inf.inflate(R.layout.help_quiet_hours, container, false);
        return rView;
    }
    public void onViewCreated(View view, Bundle saved){
        super.onViewCreated(view, saved);
        textNext = (TextView)rView.findViewById(R.id.helpQuietHoursNext);
        textNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hwl = new HelpWhiteList();
                getMainActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main,hwl).commit();
            }
        });
    }
}

package com.shhutapp.help;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.fragments.BaseFragments;

public class HelpWhiteList extends BaseFragments {
    private TextView textNext;
    HelpMessage hm;
    public HelpWhiteList(){
        super();
        rView = null;
        act = null;
    }
    @SuppressLint("ValidFragment")
    public HelpWhiteList(MainActivity act){
        super(act);
        rView = null;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState){
        rView = inf.inflate(R.layout.help_white_list, container, false);
        return rView;
    }
    public void onViewCreated(View view, Bundle saved){
        super.onViewCreated(view, saved);
        textNext = (TextView)rView.findViewById(R.id.helpWhiteListNext);
        textNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hm = new HelpMessage();
                getMainActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main, hm).commit();
            }
        });
    }
}

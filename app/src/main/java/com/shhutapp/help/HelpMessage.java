package com.shhutapp.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.fragments.BaseFragments;

public class HelpMessage extends BaseFragments {
    private TextView textNext;
    HelpLocation hl;
    public HelpMessage(){
        super();
        rView = null;
        act = null;
    }
    public HelpMessage(MainActivity act){
        super(act);
        rView = null;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState){
        rView = inf.inflate(R.layout.help_message, container, false);
        return rView;
    }
    public void onViewCreated(View view, Bundle saved){
        super.onViewCreated(view, saved);
        textNext = (TextView)rView.findViewById(R.id.helpMessageNext);
        textNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hl = new HelpLocation();
                getMainActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main, hl).commit();
            }
        });
    }
}

package com.shhutapp.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.fragments.BaseFragments;

import static android.view.View.*;

public class Help extends BaseFragments {
    private TextView textNext;
    private RelativeLayout helpmain, congr, settime;
    HelpSetTime hst;
    public Help(){
        super();
        rView = null;
        act = null;
    }
    public Help(MainActivity act){
        super(act);
        rView = null;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState){
        rView = inf.inflate(R.layout.help_congratulation, container, false);
        return rView;
    }
    public void onViewCreated(View view, Bundle saved){
        super.onViewCreated(view, saved);
        textNext = (TextView)rView.findViewById(R.id.helpCongratNext);
       // congr = (RelativeLayout)rView.findViewById(R.id.layout_help_congrat);
       // settime = (RelativeLayout)rView.findViewById(R.id.layout_help_set_time);
        textNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hst = new HelpSetTime(getMainActivity());
                getMainActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main, hst).commit();
         //   congr.setVisibility(GONE);
         //   settime.setVisibility(VISIBLE);
            }
        });
    }
}

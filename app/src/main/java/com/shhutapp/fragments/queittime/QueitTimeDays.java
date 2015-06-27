package com.shhutapp.fragments.queittime;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.controls.OnTimeChanged;
import com.shhutapp.controls.TimeSeekBar;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.pages.QueitTimePage;

import org.w3c.dom.Text;

/**
 * Created by victor on 21.05.15.
 */
public class QueitTimeDays extends BaseFragments {
    private boolean isVisible;
    private QueitTimePage page;
    private RelativeLayout rlDaysBegin;
    private QueitTimeDaysListener listener;
    private boolean[] day;
    private RelativeLayout[] rlDay;
    private TextView[] tvDay;
    private ImageView[] ivCircle;
    private RelativeLayout rlBeg;
    public QueitTimeDays(){
        super(MainActivity.getMainActivity());
        day = new boolean[7];
        for (int i = 0;i<7;i++) day[i]=false;
        rlDay = new RelativeLayout[7];
        tvDay = new TextView[7];
        ivCircle = new ImageView[7];
    }
    public QueitTimeDays(MainActivity act){
        super(act);
        day = new boolean[7];
        for (int i = 0;i<7;i++) day[i]=false;
        rlDay = new RelativeLayout[7];
        tvDay = new TextView[7];
        ivCircle = new ImageView[7];
    }
    public QueitTimeDays(MainActivity act, QueitTimePage page){
        super(act);
        this.page = page;
        day = new boolean[7];
        for (int i = 0;i<7;i++) day[i]=false;
        rlDay = new RelativeLayout[7];
        tvDay = new TextView[7];
        ivCircle = new ImageView[7];
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            boolean[] qday = getArguments().getBooleanArray("days");
            for(int i = 0; i<7;i++) day[i] = qday[i];
        }catch (Exception e){
        }
    }
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.queittime_days, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        rlDaysBegin = (RelativeLayout) rView.findViewById(R.id.rlDaysBegin);
        rlDaysBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.unvisible();
            }
        });
        ivCircle[0] = (ImageView) rView.findViewById(R.id.ivCircle1);
        ivCircle[1] = (ImageView) rView.findViewById(R.id.ivCircle2);
        ivCircle[2] = (ImageView) rView.findViewById(R.id.ivCircle3);
        ivCircle[3] = (ImageView) rView.findViewById(R.id.ivCircle4);
        ivCircle[4] = (ImageView) rView.findViewById(R.id.ivCircle5);
        ivCircle[5] = (ImageView) rView.findViewById(R.id.ivCircle6);
        ivCircle[6] = (ImageView) rView.findViewById(R.id.ivCircle7);

        tvDay[0] = (TextView) rView.findViewById(R.id.tvDay1);
        tvDay[1] = (TextView) rView.findViewById(R.id.tvDay2);
        tvDay[2] = (TextView) rView.findViewById(R.id.tvDay3);
        tvDay[3] = (TextView) rView.findViewById(R.id.tvDay4);
        tvDay[4] = (TextView) rView.findViewById(R.id.tvDay5);
        tvDay[5] = (TextView) rView.findViewById(R.id.tvDay6);
        tvDay[6] = (TextView) rView.findViewById(R.id.tvDay7);

        rlBeg = (RelativeLayout) rView.findViewById(R.id.rlDay0);

        rlDay[0] = (RelativeLayout) rView.findViewById(R.id.rlDay1);
        rlDay[1] = (RelativeLayout) rView.findViewById(R.id.rlDay2);
        rlDay[2] = (RelativeLayout) rView.findViewById(R.id.rlDay3);
        rlDay[3] = (RelativeLayout) rView.findViewById(R.id.rlDay4);
        rlDay[4] = (RelativeLayout) rView.findViewById(R.id.rlDay5);
        rlDay[5] = (RelativeLayout) rView.findViewById(R.id.rlDay6);
        rlDay[6] = (RelativeLayout) rView.findViewById(R.id.rlDay7);

        for(int i = 0; i<7;i++) {
            final int ii = i;
            rlDay[ii].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    day[ii] = !day[ii];
                    reFillDays();
                }
            });
        }
        reFillDays();
    }
    @Override
    public void onResume(){
        super.onResume();
    }
    @Override
    public void onHiddenChanged(boolean hidden){
        super.onHiddenChanged(hidden);
    }

    public void setOnQueitTimeDaysListener(QueitTimeDaysListener list){
        listener = list;
    }
    private void reFillDays(){
        Drawable dr = null;
        if(day[0]){
            dr = getMainActivity().getResources().getDrawable(R.drawable.rect_days_white);
            rlDay[0].setBackground(dr);
            tvDay[0].setTextColor(getMainActivity().getResources().getColor(R.color.blue_action_bar));
            rlBeg.setBackgroundColor(getMainActivity().getResources().getColor(R.color.white));
        }
        else{
            dr = getMainActivity().getResources().getDrawable(R.drawable.rect_days_blue);
            rlDay[0].setBackground(dr);
            tvDay[0].setTextColor(getMainActivity().getResources().getColor(R.color.white));
            rlBeg.setBackgroundColor(getMainActivity().getResources().getColor(R.color.blue_action_bar));
        }
        if(day[6]){
            dr = getMainActivity().getResources().getDrawable(R.drawable.rect_days_white_last);
            rlDay[6].setBackground(dr);
            tvDay[6].setTextColor(getMainActivity().getResources().getColor(R.color.blue_action_bar));
        }
        else{
            dr = getMainActivity().getResources().getDrawable(R.drawable.rect_days_blue_last);
            rlDay[6].setBackground(dr);
            tvDay[6].setTextColor(getMainActivity().getResources().getColor(R.color.white));
        }
        for(int i = 1; i<6; i++){
            if(!day[i]){
                ivCircle[i].setVisibility(View.INVISIBLE);
                tvDay[i].setTextColor(getMainActivity().getResources().getColor(R.color.white));
            }
        }
        for(int i = 1; i<6; i++){
            if(day[i]){
                if(day[i-1] && day[i+1]) dr = getMainActivity().getResources().getDrawable(R.drawable.rect_days_white);
                else if(day[i-1] && !day[i+1]) dr = getMainActivity().getResources().getDrawable(R.drawable.rect_days_white_last);
                else if(!day[i-1] && day[i+1]) dr = getMainActivity().getResources().getDrawable(R.drawable.rect_days_white_first);
                else dr = getMainActivity().getResources().getDrawable(R.drawable.circle_white);
                ivCircle[i].setVisibility(View.VISIBLE);
                ivCircle[i].setImageDrawable(dr);
                tvDay[i].setTextColor(getMainActivity().getResources().getColor(R.color.blue_action_bar));
            }
        }
        listener.publish(day);
    }
    /*public void free(){
        for(int i = 0; i<7; i++) day[i] = false;
    }*/
}

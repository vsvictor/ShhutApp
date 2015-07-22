package com.shhutapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.controls.MainTimeSeekBar;
import com.shhutapp.controls.OnTimeChanged;
import com.shhutapp.controls.SecondTimeSeekBar;
import com.shhutapp.pages.MainPage;
import com.shhutapp.utils.Convertor;
import com.shhutapp.utils.DateTimeOperator;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

public class Scale extends BaseFragments {
    private MainTimeSeekBar msbBegin;
    private RelativeLayout rlTimer;
    private TextView tvHoursValue;
    private TextView tvMinutesValue;
    private TextView tvBefore;
    private TextView tvHours;
    private TextView tvMinutes;
    private MainPage page;
    private boolean showed;
    private SecondTimeSeekBar msbSec;
    private int minInFirst;
    private int minInsec;
    public Scale(){
        super();
        rView = null;
        act = null;
    }
    @SuppressLint("ValidFragment")
    public Scale(MainActivity act){
        super(act);
        rView = null;
    }
    @SuppressLint("ValidFragment")
    public Scale(MainActivity act, MainPage page){
        super(act);
        rView = null;
        this.page = page;

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState){
        rView = inf.inflate(R.layout.scale_main, container, false);
        return rView;
    }
    public void onViewCreated(View view, Bundle saved){
        super.onViewCreated(view, saved);
        rlTimer = (RelativeLayout) rView.findViewById(R.id.rlTimer);
        tvHours = (TextView) rView.findViewById(R.id.tvHours);
        tvHours.setText(getMainActivity().getResources().getString(R.string.h));
        tvHoursValue = (TextView) rView.findViewById(R.id.tvHoursValue);
        tvMinutes = (TextView) rView.findViewById(R.id.tvMinutes);
        tvMinutes.setText(getMainActivity().getResources().getString(R.string.min));
        tvMinutesValue = (TextView) rView.findViewById(R.id.tvMinutesValue);
        tvBefore = (TextView) rView.findViewById(R.id.tvBefore);
        msbBegin = (MainTimeSeekBar) rView.findViewById(R.id.msbBegin);
        msbBegin.setThumb(R.drawable.thumb_full);
        msbBegin.setOnChangeListener(new OnTimeChanged() {
            @Override
            public void onTimeChanged(int minutes) {
                minInFirst = minutes;
                if (minutes > 0) {
                    rlTimer.setVisibility(View.VISIBLE);
                    if (!showed) {
                        /*getMainActivity().getSupportFragmentManager().beginTransaction().
                                show(page.getOkCancel()).
                                commit();*/
                        getMainActivity().setDream(true);
                        showed = true;
                    }
                    Date d = Calendar.getInstance().getTime();
                    d.setTime(d.getTime() + (minutes * 60 * 1000));
                    tvBefore.setText(DateTimeOperator.dateToTimeString(d));
                } else {
                    rlTimer.setVisibility(View.INVISIBLE);
                    getMainActivity().getSupportFragmentManager().beginTransaction().
                            hide(page.getOkCancel()).
                            commit();
                    showed = false;
                    getMainActivity().setDream(false);
                }
                int h = minutes / 60;
                int m = minutes - (h * 60);
                tvHoursValue.setText(String.valueOf(h));
                String sText = DateTimeOperator.minutesToString(minutes,
                        getMainActivity().getResources().getString(R.string.h),
                        getMainActivity().getResources().getString(R.string.min));
            }
        });
        msbSec = (SecondTimeSeekBar) rView.findViewById(R.id.msbSeconds);
        msbSec.setThumb(R.drawable.thumb_center, (int)Convertor.convertDpToPixel(12, getMainActivity()), (int)Convertor.convertDpToPixel(12, getMainActivity()));
        msbSec.setBeginTrack(R.drawable.thumb_left, (int) Convertor.convertDpToPixel(8, getMainActivity()), (int) Convertor.convertDpToPixel(18, getMainActivity()));
        msbSec.setEndTrack(R.drawable.thumb_right, (int) Convertor.convertDpToPixel(8, getMainActivity()), (int) Convertor.convertDpToPixel(18, getMainActivity()));

        msbSec.setScroll(false);
        msbSec.setOnChangeListener(new OnTimeChanged() {
            @Override
            public void onTimeChanged(int minutes) {
                int h = minInFirst / 60;
                int t = (h * 60) + minutes;
                msbBegin.setTime(t);
                msbBegin.invalidate();
                tvMinutesValue.setText(String.valueOf(minutes));
            }
        });
        msbBegin.setSecond(msbSec);
        msbSec.setMain(msbBegin);
    }
    public void setTime(int minutes){
        msbBegin.setTime(minutes);
    }
}

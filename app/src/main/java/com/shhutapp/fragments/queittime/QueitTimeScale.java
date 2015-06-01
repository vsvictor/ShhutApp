package com.shhutapp.fragments.queittime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.controls.MainTimeSeekBar;
import com.shhutapp.controls.OnTimeChanged;
import com.shhutapp.controls.SecondTimeSeekBar;
import com.shhutapp.controls.TimeSeekBar;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.pages.QueitTimePage;
import com.shhutapp.utils.Convertor;

/**
 * Created by victor on 20.05.15.
 */
public class QueitTimeScale extends BaseFragments {
    private QueitTimePage page;
    private MainTimeSeekBar sbBegin;
    private MainTimeSeekBar sbEnd;
    private SecondTimeSeekBar sbBegcinSec;
    private SecondTimeSeekBar sbEndSec;

    private String time1;
    private String time2;
    private int hours1;
    private int hours2;
    private int min1;
    private int min2;

    public QueitTimeScale(){
        super(MainActivity.getMainActivity());
    }
    public QueitTimeScale(MainActivity act){
        super(act);
    }
    public QueitTimeScale(MainActivity act, QueitTimePage page){
        super(act);
        this.page = page;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        time1 = "00:00";
        time2 = "00:00";
    }

    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.scale_queittime, container, false);
        return rView;
    }

    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        sbBegin = (MainTimeSeekBar) rView.findViewById(R.id.sbBegin);
        sbBegin.setThumb(R.drawable.thumb_full);
        sbBegin.setOnChangeListener(new OnTimeChanged() {
            @Override
            public void onTimeChanged(int minutes) {
                hours1 = minutes / 60;
                String sH;
                if (hours1 < 10) sH = "0" + String.valueOf(hours1);
                else sH = String.valueOf(hours1);
                String sM;
                if (min1 < 10) sM = "0" + String.valueOf(min1);
                else sM = String.valueOf(min1);
                time1 = sH + ":" + sM;// + " : " + String.valueOf(minutes);
                getMainActivity().getHeader().setTextHeader(time1 + " - " + time2);
                getMainActivity().getHeader().setVisibleOk(true);
            }
        });
        sbEnd = (MainTimeSeekBar) rView.findViewById(R.id.sbEnd);
        sbEnd.setThumb(R.drawable.thumb_full);
        sbEnd.setOnChangeListener(new OnTimeChanged() {
            @Override
            public void onTimeChanged(int minutes) {
                hours2 = minutes / 60;
                String sH;
                if (hours2 < 10) sH = "0" + String.valueOf(hours2);
                else sH = String.valueOf(hours2);
                String sM;
                if (min2 < 10) sM = "0" + String.valueOf(min2);
                else sM = String.valueOf(min2);
                time2 = sH + ":" + sM;// + " : " + String.valueOf(minutes);
                getMainActivity().getHeader().setTextHeader(time1 + " - " + time2);
            }
        });
        sbBegcinSec = (SecondTimeSeekBar) rView.findViewById(R.id.sbBeginSec);
        sbBegcinSec.setThumb(R.drawable.thumb_center, (int) Convertor.convertDpToPixel(12, getMainActivity()), (int) Convertor.convertDpToPixel(12, getMainActivity()));
        sbBegcinSec.setBeginTrack(R.drawable.thumb_left, (int) Convertor.convertDpToPixel(8, getMainActivity()), (int) Convertor.convertDpToPixel(18, getMainActivity()));
        sbBegcinSec.setEndTrack(R.drawable.thumb_right, (int) Convertor.convertDpToPixel(8, getMainActivity()), (int) Convertor.convertDpToPixel(18, getMainActivity()));
        sbBegcinSec.setScroll(false);
        sbBegcinSec.setOnChangeListener(new OnTimeChanged() {
            @Override
            public void onTimeChanged(int minutes) {
                min1 = minutes;
                String sH;
                if (hours1 < 10) sH = "0" + String.valueOf(hours1);
                else sH = String.valueOf(hours1);
                String sM;
                if (min1 < 10) sM = "0" + String.valueOf(min1);
                else sM = String.valueOf(min1);
                time1 = sH + ":" + sM;// + " : " + String.valueOf(minutes);
                getMainActivity().getHeader().setTextHeader(time1 + " - " + time2);
                getMainActivity().getHeader().setVisibleOk(true);
            }
        });
        sbEndSec = (SecondTimeSeekBar) rView.findViewById(R.id.sbEndSec);
        sbEndSec.setThumb(R.drawable.thumb_center, (int) Convertor.convertDpToPixel(12, getMainActivity()), (int) Convertor.convertDpToPixel(12, getMainActivity()));
        sbEndSec.setBeginTrack(R.drawable.thumb_left, (int) Convertor.convertDpToPixel(8, getMainActivity()), (int) Convertor.convertDpToPixel(18, getMainActivity()));
        sbEndSec.setEndTrack(R.drawable.thumb_right, (int) Convertor.convertDpToPixel(8, getMainActivity()), (int) Convertor.convertDpToPixel(18, getMainActivity()));
        sbEndSec.setScroll(false);
        sbEndSec.setOnChangeListener(new OnTimeChanged() {
            @Override
            public void onTimeChanged(int minutes) {
                min2 = minutes;
                String sH;
                if (hours2 < 10) sH = "0" + String.valueOf(hours2);
                else sH = String.valueOf(hours2);
                String sM;
                if (min2 < 10) sM = "0" + String.valueOf(min2);
                else sM = String.valueOf(min2);
                time2 = sH + ":" + sM;// + " : " + String.valueOf(minutes);
                getMainActivity().getHeader().setTextHeader(time1 + " - " + time2);
            }
        });
        sbBegin.setSecond(sbBegcinSec);
        sbBegcinSec.setMain(sbBegin);

        sbEnd.setSecond(sbEndSec);
        sbEndSec.setMain(sbEnd);
    }
    @Override
    public void onResume(){
        super.onResume();
    }
    public void free(){
        sbBegin.free();
        sbEnd.free();
    }
}

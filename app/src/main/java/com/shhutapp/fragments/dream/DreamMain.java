package com.shhutapp.fragments.dream;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.controls.AlphaTear;
import com.shhutapp.controls.TransportedLayoutCircle;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.pages.BasePage;
import com.shhutapp.utils.Convertor;
import com.shhutapp.utils.DateTimeOperator;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by victor on 24.05.15.
 */
public class DreamMain extends BaseFragments {
    private BasePage page;
    private ImageView ivBackground;
    private BitmapDrawable dr;
    private RelativeLayout rlDreamFilter;
    private Date begDate;
    private RelativeLayout rlBack;
    private OnExitListener listener;
    private ImageView ivDreamWL;
    private ImageView ivDreamMsg;
    private TextView tvDreamBeginValue;
    private TextView tvDreamTimerHourseValue;
    private TextView tvDreamTimerMinValue;
    private TextView tvDreamCallCount;
    private TextView tvDreamAppCount;
    private TextView tvDreamMsgCount;
    private Timer timer;
    private Date beg;
    private Date curr;
    private RelativeLayout rlDreamBackground;
    private TransportedLayoutCircle ivDreamBack;
    private TextView tvPressToExit;
    private TextView tvDreamBeginText;
    private AlphaTear tear;
    public DreamMain(){
        super(MainActivity.getMainActivity());
    }
    @SuppressLint("ValidFragment")
    public DreamMain(MainActivity act){
        super(act);
    }
    @SuppressLint("ValidFragment")
    public DreamMain(MainActivity act, BasePage page){
        super(act);
        this.page = page;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.dream_main, container, false);
        return rView;
    }
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);

        //tear = (AlphaTear) rView.findViewById(R.id.svDreamBack);
        //tear.setZOrderOnTop(true);

        getMainActivity().getHeader().setHeight(0);
        beg = Calendar.getInstance().getTime();

        int w = (int) Convertor.convertDpToPixel(360, getMainActivity());
        int h = (int) Convertor.convertDpToPixel(640, getMainActivity());
        BitmapFactory.Options opt = new BitmapFactory.Options();
        //opt.outHeight = h;
        //opt.outWidth = w;
        opt.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeResource(getMainActivity().getResources(), R.drawable.dream_background_night, opt);

        //Bitmap bitmap = Bitmap.createScaledBitmap(b,w,h,false);
        Drawable dr = new BitmapDrawable(bitmap);
        rlDreamBackground = (RelativeLayout) rView.findViewById(R.id.rlDreamBackground);
        rlDreamBackground.setBackground(dr);

        TransportedLayoutCircle rw = (TransportedLayoutCircle) rView.findViewById(R.id.ivDreamBack);
        rw.setBitmap(R.drawable.dream_background);
        rw.invalidate();

        ivDreamWL = (ImageView) rView.findViewById(R.id.rlDreamWL);
        if(getMainActivity().getWhiteList() != null){
            ivDreamWL.setVisibility(View.VISIBLE);
        }
        else{
            ivDreamWL.setVisibility(View.INVISIBLE);
        }
        ivDreamMsg = (ImageView) rView.findViewById(R.id.rlDreamMsg);
        if(getMainActivity().getSMS() != null){
            ivDreamMsg.setVisibility(View.VISIBLE);
        }
        else{
            ivDreamMsg.setVisibility(View.INVISIBLE);
        }
        tvPressToExit = (TextView) rView.findViewById(R.id.tvPressToExit);
        tvPressToExit.setTypeface(Typeface.createFromAsset(getMainActivity().getAssets(), "fonts/Roboto-Medium.ttf"));
        tvDreamBeginText = (TextView) rView.findViewById(R.id.tvDreamBeginText);
        tvDreamBeginText.setTypeface(Typeface.createFromAsset(getMainActivity().getAssets(), "fonts/Roboto-Light.ttf"));
        tvDreamBeginValue = (TextView) rView.findViewById(R.id.tvDreamBeginValue);
        tvDreamBeginValue.setTypeface(Typeface.createFromAsset(getMainActivity().getAssets(), "fonts/Roboto-Light.ttf"));
        tvDreamBeginValue.setText(DateTimeOperator.dateToTimeString12(beg));
        tvDreamBeginValue.setTypeface(Typeface.createFromAsset(getMainActivity().getAssets(), "fonts/Roboto-Light.ttf"));
        tvDreamTimerHourseValue = (TextView) rView.findViewById(R.id.tvDreamTimerHoursValue);
        tvDreamTimerHourseValue.setTypeface(Typeface.createFromAsset(getMainActivity().getAssets(), "fonts/Roboto-Light.ttf"));
        tvDreamTimerMinValue = (TextView) rView.findViewById(R.id.tvDreamTimerMinValue);
        tvDreamTimerMinValue.setTypeface(Typeface.createFromAsset(getMainActivity().getAssets(), "fonts/Roboto-Light.ttf"));

        tvDreamCallCount = (TextView) rView.findViewById(R.id.tvDreamCallCount);
        tvDreamCallCount.setTypeface(Typeface.createFromAsset(getMainActivity().getAssets(), "fonts/Roboto-Medium.ttf"));
        tvDreamAppCount = (TextView) rView.findViewById(R.id.tvDreamAppCount);
        tvDreamAppCount.setTypeface(Typeface.createFromAsset(getMainActivity().getAssets(), "fonts/Roboto-Medium.ttf"));
        tvDreamMsgCount = (TextView) rView.findViewById(R.id.tvDreamMsgCount);
        tvDreamMsgCount.setTypeface(Typeface.createFromAsset(getMainActivity().getAssets(), "fonts/Roboto-Medium.ttf"));
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }}, 1000, 1000);
    }
    @Override
    public void onResume(){
        super.onResume();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
    }
    Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            String sHours, sMinutes;
            TimeZone tz = TimeZone.getDefault();
            curr = Calendar.getInstance().getTime();
            Date current = new Date(curr.getTime()-beg.getTime()-tz.getRawOffset());
            int h = current.getHours();
            int m = current.getMinutes();
            if(h<10) sHours = "0"+String.valueOf(h);
            else sHours = String.valueOf(h);
            if(m<10) sMinutes = "0"+String.valueOf(m);
            else sMinutes = String.valueOf(m);
            tvDreamTimerHourseValue.setText(sHours);
            tvDreamTimerMinValue.setText(sMinutes);

        }
    };
    public void updateData(int call, int app, int msg){
        tvDreamCallCount.setText(String.valueOf(call));
        tvDreamAppCount.setText(String.valueOf(app));
        tvDreamMsgCount.setText(String.valueOf(msg));
    }
}

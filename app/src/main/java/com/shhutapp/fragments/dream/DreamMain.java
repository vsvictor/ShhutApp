package com.shhutapp.fragments.dream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.data.SMSCard;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.fragments.OnTextEntered;
import com.shhutapp.pages.BasePage;
import com.shhutapp.utils.Convertor;
import com.shhutapp.utils.DateTimeOperator;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

    public DreamMain(){
        super(MainActivity.getMainActivity());
    }
    public DreamMain(MainActivity act){
        super(act);
    }
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
        getMainActivity().getHeader().setHeight(0);
        int w = (int)Convertor.convertDpToPixel(360, act);
        int h = (int)Convertor.convertDpToPixel(640, act);
        Bitmap bm = BitmapFactory.decodeResource(getMainActivity().getResources(), R.drawable.dream_background);
        Bitmap bs = Bitmap.createScaledBitmap(bm, w, h, false);
        beg = Calendar.getInstance().getTime();
        dr = new BitmapDrawable(bs);
        rlDreamFilter = (RelativeLayout) rView.findViewById(R.id.rlDreamFilter);
        rlDreamFilter.setBackground(dr);
        rlDreamFilter.setAlpha(0.25f);
        ivDreamWL = (ImageView) rView.findViewById(R.id.rlDreamWL);
        ivDreamMsg = (ImageView) rView.findViewById(R.id.rlDreamMsg);
        tvDreamBeginValue = (TextView) rView.findViewById(R.id.tvDreamBeginValue);
        tvDreamBeginValue.setText(DateTimeOperator.dateToTimeString(beg));
        tvDreamTimerHourseValue = (TextView) rView.findViewById(R.id.tvDreamTimerHoursValue);
        tvDreamTimerMinValue = (TextView) rView.findViewById(R.id.tvDreamTimerMinValue);
        tvDreamCallCount = (TextView) rView.findViewById(R.id.tvDreamCallCount);
        tvDreamAppCount = (TextView) rView.findViewById(R.id.tvDreamAppCount);
        tvDreamMsgCount = (TextView) rView.findViewById(R.id.tvDreamMsgCount);
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

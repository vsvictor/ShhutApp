package com.shhutapp.controls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.shhutapp.utils.Convertor;

import com.shhutapp.R;

/**
 * Created by victor on 20.05.15.
 */
public class MainTimeSeekBar extends ImageView{
    private Context context;
    private OnThumbClick onThumbClick;
    private int dis = Color.argb(128,255,255,255);
    private int en = Color.argb(255,255,255,255);
    private Bitmap thumb;
    private GestureDetector gestureDetector;
    private int minus;
    private float offset;
    private boolean isUp;
    private OnTimeChanged onChange;
    private boolean isScroll;
    private SecondTimeSeekBar second;
    private MotionEvent ev;
    private boolean isStop;
    private boolean lock;

    public MainTimeSeekBar(Context context) {
        super(context);
        this.context = context;
        init();
    }
    public MainTimeSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
    public MainTimeSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }
    @SuppressLint("NewApi")
    public MainTimeSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init();
    }
    private void init(){
        offset = 0;
        minus = 0;
        isUp = true;
        isScroll = true;
        isStop = false;
        lock(true);
        gestureDetector = new GestureDetector(new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
            @Override
            public void onShowPress(MotionEvent e) {
            }
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                offset = e2.getX();
                //Log.i("Offset", ""+offset+" e1.getX():"+e1.getX()+" e2.getX():"+e2.getX()+" distanseX:"+distanceX);
                return true;
            }
            @Override
            public void onLongPress(MotionEvent e) {
                isUp = true;
                setVisible(false);
                MotionEvent toSec = MotionEvent.obtain(ev.getDownTime(), ev.getEventTime(),
                        MotionEvent.ACTION_SCROLL,ev.getX(), ev.getY(), 0);

                isStop = true;
                getSecond().setFocusableInTouchMode(true);
                getSecond().requestFocus();
                getSecond().onTouchEvent(toSec);
            }
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(isStop) return getSecond().onTouchEvent(event);
        ev = event;
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:{
                isUp = true;
                break;
            }
            case MotionEvent.ACTION_DOWN:{
                if(event.getX()>=getMeasuredWidth()/2) {
                    isUp = false;
                }
                break;
            }
        }
        if(offset == 0) isUp = false;
        gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas){
        super.onDraw(canvas);
        float width = getLayoutParams().width-(thumb.getWidth()/2);
        float height = getLayoutParams().height;
        float line = 2*(height/4);
        float fourty = height/4;
        //int step = Math.round(width/diriv);
        Paint p = new Paint();
        Paint e = new Paint();
        p.setColor(dis);
        e.setColor(en);

        canvas.drawRect(0, line - 1, width, line + 1, p);
        canvas.drawRect(0, line - 1, offset, line + 1, e);
/*
        if(isScroll) {
            int ii = offsetToTime(0,Math.round(offset));
            if (!isUp && ii>=480) minus -= 1;
            else if (!isUp && offset == 0 && minus < 0) minus += 1;
        }
*/
        boolean isSmall = true;
        int beg = 1;
        if(minus<0) beg=minus;
        if(thumb != null){
            if(offset>width) offset=width;
            if(offset<0) offset = 0;
            canvas.drawBitmap(thumb, offset, (height/2)-fourty, e);
        }
        int offtime = offsetToTime(minus, Math.round(offset));
        Log.i("onDraw",""+offtime+" offset:"+offset+" minus:"+minus);
        if(onChange != null) onChange.onTimeChanged(offtime);
        invalidate();
    }
    public void setOnChangeListener(OnTimeChanged list){
        onChange = list;
    }
/*
    public int offsetTo(int minus, int offset){
        float res = 0;
        float hour;
        //hour = Math.round((getMeasuredWidth()-(thumb.getWidth()/2))/8);
        hour = Math.round((getLayoutParams().width-(thumb.getWidth()/2))/8);
        float min = hour/60;
        res = Math.round((Math.abs(minus)+Math.abs(offset))*min);
        if(res > 24*60) res = 24*60;
        Log.i("Time",""+res+" offset:"+offset+" minus:"+minus+" onemin:"+min);
        return Math.round(res);
    }
*/
    public int offsetToTime(int minus, int offset) {
        float arg = offset;
        float res = 0;
        float add = 0;
        float minutes = 0;
        float step = (this.getLayoutParams().width-thumb.getWidth()/2)/6;
        if(offset>=0 && offset<=step){
            minutes = 60;
            res = (arg*minutes/step);
            Log.i("settime", "1");
        }
        else if(offset>step && offset<=(2*step)){
            arg = arg-step;
            minutes = 120;
            add = 60;
            res = (arg*minutes/step)+add;
            Log.i("settime", "2");
        }
        else if(offset>2*step && offset<=3*step){
            arg = arg-2*step;
            add = 180;
            minutes = 180;
            res = (arg*minutes/step)+add;
            Log.i("settime", "3");
        }
        else if(offset>3*step && offset<=4*step){
            arg = arg-3*step;
            add = 360;
            minutes = 240;
            res = (arg*minutes/step)+add;
            Log.i("settime", "4");
        }
        else if(offset>4*step && offset<=5*step){
            arg = arg-4*step;
            add = 600;
            minutes = 360;
            res = (arg*minutes/step)+add;
            Log.i("settime", "6");
        }
        else if(offset>5*step && offset<=6*step){
            arg = arg-5*step;
            add = 960;
            minutes = 480;
            res = (arg*minutes/step)+add;
            Log.i("settime", "8");
        }
        else if(offset>6*step){
            res=1440;
        }
        Log.i("settime"," Section:"+res+" minutes:"+minutes+" res:"+res+" arg:"+arg+" add:"+add+" step:"+step);
        return Math.round(res);
    }
    public void setThumb(int res){
        Bitmap tmp = BitmapFactory.decodeResource(context.getResources(), res);
        int size = Math.round(getLayoutParams().height/2);
        thumb = Bitmap.createScaledBitmap(tmp, size, size, false);
        invalidate();
    }
    public void free(){
        minus = 0;
        offset = 0;
    }
/*    public void setTime(int minutes){
        float ff;
        //Log.i("setTime:", "minutes:"+minutes);
        float f = getLayoutParams().width-(thumb.getWidth()/2);
        float onemin = f/480f;
        //f = minutes*onemin;
        if((minutes*onemin)>(f-1)){
            Log.i("setTime minus"," minutes:"+minutes);
            float full = (int)(minutes/480f);
            Log.i("setTime minus"," full:"+full);
            float min = minutes-(full*480f);
            Log.i("setTime minus"," min:"+min);
            minus = -(int)Math.abs(full*480*onemin);//-Math.round(rr*480*onemin);
            offset = (int)Math.abs(onemin * min);
            int qwe = 1;
            Log.i("setTime minus", "offset:"+offset+" minutes:"+minutes+" onemin:"+onemin+" hours:"+minutes/60+" full:"+full);
        }
        else {
            ff = (float)(onemin*minutes);
            minus = 0;
            offset = ff;
            Log.i("setTime", "offset:"+offset+" minutes:"+minutes+" onemin:"+onemin+" hours:"+minutes/60);
        }
        lock(false);
        invalidate();
        lock(true);
    }
*/
    public void setTime(int minutes){
        float step = (this.getLayoutParams().width-thumb.getWidth()/2)/6;
        float add = 0;
        float mins = 0;
        if(minutes<=60){
            offset=step*minutes/60;
        }
        else if(minutes>60 && minutes<=180){
            offset = step;
            add = 60;
            mins = 120;
            offset +=(step*(minutes-add)/mins);
        }
        else if(minutes>180 && minutes<=360){
            offset = 2*step;
            add = 180;
            mins = 180;
            offset +=(step*(minutes-add)/mins);
        }
        else if(minutes>360 && minutes<=600){
            offset = 3*step;
            add = 360;
            mins = 240;
            offset +=(step*(minutes-add)/mins);
        }
        else if(minutes>600 && minutes<=960){
            offset = 4*step;
            add = 600;
            mins = 360;
            offset +=(step*(minutes-add)/mins);
        }
        else if(minutes>960 && minutes<1440){
            offset = 5*step;
            add = 960;
            mins = 480;
            offset +=(step*(minutes-add)/mins);
        }
        else if(minutes>1440){
            offset = (this.getLayoutParams().width-thumb.getWidth()/2);
        }

    }
    public void setScroll(boolean b){
        isScroll = b;
    }
    public void setVisible(boolean b){
        isStop = false;
        this.setVisibility(b ? VISIBLE : INVISIBLE);
        if(second != null){
            this.second.setVisibility(!b ? VISIBLE : INVISIBLE);
        }
    }
    public void setSecond(SecondTimeSeekBar second){
        this.second = second;
    }
    public SecondTimeSeekBar getSecond(){
        return  this.second;
    }
    public MainTimeSeekBar getMain(){
        return  this;
    }
    public void setStop(boolean b){
        isStop = b;
    }
    public boolean lock(boolean b){ this.lock = b; return this.lock;}
    public boolean lock(){return this.lock;}
}

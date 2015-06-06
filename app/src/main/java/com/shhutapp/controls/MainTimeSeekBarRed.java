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

import com.shhutapp.MainActivity;
import com.shhutapp.utils.Convertor;

import com.shhutapp.R;

/**
 * Created by victor on 20.05.15.
 */
public class MainTimeSeekBarRed extends ImageView{
    private Context context;
    private OnThumbClick onThumbClick;
    private int dis = MainActivity.getMainActivity().getResources().getColor(R.color.geoscale_red_disable);
    private int en = MainActivity.getMainActivity().getResources().getColor(R.color.geoscale_red_enable);
    private Bitmap thumb;
    private GestureDetector gestureDetector;
    private int minus;
    private float offset;
    private boolean isUp;
    private OnTimeChanged onChange;
    private int tapCounter;
    private int diriv;
    private boolean isScroll;
    private SecondTimeSeekBarRed second;
    private MotionEvent ev;
    private boolean isStop;

    public MainTimeSeekBarRed(Context context) {
        super(context);
        this.context = context;
        init();
    }
    public MainTimeSeekBarRed(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
    public MainTimeSeekBarRed(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }
    @SuppressLint("NewApi")
    public MainTimeSeekBarRed(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init();
    }
    private void init(){
        offset = 0;
        minus = 0;
        isUp = true;
        tapCounter = 1;
        diriv = 6;
        isScroll = true;
        isStop = false;
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
                return true;
            }
            @Override
            public void onLongPress(MotionEvent e) {
                isUp = true;
                setVisible(false);
                MotionEvent toSec = MotionEvent.obtain(ev.getDownTime(), ev.getEventTime(), MotionEvent.ACTION_SCROLL,
                        ev.getX(), ev.getY(), 0);
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
        if(!isEnabled()) return true;
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
        float width = getLayoutParams().width;
        float height = getLayoutParams().height;
        float line = 2*(height/4);
        float fourty = height/4;
        int step = Math.round(width/diriv);
        Paint p = new Paint();
        Paint e = new Paint();

        if(isEnabled()){
            dis = MainActivity.getMainActivity().getResources().getColor(R.color.geoscale_red_disable);
            en = MainActivity.getMainActivity().getResources().getColor(R.color.geoscale_red_enable);
        }
        else{
            dis = Color.LTGRAY;
            en = Color.LTGRAY;
        }
        p.setColor(dis);
        e.setColor(en);

        canvas.drawRect(0, line - 1, width, line + 1, p);
        canvas.drawRect(0, line - 1, offset, line + 1, e);

        if(isScroll) {
            if (!isUp && offset > width / 2) minus -= 2;
            else if (!isUp && offset == 0 && minus < 0) minus += 2;
        }
        boolean isSmall = true;
        int beg = 1;
        if(minus<0) beg=minus;
        for(int i = beg;i<width;i++){
            if(i%(step*tapCounter)==0){
                if(isSmall) canvas.drawRect(i-2,line-fourty/2,i+2,line+fourty/2,p);
                else canvas.drawRect(i-2,line-fourty,i+2,line+fourty,p);
                isSmall = !isSmall;
            }
        }
        if(thumb != null){
            if(offset>width) offset=width;
            if(offset<0) offset = 0;
            if(isEnabled()) {
                canvas.drawBitmap(thumb, offset, (height / 2) - fourty, p);
            }
            else{
                Bitmap b = BitmapFactory.decodeResource(context.getResources(), R.drawable.thumb_full_gray);
                Bitmap thumb_gray = Bitmap.createScaledBitmap(b, (int)Convertor.convertDpToPixel(18,context), (int)Convertor.convertDpToPixel(18,context), false);
                canvas.drawBitmap(thumb_gray, offset, (height / 2) - fourty, p);
            }
        }
        invalidate();
        if(onChange != null) onChange.onTimeChanged(offsetToTime(minus, Math.round(offset)));
    }
    public void setOnChangeListener(OnTimeChanged list){
        onChange = list;
    }
    public int offsetToTime(int minus, int offset){
        float res = 0;
        float one = getMeasuredWidth()/180;
        res = Math.round((Math.abs(minus)+Math.abs(offset))*180/getMeasuredWidth());
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
    public void setTime(int minutes){
        float f = getMeasuredWidth();
        float onemin = f/(float)180;
        if((minutes*onemin)>getMeasuredWidth()){
            int full = minutes/60;
            int min = minutes-(full*60);
            minus = -Math.round(full*60*onemin);
            offset = min*onemin;
        }
        else {
            offset = ((float)(minutes)*onemin);
        }
        invalidate();
    }
    public void setDiriv(int dir){
        diriv = dir;
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
    public void setSecond(SecondTimeSeekBarRed second){
        this.second = second;
    }
    public SecondTimeSeekBarRed getSecond(){
        return  this.second;
    }
    public MainTimeSeekBarRed getMain(){
        return  this;
    }
    public void setStop(boolean b){
        isStop = b;
    }

}

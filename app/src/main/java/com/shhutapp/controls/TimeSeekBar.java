package com.shhutapp.controls;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.shhutapp.R;

/**
 * Created by victor on 20.05.15.
 */
public class TimeSeekBar extends ImageView{
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
    private int tapCounter;
    public TimeSeekBar(Context context) {
        super(context);
        this.context = context;
        init();
    }
    public TimeSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
    public TimeSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }
    @SuppressLint("NewApi")
    public TimeSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init();
    }
    private void init(){
        offset = 0;
        minus = 0;
        isUp = true;
        tapCounter = 1;
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
                isUp = true;
                tapCounter++;
                Log.i("Gesture", "onSingleTapUp:"+String.valueOf(offset));
                return true;
            }
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                offset = e2.getX();
                return true;
            }
            @Override
            public void onLongPress(MotionEvent e) {
                offset = e.getX();
                if(offset ==0){isUp=false;return;}
                if(e.getX()>=getWidth()/2)isUp = false;
                else isUp = true;
            }
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //offset = e2.getX();
                //Log.i("Gesture", "onFling:"+String.valueOf(offset));
                return true;
            }
        });
    }
    @Override
    protected synchronized void onDraw(Canvas canvas){
        super.onDraw(canvas);
        float width = getLayoutParams().width;
        float height = getLayoutParams().height;
        //float line = 3*(height/4);
        float line = 2*(height/4);
        float fourty = height/4;
        int step = Math.round(width/6);
        Paint p = new Paint();
        Paint e = new Paint();
        p.setColor(dis);
        e.setColor(en);
        //canvas.drawRect(width / 2 - 1, 0, width / 2 + 1, fourty, p);
        canvas.drawRect(0, line - 1, width, line + 1, p);
        canvas.drawRect(0, line - 1, offset, line + 1, e);

        if(!isUp && offset>width/2) minus-=2;
/*        else if(!isUp && offset>=((width/2)+step)) minus-=4;
        else if(!isUp && offset>=((width/2)+(2*step))) minus-=8;
        else if(!isUp && offset>=((width/2)+(2*step))) minus-=16;*/
        else if(!isUp && offset == 0 && minus<0) minus+=2;

        //Log.i("Minus", String.valueOf(minus)+", isUp:"+String.valueOf(isUp)+", offset:"+String.valueOf(offset));
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
            canvas.drawBitmap(thumb, offset, (height/2)-(height/4), p);
        }
        invalidate();
        if(onChange != null) onChange.onTimeChanged(offsetToTime(minus, Math.round(offset)));
    }
    public void setOnChangeListener(OnTimeChanged list){
        onChange = list;
    }
    private int offsetToTime(int minus, int offset){
        int res = 0;
        int step = getWidth()/6;
        int full = (Math.abs(minus)+Math.abs(offset)+thumb.getWidth()/2)/(step*tapCounter);
        int over = (Math.abs(minus)+Math.abs(offset)+thumb.getWidth()/2)-full*(step*tapCounter);
        int ost = over*30/(step*tapCounter);
        res = (full*30)+ost;
        return res;
    }
    public void setThumb(int res){
        Bitmap tmp = BitmapFactory.decodeResource(context.getResources(), res);
        int size = Math.round(getLayoutParams().height/2);
        thumb = Bitmap.createScaledBitmap(tmp, size, size, false);
        invalidate();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:{
                isUp = true;
                Log.i("Touch", "onUp:" + String.valueOf(offset));
                break;
            }
            case MotionEvent.ACTION_DOWN:{
                if(event.getX()>=getWidth()/2) {
                    isUp = false;
                }
                Log.i("Touch", "onDown:" + String.valueOf(offset));
                break;
            }
        }
        if(offset == 0) isUp = false;
        gestureDetector.onTouchEvent(event);
        return true;
    }
    public void free(){
        minus = 0;
        offset = 0;
    }
}

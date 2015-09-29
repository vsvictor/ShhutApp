package com.shhutapp.controls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
public class SecondTimeSeekBar extends ImageView{
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
    private int diriv;
    private boolean isScroll;
    private MainTimeSeekBar main;
    private Bitmap begPic;
    private Bitmap endPic;

    public SecondTimeSeekBar(Context context) {
        super(context);
        this.context = context;
        init();
    }
    public SecondTimeSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
    public SecondTimeSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }
    @SuppressLint("NewApi")
    public SecondTimeSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init();
    }
    private void init(){
        offset = 0;
        minus = 0;
        isUp = true;
        tapCounter = 1;
        diriv = 60;
        isScroll = true;
        gestureDetector = new GestureDetector(new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                Log.i("second gesture", "OnDown");
                return true;
            }
            @Override
            public void onShowPress(MotionEvent e) {
                Log.i("second gesture", "onShowPress");
            }
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.i("second gesture", "onSingleTapUp");
                setVisible(false);
                return true;
            }
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.i("second gesture", "onScroll");
                offset = e2.getX();
                return true;
            }
            @Override
            public void onLongPress(MotionEvent e) {
            }
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:{
                isUp = true;
                setVisible(false);
                break;
            }
            case MotionEvent.ACTION_DOWN:{
                if(event.getX()>=getWidth()/2) {
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
        int step = Math.round(width/6);
        Paint p = new Paint();
        Paint e = new Paint();
        p.setColor(dis);
        e.setColor(en);

        canvas.drawRect(0, line - 1, width, line + 1, p);
        canvas.drawRect(0, line - 1, offset, line + 1, e);

        if(isScroll) {
            if (!isUp && offset > width / 2) minus -= 2;
            else if (!isUp && offset == 0 && minus < 0) minus += 2;
        }
        boolean isSmall = true;
        int beg = (begPic.getHeight()-thumb.getHeight())/2;
        int end = Math.round(width)-beg;
        if(minus<0) beg=minus;

        if(begPic != null){
            canvas.drawBitmap(begPic, 0, line-begPic.getHeight()/2, e);
        }
/*
        for(int i = beg;i<end;i++){
            if(i%(step*tapCounter)==0){
                if(isSmall) canvas.drawRect(i-2,line-fourty/2,i+2,line+fourty/2,p);
                else canvas.drawRect(i-2,line-fourty,i+2,line+fourty,p);
                isSmall = !isSmall;
            }
        }
*/
        if(endPic != null){
            canvas.drawBitmap(endPic, width-endPic.getWidth(), line-endPic.getHeight()/2, e);
        }

        if(thumb != null){
            if(offset>width) offset=width;
            if(offset<beg) offset = beg;
            if(offset>end) offset = end;
            canvas.drawBitmap(thumb, offset, line-thumb.getHeight()/2, e);
        }
        invalidate();
        if(onChange != null) onChange.onTimeChanged(offsetToTime(minus, Math.round(offset)));
    }
    public void setOnChangeListener(OnTimeChanged list){
        onChange = list;
    }
    private int offsetToTime(int minus, int offset){
        int res = Math.round(60*offset/getWidth());
        return res;
    }
    public void setThumb(int res){
        Bitmap tmp = BitmapFactory.decodeResource(context.getResources(), res);
        int size = Math.round(getLayoutParams().height/2);
        thumb = Bitmap.createScaledBitmap(tmp, size, size, false);
        invalidate();
    }
    public void setThumb(int res, int width, int height){
        Bitmap tmp = BitmapFactory.decodeResource(context.getResources(), res);
        thumb = Bitmap.createScaledBitmap(tmp, width, height, false);
        invalidate();
    }

    public void free(){
        minus = 0;
        offset = 0;
    }
    public void setTime(int minutes){
        offset = getWidth()*minutes/60;
        invalidate();
    }
    public void setDiriv(int dir){
        diriv = dir;
    }
    public void setScroll(boolean b){
        isScroll = b;
    }
    public void setVisible(boolean b){
        this.setVisibility(b ? VISIBLE : INVISIBLE);
        if(main != null){
            this.main.setVisibility(!b?VISIBLE:INVISIBLE);
            this.main.setScroll(true);
            this.main.setStop(false);
        }
    }
    public void setMain(MainTimeSeekBar main){
        this.main = main;
    }
    public void setPosition(float pos){
        minus = 0;
        offset = pos;
        invalidate();
    }
    public void setBeginTrack(int res){
        Bitmap tmp = BitmapFactory.decodeResource(context.getResources(), res);
        begPic = Bitmap.createScaledBitmap(tmp, thumb.getWidth(), thumb.getHeight(), false);
    }
    public void setEndTrack(int res){
        Bitmap tmp = BitmapFactory.decodeResource(context.getResources(), res);
        endPic = Bitmap.createScaledBitmap(tmp, thumb.getWidth(), thumb.getHeight(), false);
    }
    public void setBeginTrack(int res, int width, int height){
        Bitmap tmp = BitmapFactory.decodeResource(context.getResources(), res);
        begPic = Bitmap.createScaledBitmap(tmp, width, height, false);
    }
    public void setEndTrack(int res, int width, int height){
        Bitmap tmp = BitmapFactory.decodeResource(context.getResources(), res);
        endPic = Bitmap.createScaledBitmap(tmp, width, height, false);
    }

}

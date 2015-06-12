package com.shhutapp.controls;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Xfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.shhutapp.R;
import com.shhutapp.utils.Convertor;

/**
 * Created by victor on 08.06.15.
 */
public class AlphaTearCircle extends ImageView {
    private Context context;
    //private int lLeft, lTop, lRight, lBottom;
    public boolean start;
    private int radius;
    private Point center;
    private Bitmap bitmap;
    private int ll,lt,lr,lb;
    private OnStopLister listener;
    private int maxRadius;
    private int background_color = Color.argb(192,0,0,0);
    private int draw = -1;
    private boolean isLarge;
    public AlphaTearCircle(Context context) {
        super(context);
        this.context= context;
        center = new Point(0,0);
        radius = 0;
    }
    public AlphaTearCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context= context;
        center = new Point(0,0);
        radius = 0;
    }
    public AlphaTearCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context= context;
        center = new Point(0,0);
        radius = 0;
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AlphaTearCircle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context= context;
        center = new Point(0,0);
        radius = 0;
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom){
        super.onLayout(changed, left, top, right, bottom);
    }
    @SuppressLint("NewApi")
    @Override
    protected void onDraw(Canvas scanvas) {
        super.onDraw(scanvas);
            if (center.x != 0 && center.y != 0 && radius != 0 && bitmap != null) {
                Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(output);

                final Paint paint = new Paint();
                final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                paint.setAntiAlias(true);
                canvas.drawARGB(0, 0, 0, 0);
                paint.setColor(Color.BLACK);
                canvas.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2, radius, paint);

                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(bitmap, rect, rect, paint);

                scanvas.drawColor(background_color);
                scanvas.drawBitmap(output, center.x, center.y, new Paint());
            } else {
                scanvas.drawColor(background_color);
            }
    }


    public void setRadius(int radius){
        this.radius = radius;
    }

    public int getRadius(){
        return radius;
    }
    public int getMaxRadius(){
        return maxRadius;
    }
    public void addRadius(final int step){
        if(radius>maxRadius) {
            this.listener.onStop();
            return;
        }
        radius += step;
    }

    public void addRadius(){
        if(radius>maxRadius) {
            if(this.listener!= null) this.listener.onStop();
            return;
        }
        radius++;
    }

    public void setCenter(Point center){
        this.center = center;
    }
    public void setMaxRadius(int max){
        this.maxRadius = max;
    }
    public void setOnStopListener(OnStopLister listener){
        this.listener =listener;
    }
    public void setBack(int color){
        this.background_color = color;
    }
    public void setBitmap(Bitmap b){
        bitmap = b;
        invalidate();
    }
}

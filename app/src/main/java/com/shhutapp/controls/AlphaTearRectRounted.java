package com.shhutapp.controls;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
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

import java.util.zip.Checksum;

/**
 * Created by victor on 08.06.15.
 */
public class AlphaTearRectRounted extends ImageView {
    private Context context;
    public boolean start;
    private int background_color = Color.argb(192,0,0,0);
    private Point position;
    private Bitmap bitmap;

    public AlphaTearRectRounted(Context context) {
        super(context);
        this.context= context;
        position = new Point(0,0);
    }
    public AlphaTearRectRounted(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context= context;
        position = new Point(0,0);
    }
    public AlphaTearRectRounted(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context= context;
        position = new Point(0,0);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AlphaTearRectRounted(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context= context;
        position = new Point(0,0);
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom){
        super.onLayout(changed, left, top, right, bottom);
    }
    @SuppressLint("NewApi")
    @Override
    protected void onDraw(Canvas scanvas) {
        super.onDraw(scanvas);
        if (position.x != 0 && position.y != 0 && bitmap != null) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.BLACK);
            canvas.drawRoundRect(0, 0, getWidth(), getHeight(), 10, 10, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            scanvas.drawColor(background_color);
            scanvas.drawBitmap(output, position.x, position.y, new Paint());
        } else {
            scanvas.drawColor(background_color);
        }
    }

    public void setBack(int color){
        this.background_color = color;
    }
    public void setBitmap(Bitmap b){
        bitmap = b;
        invalidate();
    }

    public void setPosition(Point point) {
    }
}

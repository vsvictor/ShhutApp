package com.shhutapp.controls;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceView;

/**
 * Created by victor on 07.09.15.
 */
public class AlphaTear extends SurfaceView {
    private int background = Color.argb(192,0,0,0);
    public AlphaTear(Context context) {
        super(context);
        this.setAlpha(0.7f);
        this.setZOrderOnTop(true);
    }

    public AlphaTear(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setAlpha(0.7f);
        this.setZOrderOnTop(true);
    }

    public AlphaTear(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setAlpha(0.7f);
        this.setZOrderOnTop(true);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AlphaTear(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.setAlpha(0.7f);
        this.setZOrderOnTop(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setARGB(255,255,255,255);
        canvas.drawRect(0,0,100, 100,p);
    }
}

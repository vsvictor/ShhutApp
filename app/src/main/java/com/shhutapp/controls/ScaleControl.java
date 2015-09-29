package com.shhutapp.controls;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.SeekBar;

import com.shhutapp.R;
import com.shhutapp.utils.Convertor;

/**
 * Created by victor on 14.09.15.
 */
public class ScaleControl extends SeekBar  implements SeekBar.OnSeekBarChangeListener {
    private Context context;
    public ScaleControl(Context context) {
        super(context);
        this.context = context;
    }

    public ScaleControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public ScaleControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScaleControl(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        Drawable dr = context.getResources().getDrawable(R.drawable.thumb_timebar);
        Drawable dr_new = resize(dr);
        this.setThumb(dr_new);
    }
    private Drawable resize(Drawable image) {
        int h = Math.round(Convertor.convertDpToPixel(18, context));
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, h, h, false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}

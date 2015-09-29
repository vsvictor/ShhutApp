package com.shhutapp.controls;

import com.shhutapp.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.view.View.OnTouchListener;
import android.view.LayoutInflater;
import com.shhutapp.R;
import com.shhutapp.utils.Convertor;

public class TimeBarHours extends SeekBar{
	private Context context;
	private int colorDisabled = Color.argb(128, 255, 255, 255);
	private int colorEnabled = Color.argb(255, 255, 255, 255);

	public TimeBarHours(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		//this.setOnSeekBarChangeListener(this);
		this.context = context;
	}
	public TimeBarHours(Context context, AttributeSet attrs) {
		super(context, attrs);
		//this.setOnSeekBarChangeListener(this);
		this.context = context;
	}
	public TimeBarHours(Context context) {
		super(context);
		//this.setOnSeekBarChangeListener(this);
		this.context = context;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float centerX = this.getWidth()/2;
		float centerY = this.getHeight()/2;
		float nHeight = Convertor.convertDpToPixel(19, context);
		float i1 = Convertor.convertDpToPixel(14, context);
		float i2 = Convertor.convertDpToPixel(18, context);
		float dp = Convertor.convertDpToPixel(1, context);
		float h = dp/2;
		if(h<1) h=1;

		Drawable dr = context.getResources().getDrawable(R.drawable.thumb_timebar);
		Drawable dr_new = resize(dr);
		this.setThumb(dr_new);
		Paint p = new Paint();
		p.setColor(colorDisabled);
		canvas.drawRect(0, this.getHeight()-this.getThumb().getBounds().height()/2-2, getWidth(), this.getHeight() - this.getThumb().getBounds().height()/2+1, p);
		canvas.drawRect(centerX-(2*h), centerY+i1/2, centerX+(2*h), centerY-i1/2, p);
		canvas.drawRect(centerX/3*2-h, centerY+i2/2, centerX/3*2+h, centerY-i2/2, p);
		canvas.drawRect(centerX/3-h, centerY+i1/2, centerX/3+h, centerY-i1/2, p);
		canvas.drawRect(centerX+centerX/3-h, centerY+i2/2, centerX+centerX/3+h, centerY-i2/2, p);
		canvas.drawRect(centerX + centerX / 3 * 2 - h, centerY + i1 / 2, centerX + centerX / 3 * 2 + h, centerY - i1 / 2, p);

	}
	private Drawable resize(Drawable image) {
		int h = Math.round(Convertor.convertDpToPixel(18, context));
		Bitmap b = ((BitmapDrawable)image).getBitmap();
		Bitmap bitmapResized = Bitmap.createScaledBitmap(b, h, h, false);
		return new BitmapDrawable(getResources(), bitmapResized);
	}

	public void setTime(int time){
		setProgress(time);
	}
}

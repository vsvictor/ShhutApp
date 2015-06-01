package com.shhutapp.controls;

import com.shhutapp.utils.Convertor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

public class VertLine extends ImageView{
	private Context context;
	private int color = Color.argb(64, 255, 255, 255);
	public VertLine(Context context) {
		super(context);
		this.context = context;
	}
	public VertLine(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}
	public VertLine(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
	}
	@SuppressLint("NewApi")
	public VertLine(Context context, AttributeSet attrs, int defStyleAttr,int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		this.context = context;
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float centerX = this.getWidth()/2;
		float centerY = this.getHeight()/2;
		float h = Convertor.convertDpToPixel(17, context);
		float w = Convertor.convertDpToPixel(2, context);
		float s = w/2;
		if(h<1) h=1;
		Paint paint = new Paint();
		paint.setColor(color);
		canvas.drawRect(centerX-s, 0, centerX+s, h, paint);
	}

}

package com.shhutapp.controls;

import com.shhutapp.R;
import com.shhutapp.utils.Convertor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

public class MainSeekBar extends ImageView {

	private String TAG = this.getClass().getSimpleName();
	private Bitmap thumbl = BitmapFactory.decodeResource(getResources(),R.drawable.thumb_center);
	private Bitmap thumbr = BitmapFactory.decodeResource(getResources(),R.drawable.thumb_right);
	private Bitmap centre = BitmapFactory.decodeResource(getResources(),R.drawable.line_disabled);
	
	private Bitmap leadtrail = BitmapFactory.decodeResource(getResources(),R.drawable.line_disabled);
	private Bitmap left_end = BitmapFactory.decodeResource(getResources(),R.drawable.thumb_left);
	private Bitmap right_end = BitmapFactory.decodeResource(getResources(),R.drawable.empty_back);
	
	private int thumblX, thumbrX;
	private int thumb1Value, thumb2Value;
	private int thumbY;
	private Paint paint = new Paint();
	private int selectedThumb;
	private SeekBarChangeListener scl;
	private int offset;
	private int minwindow = 10;
	private int colorDisabled = Color.argb(128, 255, 255, 255);
	private int colorEnabled = Color.argb(255, 255, 255, 255);
	private float coef;
	private Context context;
	public MainSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public MainSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public MainSeekBar(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	public void setSeekBarChangeListener(SeekBarChangeListener scl){
		this.scl = scl;
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

		this.coef = nHeight/this.left_end.getHeight();
		Bitmap ln = Bitmap.createScaledBitmap(this.left_end, (int)(this.left_end.getWidth()*this.coef), (int) nHeight, true);
		Bitmap lt = Bitmap.createScaledBitmap(this.thumbl, Math.round(this.thumbl.getWidth()*this.coef), Math.round(this.thumbl.getHeight()*this.coef), true);
		Bitmap rt = Bitmap.createScaledBitmap(this.thumbr, Math.round(this.thumbr.getWidth()*this.coef), Math.round(this.thumbr.getHeight()*this.coef), true);
		Paint p = new Paint();
		if(this.isEnabled()) p.setColor(colorEnabled);
		else p.setColor(colorDisabled);
		canvas.drawBitmap(ln,0,0,p);

		if(thumblX == 0) thumblX = 1;
		if(thumbrX == 0) thumbrX = thumblX+lt.getWidth()/2+1+thumblX;
		
		p.setColor(colorDisabled);
		canvas.drawRect(0, this.getHeight()-ln.getHeight()/2-2, getWidth(), this.getHeight()-ln.getHeight()/2+1, p);
		canvas.drawRect(centerX-(2*h), centerY+i1/2, centerX+(2*h), centerY-i1/2, p);
		canvas.drawRect(centerX/3*2-h, centerY+i2/2, centerX/3*2+h, centerY-i2/2, p);
		canvas.drawRect(centerX/3-h, centerY+i1/2, centerX/3+h, centerY-i1/2, p);
		canvas.drawRect(centerX+centerX/3-h, centerY+i2/2, centerX+centerX/3+h, centerY-i2/2, p);
		canvas.drawRect(centerX+centerX/3*2-h, centerY+i1/2, centerX+centerX/3*2+h, centerY-i1/2, p);
		
		if(this.isEnabled()) p.setColor(colorEnabled);
		else p.setColor(colorDisabled);
		
		canvas.drawRect(0, this.getHeight()-ln.getHeight()/2-2, thumbrX+thumbr.getWidth(), this.getHeight()-ln.getHeight()/2+1, p);
		
		//canvas.drawBitmap(lt, thumblX, this.getHeight()-lt.getHeight(), p);
		canvas.drawBitmap(rt, thumbrX+rt.getWidth(), this.getHeight()-rt.getHeight(), p);
	}
	public void setColorEnabled(int color){
		colorEnabled = color;
	}
	public void setColorDisabled(int color){
		colorDisabled = color;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(!this.isEnabled()) return false;
		int mx = (int) event.getX();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//if (mx >= thumblX - thumbl.getWidth() && mx <= thumblX) {
			if (Math.abs(mx-thumblX)<Math.abs(mx-thumbrX)) {
				selectedThumb = 1;
				offset = mx - thumblX;
				printLog("Select Thumb 1 " + offset);
			} else {
				selectedThumb = 2;
				offset = thumbrX - mx;
				printLog("Select Thumb 2");
			}
			break;
		case MotionEvent.ACTION_MOVE:
			printLog("Mouse Move : " + selectedThumb);
			if (selectedThumb == 1) {
				thumblX = mx - offset;
				if (thumblX < thumbl.getWidth())
				   thumblX = thumbl.getWidth();
			} else if (selectedThumb == 2) {
				thumbrX = mx + offset;
			}
			break;
		case MotionEvent.ACTION_UP:
			selectedThumb = 0;
			break;
		}
		
		if (selectedThumb == 2)
			{
			if (thumbrX > getWidth()- thumbr.getWidth())
				thumbrX = getWidth()- thumbr.getWidth();
			if (thumbrX <= thumbl.getWidth()+1+minwindow)
				thumbrX = thumbl.getWidth()+1+minwindow;
			if (thumbrX <= thumblX+minwindow)
				thumblX = thumbrX-1-minwindow;
			}
		else if (selectedThumb == 1)
			{
			if (thumblX < thumbl.getWidth())
				thumblX = thumbl.getWidth();
			if(thumblX >= getWidth()- (thumbr.getWidth()+minwindow))
				thumblX = getWidth()- (thumbr.getWidth()+minwindow);
			if (thumblX > thumbrX-minwindow)
				thumbrX = thumblX+1+minwindow;
			}	
		invalidate();
		if(scl !=null){
			calculateThumbValue();
			scl.SeekBarValueChanged(thumb1Value,thumblX, thumb2Value, thumbrX, getWidth(),thumbY, 0);
		}
		return true;
	}
	
	private void calculateThumbValue(){
		int width = getWidth() - (thumbl.getWidth() + thumbr.getWidth());
		thumb1Value = (100*(thumblX-thumbl.getWidth())/width);
		thumb2Value = (100*(thumbrX-thumbl.getWidth())/width);
	}
	
	private void printLog(String log){
		Log.i(TAG, log);
	}
}

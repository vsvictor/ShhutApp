package com.shhutapp.controls;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import com.google.maps.android.ui.IconGenerator;
import com.shhutapp.R;

/**
 * Created by victor on 02.06.15.
 */
public class ExIconGenerator extends IconGenerator {
    private TextView mTextView;

    public ExIconGenerator(Context context) {
        super(context);
        mTextView = new TextView(context);
        mTextView.setId(R.id.text);
        setContentView(mTextView);
    }
    public Bitmap makeIcon(String text) {
        if (mTextView != null) {
            mTextView.setText(text);
        }
        return makeIcon();
    }
    public Bitmap makeIcon(String text, int color) {
        if (mTextView != null) {
            mTextView.setTextColor(ColorStateList.valueOf(color));
            mTextView.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL | Gravity.CLIP_VERTICAL);
            mTextView.setText(text);
        }
        return makeIcon();
    }
    public Bitmap makeIcon(String text, int color, int textSizeInSP) {
        if (mTextView != null) {
            mTextView.setTextColor(ColorStateList.valueOf(color));
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            mTextView.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL | Gravity.CLIP_VERTICAL);
            mTextView.setText(text);
        }
        return makeIcon();
    }
    public Bitmap makeIcon(String text, int color, int textSizeInSP, Typeface tf) {
        if (mTextView != null) {
            mTextView.setTypeface(tf);
            mTextView.setTextColor(ColorStateList.valueOf(color));
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            mTextView.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL|Gravity.CLIP_VERTICAL);
            mTextView.setText(text);
        }
        return makeIcon();
    }
}

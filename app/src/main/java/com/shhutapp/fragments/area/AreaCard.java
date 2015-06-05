package com.shhutapp.fragments.area;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.controls.MainTimeSeekBar;
import com.shhutapp.controls.MainTimeSeekBarRed;
import com.shhutapp.controls.SecondTimeSeekBarRed;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.pages.BasePage;
import com.shhutapp.utils.Convertor;

import org.apache.log4j.chainsaw.Main;

/**
 * Created by victor on 03.06.15.
 */
public class AreaCard extends BaseFragments {
    private BasePage page;
    private MainTimeSeekBarRed mainScale;
    private SecondTimeSeekBarRed secondScale;
    private RelativeLayout rlMapMap;
    private BitmapDrawable dr;
    private ImageView ivMapMap;

    public AreaCard(){
        super(MainActivity.getMainActivity());
    }
    public AreaCard(MainActivity act){
        super(act);
    }
    public AreaCard(MainActivity act, BasePage page){
        super(act);
        this.page = page;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String ph = getArguments().getString("photo");
        Bitmap bp = Convertor.Base64ToBitmap(ph);
        dr = new BitmapDrawable(bp);
    }
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.map_card, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        //rlMapMap = (RelativeLayout) rView.findViewById(R.id.rlMapMap);
        ivMapMap = (ImageView) rView.findViewById(R.id.ivMapMap);
        ivMapMap.setImageDrawable(dr);
        //rlMapMap.setBackground(dr);
        mainScale = (MainTimeSeekBarRed) rView.findViewById(R.id.sbGeocardActivationMain);
        mainScale.setThumb(R.drawable.thumb_full_red);
        secondScale = (SecondTimeSeekBarRed) rView.findViewById(R.id.sbGeocardActivationSecond);
        secondScale.setThumb(R.drawable.thumb_red_center);
        secondScale.setBeginTrack(R.drawable.thumb_red_left, (int) Convertor.convertDpToPixel(8, getMainActivity()), (int) Convertor.convertDpToPixel(18, getMainActivity()));
        secondScale.setEndTrack(R.drawable.thumb_red_right, (int) Convertor.convertDpToPixel(8, getMainActivity()), (int) Convertor.convertDpToPixel(18, getMainActivity()));
        secondScale.setScroll(false);
        mainScale.setSecond(secondScale);
        secondScale .setMain(mainScale);

    }
}

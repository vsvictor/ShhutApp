package com.shhutapp.fragments.area;

import android.content.ContentValues;
import android.database.Cursor;
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
import android.widget.TextView;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.controls.MainTimeSeekBar;
import com.shhutapp.controls.MainTimeSeekBarRed;
import com.shhutapp.controls.OnTimeChanged;
import com.shhutapp.controls.SecondTimeSeekBarRed;
import com.shhutapp.data.Card;
import com.shhutapp.data.CardType;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.fragments.OnOkListener;
import com.shhutapp.pages.BasePage;
import com.shhutapp.utils.Convertor;
import com.shhutapp.utils.DateTimeOperator;

import org.apache.http.util.VersionInfo;
import org.apache.log4j.chainsaw.Main;

import java.util.Calendar;
import java.util.Date;

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
    private TextView tvActHours;
    private TextView tvActMinutes;
    private boolean cbOnOff = false;
    private RelativeLayout rlCBOnOff;
    private ImageView ivCBOn;
    private ImageView ivCBOff;
    private String address;
    private TextView tvMapAddress;
    private String locName;
    private boolean checked;
    private ImageView ivGeocardQuietOn;
    private ImageView ivGeocardQuietOff;

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
        locName = getArguments().getString("name");
        String ph = getArguments().getString("photo");
        Bitmap bp = Convertor.Base64ToBitmap(ph);
        address = getArguments().getString("address");
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
        getMainActivity().getHeader().setVisibleNext(false);
        getMainActivity().getHeader().setVisibleOk(true);
        getMainActivity().getHeader().setOnOkListener(new OnOkListener() {
            @Override
            public void onOk() {
                int idLoc = -1;
                String[] cols = {"id"};
                String[] args = {locName};
                Cursor c = getMainActivity().getDB().query("locations", cols, "name=?", args, null, null, null);
                if (c.moveToFirst()) idLoc = c.getInt(0);
                ContentValues cv = new ContentValues();

                if (!cbOnOff)cv.put("idActivate", 1);
                else cv.put("idActivate", 3);

                cv.put("type", Card.cardTypeToId(CardType.Geo));

                cv.put("name", locName);
                cv.put("idGeo", idLoc);
                cv.put("idDream", -1);
                cv.put("idWhiteList", -1);
                if (getMainActivity().getSMS() != null) {
                    cv.put("idMessage", getMainActivity().getSMS().getID());
                    getMainActivity().clearSMS();
                } else cv.put("idMessage", -1);
                getMainActivity().getDB().insert("cards", null, cv);
                BasePage page = getMainActivity().createPageFromID(BasePage.Pages.mainPage);

                //getMainActivity().getSupportFragmentManager().beginTransaction().remove(page).commit();
                getMainActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, page).commit();
            }
        });
        //rlMapMap = (RelativeLayout) rView.findViewById(R.id.rlMapMap);
        tvMapAddress = (TextView) rView.findViewById(R.id.tvMapAddress);
        tvMapAddress.setText(address);
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
        tvActHours = (TextView) rView.findViewById(R.id.tvActHours);
        tvActMinutes = (TextView) rView.findViewById(R.id.tvActMinutes);
        mainScale.setOnChangeListener(new OnTimeChanged() {
            @Override
            public void onTimeChanged(int minutes) {
                int h = minutes / 60;
                int m = minutes - (h * 60);
                tvActHours.setText(String.valueOf(h));
            }
        });
        secondScale.setOnChangeListener(new OnTimeChanged() {
            @Override
            public void onTimeChanged(int minutes) {
                tvActMinutes.setText(String.valueOf(minutes));
            }
        });
        ivGeocardQuietOff = (ImageView) rView.findViewById(R.id.ivGeocardQuietOff);
        ivGeocardQuietOn = (ImageView) rView.findViewById(R.id.ivGeocardQueitOn);
        ivCBOn = (ImageView) rView.findViewById(R.id.ivGeocardCBOn);
        ivCBOn.setVisibility(cbOnOff?View.VISIBLE:View.INVISIBLE);
        ivCBOff = (ImageView) rView.findViewById(R.id.ivGeocardCBOff);
        ivCBOff.setVisibility(cbOnOff?View.INVISIBLE:View.VISIBLE);
        rlCBOnOff = (RelativeLayout) rView.findViewById(R.id.rlGeocardCB);
        rlCBOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbOnOff = !cbOnOff;
                ivCBOn.setVisibility(cbOnOff ? View.VISIBLE : View.INVISIBLE);
                ivCBOff.setVisibility(cbOnOff ? View.INVISIBLE : View.VISIBLE);
                mainScale.setEnabled(!cbOnOff);
                secondScale.setEnabled(!cbOnOff);
                mainScale.invalidate();
                secondScale.invalidate();
                //ivGeocardQuietOff.setVisibility(cbOnOff ? View.VISIBLE : View.INVISIBLE);
                //ivGeocardQuietOn.setVisibility(cbOnOff ? View.INVISIBLE : View.VISIBLE);
                ivGeocardQuietOff.setAlpha(cbOnOff ? 56 : 255);
                ivGeocardQuietOn.setAlpha(cbOnOff ? 56 : 255);
            }
        });
    }
}

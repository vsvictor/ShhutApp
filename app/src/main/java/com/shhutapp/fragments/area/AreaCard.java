package com.shhutapp.fragments.area;

import android.content.ComponentCallbacks;
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
import android.util.Log;
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
import com.shhutapp.data.QueitCard;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.fragments.OnBackListener;
import com.shhutapp.fragments.OnCancelListener;
import com.shhutapp.fragments.OnOkListener;
import com.shhutapp.pages.BasePage;
import com.shhutapp.pages.MainPage;
import com.shhutapp.pages.MessagePage;
import com.shhutapp.pages.QueitTimePage;
import com.shhutapp.pages.WhiteListPage;
import com.shhutapp.utils.Convertor;
import com.shhutapp.utils.DateTimeOperator;

import org.apache.http.util.VersionInfo;
import org.apache.log4j.chainsaw.Main;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by victor on 03.06.15.
 */
public class AreaCard extends BasePage {
    private BasePage page;
    public static AreaCard instance;
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
    private RelativeLayout rlGeocardQueit;
    private RelativeLayout rlGeocardWhiteList;
    private RelativeLayout rlGeocardMessage;
    private ImageView ivGeocardWLOff;
    private ImageView ivGeocardWLOn;
    private ImageView ivGeocardMessageOff;
    private ImageView ivGeocardMessageOn;
    private int hours;
    private int min;
    private TextView tvNearBegin;
    private TextView tvNearEnd;

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
        instance = this;
        locName = getArguments().getString("name");
        String ph = getArguments().getString("photo");
        Bitmap bp = Convertor.Base64ToBitmap(ph);
        address = getArguments().getString("address");
        dr = new BitmapDrawable(bp);
        //getMainActivity().clearSMS();
        //getMainActivity().clearWhiteList();
        //getMainActivity().clearQueitCard();
    }

    @Override
    public int getID() {
        return Pages.areaCard;
    }

    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rootView = inf.inflate(R.layout.map_card, container, false);
        return rootView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        getMainActivity().getHeader().setVisibleNext(false);
        getMainActivity().getHeader().setVisibleOk(true);
        getMainActivity().getHeader().setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel() {
                MainPage mp = new MainPage(act);
                getMainActivity().getSupportFragmentManager().beginTransaction().remove(getCurrent()).commit();
                getMainActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, mp).commit();
            }
        });
        getMainActivity().getHeader().setOnOkListener(new OnOkListener() {
            @Override
            public void onOk() {
                int idLoc = -1;
                String[] cols = {"id"};
                String[] args = {locName};
                Cursor c = getMainActivity().getDB().query("locations", cols, "name=?", args, null, null, null);
                if (c.moveToFirst()) idLoc = c.getInt(0);
                ContentValues cv = new ContentValues();

                if (cbOnOff)cv.put("idActivate", 1);
                else cv.put("idActivate", 3);

                cv.put("type", Card.cardTypeToId(CardType.Geo));

                cv.put("name", locName);
                cv.put("idGeo", idLoc);
                if(getMainActivity().getQueitCard() != null)
                    cv.put("idDream", getMainActivity().getQueitCard().getID());
                if(getMainActivity().getWhiteList()!=null)
                    cv.put("idWhiteList", getMainActivity().getWhiteList().getID());
                if (getMainActivity().getSMS() != null) {
                    cv.put("idMessage", getMainActivity().getSMS().getID());
                    getMainActivity().clearSMS();
                } else cv.put("idMessage", -1);
                cv.put("time",(hours*60+min));
                getMainActivity().getDB().insert("cards", null, cv);
                BasePage page = getMainActivity().createPageFromID(BasePage.Pages.mainPage);
                //getMainActivity().getSupportFragmentManager().beginTransaction().remove(page).commit();
                getMainActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, page).commit();
            }
        });
        //rlMapMap = (RelativeLayout) rView.findViewById(R.id.rlMapMap);
        tvMapAddress = (TextView) rootView.findViewById(R.id.tvMapAddress);
        tvMapAddress.setText(address);
        ivMapMap = (ImageView) rootView.findViewById(R.id.ivMapMap);
        ivMapMap.setImageDrawable(dr);
        //rlMapMap.setBackground(dr);
        mainScale = (MainTimeSeekBarRed) rootView.findViewById(R.id.sbGeocardActivationMain);
        mainScale.setThumb(R.drawable.thumb_full_red);
        secondScale = (SecondTimeSeekBarRed) rootView.findViewById(R.id.sbGeocardActivationSecond);
        secondScale.setThumb(R.drawable.thumb_red_center);
        secondScale.setBeginTrack(R.drawable.thumb_red_left, (int) Convertor.convertDpToPixel(8, getMainActivity()), (int) Convertor.convertDpToPixel(18, getMainActivity()));
        secondScale.setEndTrack(R.drawable.thumb_red_right, (int) Convertor.convertDpToPixel(8, getMainActivity()), (int) Convertor.convertDpToPixel(18, getMainActivity()));
        secondScale.setScroll(false);
        mainScale.setSecond(secondScale);
        secondScale .setMain(mainScale);
        tvActHours = (TextView) rootView.findViewById(R.id.tvActHours);
        tvActMinutes = (TextView) rootView.findViewById(R.id.tvActMinutes);
        mainScale.setOnChangeListener(new OnTimeChanged() {
            @Override
            public void onTimeChanged(int minutes) {
                int h = minutes / 60;
                int m = minutes - (h * 60);
                hours = h;
                tvActHours.setText(String.valueOf(h));
            }
        });
        secondScale.setOnChangeListener(new OnTimeChanged() {
            @Override
            public void onTimeChanged(int minutes) {
                tvActMinutes.setText(String.valueOf(minutes));
                min = minutes;
            }
        });
        ivGeocardQuietOff = (ImageView) rootView.findViewById(R.id.ivGeocardQuietOff);
        ivGeocardQuietOn = (ImageView) rootView.findViewById(R.id.ivGeocardQueitOn);
        rlGeocardQueit = (RelativeLayout) rootView.findViewById(R.id.rlGeoCardQueit);
        rlGeocardWhiteList = (RelativeLayout) rootView.findViewById(R.id.rlGeoCardWhiteList);
        rlGeocardMessage = (RelativeLayout) rootView.findViewById(R.id.rlGeoCardMessages);
        ivCBOn = (ImageView) rootView.findViewById(R.id.ivGeocardCBOn);
        ivCBOn.setVisibility(cbOnOff?View.VISIBLE:View.INVISIBLE);
        ivCBOff = (ImageView) rootView.findViewById(R.id.ivGeocardCBOff);
        ivCBOff.setVisibility(cbOnOff?View.INVISIBLE:View.VISIBLE);
        rlCBOnOff = (RelativeLayout) rootView.findViewById(R.id.rlGeocardCB);
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
        rlGeocardQueit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cbOnOff) {
                    QueitTimePage page = new QueitTimePage(MainActivity.getMainActivity(), instance);
                    Bundle args = new Bundle();
                    args.putInt("back", Pages.areaCard);
                    page.setArguments(args);
                    getMainActivity().getSupportFragmentManager().beginTransaction().remove(getCurrent()).commit();
                    getMainActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, page).commit();
                }
            }
        });
        ivGeocardWLOff = (ImageView) rootView.findViewById(R.id.ivGeocardWLOff);
        ivGeocardWLOn = (ImageView) rootView.findViewById(R.id.ivGeocardWLOn);
        rlGeocardWhiteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WhiteListPage wlp = new WhiteListPage(getMainActivity(), instance);
                Bundle args = new Bundle();
                args.putInt("back", getID());
                args.putBoolean("isRadio", true);
                wlp.setArguments(args);
                getMainActivity().getSupportFragmentManager().beginTransaction().hide(instance).add(R.id.container, wlp).commit();
            }
        });
        ivGeocardMessageOff = (ImageView) rootView.findViewById(R.id.ivGeocardMsgOff);
        ivGeocardMessageOn = (ImageView) rootView.findViewById(R.id.ivGeocardMsgOn);
        rlGeocardMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessagePage mp = new MessagePage(getMainActivity(), instance);
                Bundle args = new Bundle();
                args.putInt("back", getID());
                args.putBoolean("isRadio", true);
                mp.setArguments(args);
                getMainActivity().getSupportFragmentManager().beginTransaction().hide(instance).add(R.id.container, mp).commit();
            }
        });
        tvNearBegin = (TextView) rootView.findViewById(R.id.tvNearBegin);
        tvNearEnd = (TextView) rootView.findViewById(R.id.tvNearEnd);
        QueitCard near = getMainActivity().getDBHelper().findNear();
        if(near != null){
            tvNearBegin.setVisibility(View.VISIBLE);
            tvNearEnd.setVisibility(View.VISIBLE);
            tvNearBegin.setText(DateTimeOperator.dateToTimeString(near.getBegin()));
            tvNearEnd.setText(DateTimeOperator.dateToTimeString(near.getEnd()));
        }
        else{
            tvNearBegin.setVisibility(View.INVISIBLE);
            tvNearEnd.setVisibility(View.INVISIBLE);
        }
    }
    public void onHiddenChanged(boolean hidden){
        super.onHiddenChanged(hidden);
        if(!hidden) {
            getMainActivity().getHeader().setInvisibleAll();
            getMainActivity().getHeader().setVisibleCancel(true);
            getMainActivity().getHeader().setVisibleOk(true);
            getMainActivity().getHeader().setTextHeader(locName);
            getMainActivity().getHeader().setOnOkListener(new OnOkListener() {
                @Override
                public void onOk() {
                    int idLoc = -1;
                    String[] cols = {"id"};
                    String[] args = {locName};
                    Cursor c = getMainActivity().getDB().query("locations", cols, "name=?", args, null, null, null);
                    if (c.moveToFirst()) idLoc = c.getInt(0);
                    ContentValues cv = new ContentValues();
                    if (cbOnOff) cv.put("idActivate", 1);
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
                    getMainActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, page).commit();
                }
            });
            if(getMainActivity().getQueitCard() != null){
                ivGeocardQuietOff.setVisibility(View.INVISIBLE);
                ivGeocardQuietOn.setVisibility(View.VISIBLE);
            }
            else{
                ivGeocardQuietOff.setVisibility(View.VISIBLE);
                ivGeocardQuietOn.setVisibility(View.INVISIBLE);
            }
            if(getMainActivity().getWhiteList() != null){
                ivGeocardWLOff.setVisibility(View.INVISIBLE);
                ivGeocardWLOn.setVisibility(View.VISIBLE);
            }
            else{
                ivGeocardWLOff.setVisibility(View.VISIBLE);
                ivGeocardWLOn.setVisibility(View.INVISIBLE);
            }
            if(getMainActivity().getSMS() != null){
                ivGeocardMessageOff.setVisibility(View.INVISIBLE);
                ivGeocardMessageOn.setVisibility(View.VISIBLE);
            }
            else{
                ivGeocardMessageOff.setVisibility(View.VISIBLE);
                ivGeocardMessageOn.setVisibility(View.INVISIBLE);
            }

        }
    }
}

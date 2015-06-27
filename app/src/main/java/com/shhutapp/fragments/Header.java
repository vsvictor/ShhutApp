package com.shhutapp.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.utils.Convertor;

public class Header extends BaseFragments {
    private TextView tvHeader;
    private ImageView ivCancel;
    private ImageView ivBack;
    private ImageView ivOk;
    private ImageView ivNext;
    private ImageView ivSearch;
    private ImageView ivHelp;
    private ImageView ivMenu;
    private ImageView ivCardOn;
    private ImageView ivCardOff;
    private EditText edSearch;
    private ImageView ivBackSearch;
    private ImageView ivCancelSearch;
    private TextView tvCounter;
    private RelativeLayout rlCardOnOff;
    private boolean isOn = true;
    //private HeaderActionsListener listener;
    private OnOkListener onOk;
    private OnCancelListener onCancel;
    private OnBackListener onBack;
    private OnNextListener onNext;
    private OnHelpListener onHelp;
    private OnSearchListener onSearch;
    private OnMenuListener onMenu;
    private OnBackListener onBackSearch;
    private OnCancelListener onCancelSearch;
    private OnCardOnOff onOnOff;
    public Header(){
        super();
    }
    public Header(MainActivity act){
        super(act);
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState){
        rView = inf.inflate(R.layout.header, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        tvHeader = (TextView)rView.findViewById(R.id.tvHeader);
        tvHeader.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Medium.ttf"));
        tvCounter = (TextView)rView.findViewById(R.id.tvCounter);
        tvCounter.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Medium.ttf"));
        ivCancel = (ImageView)rView.findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCancel != null) onCancel.onCancel();
            }
        });
        ivBack = (ImageView)rView.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onBack != null) onBack.onBack();
            }
        });
        ivOk = (ImageView)rView.findViewById(R.id.ivOk);
        ivOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onOk != null) onOk.onOk();
            }
        });
        ivNext = (ImageView)rView.findViewById(R.id.ivNext);
        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onNext != null) onNext.onNext();
            }
        });
        ivSearch = (ImageView)rView.findViewById(R.id.ivSearch);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onSearch != null) onSearch.onSearch();
            }
        });
        ivHelp = (ImageView)rView.findViewById(R.id.ivHelp);
        ivHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onHelp != null) onHelp.onHelp();
            }
        });
        ivMenu = (ImageView)rView.findViewById(R.id.ivMenu);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onMenu != null) onMenu.onMenu();
            }
        });
        edSearch = (EditText) rView.findViewById(R.id.edSearch);
        ivBackSearch = (ImageView) rView.findViewById(R.id.ivBackSearch);
        ivBackSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onBackSearch != null) onBackSearch.onBack();
            }
        });
        ivCancelSearch = (ImageView) rView.findViewById(R.id.ivCancelSearch);
        ivCancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onCancelSearch != null ) onCancelSearch.onCancel();
            }
        });
        ivCardOn = (ImageView) rView.findViewById(R.id.ivCardOn);
        ivCardOff = (ImageView) rView.findViewById(R.id.ivCardOff);
        rlCardOnOff = (RelativeLayout) rView.findViewById(R.id.rlCardOnOff);
        rlCardOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOn = !isOn;
                ivCardOn.setVisibility(isOn ? View.VISIBLE : View.INVISIBLE);
                ivCardOff.setVisibility(isOn?View.INVISIBLE:View.VISIBLE);
                onOnOff.onSet(isOn);
            }
        });
    }
    public void setTextHeader(String text){
        tvHeader = (TextView)rView.findViewById(R.id.tvHeader);
        tvHeader.setText(text);
    }
    public String getTextHeader(){
        tvHeader = (TextView)rView.findViewById(R.id.tvHeader);
        return tvHeader.getText().toString();
    }

    public void setLeftText(int dp){
        int left = Math.round(Convertor.convertDpToPixel(dp, act));
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.CENTER_VERTICAL);
        p.setMargins(left,0,0, 0);
        tvHeader.setLayoutParams(p);
    }
    public void setGray(){
        setLeftText(72);
        setInvisibleAll();
        rView.setBackgroundColor(getMainActivity().getResources().getColor(R.color.gray_light));
    }
    public void setBlue(){
        setLeftText(72);
        setInvisibleAll();
        rView.setBackgroundColor(getMainActivity().getResources().getColor(R.color.blue_body));
    }

    public void setInvisibleAll(){
        ivCancel.setVisibility(View.INVISIBLE);
        ivBack.setVisibility(View.INVISIBLE);
        ivOk.setVisibility(View.INVISIBLE);
        ivNext.setVisibility(View.INVISIBLE);
        ivSearch.setVisibility(View.INVISIBLE);
        ivHelp.setVisibility(View.INVISIBLE);
        ivMenu.setVisibility(View.INVISIBLE);
        edSearch.setVisibility(View.INVISIBLE);
        ivBackSearch.setVisibility(View.INVISIBLE);
        ivCancelSearch.setVisibility(View.INVISIBLE);
        rlCardOnOff.setVisibility(View.INVISIBLE);
    }
    public void setVisibleTextHeader(boolean on){
        tvHeader.setVisibility(on?View.VISIBLE:View.INVISIBLE);
    }
    public void setVisibleCounter(boolean on) {
        tvCounter.setVisibility(on?View.VISIBLE:View.INVISIBLE);
    }
    public void setCounter(int counter) {
        tvCounter.setText(String.valueOf(counter));
    }
    public void setVisibleCancel(boolean on) {
        ivCancel.setVisibility(on ? View.VISIBLE : View.INVISIBLE);
    }
    public void setVisibleBack(boolean on){
        ivBack.setVisibility(on?View.VISIBLE:View.INVISIBLE);
    }
    public void setVisibleOk(boolean on){
        ivOk.setVisibility(on?View.VISIBLE:View.INVISIBLE);
    }
    public void setVisibleNext(boolean on){
        ivNext.setVisibility(on?View.VISIBLE:View.INVISIBLE);
    }
    public void setVisibleSearch(boolean on){
        ivSearch.setVisibility(on?View.VISIBLE:View.INVISIBLE);
    }
    public void setVisibleHelp(boolean on){
        ivHelp.setVisibility(on?View.VISIBLE:View.INVISIBLE);
    }
    public void setVisibleMenu(boolean on){
        ivMenu.setVisibility(on?View.VISIBLE:View.INVISIBLE);
    }
    public void setVisibleEditText(boolean on){
        edSearch.setVisibility(on?View.VISIBLE:View.INVISIBLE);
    }
    public void setVisibleBackSearch(boolean on){
        ivBackSearch.setVisibility(on?View.VISIBLE:View.INVISIBLE);
    }
    public void setVisibleCancelSearch(boolean on){
        ivCancelSearch.setVisibility(on?View.VISIBLE:View.INVISIBLE);
    }
    public void setVisibleOnOff(boolean on){
        rlCardOnOff.setVisibility(on?View.VISIBLE:View.INVISIBLE);
    }
    public void setOnOkListener(OnOkListener ok){
        onOk = ok;
    }
    public void setOnCancelListener(OnCancelListener cancel){
        onCancel = cancel;
    }
    public void setOnBackListener(OnBackListener back){
        onBack = back;
    }
    public void setOnNextListener(OnNextListener next){
        onNext = next;
    }
    public void setOnSearchListener(OnSearchListener search){
        onSearch = search;
    }
    public void setOnHelpListener(OnHelpListener help){
        onHelp = help;
    }
    public void setOnMenuListener(OnMenuListener menu){
        onMenu = menu;
    }
    public void setOnBackSearchListener(OnBackListener search){
        onBackSearch = search;
    }
    public void setOnCancelSearchListener(OnCancelListener search){
        onCancelSearch = search;
    }
    public void setOnCardOnOff( OnCardOnOff onoff){onOnOff = onoff;}
    public void setCardOnOff(boolean onoff){
        isOn = onoff;
        ivCardOn.setVisibility(isOn?View.VISIBLE:View.INVISIBLE);
        ivCardOff.setVisibility(isOn?View.INVISIBLE:View.VISIBLE);
    }
    public EditText getEdSearch(){
        return edSearch;
    }
}

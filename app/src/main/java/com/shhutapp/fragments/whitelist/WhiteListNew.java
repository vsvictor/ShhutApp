package com.shhutapp.fragments.whitelist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.controls.ExRelativeLayout;
import com.shhutapp.data.BaseObject;
import com.shhutapp.data.BaseObjectList;
import com.shhutapp.data.SMSCard;
import com.shhutapp.data.WhiteListCard;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.fragments.OnBackListener;
import com.shhutapp.fragments.OnCancelListener;
import com.shhutapp.fragments.OnOkListener;
import com.shhutapp.pages.BasePage;
import com.shhutapp.pages.MessagePage;
import com.shhutapp.pages.WhiteListPage;

/**
 * Created by victor on 04.05.15.
 */
public class WhiteListNew extends BaseFragments {
    private EditText edWhiteList;
    private WhiteListPage page;
    private WhiteListCard card;
    private RelativeLayout rlEditTextNormal;
    private RelativeLayout rlEditTextError;
    private TextView tvCounter;
    private boolean isError;
    //private WhiteListAppCont whitelistAppCont;
    private ExRelativeLayout rootLayout;
    private int itemNumber;
    public WhiteListNew(){
        super(MainActivity.getMainActivity());
    }
    @SuppressLint("ValidFragment")
    public WhiteListNew(MainActivity act){
        super(act);
    }
    @SuppressLint("ValidFragment")
    public WhiteListNew(MainActivity act, WhiteListPage page){
        super(act);
        this.page = page;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getMainActivity().getHeader().setOnOkListener(new OnOkListener() {
            @Override
            public void onOk() {
                String sName = edWhiteList.getText().toString();
                WhiteListCard c = new WhiteListCard();
                c.setID(-1);
                c.setName(sName);
                boolean r = getMainActivity().getDBHelper().isExistWhiteList(sName);
                if (!r) {
                    int id = c.newlist(getMainActivity().getDB());
                    Bundle b = new Bundle();
                    b.putInt("id", id);
                    b.putString("name", sName);
                    page.whitelistAppCont = new WhiteListAppCont(getMainActivity(), page);
                    page.whitelistAppCont.setArguments(b);
                    getMainActivity().getSupportFragmentManager().beginTransaction().
                            addToBackStack(null).
                            remove(getIAm()).
                            add(R.id.whitelistPage, page.whitelistAppCont, "AppCont").
                            commit();
                    //hideKeyboard();
                } else {
                    setError(true);
                }
            }
        });
        getMainActivity().getHeader().setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel() {
                if (getMainActivity().isWhiteListEmpty()) {
                    getMainActivity().getSupportFragmentManager().beginTransaction().
                            addToBackStack(null).
                            remove(getIAm()).
                            add(R.id.whitelistPage, page.getWhiteListEmpty()).
                            commit();
                } else {
                    getMainActivity().getSupportFragmentManager().beginTransaction().
                            addToBackStack(null).
                            remove(page.getWhitelistNew()).
                            add(R.id.whitelistPage, page.getWhitelistList()).
                            commit();
                }
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.whitelist_new, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        rootLayout = (ExRelativeLayout) rView.findViewById(R.id.rlWhiteListNew);
        edWhiteList = (EditText)rView.findViewById(R.id.edWhiteListNew);
        rlEditTextNormal = (RelativeLayout) rView.findViewById(R.id.rlWhiteListEditTextNormal);
        rlEditTextError  = (RelativeLayout) rView.findViewById(R.id.rlWhiteListEditTextError);
        tvCounter = (TextView) rView.findViewById(R.id.tvWhiteListCounter);
        setError(false);
    }
    @Override
    public void onResume(){
        super.onResume();
        Bundle b = getArguments();
        int count = b.getInt("count");
        itemNumber = count+1;
        if(b != null && b.getBoolean("isArgs")){
            card = new WhiteListCard();
            card.setID(b.getInt("id"));
            card.setName(b.getString("text"));
        }
        else card = new WhiteListCard();
        if(card.getName() != null) edWhiteList.setText(card.getName());
        edWhiteList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setError(false);
                if (edWhiteList.getText().toString().length() > 0) {
                    getMainActivity().getHeader().setVisibleOk(true);
                }
                if(edWhiteList.getText().toString().length()>=24){
                    edWhiteList.setText(edWhiteList.getText().toString().substring(0,23));
                    edWhiteList.setSelection(edWhiteList.getText().toString().length(), edWhiteList.getText().toString().length());
                }
                StringBuilder sb = new StringBuilder();
                sb.append(edWhiteList.getText().toString().length());
                sb.append("/");
                sb.append(itemNumber);
                tvCounter.setText(sb.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        StringBuilder sb = new StringBuilder();
        sb.append(0);
        sb.append("/");
        sb.append(count+1);
        tvCounter.setText(sb.toString());
        getMainActivity().getHeader().setTextHeader(getMainActivity().getResources().getString(R.string.whitelist));
        getMainActivity().getHeader().setInvisibleAll();
        getMainActivity().getHeader().setVisibleCancel(true);
        rootLayout.showKeyboard();
    }
    @Override
    public void onPause(){
        super.onPause();
        rootLayout.hideKeyboard();
    }
    public void setEnteredText(String message){
        edWhiteList.setText(message);
    }
    public String getEnteredText(){
        return edWhiteList.getText().toString();
    }
    public boolean isPresent(String message){
        BaseObjectList list = getMainActivity().getDBHelper().loadWhiteLists();
        for(BaseObject card : list){
            String mess = ((WhiteListCard) card).getName();
            if(mess.equalsIgnoreCase(message)) return true;
        }
        return false;
    }
    private void setError(boolean b){
        if(b){
            rlEditTextNormal.setVisibility(View.INVISIBLE);
            rlEditTextError.setVisibility(View.VISIBLE);
        }
        else{
            rlEditTextNormal.setVisibility(View.VISIBLE);
            rlEditTextError.setVisibility(View.INVISIBLE);
        }
    }
}

package com.shhutapp.fragments.whitelist;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
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
    private WhiteListAppCont whitelistAppCont;
    public WhiteListNew(){
        super(MainActivity.getMainActivity());
    }
    public WhiteListNew(MainActivity act){
        super(act);
    }
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
                int id = c.newlist(getMainActivity().getDB());
                Bundle b = new Bundle();
                b.putInt("id",id);
                b.putString("name", sName);
                whitelistAppCont = new WhiteListAppCont(getMainActivity(), page);
                whitelistAppCont.setArguments(b);
                getMainActivity().getSupportFragmentManager().beginTransaction().
                        remove(getIAm()).
                        add(R.id.whitelistPage, whitelistAppCont).
                        commit();
            }
        });
        getMainActivity().getHeader().setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel() {
                    if (getMainActivity().isWhiteListEmpty()) {
                        getMainActivity().getSupportFragmentManager().beginTransaction().
                                remove(getIAm()).
                                add(R.id.whitelistPage, page.getWhiteListEmpty()).
                                commit();
                    } else {
                        getMainActivity().getSupportFragmentManager().beginTransaction().
                                remove(page.getWhitelistNew()).
                                add(R.id.whitelistPage, page.getWhitelistList()).
                                //add(R.id.whitelistPage, page.getWhiteListEmpty()).
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
                if (edWhiteList.getText().toString().length() > 3) {
                    getMainActivity().getHeader().setVisibleOk(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        StringBuilder sb = new StringBuilder();
        sb.append(count+1);
        sb.append("/");
        sb.append(count+1);
        tvCounter.setText(sb.toString());
        getMainActivity().getHeader().setTextHeader(getMainActivity().getResources().getString(R.string.whitelist));
        getMainActivity().getHeader().setInvisibleAll();
        getMainActivity().getHeader().setVisibleCancel(true);
        //showKeyboadr();
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

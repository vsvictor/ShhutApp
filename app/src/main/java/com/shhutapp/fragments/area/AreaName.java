package com.shhutapp.fragments.area;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.fragments.OnNextListener;
import com.shhutapp.pages.AreaPage;
import com.shhutapp.pages.BasePage;

import org.apache.log4j.chainsaw.Main;

/**
 * Created by victor on 02.06.15.
 */
public class AreaName extends BaseFragments {
    private BasePage page;
    private EditText edMessage;
    private RelativeLayout rlEditTextNormal;
    private RelativeLayout rlEditTextError;
    private String currname;
    public AreaName(){
        super(MainActivity.getMainActivity());
    }
    public AreaName(MainActivity act){
        super(act);
    }
    public AreaName(MainActivity act, BasePage page){
        super(act);
        this.page = page;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currname = getArguments().getString("number");
    }
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.new_message, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        getMainActivity().getHeader().setOnNextListener(new OnNextListener() {
            @Override
            public void onNext() {
                boolean check = checkName(edMessage.getText().toString());
                if(check){
                    ContentValues row = new ContentValues();
                    String[] args = {currname};
                    row.put("name", edMessage.getText().toString());
                    act.getDB().update("locations", row, "name=?", args);
                    AreaCard ac = new AreaCard(getMainActivity(), page);
                    Bundle b = new Bundle();
                    b.putString("name", edMessage.getText().toString());
                    ac.setArguments(b);
                    getMainActivity().getHeader().setTextHeader(edMessage.getText().toString());
                    getMainActivity().getSupportFragmentManager().beginTransaction().replace(R.id.areaPage, ac).commit();
                }
                else setError(true);
            }
        });
        edMessage = (EditText)rView.findViewById(R.id.edMessageNew);
        edMessage.setHint(act.getResources().getString(R.string.area_name_hint));
        edMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edMessage.getText().toString().length()>32){
                    String ss= edMessage.getText().toString();
                    edMessage.setText(ss.substring(0,31));
                }
                setError(false);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        RelativeLayout rlHeader = (RelativeLayout) rView.findViewById(R.id.rlMessageNewHeader);
        rlHeader.getLayoutParams().height = 0;
        TextView tvCounter = (TextView) rView.findViewById(R.id.tvMessageCounter);
        tvCounter.setText(currname+"/"+currname);
        rlEditTextNormal = (RelativeLayout) rView.findViewById(R.id.rlEditTextNormal);
        rlEditTextError  = (RelativeLayout) rView.findViewById(R.id.rlEditTextError);
        TextView tvError = (TextView) rView.findViewById(R.id.tvTextError);
        tvError.setText(getMainActivity().getResources().getString(R.string.area_name_error));
        setError(false);
    }

    private boolean checkName(String s) {
        String[] args = {edMessage.getText().toString()};
        Cursor c = act.getDB().query("locations", null, "name=?", args, null, null, null);
        return !c.moveToFirst();
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

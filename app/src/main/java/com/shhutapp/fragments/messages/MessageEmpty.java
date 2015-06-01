package com.shhutapp.fragments.messages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.pages.BasePage;

/**
 * Created by victor on 04.05.15.
 */
public class MessageEmpty extends BaseFragments {
    private BasePage page;
    public MessageEmpty(){
        super(MainActivity.getMainActivity());
    }
    public MessageEmpty(MainActivity act){
        super(act);
    }
    public MessageEmpty(MainActivity act, BasePage page){
        super(act);
        this.page = page;
    }
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState){
        rView = inf.inflate(R.layout.messages_list, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
    }
}

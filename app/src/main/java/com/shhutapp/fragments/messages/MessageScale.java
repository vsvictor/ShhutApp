package com.shhutapp.fragments.messages;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.pages.MessagePage;

/**
 * Created by victor on 03.05.15.
 */
public class MessageScale extends BaseFragments {
    private MessagePage page;
    public MessageScale(){
        super();
    }
    @SuppressLint("ValidFragment")
    public MessageScale(MainActivity act){
        super(act);
    }
    @SuppressLint("ValidFragment")
    public MessageScale(MainActivity act, MessagePage page) {
        super(act);
        this.page = page;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState){
        rView = inf.inflate(R.layout.scale_messages, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved){
        super.onViewCreated(view,saved);
    }
}

package com.shhutapp.fragments.settings;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.pages.BasePage;

/**
 * Created by victor on 29.09.15.
 */
public class About extends BaseFragments {
    private BasePage prev;
    public About(){
        super(MainActivity.getMainActivity());
    }
    @SuppressLint("ValidFragment")
    public About(MainActivity act){
        super(act);
    }
    @SuppressLint("ValidFragment")
    public About(MainActivity act, BasePage prev){
        super(act);
        this.prev = prev;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.about, container, false);
        return rView;
    }
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
    }
}

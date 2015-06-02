package com.shhutapp.fragments.area;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.pages.BasePage;

import org.apache.log4j.chainsaw.Main;

/**
 * Created by victor on 03.06.15.
 */
public class AreaCard extends BaseFragments {
    private BasePage page;

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
    }
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.map_card, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
    }
}

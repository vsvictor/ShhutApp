package com.shhutapp.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.fragments.area.Map;

/**
 * Created by victor on 28.05.15.
 */
public class AreaPage extends BasePage{
    public BasePage prev;
    private Map ma;
    public AreaPage(){
        super(MainActivity.getMainActivity());
    }
    public AreaPage(MainActivity act){
        super(act);
    }
    public AreaPage(MainActivity act, BasePage prev){
        super(act);
        this.prev = prev;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        ma = new Map(getMainActivity(), this);
        rootView = inf.inflate(R.layout.area_page, container, false);
        return rootView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        getMainActivity().getSupportFragmentManager().beginTransaction().add(R.id.areaPage, ma).commit();
    }
    @Override
    public void onResume(){
        super.onResume();
    }
    @Override
    public int getID() {
        return Pages.areaPage;
    }
}

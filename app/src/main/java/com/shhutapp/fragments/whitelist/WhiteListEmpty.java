package com.shhutapp.fragments.whitelist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.pages.BasePage;
import com.shhutapp.pages.WhiteListPage;

/**
 * Created by victor on 16.05.15.
 */
public class WhiteListEmpty extends BaseFragments {
    private WhiteListPage page;
    public WhiteListEmpty(){
        super(MainActivity.getMainActivity());
    }
    public WhiteListEmpty(MainActivity act){
        super(act);
    }
    public WhiteListEmpty(MainActivity act, WhiteListPage page){
        super(act);
        this.page = page;
    }
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState){
        rView = inf.inflate(R.layout.whitelist, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
    }

}

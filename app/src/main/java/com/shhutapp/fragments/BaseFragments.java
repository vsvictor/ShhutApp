package com.shhutapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.shhutapp.MainActivity;
import com.shhutapp.utils.Convertor;

public abstract class BaseFragments  extends Fragment{
    protected View rView;
    protected MainActivity act;
    protected BaseFragments i_am;
    protected int height = -1;
    protected BaseFragments owner;
    public BaseFragments(){
        rView = null;
        act = null;
    }
    public BaseFragments(MainActivity act){
        rView = null;
        this.act = act;
    }
    public void setHeight(int dp){
        int pixels = Math.round(Convertor.convertDpToPixel(dp, act));
        if(rView != null) {
            ViewGroup.LayoutParams param = rView.getLayoutParams();
            param.height = pixels;
            rView.setLayoutParams(param);
        }
        else height = dp;
    }
    public void onViewCreated(View view, Bundle saved){
        super.onViewCreated(view, saved);
        if(height >= 0) {
            setHeight(height);
            height = -1;
        }
    }
    public BaseFragments getIAm(){
        return this;
    }
    public MainActivity getMainActivity(){
        return MainActivity.getMainActivity();
    }
    public void onUpdateData(){}
    public void showKeyboadr(){
        InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }
    public void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)act.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }
    public void setOwner(BaseFragments own){
        this.owner = own;
    }
    public BaseFragments getOwner(){
        return owner;
    }
}

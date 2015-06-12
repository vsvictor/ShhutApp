package com.shhutapp.pages;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.v4.app.FragmentManager;
import android.view.inputmethod.InputMethodManager;

import com.shhutapp.MainActivity;

public abstract class BasePage extends Fragment {
    public enum LastOperation{Add, Delete, Edit, Select};
    protected View rootView;
    protected MainActivity act;
    protected BasePage prev;
    protected BasePage next;
    protected BasePage current;
    protected int prevID;
    protected LastOperation lastOperation;
    public BasePage() {
        rootView = null;
    }
    public BasePage(MainActivity act){
        this();
        this.act = act;
    }
    public BasePage(MainActivity act, BasePage prev){
        this(act);
        this.prev = prev;
    }
    public BasePage(MainActivity act, BasePage prev, BasePage next){
        this(act, prev);
        this.next = next;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        current = this;
    }
    public BasePage getCurrent(){
        return current;
    }
    public MainActivity getMainActivity(){
        return MainActivity.getMainActivity();
    }
    public FragmentManager fragmentManager(){
        return getMainActivity().getSupportFragmentManager();
    }
    public void showKeyboadr(){
        InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }
    public void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }
    public void setLastOperation(LastOperation oper){
        lastOperation = oper;
    }
    public LastOperation getLastOperation(){
        return lastOperation;
    }
    public BasePage getIAm(){return this;}
    public abstract int getID();
    public static class Pages{
        public static final int mainPage = 0;
        public static final int messagePage = 1;
        public static final int whitelistPage = 2;
        public static final int queitTimePage = 3;
        public static final int dreamPage = 4;
        public static final int settingsPage = 5;
        public static final int areaPage = 6;
        public static final int areaCard = 7;
        public static final int startHelp = 8;
    }
    public BasePage getPrev(){
        return prev;
    }
    public int getPrevID(){
        return -1;
    }
}

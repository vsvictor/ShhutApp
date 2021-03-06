package com.shhutapp.pages;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.data.WhiteListCard;
import com.shhutapp.fragments.Header;
import com.shhutapp.fragments.OnBackListener;
import com.shhutapp.fragments.area.AreaCard;
import com.shhutapp.fragments.messages.MessageListListener;
import com.shhutapp.fragments.whitelist.WhiteListAppCont;
import com.shhutapp.fragments.whitelist.WhiteListApplications;
import com.shhutapp.fragments.whitelist.WhiteListContacts;
import com.shhutapp.fragments.whitelist.WhiteListEmpty;
import com.shhutapp.fragments.whitelist.WhiteListList;
import com.shhutapp.fragments.whitelist.WhiteListNew;

/**
 * Created by victor on 16.05.15.
 */
public class WhiteListPage extends BasePage{
    private Header header;
    private WhiteListEmpty empty;
    private WhiteListList whitelistList;
    private WhiteListNew whitelistNew;
    public WhiteListAppCont whitelistAppCont;
    private WhiteListContacts whitelistCont;
    private WhiteListApplications whitelistApp;
    private BasePage prev;
    private WhiteListPage instance;
    //private WhiteListAddContact whiteListAddContact;
    public WhiteListPage(){
        super(MainActivity.getMainActivity());
    }
    @SuppressLint("ValidFragment")
    public WhiteListPage(MainActivity act){
        super(act);
    }
    @SuppressLint("ValidFragment")
    public WhiteListPage(MainActivity act, BasePage prev){
        super(act);
        this.prev = prev;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        prevID = getArguments().getInt("back");
        boolean isRadio = false;
        try{
            isRadio = getArguments().getBoolean("isRadio");
        }catch (NullPointerException e){
            isRadio = false;
        }
        header = getMainActivity().getHeader();
        empty = new WhiteListEmpty(getMainActivity(), this);
        whitelistList = new WhiteListList(getMainActivity(), this);
        Bundle bList = new Bundle();
        bList.putBoolean("isRadio", isRadio);
        bList.putInt("back", prevID);
        whitelistList.setArguments(bList);
        whitelistNew = new WhiteListNew(getMainActivity(), this);
        instance = this;
    }
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rootView = inf.inflate(R.layout.whitelist_page, container, false);
        if(act.isWhiteListEmpty()) {
            fragmentManager().beginTransaction().
                    addToBackStack(null).
                    add(R.id.whitelistPage, empty).
                    commit();
        }
        else{
            fragmentManager().beginTransaction().
                    addToBackStack(null).
                    add(R.id.whitelistPage, whitelistList).
                    commit();
        }
        return rootView;
    }

    public void onViewCreated(View view, Bundle saved){
        super.onViewCreated(view, saved);
        header.setHeight(56);
        header.setInvisibleAll();
        header.setLeftText(72);
        whitelistList.setHeight(getMainActivity().isWhiteListEmpty() ? 486 : 560);
        whitelistList.showEmpty(getMainActivity().isWhiteListEmpty());
        whitelistList.setOnMessageListListener(new MessageListListener() {
            @Override
            public void onAdd() {
                header.setInvisibleAll();
                header.setVisibleCancel(true);
                whitelistNew = new WhiteListNew(getMainActivity(), instance);
                Bundle b = new Bundle();
                b.putBoolean("isArgs", false);
                b.putInt("count", whitelistList.getCount());
                whitelistNew.setArguments(b);
                fragmentManager().beginTransaction().
                        addToBackStack(null).
                        remove(whitelistList).
                        remove(empty).
                        add(R.id.whitelistPage, whitelistNew).
                        commit();
            }

            @Override
            public void onDelete() {
            }

            @Override
            public void onEdit(int id) {
                header.setInvisibleAll();
                header.setVisibleBack(true);
                Bundle b = new Bundle();
                b.putBoolean("isArgs", true);
                b.putInt("id", id);
                whitelistAppCont = new WhiteListAppCont(getMainActivity(), (WhiteListPage) getIAm());
                whitelistAppCont.setArguments(b);
                fragmentManager().beginTransaction().
                        addToBackStack(null).
                        remove(whitelistList).
                        remove(empty).
                        add(R.id.whitelistPage, whitelistAppCont).
                        commit();
            }

            @Override
            public void onSelected(int id) {
                WhiteListCard wl = getMainActivity().getDBHelper().loadWhiteLists(id);
                getMainActivity().selectWhiteList(wl);
            }
        });
        empty.setOnMessageListListener(new MessageListListener() {
            @Override
            public void onAdd() {
                header.setInvisibleAll();
                header.setVisibleCancel(true);
                whitelistList = new WhiteListList(getMainActivity(), instance);
                Bundle b = new Bundle();
                b.putBoolean("isArgs", false);
                b.putInt("count", 0);
                whitelistNew.setArguments(b);
                fragmentManager().beginTransaction().
                        addToBackStack(null).
                        remove(whitelistList).
                        remove(empty).
                        add(R.id.whitelistPage, whitelistNew).
                        commit();
            }
            @Override
            public void onDelete() {}
            @Override
            public void onEdit(int id) {}
            @Override
            public void onSelected(int id) {
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        header.setTextHeader(getMainActivity().getResources().getString(R.string.whitelist));
        header.setInvisibleAll();
        header.setVisibleBack(true);
        header.setOnBackListener(new OnBackListener() {
            @Override
            public void onBack() {
                switch (prevID) {
                    case BasePage.Pages.mainPage: {
                        BasePage p = getMainActivity().createPageFromID(prevID);
                        getMainActivity().getSupportFragmentManager().beginTransaction().
                                addToBackStack(null).
                                remove(getCurrent()).
                                add(R.id.container, p).
                                commit();
                        /*getMainActivity().getSupportFragmentManager().beginTransaction().
                                add(R.id.container, p).
                                commit();*/
                        break;
                    }
                    case BasePage.Pages.queitTimePage: {
                        getMainActivity().getHeader().setVisibleBack(false);
                        getMainActivity().getHeader().setVisibleCancel(true);
                        getMainActivity().getSupportFragmentManager().beginTransaction().
                                addToBackStack(null).
                                remove(getCurrent()).
                                show(QueitTimePage.instance).
                                commit();
                        break;
                    }
                    case BasePage.Pages.areaCard: {
                        getMainActivity().getHeader().setVisibleBack(false);
                        getMainActivity().getHeader().setVisibleCancel(true);
                        getMainActivity().getSupportFragmentManager().beginTransaction().
                                addToBackStack(null).
                                remove(getCurrent()).
                                show(AreaCard.instance).
                                commit();
                        break;
                    }
                }
            }
        });
    }
    public WhiteListNew getWhitelistNew(){
        return whitelistNew;
    }
    public WhiteListList getWhitelistList(){
        return whitelistList;
    }
    public WhiteListEmpty getWhiteListEmpty(){
        return empty;
    }
    public int getPrevID(){
        return prevID;
    }
    @Override
    public int getID(){
        return Pages.whitelistPage;
    }
    public BasePage getPrev(){return prev;}
}

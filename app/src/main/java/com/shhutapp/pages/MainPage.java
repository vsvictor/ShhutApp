package com.shhutapp.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shhutapp.MainActivity;
import com.shhutapp.OkCancelListener;
import com.shhutapp.AppSettings;
import com.shhutapp.fragments.CardList;
import com.shhutapp.fragments.Header;
import com.shhutapp.R;
import com.shhutapp.fragments.MainControlPanel;
import com.shhutapp.fragments.MainControlPanelListener;
import com.shhutapp.fragments.OkCancel;
import com.shhutapp.fragments.OnMenuListener;
import com.shhutapp.fragments.Scale;
import com.shhutapp.utils.DateTimeOperator;

import java.util.Calendar;
import java.util.Date;

public class MainPage extends BasePage {
    private Header header;
    private Scale scale;
    private MainControlPanel panel;
    private OkCancel okcancel;
    private CardList cardList;
    public MainPage() {
        super(MainActivity.getMainActivity());
    }

    public MainPage(MainActivity act) {
        super(act);
        header = getMainActivity().getHeader();
        scale = new Scale(getMainActivity(), this);
        panel = new MainControlPanel(getMainActivity());
        okcancel = new OkCancel(getMainActivity());
        cardList = new CardList(getMainActivity(), this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            fragmentManager().beginTransaction().add(R.id.mainPage, scale).commit();
            fragmentManager().beginTransaction().add(R.id.mainPage, panel).commit();
            fragmentManager().beginTransaction().add(R.id.mainPage, okcancel).commit();
            fragmentManager().beginTransaction().add(R.id.mainPage, cardList).commit();
            fragmentManager().beginTransaction().hide(okcancel).commit();
        }catch (IllegalStateException e){
        }

    }

    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rootView = inf.inflate(R.layout.main_page, container, false);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        header.setInvisibleAll();
        header.setHeight(56);
        header.setLeftText(16);
        header.setTextHeader(getMainActivity().getResources().getString(R.string.app_name));
        header.setVisibleMenu(true);
        //if(!header.isAdded()){
        //    fragmentManager().beginTransaction().add(R.id.header, header).commit();
        //}
        scale.setHeight(82);
        panel.setHeight(72);
        panel.setOnMainControlPanelListener(new MainControlPanelListener() {
            @Override
            public void onDreamMode() {
                DreamPage dp = new DreamPage(getMainActivity());
                Bundle args = new Bundle();
                args.putInt("back", getID());
                dp.setArguments(args);
                getMainActivity().getSupportFragmentManager().beginTransaction().hide(header).commit();
                getMainActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, dp).commit();
                //MainActivity.mpage = null;
            }

            @Override
            public void onQuietHours() {
                QueitTimePage qt = new QueitTimePage(getMainActivity());
                Bundle args = new Bundle();
                args.putInt("back", getID());
                qt.setArguments(args);
                getMainActivity().getSupportFragmentManager().beginTransaction().remove(getCurrent()).commit();
                getMainActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, qt).commit();
            }

            @Override
            public void onWhiteList() {
                WhiteListPage wlp = new WhiteListPage(getMainActivity());
                Bundle args = new Bundle();
                args.putInt("back", getID());
                wlp.setArguments(args);
                getMainActivity().getSupportFragmentManager().beginTransaction().remove(getCurrent()).commit();
                getMainActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, wlp).commit();
            }

            @Override
            public void onMessages() {
                MessagePage mp = new MessagePage(getMainActivity());
                Bundle args = new Bundle();
                args.putInt("back", getID());
                mp.setArguments(args);
                getMainActivity().getSupportFragmentManager().beginTransaction().
                        remove(getCurrent()).
                        add(R.id.container, mp).
                        commit();
            }
        });
        okcancel.setOnOkCancelListener(new OkCancelListener() {
            @Override
            public void ok() {
                //scale.setTime(100);
                getMainActivity().setDream(true);
                getMainActivity().getSupportFragmentManager().beginTransaction().hide(okcancel).commit();
            }

            @Override
            public void cancel() {
                getMainActivity().getSupportFragmentManager().beginTransaction().hide(okcancel).commit();
            }
        });
        header.setOnMenuListener(new OnMenuListener() {
            @Override
            public void onMenu() {
                SettingsPage sp = new SettingsPage(getMainActivity());
                Bundle args = new Bundle();
                args.putInt("back", getID());
                sp.setArguments(args);
                getMainActivity().getSupportFragmentManager().beginTransaction().
                        remove(getCurrent()).
                        add(R.id.container, sp).
                        commit();
            }
        });
        cardList.showEmpty(getMainActivity().isCardListEmty());
    }
    @Override
    public void onPause(){
        super.onPause();
        fragmentManager().beginTransaction().
                remove(scale).
                remove(panel).
                remove(okcancel).
                remove(cardList).
                remove(okcancel).
                commit();
    }
    public CardList getCardList(){
        return cardList;
    }
    public OkCancel getOkCancel(){
        return okcancel;
    }

    @Override
    public int getID() {
        return Pages.mainPage;
    }
}

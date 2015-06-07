package com.shhutapp.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.data.QueitCard;
import com.shhutapp.fragments.OnBackListener;
import com.shhutapp.fragments.OnCancelListener;
import com.shhutapp.fragments.OnOkListener;
import com.shhutapp.fragments.messages.MessageListListener;
import com.shhutapp.fragments.queittime.QueitTimeControlPanel;
import com.shhutapp.fragments.queittime.QueitTimeControlPanelListener;
import com.shhutapp.fragments.queittime.QueitTimeDays;
import com.shhutapp.fragments.queittime.QueitTimeDaysListener;
import com.shhutapp.fragments.queittime.QueitTimeList;
import com.shhutapp.fragments.queittime.QueitTimeScale;
import com.shhutapp.utils.DateTimeOperator;

/**
 * Created by victor on 19.05.15.
 */
public class QueitTimePage extends BasePage {
    private QueitTimeList qt_list;
    private QueitTimeScale qt_scale;
    private QueitTimeControlPanel qt_control;
    private QueitTimeDays qt_days;
    private boolean[] days;
    public static QueitTimePage instance;
    public QueitTimePage(){
        super(MainActivity.getMainActivity());
        days = new boolean[7];
    }
    public QueitTimePage(MainActivity act){
        super(act);
        qt_list = new QueitTimeList(getMainActivity(),this);
        qt_scale = new QueitTimeScale(getMainActivity(), this);
        qt_control = new QueitTimeControlPanel(getMainActivity(), this);
        qt_days = new QueitTimeDays(getMainActivity(), this);
        days = new boolean[7];
    }
    public QueitTimePage(MainActivity act, BasePage prev){
        super(act);
        qt_list = new QueitTimeList(getMainActivity(),this);
        qt_scale = new QueitTimeScale(getMainActivity(), this);
        qt_control = new QueitTimeControlPanel(getMainActivity(), this);
        qt_days = new QueitTimeDays(getMainActivity(), this);
        days = new boolean[7];
        this.prev = prev;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        instance = this;
        try {
            prevID = getArguments().getInt("back");
        }catch (NullPointerException e){
            prevID = Pages.mainPage;
        }
        for(int i = 0;i<7;i++) days[i] = false;
    }
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rootView = inf.inflate(R.layout.queittime_page, container, false);
        fragmentManager().beginTransaction().add(R.id.queitTimePage, qt_list).commit();
        getMainActivity().getHeader().setOnOkListener(new OnOkListener() {
            @Override
            public void onOk() {
                long sms_id = -1;
                if (getMainActivity().getSMS() != null) {
                    sms_id = getMainActivity().getSMS().getID();
                    getMainActivity().clearSMS();
                }
                long wl_id = -1;
                if (getMainActivity().getWhiteList() != null) {
                    wl_id = getMainActivity().getWhiteList().getID();
                    getMainActivity().clearWhiteList();
                }
                QueitCard card = new QueitCard();
                String s = getMainActivity().getHeader().getTextHeader();
                String[] ss = s.trim().split("-");
                card.setBegin(DateTimeOperator.toDateTime(ss[0].trim(), "HH:mm"));
                card.setEnd(DateTimeOperator.toDateTime(ss[1].trim(), "HH:mm"));
                card.setDays(days);
                card.save(getMainActivity().getDB(), sms_id, wl_id);
                if (qt_control.isAdded()) {
                    fragmentManager().beginTransaction().
                            remove(qt_scale).
                            remove(qt_control).
                            show(qt_list).
                            commit();
                } else {
                    fragmentManager().beginTransaction().
                            //remove(qt_list).
                                    remove(qt_days).
                            remove(qt_scale).
                            show(qt_list).
                            commit();
                }
                getMainActivity().getHeader().setInvisibleAll();
                getMainActivity().getHeader().setVisibleBack(true);
                getMainActivity().getHeader().setTextHeader(getMainActivity().getResources().getString(R.string.queittime));
            }
        });
        getMainActivity().getHeader().setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel() {
                if (qt_control.isAdded()) {
                    fragmentManager().beginTransaction().
                            remove(qt_scale).
                            remove(qt_control).
                            show(qt_list).
                            commit();
                } else {
                    fragmentManager().beginTransaction().
                            //remove(qt_list).
                                    remove(qt_days).
                            remove(qt_scale).
                            show(qt_list).
                            commit();
                }
                getMainActivity().getHeader().setInvisibleAll();
                getMainActivity().getHeader().setVisibleBack(true);
                getMainActivity().getHeader().setTextHeader(getMainActivity().getResources().getString(R.string.queittime));
            }
        });
        return rootView;
    }
    public void onViewCreated(View view, Bundle saved){
        super.onViewCreated(view, saved);

        qt_list.setOnMessageListListener(new MessageListListener() {
            @Override
            public void onAdd() {
                getMainActivity().getHeader().setInvisibleAll();
                getMainActivity().getHeader().setVisibleOk(true);
                getMainActivity().getHeader().setVisibleCancel(true);
                fragmentManager().beginTransaction().hide(qt_list).add(R.id.queitTimePage, qt_scale).commit();
                fragmentManager().beginTransaction().add(R.id.queitTimePage, qt_control).commit();
            }
            @Override
            public void onDelete() {

            }

            @Override
            public void onEdit(int id) {

            }

            @Override
            public void onSelected() {

            }
        });
        qt_control.setOnQueitTimeControlPanelListener(new QueitTimeControlPanelListener() {
            @Override
            public void onDays() {
                fragmentManager().beginTransaction().
                        remove(qt_list).
                        remove(qt_control).
                        add(R.id.queitTimePage, qt_days).
                        add(R.id.queitTimePage, qt_list).
                        commit();
            }

            @Override
            public void onWhiteList() {
                WhiteListPage wlp = new WhiteListPage(getMainActivity(), instance);
                Bundle args = new Bundle();
                args.putInt("back", getID());
                args.putBoolean("isRadio", true);
                wlp.setArguments(args);
                getMainActivity().getSupportFragmentManager().beginTransaction().hide(instance).add(R.id.container, wlp).commit();
            }

            @Override
            public void onMessage() {
                MessagePage mp = new MessagePage(getMainActivity(), instance);
                Bundle args = new Bundle();
                args.putInt("back", getID());
                mp.setArguments(args);
                getMainActivity().getSupportFragmentManager().beginTransaction().hide(instance).add(R.id.container, mp).commit();
            }
        });
        qt_days.setOnQueitTimeDaysListener(new QueitTimeDaysListener() {
            @Override
            public void unvisible(boolean[] sel) {
                for (int i = 0; i < 7; i++) {
                    days[i] = sel[i];
                }
                fragmentManager().beginTransaction().
                        remove(qt_list).
                        remove(qt_days).
                        add(R.id.queitTimePage, qt_control).
                        add(R.id.queitTimePage, qt_list).
                        hide(qt_control).
                        commit();
                fragmentManager().beginTransaction().
                        show(qt_control).
                        commit();

            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
    }
    @Override
    public void onHiddenChanged(boolean hidden){
        super.onHiddenChanged(hidden);
        qt_control.onHiddenChanged(hidden);
        if(!hidden) {
            getMainActivity().getHeader().setOnBackListener(new OnBackListener() {
                @Override
                public void onBack() {
                    BasePage p = getMainActivity().createPageFromID(getPrevID());
                    Bundle args = new Bundle();
                    args.putInt("back", p.getID());
                    p.setArguments(args);
                    getMainActivity().getSupportFragmentManager().beginTransaction().remove(getCurrent()).commit();
                    getMainActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, p).commit();
                }
            });
        }
    }
    public boolean isDays(){

        for (int i = 0; i<7; i++){
            if(days[i]) return true;
        }
        return false;
    }
    public int getPrevID(){
        return prevID;
    }
    @Override
    public int getID() {
        return Pages.queitTimePage;
    }
}

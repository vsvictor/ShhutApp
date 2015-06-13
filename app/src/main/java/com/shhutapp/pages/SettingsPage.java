package com.shhutapp.pages;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shhutapp.fragments.OnBackListener;
import com.shhutapp.fragments.OnHelpListener;
import com.shhutapp.fragments.area.AreaCard;
import com.shhutapp.social.common.AuthListener;
import com.shhutapp.social.facebook.FacebookFacade;
import com.shhutapp.social.twitter.TwitterFacade;
import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.fragments.settings.OnNetworks;
import com.shhutapp.fragments.settings.SettingsSN;
import com.shhutapp.social.SocialCommon;
import com.shhutapp.start.StartHelpFirst;

/**
 * Created by victor on 26.05.15.
 */
public class SettingsPage extends BasePage {
    private BasePage page;
    private BasePage instance;
    private SettingsSN sn;

    public SettingsPage(){
        super(MainActivity.getMainActivity());
        sn = new SettingsSN(getMainActivity());
        this.instance = this;
    }
    public SettingsPage(MainActivity act){
        super(act);
        sn = new SettingsSN(act);
        this.instance = this;
    }
    public SettingsPage(MainActivity act, BasePage page){
        super(act);
        this.page = page;
        sn = new SettingsSN(act, page);
        this.instance = this;
        this.page = page;
    }
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rootView = inf.inflate(R.layout.settings_page, container, false);
        return rootView;
    }
    public void onViewCreated(View view, Bundle saved){
        super.onViewCreated(view, saved);
        fragmentManager().beginTransaction().add(R.id.settingsPage,sn).commit();
        getMainActivity().getHeader().setInvisibleAll();
        getMainActivity().getHeader().setLeftText(73);
        getMainActivity().getHeader().setTextHeader(getMainActivity().getResources().getString(R.string.settings));
        getMainActivity().getHeader().setVisibleBack(true);
        getMainActivity().getHeader().setVisibleHelp(true);
        getMainActivity().getHeader().setOnHelpListener(new OnHelpListener() {
            @Override
            public void onHelp() {
                getMainActivity().setHelpInStart(false);
                BasePage page = getMainActivity().createPageFromID(prevID);
                getMainActivity().getSupportFragmentManager().beginTransaction().remove(getCurrent()).commit();
                getMainActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, page).commit();
                StartHelpFirst help = new StartHelpFirst(getMainActivity());//Help(this);
                getMainActivity().getSupportFragmentManager().beginTransaction().add(R.id.main, help).commitAllowingStateLoss();
            }
        });
        getMainActivity().getHeader().setOnBackListener(new OnBackListener() {
            @Override
            public void onBack() {
                BasePage page = getMainActivity().createPageFromID(prevID);
                getMainActivity().getSupportFragmentManager().beginTransaction().remove(getCurrent()).commit();
                getMainActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, page).commit();
            }
        });

        sn.setOnNetworkListener(new OnNetworks() {
            @Override
            public void onFacebook() {
                Log.i("Facebook", "Click");
                String fb_id = getMainActivity().getResources().getString(R.string.fb_id);
                final FacebookFacade fb = new FacebookFacade(getMainActivity(), fb_id);
                if (fb.isAuthorized()) {
                    //fb.logout();
                    fb.publishMessage(SocialCommon.getText(), SocialCommon.getUrl(), SocialCommon.getName(), SocialCommon.getDescription());
                } else {
                    fb.authorize(new AuthListener() {
                        @Override
                        public void onAuthSucceed() {
                            fb.publishMessage(SocialCommon.getText(), SocialCommon.getUrl(), SocialCommon.getName(), SocialCommon.getDescription());
                        }

                        @Override
                        public void onAuthFail(String error) {
                        }
                    });
                }
            }

            @Override
            public void onTwitter() {
                Log.i("Twitter", "Click");
                String tw_key = getMainActivity().getResources().getString(R.string.twitter_api_key);
                String tw_secret = getMainActivity().getResources().getString(R.string.twitter_api_secret);
                final TwitterFacade tw = new TwitterFacade(getMainActivity(), tw_key, tw_secret);
                if (tw.isAuthorized()) {
                    tw.publishMessage(SocialCommon.getText());
                } else {
                    tw.authorize(new AuthListener() {
                        @Override
                        public void onAuthSucceed() {
                            tw.publishMessage(SocialCommon.getText());
                        }

                        @Override
                        public void onAuthFail(String error) {
                        }
                    });
                }
            }

            @Override
            public void onVK() {
            }

            @Override
            public void onSMS() {
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
    }
    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public int getID() {
        return Pages.settingsPage;
    }
}

package com.shhutapp.pages;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shhutapp.fragments.OnBackListener;
import com.shhutapp.fragments.OnHelpListener;
import com.shhutapp.fragments.area.AreaCard;
import com.shhutapp.fragments.settings.About;
import com.shhutapp.fragments.settings.MainSettings;
import com.shhutapp.fragments.settings.OnAbout;
import com.shhutapp.social.common.AuthListener;
import com.shhutapp.social.facebook.FacebookFacade;
import com.shhutapp.social.twitter.TwitterFacade;
import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.fragments.settings.OnNetworks;
import com.shhutapp.fragments.settings.SettingsSN;
import com.shhutapp.social.SocialCommon;
import com.shhutapp.start.StartHelpFirst;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKWallPostResult;
import com.vk.sdk.dialogs.VKCaptchaDialog;
import com.vk.sdk.util.VKUtil;

/**
 * Created by victor on 26.05.15.
 */
public class SettingsPage extends BasePage {
    private BasePage page;
    private BasePage instance;
    private SettingsSN sn;
    private MainSettings ms;
    private static final String[] sMyScope = new String[] {

            VKScope.FRIENDS,
            VKScope.WALL,
            VKScope.PHOTOS,
            VKScope.NOHTTPS
    };
    public SettingsPage(){
        super(MainActivity.getMainActivity());
        sn = new SettingsSN(getMainActivity());
        ms = new MainSettings(getMainActivity());
        this.instance = this;
    }
    @SuppressLint("ValidFragment")
    public SettingsPage(MainActivity act){
        super(act);
        sn = new SettingsSN(act);
        ms = new MainSettings(act);
        this.instance = this;
    }
    @SuppressLint("ValidFragment")
    public SettingsPage(MainActivity act, BasePage page){
        super(act);
        this.page = page;
        sn = new SettingsSN(act, page);
        ms = new MainSettings(act, page);
        this.instance = this;
        this.page = page;
    }
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        try {
            prevID = getArguments().getInt("prevID");
        }catch (Exception e){
            prevID = Pages.mainPage;
        }
    }
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rootView = inf.inflate(R.layout.settings_page, container, false);
        return rootView;
    }
    public void onViewCreated(View view, Bundle saved){
        super.onViewCreated(view, saved);
        fragmentManager().beginTransaction().add(R.id.rlNet,sn).add(R.id.rlMainSet, ms).commit();
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
                VKUIHelper.onCreate(getActivity());
                VKSdk.initialize(sdkListener, "4961271");
                if (VKSdk.wakeUpSession()) {
                    vkWallPost();
                    return;
                }

                String[] fingerprint = VKUtil.getCertificateFingerprint(getActivity(), getActivity().getPackageName());
                Log.d("Fingerprint", fingerprint[0]);
                VKSdk.authorize(sMyScope, true, true);

            }

            @Override
            public void onSMS() {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setType("vnd.android-dir/mms-sms");
                sendIntent.putExtra("sms_body", SocialCommon.getText());
                getMainActivity().startActivity(sendIntent);
            }
        });
        ms.setOnAbout(new OnAbout() {
            @Override
            public void onAbout() {
                About ab = new About(getMainActivity(), instance);
                //getMainActivity().getHeader().setHeight(0);
                getMainActivity().getSupportFragmentManager().beginTransaction().
                        addToBackStack(null).
                        remove(sn).
                        remove(ms).
                        add(R.id.main, ab).
                        commit();
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
    private void vkWallPost() {
        VKRequest post = VKApi.wall().post(VKParameters.from(VKApiConst.MESSAGE, SocialCommon.getText()));
        post.setModelClass(VKWallPostResult.class);

        post.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKWallPostResult result = (VKWallPostResult) response.parsedModel;
             /*   Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("https://vk.com/wall-60479154_%s", result.post_id)));
                startActivity(i);*/
            }

            @Override
            public void onError(VKError error) {
                showError(error.apiError != null ? error.apiError : error);
            }
        });
    }
    private void showError(VKError error) {
        new AlertDialog.Builder(getActivity())
                .setMessage(error.errorMessage)
                .setPositiveButton("OK", null)
                .show();

        if (error.httpError != null) {
            Log.w("Test", "Error in request or upload", error.httpError);
        }
    }
    private final VKSdkListener sdkListener = new VKSdkListener() {
        @Override
        public void onCaptchaError(VKError captchaError) {
            new VKCaptchaDialog(captchaError).show();
        }

        @Override
        public void onTokenExpired(VKAccessToken expiredToken) {
            VKSdk.authorize(sMyScope);
        }

        @Override
        public void onAccessDenied(final VKError authorizationError) {
            new AlertDialog.Builder(VKUIHelper.getTopActivity())
                    .setMessage(authorizationError.toString())
                    .show();
        }

        @Override
        public void onReceiveNewToken(VKAccessToken newToken) {
            vkWallPost();
        }

        @Override
        public void onAcceptUserToken(VKAccessToken token) {
            vkWallPost();
        }
    };
}

package com.shhutapp.fragments.whitelist;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.data.WhiteListCard;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.fragments.Header;
import com.shhutapp.fragments.OnBackListener;
import com.shhutapp.pages.WhiteListPage;
import android.support.v4.app.Fragment;
/**
 * Created by victor on 16.05.15.
 */
public class WhiteListAppCont extends BaseFragments {
    private WhiteListPage page;
    private RelativeLayout rlApplications;
    private RelativeLayout rlApplicationsRed;
    private RelativeLayout rlContacts;
    private RelativeLayout rlContactsRed;
    private WhiteListContacts whitelistCont;
    private WhiteListApplications whitelistApp;
    private Header header;
    private int listID = -1;
    private String sName;
    private WhiteListCard card;
    private TextView tvContactsText;
    private TextView tvApplicationsText;

    public WhiteListAppCont(){
        super(MainActivity.getMainActivity());
    }
    @SuppressLint("ValidFragment")
    public WhiteListAppCont(MainActivity act){
        super(act);
    }
    @SuppressLint("ValidFragment")
    public WhiteListAppCont(MainActivity act, WhiteListPage page){
        super(act);
        this.page = page;
        header = getMainActivity().getHeader();
        whitelistCont = new WhiteListContacts(getMainActivity(),page);
        whitelistCont.setOwner(this);
        whitelistApp = new WhiteListApplications(getMainActivity(), page);
        whitelistApp.setOwner(this);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if(b != null){
            listID = b.getInt("id");
        }
        card = new WhiteListCard(listID);
        card.load(getMainActivity().getDB());
        sName = card.getName();
        header.setTextHeader(sName);
    }

    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState){
        rView = inf.inflate(R.layout.whitelist_app_cont, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        final Bundle args = new Bundle();
        args.putInt("id", card.getID());
        args.putString("name", card.getName());
        tvContactsText = (TextView) rView.findViewById(R.id.tvContactsText);
        tvContactsText.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Medium.ttf"));
        tvApplicationsText = (TextView) rView.findViewById(R.id.tvApplicationsText);
        tvApplicationsText.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Medium.ttf"));

        rlApplications = (RelativeLayout) rView.findViewById(R.id.rlApplications);
        rlApplicationsRed = (RelativeLayout) rView.findViewById(R.id.rlApplicationRed);
        rlContacts = (RelativeLayout) rView.findViewById(R.id.rlContacts);
        rlContactsRed = (RelativeLayout) rView.findViewById(R.id.rlContactsRed);
        rlContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvContactsText = (TextView) rView.findViewById(R.id.tvContactsText);
                TextView tvApplicationsText = (TextView) rView.findViewById(R.id.tvApplicationsText);
                RelativeLayout rlContactsRed = (RelativeLayout) rView.findViewById(R.id.rlContactsRed);
                RelativeLayout rlApplicationsRed = (RelativeLayout) rView.findViewById(R.id.rlApplicationRed);
                tvContactsText.setTextColor(Color.argb(255, 255, 255, 255));
                tvApplicationsText.setTextColor(Color.argb(255, 153, 228, 238));
                rlContactsRed.setVisibility(View.VISIBLE);
                rlApplicationsRed.setVisibility(View.INVISIBLE);
                try {
                    whitelistCont.setArguments(args);
                } catch (IllegalStateException e) {
                }
                getMainActivity().getSupportFragmentManager().beginTransaction().
                        //addToBackStack(null).
                                remove(whitelistApp).
                        add(R.id.whitelistPage, whitelistCont).
                        commit();
                //white
            }
        });
        rlApplications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvContactsText = (TextView) rView.findViewById(R.id.tvContactsText);
                TextView tvApplicationsText = (TextView) rView.findViewById(R.id.tvApplicationsText);
                RelativeLayout rlContactsRed = (RelativeLayout) rView.findViewById(R.id.rlContactsRed);
                RelativeLayout rlApplicationsRed = (RelativeLayout) rView.findViewById(R.id.rlApplicationRed);
                tvContactsText.setTextColor(Color.argb(255, 153, 228, 238));
                tvApplicationsText.setTextColor(Color.argb(255, 255, 255, 255));
                rlContactsRed.setVisibility(View.INVISIBLE);
                rlApplicationsRed.setVisibility(View.VISIBLE);
                try {
                    whitelistApp.setArguments(args);
                } catch (IllegalStateException e) {
                }
                getMainActivity().getSupportFragmentManager().beginTransaction().
                        //addToBackStack(null).
                        remove(whitelistCont).
                        add(R.id.whitelistPage, whitelistApp).
                        commit();
            }
        });
        //if(whitelistCont != null) whitelistCont = new WhiteListContacts(getMainActivity(),page);
        whitelistCont.setArguments(args);
        getMainActivity().getSupportFragmentManager().beginTransaction().
                //remove(getIAm()).
                remove(whitelistApp).
                add(R.id.whitelistPage, whitelistCont).
                commit();
        header.setInvisibleAll();
        header.setVisibleBack(true);
        header.setOnBackListener(new OnBackListener() {
            @Override
            public void onBack() {
                Fragment ff = getMainActivity().getSupportFragmentManager().findFragmentByTag("AppCont");
                getMainActivity().getSupportFragmentManager().beginTransaction().
                        //addToBackStack(null).
                        remove(whitelistApp).
                        commit();
                getMainActivity().getSupportFragmentManager().beginTransaction().
                        //addToBackStack(null).
                        remove(whitelistCont).
                        commit();
                getMainActivity().getSupportFragmentManager().beginTransaction().
                        //addToBackStack(null).
                        remove(getIAm()).
                        add(R.id.whitelistPage, page.getWhitelistList()).
                        commit();
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        //getMainActivity().getHeader().setVisibleCounter(false);
    }
    @Override
    public void onPause(){
        super.onPause();
        getMainActivity().getSupportFragmentManager().beginTransaction().
                remove(whitelistApp).
                remove(whitelistCont).
                commit();

    }
}

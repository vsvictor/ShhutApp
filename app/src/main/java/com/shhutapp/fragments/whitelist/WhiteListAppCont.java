package com.shhutapp.fragments.whitelist;

import android.graphics.Color;
import android.os.Bundle;
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
    public WhiteListAppCont(){
        super(MainActivity.getMainActivity());
    }
    public WhiteListAppCont(MainActivity act){
        super(act);
    }
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
                tvApplicationsText.setTextColor(Color.argb(127, 255, 255, 255));
                rlContactsRed.setVisibility(View.VISIBLE);
                rlApplicationsRed.setVisibility(View.INVISIBLE);
                whitelistCont.setArguments(args);
                getMainActivity().getSupportFragmentManager().beginTransaction().
                        remove(whitelistApp).
                        add(R.id.whitelistPage, whitelistCont).
                        commit();
            }
        });
        rlApplications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvContactsText = (TextView) rView.findViewById(R.id.tvContactsText);
                TextView tvApplicationsText = (TextView) rView.findViewById(R.id.tvApplicationsText);
                RelativeLayout rlContactsRed = (RelativeLayout) rView.findViewById(R.id.rlContactsRed);
                RelativeLayout rlApplicationsRed = (RelativeLayout) rView.findViewById(R.id.rlApplicationRed);
                tvContactsText.setTextColor(Color.argb(127, 255, 255, 255));
                tvApplicationsText.setTextColor(Color.argb(255, 255, 255, 255));
                rlContactsRed.setVisibility(View.INVISIBLE);
                rlApplicationsRed.setVisibility(View.VISIBLE);
                whitelistApp.setArguments(args);
                getMainActivity().getSupportFragmentManager().beginTransaction().
                        remove(whitelistCont).
                        add(R.id.whitelistPage, whitelistApp).
                        commit();
            }
        });
        whitelistCont.setArguments(args);
        getMainActivity().getSupportFragmentManager().beginTransaction().
                remove(whitelistApp).
                add(R.id.whitelistPage, whitelistCont).
                commit();
        header.setInvisibleAll();
        header.setVisibleBack(true);
        header.setOnBackListener(new OnBackListener() {
            @Override
            public void onBack() {
                if (whitelistApp.isAdded()) {
                    getMainActivity().getSupportFragmentManager().beginTransaction().
                            remove(whitelistApp).
                            commit();
                }
                if (whitelistCont.isAdded()) {
                    getMainActivity().getSupportFragmentManager().beginTransaction().
                            remove(whitelistCont).
                            commit();
                }
                getMainActivity().getSupportFragmentManager().beginTransaction().
                        remove(getIAm()).
                        add(R.id.whitelistPage, page.getWhitelistList()).
                        commit();
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
    }
}

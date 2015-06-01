package com.shhutapp.fragments.whitelist;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.data.BaseObjectList;
import com.shhutapp.data.ContactCard;
import com.shhutapp.data.WhiteListCard;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.fragments.Header;
import com.shhutapp.fragments.OnBackListener;
import com.shhutapp.pages.WhiteListPage;

/**
 * Created by victor on 17.05.15.
 */
public class WhiteListContacts extends BaseFragments {
    //private Header header;
    private WhiteListPage page;
    private RelativeLayout rlContactPress;
    private ImageView ivAddContact;
    private ImageView ivAddAllContacts;
    private ImageView ivUnFamOn;
    private ImageView ivUnFamOff;
    private ImageView ivOrgOn;
    private ImageView ivOrgOff;
    private ImageView ivUrgOn;
    private ImageView ivUrgOff;

    private int id = -1;
    private WhiteListCard card;
    private WhiteListAddContact whitelistAddContact;
    public WhiteListContacts(){
        super(MainActivity.getMainActivity());
    }
    public WhiteListContacts(MainActivity act){
        super(act);
    }
    public WhiteListContacts(MainActivity act, WhiteListPage page){
        super(act);
        this.page = page;
        whitelistAddContact = new WhiteListAddContact(getMainActivity(), page);
        whitelistAddContact.setOwner(getOwner());
    }
    public void onCreate(Bundle saved){
        super.onCreate(saved);
        Bundle b = getArguments();
        if(b != null){
            id = b.getInt("id");
        }
        if(card == null) {
            card = new WhiteListCard(id);
            card.load(getMainActivity().getDB());
        }
    }
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState){
        rView = inf.inflate(R.layout.whitelist_contacts, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        ivAddContact = (ImageView) rView.findViewById(R.id.ivAddContact);
        ivAddAllContacts = (ImageView) rView.findViewById(R.id.ivAddAllContact);
        ivAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putInt("id", id);
                whitelistAddContact.setArguments(b);
                getMainActivity().getSupportFragmentManager().beginTransaction().
                        remove(getOwner()).
                        remove(getIAm()).
                        add(R.id.whitelistPage, whitelistAddContact).
                        commit();
            }
        });
        ivAddAllContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseObjectList all = getMainActivity().getDBHelper().loadContacts(card.getID());
                for(int i = 0;i<all.size();i++){
                    ContactCard c = (ContactCard)all.get(i);
                    card.insert(getMainActivity().getDB(),c);
                }
            }
        });
        ivUnFamOff = (ImageView) rView.findViewById(R.id.ivCBUnfamiliar_off);
        ivUnFamOn = (ImageView) rView.findViewById(R.id.ivCBUnfamiliar_on);
        ivUnFamOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivUnFamOff.setVisibility(View.INVISIBLE);
                ivUnFamOn.setVisibility(View.VISIBLE);
                card.setUnknown(true);
                card.save(getMainActivity().getDB());
            }
        });
        ivUnFamOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivUnFamOff.setVisibility(View.VISIBLE);
                ivUnFamOn.setVisibility(View.INVISIBLE);
                card.setUnknown(false);
                card.save(getMainActivity().getDB());
            }
        });
        ivOrgOff = (ImageView) rView.findViewById(R.id.ivCBOrganizations_off);
        ivOrgOn = (ImageView) rView.findViewById(R.id.ivCBOrganizations_on);
        ivOrgOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivOrgOff.setVisibility(View.INVISIBLE);
                ivOrgOn.setVisibility(View.VISIBLE);
                card.setOrganizations(true);
                card.save(getMainActivity().getDB());
            }
        });
        ivOrgOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivOrgOff.setVisibility(View.VISIBLE);
                ivOrgOn.setVisibility(View.INVISIBLE);
                card.setOrganizations(false);
                card.save(getMainActivity().getDB());
            }
        });
        ivUrgOff = (ImageView) rView.findViewById(R.id.ivCBUrgent_off);
        ivUrgOn = (ImageView) rView.findViewById(R.id.ivCBUrgent_on);
        ivUrgOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivUrgOff.setVisibility(View.INVISIBLE);
                ivUrgOn.setVisibility(View.VISIBLE);
                card.setUrgent(true);
                card.save(getMainActivity().getDB());
            }
        });
        ivUrgOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivUrgOff.setVisibility(View.VISIBLE);
                ivUrgOn.setVisibility(View.INVISIBLE);
                card.setUrgent(false);
                card.save(getMainActivity().getDB());
            }
        });

    }
    @Override
    public void onResume(){
        super.onResume();
        if(card.getUnknown()){
            ivUnFamOff.setVisibility(View.INVISIBLE);
            ivUnFamOn.setVisibility(View.VISIBLE);
        }
        else{
            ivUnFamOff.setVisibility(View.VISIBLE);
            ivUnFamOn.setVisibility(View.INVISIBLE);
        }
        if(card.getOrganizations()){
            ivOrgOff.setVisibility(View.INVISIBLE);
            ivOrgOn.setVisibility(View.VISIBLE);
        }
        else{
            ivOrgOff.setVisibility(View.VISIBLE);
            ivOrgOn.setVisibility(View.INVISIBLE);
        }
        if(card.getUrgent()){
            ivUrgOff.setVisibility(View.INVISIBLE);
            ivUrgOn.setVisibility(View.VISIBLE);
        }
        else{
            ivUrgOff.setVisibility(View.VISIBLE);
            ivUrgOn.setVisibility(View.INVISIBLE);
        }
    }
}

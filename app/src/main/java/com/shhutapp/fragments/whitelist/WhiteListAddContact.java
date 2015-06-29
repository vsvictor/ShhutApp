package com.shhutapp.fragments.whitelist;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.data.BaseObjectList;
import com.shhutapp.data.ContactCard;
import com.shhutapp.data.FilteredAdapter;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.fragments.OnBackListener;
import com.shhutapp.fragments.OnCancelListener;
import com.shhutapp.fragments.OnSearchListener;
import com.shhutapp.pages.WhiteListPage;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by victor on 17.05.15.
 */
public class WhiteListAddContact extends BaseFragments {
    private WhiteListPage page;
    private BaseObjectList contacts;
    private BaseObjectList selected;

    private WhiteListContactsAdapter adapterCont;
    private WhiteListSelectedAdapter adapterSelect;
    private ListView lvAdded;
    private ListView lvAll;
    private int id = -1;
    private String sName;
    private EditText edSearch;
    private RelativeLayout rlSelected;
    private RelativeLayout rlAll;
    private int hSelected;
    private int hAll;
    public WhiteListAddContact(){
        super(MainActivity.getMainActivity());
    }
    public WhiteListAddContact(MainActivity act){
        super(act);
    }
    public WhiteListAddContact(MainActivity act, WhiteListPage page){
        super(act);
        this.page = page;
    }
    public void onCreate(Bundle saved){
        super.onCreate(saved);
        id = getArguments().getInt("id");
        sName = getArguments().getString("name");
    }
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState){
        rView = inf.inflate(R.layout.whitelist_add_contact, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        rlSelected = (RelativeLayout) rView.findViewById(R.id.rlWhiteListAddContact);
        rlAll = (RelativeLayout) rView.findViewById(R.id.rlWhiteListAddContactList);
        hSelected = rlSelected.getLayoutParams().height;
        hAll = rlAll.getLayoutParams().height;
        lvAll = (ListView)rView.findViewById(R.id.lvAddContactList);
        lvAdded = (ListView)rView.findViewById(R.id.lvAddContactSelected);
        getMainActivity().getHeader().setOnSearchListener(new OnSearchListener() {
            @Override
            public void onSearch() {
                getMainActivity().setActionBarGray();
                getMainActivity().getHeader().setGray();
                getMainActivity().getHeader().setInvisibleAll();
                getMainActivity().getHeader().setVisibleTextHeader(false);
                getMainActivity().getHeader().setVisibleEditText(true);
                getMainActivity().getHeader().setVisibleBackSearch(true);
                getMainActivity().getHeader().setVisibleCancelSearch(true);
                edSearch = getMainActivity().getHeader().getEdSearch();
                edSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapterCont.resetFilter();
                        adapterCont.notifyDataSetUpdated();
                        adapterCont.getFilter().filter(edSearch.getText().toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                rlSelected.getLayoutParams().height = 0;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                rlAll.setLayoutParams(params);
            }
        });
        getMainActivity().getHeader().setOnCancelSearchListener(new OnCancelListener() {
            @Override
            public void onCancel() {
                edSearch.setText("");
                //adapterCont.resetFilter();
                //adapterCont.notifyDataSetUpdated();
                adapterCont = new WhiteListContactsAdapter(getMainActivity(), contacts);
                lvAll.setAdapter(adapterCont);
                lvAll.setSelection(0);
                lvAdded.setSelection(adapterSelect.getCount() - 1);
            }
        });
        getMainActivity().getHeader().setOnBackSearchListener(new OnBackListener() {
            @Override
            public void onBack() {
                LinearLayout.LayoutParams pSelected = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                pSelected.height = hSelected;
                rlSelected.setLayoutParams(pSelected);
                LinearLayout.LayoutParams pAll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                pAll.height = hAll;
                rlAll.setLayoutParams(pAll);
                getMainActivity().setActionBarBlue();
                getMainActivity().getHeader().setBlue();
                getMainActivity().getHeader().setInvisibleAll();
                getMainActivity().getHeader().setVisibleTextHeader(true);
                getMainActivity().getHeader().setVisibleBack(true);
                getMainActivity().getHeader().setVisibleSearch(true);
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        getMainActivity().getHeader().setInvisibleAll();
        getMainActivity().getHeader().setVisibleBack(true);
        getMainActivity().getHeader().setVisibleSearch(true);
        getMainActivity().getHeader().setTextHeader(getMainActivity().getResources().getString(R.string.familiar_nums));
        getMainActivity().getHeader().setVisibleCounter(true);
        getMainActivity().getHeader().setOnBackListener(new OnBackListener() {
            @Override
            public void onBack() {
                getMainActivity().getHeader().setCounter(0);
                getMainActivity().getHeader().setVisibleCounter(false);
                /*
                Bundle b = new Bundle();
                b.putInt("id", id);
                b.putString("name", sName);
                page.whitelistAppCont = new WhiteListAppCont(getMainActivity(), page);
                if(page.whitelistAppCont.getArguments() != null) {
                    page.whitelistAppCont.getArguments().putInt("id", id);
                    page.whitelistAppCont.getArguments().putString("name", sName);
                }
                else{
                    page.whitelistAppCont.setArguments(b);
                }
                */
                WhiteListContacts www = new WhiteListContacts(getMainActivity(), page);
                getMainActivity().getSupportFragmentManager().beginTransaction().
                        //addToBackStack(null).
                        remove(getIAm()).
                        add(R.id.whitelistPage, www).
                        commit();
            }
        });
        Bundle b = getArguments();
        if(b != null){
            id = b.getInt("id");
        }
        contacts = getMainActivity().getDBHelper().loadContacts(id);
        adapterCont = new WhiteListContactsAdapter(getMainActivity(),contacts);
        lvAll.setAdapter(adapterCont);
        selected = getMainActivity().getDBHelper().loadContactsSelected(id);
        adapterSelect = new WhiteListSelectedAdapter(getMainActivity(), selected);
        lvAdded.setAdapter(adapterSelect);
        getMainActivity().getHeader().setVisibleCounter(true);
        if(adapterSelect != null) getMainActivity().getHeader().setCounter(adapterSelect.getCount());
        else getMainActivity().getHeader().setCounter(0);
    }
    private class WhiteListContactsAdapter extends FilteredAdapter {
        public WhiteListContactsAdapter(Context context, BaseObjectList list) {
            super(context, list);
        }
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            ContactCard prevcard = null;
            if(position>0)prevcard = (ContactCard) getData().get(position - 1);
            final ContactCard card = (ContactCard) getData().get(position);
            view = this.getInflater().inflate(R.layout.contact_item_alphabet, parent, false);
            TextView tvSymbol = (TextView) view.findViewById(R.id.tvSymbol);
            tvSymbol.setText(getSymbol(card, prevcard));
            TextView tvText = (TextView) view.findViewById(R.id.tvNameWhiteListAddAlpha);
            tvText.setText(card.getName());
            CircleImageView avatar = (CircleImageView) view.findViewById(R.id.ivAvatarAlpha);
            if(card.getAvatar() != null) avatar.setImageBitmap(card.getAvatar());
            final ImageView ivOn = (ImageView) view.findViewById(R.id.rbWhiteListOnAddAlpha);
            final ImageView ivOff = (ImageView) view.findViewById(R.id.rbWhiteListOffAddAlpha);
            if(card.getState()) {
                ivOn.setVisibility(View.VISIBLE);
                ivOff.setVisibility(View.INVISIBLE);
            }
            else {
                ivOn.setVisibility(View.INVISIBLE);
                ivOff.setVisibility(View.VISIBLE);
            }
            ivOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    card.setState(false);
                    card.deleteFromList(getMainActivity().getDB(), id);
                    adapterSelect.notifyDataSetSelected();
                    ivOn.setVisibility(View.INVISIBLE);
                    ivOff.setVisibility(View.VISIBLE);
                    if(adapterSelect != null) getMainActivity().getHeader().setCounter(adapterSelect.getCount());
                    else getMainActivity().getHeader().setCounter(0);
                }
            });
            ivOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    card.setState(true);
                    card.saveToList(getMainActivity().getDB(), id);
                    adapterSelect.notifyDataSetSelected();
                    ivOn.setVisibility(View.VISIBLE);
                    ivOff.setVisibility(View.INVISIBLE);
                    if(adapterSelect != null) getMainActivity().getHeader().setCounter(adapterSelect.getCount());
                    else getMainActivity().getHeader().setCounter(0);
                }
            });
            return view;
        }
        private String getSymbol(ContactCard curr, ContactCard prev){
            String res = " ";
            if((prev==null) ||(!curr.getName().substring(0,1).equalsIgnoreCase(prev.getName().substring(0,1)))){
                res = curr.getName().substring(0,1);
            }
            return res;
        }
        public void notifyDataSetUpdated(){
            list = MainActivity.getMainActivity().getDBHelper().loadContacts(id);
            data = list;
            super.notifyDataSetChanged();
        }
        public void notifyDataSetSelected(){
            list = MainActivity.getMainActivity().getDBHelper().loadContactsSelected(id);
            data = list;
            super.notifyDataSetChanged();
        }
    }
    private class WhiteListSelectedAdapter extends FilteredAdapter {
        public WhiteListSelectedAdapter(Context context, BaseObjectList list) {
            super(context, list);
        }
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final ContactCard card = (ContactCard) getData().get(position);
            view = this.getInflater().inflate(R.layout.contact_item, parent, false);
            TextView tvText = (TextView) view.findViewById(R.id.tvNameWhiteListAdd);
            tvText.setText(card.getName());
            CircleImageView avatar = (CircleImageView) view.findViewById(R.id.ivAvatar);
            if(card.getAvatar() != null) avatar.setImageBitmap(card.getAvatar());
            final ImageView ivOn = (ImageView) view.findViewById(R.id.rbWhiteListOnAdd);
            final ImageView ivOff = (ImageView) view.findViewById(R.id.rbWhiteListOffAdd);
            if(card.getState()) {
                ivOn.setVisibility(View.VISIBLE);
                ivOff.setVisibility(View.INVISIBLE);
            }
            else {
                ivOn.setVisibility(View.INVISIBLE);
                ivOff.setVisibility(View.VISIBLE);
            }
            return view;
        }
        public void notifyDataSetUpdated(){
            list = MainActivity.getMainActivity().getDBHelper().loadContacts(id);
            data = list;
            super.notifyDataSetChanged();
        }
        public void notifyDataSetSelected(){
            list = MainActivity.getMainActivity().getDBHelper().loadContactsSelected(id);
            data = list;
            super.notifyDataSetChanged();
        }
    }
}

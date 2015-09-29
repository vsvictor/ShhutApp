package com.shhutapp.fragments.whitelist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.controls.ExLinearLayout;
import com.shhutapp.controls.ExRelativeLayout;
import com.shhutapp.data.ApplicationCard;
import com.shhutapp.data.BaseObject;
import com.shhutapp.data.BaseObjectList;
import com.shhutapp.data.ContactCard;
import com.shhutapp.data.FilteredAdapter;
import com.shhutapp.data.StringStringPair;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.fragments.OnBackListener;
import com.shhutapp.fragments.OnCancelListener;
import com.shhutapp.fragments.OnClickCounter;
import com.shhutapp.fragments.OnSearchListener;
import com.shhutapp.pages.WhiteListPage;
import com.shhutapp.utils.Convertor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by victor on 17.05.15.
 */
public class WhiteListAddContact extends BaseFragments {
    private WhiteListPage page;
    private BaseObjectList contacts;
    private BaseObjectList selected;

    private WhiteListContactsAdapter adapterCont;
    //private WhiteListSelectedAdapter adapterSelect;
    //private ListView lvAdded;
    private ListView lvAll;
    private int listID = -1;
    private String sName;
    private EditText edSearch;
    //private RelativeLayout rlSelected;
    private RelativeLayout rlAll;
    //private int hSelected;
    private int hAll;
    private int it;
    private ExLinearLayout rootLayout;
    public WhiteListAddContact(){
        super(MainActivity.getMainActivity());
    }
    @SuppressLint("ValidFragment")
    public WhiteListAddContact(MainActivity act){
        super(act);
    }
    @SuppressLint("ValidFragment")
    public WhiteListAddContact(MainActivity act, WhiteListPage page){
        super(act);
        this.page = page;
    }
    public void onCreate(Bundle saved){
        super.onCreate(saved);
        listID = getArguments().getInt("id");
        sName = getArguments().getString("name");
    }
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState){
        rView = inf.inflate(R.layout.whitelist_add_contact, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        rootLayout = (ExLinearLayout) rView.findViewById(R.id.llAddContactSelector);
        rlAll = (RelativeLayout) rView.findViewById(R.id.rlWhiteListAddContactList);
        hAll = rlAll.getLayoutParams().height;
        lvAll = (ListView)rView.findViewById(R.id.lvAddContactList);
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
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                rlAll.setLayoutParams(params);
                rootLayout.showKeyboard();
/*
                InputMethodManager imm = (InputMethodManager) getMainActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
*/
            }
        });
        getMainActivity().getHeader().setOnCancelSearchListener(new OnCancelListener() {
            @Override
            public void onCancel() {
                edSearch.setText("");
                adapterCont = new WhiteListContactsAdapter(getMainActivity(), contacts, listID);
                lvAll.setAdapter(adapterCont);
                lvAll.setSelection(0);
            }
        });
        getMainActivity().getHeader().setOnBackSearchListener(new OnBackListener() {
            @Override
            public void onBack() {

                LinearLayout.LayoutParams pSelected = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                LinearLayout.LayoutParams pAll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                pAll.height = hAll;
                rlAll.setLayoutParams(pAll);
                getMainActivity().setActionBarBlue();
                getMainActivity().getHeader().setBlue();
                getMainActivity().getHeader().setInvisibleAll();
                getMainActivity().getHeader().setVisibleTextHeader(true);
                getMainActivity().getHeader().setVisibleBack(true);
                getMainActivity().getHeader().setVisibleSearch(true);
                rootLayout.hideKeyboard();
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
                page.whitelistAppCont = new WhiteListAppCont(getMainActivity(), page);
                Bundle bcont = new Bundle();
                bcont.putInt("id", listID);
                bcont.putString("name", sName);
                page.whitelistAppCont.setArguments(bcont);
                WhiteListContacts www = new WhiteListContacts(getMainActivity(), page);
                getMainActivity().getSupportFragmentManager().beginTransaction().
                        remove(getIAm()).
                        add(R.id.whitelistPage, page.whitelistAppCont).
                        add(R.id.whitelistPage, www).
                        commit();
            }
        });
        getMainActivity().getHeader().setOnClickCounter(new OnClickCounter() {
            @Override
            public void onClick() {
                lvAll.smoothScrollToPosition(0);
            }
        });
        Bundle b = getArguments();
        if(b != null){
            listID = b.getInt("id");
        }
        selected = getMainActivity().getDBHelper().loadContactsSelected(listID);
        contacts = getMainActivity().getDBHelper().loadContacts(listID);

        BaseObjectList ol = new BaseObjectList();
        for(int i = 0; i<selected.size();i++){
            ContactCard ap = (ContactCard)selected.get(i);
            ap.setSection(0);
            ol.add(ap);
        }
        for(int i = 0; i<contacts.size();i++){
            ContactCard ap = (ContactCard)contacts.get(i);
            ap.setSection(1);
            ol.add(ap);
        }
        adapterCont = new WhiteListContactsAdapter(getMainActivity(),ol, listID);
        lvAll.setAdapter(adapterCont);
        lvAll.setChoiceMode(AbsListView.CHOICE_MODE_NONE);
        getMainActivity().getHeader().setVisibleCounter(true);
        if(selected != null) getMainActivity().getHeader().setCounter(selected.size());
        else getMainActivity().getHeader().setCounter(0);
        lvAll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ImageView ivOn = (ImageView) view.findViewById(R.id.rbWhiteListOnAddAlpha);
                final ImageView ivOff = (ImageView) view.findViewById(R.id.rbWhiteListOffAddAlpha);
                WhiteListContactsAdapter ad = (WhiteListContactsAdapter)parent.getAdapter();
                ContactCard card = (ContactCard) ad.getData().get(position);
                if(card.getState()) {
                    //ivOn.performClick();
                    it = position;
                    card.setState(false);
                    selected.remove(card);
                    card.setSection(1);
                    card.deleteFromList(getMainActivity().getDB(), listID);
                    ivOn.setVisibility(View.INVISIBLE);
                    ivOff.setVisibility(View.VISIBLE);
                    ad.notifyDataSetAdded();
                    getMainActivity().getHeader().setCounter(selected.size());

                }
                else {
                    //ivOff.performClick();
                    it = position;
                    card.setState(true);
                    selected.add(card);
                    card.setSection(0);
                    card.saveToList(getMainActivity().getDB(), listID);
                    ivOn.setVisibility(View.VISIBLE);
                    ivOff.setVisibility(View.INVISIBLE);
                    ad.notifyDataSetAdded();
                    getMainActivity().getHeader().setCounter(selected.size());

                }
//                ad.notifyDataSetChanged();
//                lvAll.invalidate();
            }
        });
    }
    private class WhiteListContactsAdapter extends FilteredAdapter  implements SectionIndexer{
        private ArrayList<String> myElements;
        private HashMap<String, Integer> azIndexer;
        private String[] sections;
        private int listID;
        public WhiteListContactsAdapter(Context context, BaseObjectList list, int id) {
            super(context, list);
            listID = id;
            myElements = new ArrayList<String>();
            azIndexer = new HashMap<String, Integer>();
            for(BaseObject b: list) {
                myElements.add((String) b.getName());
            }
            int size = myElements.size();
            for(int i = size-1;i>=0;i--){
                String ss = myElements.get(i);
                azIndexer.put(ss.substring(0,1),i);
            }

            Set<String> keys = azIndexer.keySet(); // set of letters

            Iterator<String> it = keys.iterator();
            ArrayList<String> keyList = new ArrayList<String>();

            while (it.hasNext()) {
                String key = it.next();
                keyList.add(key);
            }
            Collections.sort(keyList);//sort the keylist
            sections = new String[keyList.size()]; // simple conversion to array
            keyList.toArray(sections);
        }
        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            ContactCard prevcard = null;
            if(position>0)prevcard = (ContactCard) getData().get(position - 1);
            final ContactCard card = (ContactCard) getData().get(position);


            view = this.getInflater().inflate(R.layout.contact_item_alphabet, parent, false);
            ImageView ivFav = (ImageView) view.findViewById(R.id.ivAddContactSelectedIcon);
            if(position == 0 && card.getSection() == 0) ivFav.setVisibility(View.VISIBLE);

            TextView tvSymbol = (TextView) view.findViewById(R.id.tvSymbol);
            //if(card.getSection() == 1)tvSymbol.setText(getSymbol(card, prevcard));
            //else tvSymbol.setText(" ");
            if(position>selected.size()-1)tvSymbol.setText(getSymbol(card, prevcard));
            else tvSymbol.setText(" ");

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
                    it = position;
                    card.setState(false);
                    selected.remove(card);
                    card.setSection(1);
                    card.deleteFromList(getMainActivity().getDB(), listID);
                    ivOn.setVisibility(View.INVISIBLE);
                    ivOff.setVisibility(View.VISIBLE);
                    notifyDataSetAdded();
                    getMainActivity().getHeader().setCounter(selected.size());
                }
            });
            ivOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    it = position;
                    card.setState(true);
                    selected.add(card);
                    card.setSection(0);
                    card.saveToList(getMainActivity().getDB(), listID);
                    ivOn.setVisibility(View.VISIBLE);
                    ivOff.setVisibility(View.INVISIBLE);
                    notifyDataSetAdded();
                    getMainActivity().getHeader().setCounter(selected.size());
                }
            });
            if((prevcard!=null)&&(card.getSection()==1 && prevcard.getSection() == 0)&&position<=selected.size()){
                ImageView ivDirivider = (ImageView) view.findViewById(R.id.ivDiriveder);
                ivDirivider.setVisibility(View.VISIBLE);
            }
            return view;
        }
        private String getSymbol(ContactCard curr, ContactCard prev){
            String res = " ";
            if((prev==null) ||(!curr.getName().substring(0,1).equalsIgnoreCase(prev.getName().substring(0,1)))){
                res = curr.getName().substring(0,1);
            }
            return res;
        }
        private void setDelimiter(ContactCard curr, ContactCard prev){
        }
        public void notifyDataSetUpdated(){
            list = MainActivity.getMainActivity().getDBHelper().loadContacts(listID);
            data = list;
            super.notifyDataSetChanged();
            lvAll.setSelection(it);
        }
        public void notifyDataSetAdded(){
            //for(int i = 0; i<selected.size();i++){ContactCard c = (ContactCard)selected.get(i);c.setSection(0);}
            //for(int i = 0; i<list.size();i++){ContactCard c = (ContactCard)list.get(i);c.setSection(1);}
            //data = BaseObjectList.concat(selected,list);
            data = new BaseObjectList();
            for(int i = 0; i<selected.size();i++){ContactCard c = (ContactCard) selected.get(i);data.add(c);}
            for(int i = 0; i<contacts.size();i++){ContactCard c = (ContactCard) contacts.get(i);data.add(c);}
            super.notifyDataSetChanged();
            //lvAll.smoothScrollToPosition(it);
            //lvAll.setSelection(it);
            lvAll.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        }
        @Override
        public Object[] getSections() {
            return sections;
        }
        @Override
        public int getPositionForSection(int sectionIndex) {
            String letter = sections[sectionIndex];
            return azIndexer.get(letter);
        }
        @Override
        public int getSectionForPosition(int position) {
            return 0;
        }
    }
}

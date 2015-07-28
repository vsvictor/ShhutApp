package com.shhutapp.fragments.whitelist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.shhutapp.data.ApplicationCard;
import com.shhutapp.data.BaseObject;
import com.shhutapp.data.BaseObjectList;
import com.shhutapp.data.ContactCard;
import com.shhutapp.data.FilteredAdapter;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.fragments.OnBackListener;
import com.shhutapp.fragments.OnCancelListener;
import com.shhutapp.fragments.OnClickCounter;
import com.shhutapp.fragments.OnSearchListener;
import com.shhutapp.pages.WhiteListPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by victor on 17.05.15.
 */
public class WhiteListApplications extends BaseFragments {
    private int id = -1;
    private String sName;
    public WhiteListPage page;
    private WhiteListApplicationsAdapter adapterCont;
    //private WhiteListSelectedApplicationsAdapter adapterSelect;
    //private RelativeLayout rlSelected;
    private RelativeLayout rlAll;
    private int hSelected;
    private int hAll;
    private ListView lvAll;
    //private ListView lvAdded;
    private EditText edSearch;
    private BaseObjectList contacts;
    private BaseObjectList selected;

    public WhiteListApplications(){
        super(MainActivity.getMainActivity());
    }
    @SuppressLint("ValidFragment")
    public WhiteListApplications(MainActivity act){
        super(act);
    }
    @SuppressLint("ValidFragment")
    public WhiteListApplications(MainActivity act, WhiteListPage page){
        super(act);
        this.page = page;
    }
    public void onCreate(Bundle saved){
        super.onCreate(saved);
        try {
            id = getArguments().getInt("id");
            sName = getArguments().getString("name");
        }catch (NullPointerException e){}

    }
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.whitelist_app, container, false);
        //rView = inf.inflate(R.layout.whitelist_add_contact, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        rlAll = (RelativeLayout) rView.findViewById(R.id.rlWhiteListAppList);
        hAll = rlAll.getLayoutParams().height;
        lvAll = (ListView)rView.findViewById(R.id.lvAppList);
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
                //rlSelected.getLayoutParams().height = 0;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                rlAll.setLayoutParams(params);
            }
        });
        getMainActivity().getHeader().setOnCancelSearchListener(new OnCancelListener() {
            @Override
            public void onCancel() {
                edSearch.setText("");
                adapterCont = new WhiteListApplicationsAdapter(getMainActivity(), contacts);
                lvAll.setAdapter(adapterCont);
                lvAll.setSelection(0);
            }
        });
        getMainActivity().getHeader().setOnBackSearchListener(new OnBackListener() {
            @Override
            public void onBack() {
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
        getMainActivity().getHeader().setVisibleCounter(true);
        getMainActivity().getHeader().setOnClickCounter(new OnClickCounter() {
            @Override
            public void onClick() {
                lvAll.smoothScrollToPosition(0);
            }
        });

        Bundle b = getArguments();
        if(b != null){
            id = b.getInt("id");
        }
        contacts = getMainActivity().getDBHelper().loadApplications(id);
        selected = getMainActivity().getDBHelper().loadApplicationsSelected(id);
        BaseObjectList ol = new BaseObjectList();
        for(int i = 0; i<selected.size();i++){
            ApplicationCard ap = (ApplicationCard)selected.get(i);
            ap.setSection(0);
            ol.add(ap);
        }
        for(int i = 0; i<contacts.size();i++){
            ApplicationCard ap = (ApplicationCard)contacts.get(i);
            ap.setSection(1);
            ol.add(ap);
        }

        adapterCont = new WhiteListApplicationsAdapter(getMainActivity(),ol);
        lvAll.setAdapter(adapterCont);
        getMainActivity().getHeader().setVisibleCounter(true);
        lvAll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ImageView ivOn = (ImageView) view.findViewById(R.id.rbWhiteListOnAddAlpha);
                final ImageView ivOff = (ImageView) view.findViewById(R.id.rbWhiteListOffAddAlpha);
                WhiteListApplicationsAdapter ad = (WhiteListApplicationsAdapter) parent.getAdapter();
                ApplicationCard card = (ApplicationCard) ad.getData().get(position);
                if (card.getState()) {
                    ivOn.performClick();
                } else {
                    ivOff.performClick();
                }
            }
        });
    }
    private class WhiteListApplicationsAdapter extends FilteredAdapter implements SectionIndexer{
        private ArrayList<String> myElements;
        private HashMap<String, Integer> azIndexer;
        private String[] sections;
        public WhiteListApplicationsAdapter(Context context, BaseObjectList list) {
            super(context, list);
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
        public View getView(int position, View view, ViewGroup parent) {
            ApplicationCard prevcard = null;
            if(position>0)prevcard = (ApplicationCard) getData().get(position - 1);
            final ApplicationCard card = (ApplicationCard) getData().get(position);
            view = this.getInflater().inflate(R.layout.contact_item_alphabet, parent, false);
            TextView tvSymbol = (TextView) view.findViewById(R.id.tvSymbol);
            tvSymbol.setText(getSymbol(card, prevcard));

            if(position>selected.size()-1)tvSymbol.setText(getSymbol(card, prevcard));
            else tvSymbol.setText(" ");

            TextView tvText = (TextView) view.findViewById(R.id.tvNameWhiteListAddAlpha);
            tvText.setText(card.getName());
            CircleImageView avatar = (CircleImageView) view.findViewById(R.id.ivAvatarAlpha);
            if(card.getIcon() != null) avatar.setImageBitmap(card.getIcon());
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
                    selected.remove(card);
                    card.setSection(1);
                    card.deleteFromList(getMainActivity().getDB(), id);
                    ivOn.setVisibility(View.INVISIBLE);
                    ivOff.setVisibility(View.VISIBLE);
                    notifyDataSetAdded();
                    getMainActivity().getHeader().setCounter(selected.size());
                }
            });
            ivOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    card.setState(true);
                    selected.add(card);
                    card.setSection(0);
                    card.saveToList(getMainActivity().getDB(), id);
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
        private String getSymbol(ApplicationCard curr, ApplicationCard prev){
            String res = " ";
            if((prev==null) ||(!curr.getName().substring(0,1).equalsIgnoreCase(prev.getName().substring(0,1)))){
                res = curr.getName().substring(0,1);
            }
            return res;
        }
        public void notifyDataSetUpdated(){
            list = MainActivity.getMainActivity().getDBHelper().loadApplications(id);
            data = list;
            super.notifyDataSetChanged();
        }
        public void notifyDataSetSelected(){
            list = MainActivity.getMainActivity().getDBHelper().loadApplicationsSelected(id);
            data = list;
            super.notifyDataSetChanged();
        }
        public void notifyDataSetAdded(){
            data = new BaseObjectList();
            for(int i = 0; i<selected.size();i++){ApplicationCard c = (ApplicationCard) selected.get(i);data.add(c);}
            for(int i = 0; i<contacts.size();i++){ApplicationCard c = (ApplicationCard) contacts.get(i);data.add(c);}
            //data.sortByName();
            super.notifyDataSetChanged();
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
/*
    private class WhiteListSelectedApplicationsAdapter extends FilteredAdapter {
        public WhiteListSelectedApplicationsAdapter(Context context, BaseObjectList list) {
            super(context, list);
        }
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final ApplicationCard card = (ApplicationCard) getData().get(position);
            view = this.getInflater().inflate(R.layout.contact_item, parent, false);
            TextView tvText = (TextView) view.findViewById(R.id.tvNameWhiteListAdd);
            tvText.setText(card.getName());
            CircleImageView avatar = (CircleImageView) view.findViewById(R.id.ivAvatar);
            if(card.getIcon() != null) avatar.setImageBitmap(card.getIcon());
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
            list = MainActivity.getMainActivity().getDBHelper().loadApplications(id);
            data = list;
            super.notifyDataSetChanged();
        }
        public void notifyDataSetSelected(){
            list = MainActivity.getMainActivity().getDBHelper().loadApplicationsSelected(id);
            data = list;
            super.notifyDataSetChanged();
        }
    }
*/
    public void onPause(){
        super.onPause();
        getMainActivity().getHeader().setVisibleCounter(false);
    }
}

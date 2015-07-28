package com.shhutapp.fragments.whitelist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.data.BaseObjectList;
import com.shhutapp.data.FilteredAdapter;
import com.shhutapp.data.SMSCard;
import com.shhutapp.data.WhiteListCard;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.fragments.Header;
import com.shhutapp.fragments.OnBackListener;
import com.shhutapp.fragments.area.AreaCard;
import com.shhutapp.fragments.messages.MessageListListener;
import com.shhutapp.pages.BasePage;
import com.shhutapp.pages.QueitTimePage;
import com.shhutapp.pages.WhiteListPage;

/**
 * Created by victor on 16.05.15.
 */
public class WhiteListList extends BaseFragments {
    private WhiteListPage page;
    private BaseObjectList list;
    private boolean isEmpty;
    private Header header;
    private WhiteListAdapter adapter;
    private RelativeLayout rlWhiteListEmpty;
    private RelativeLayout rlWhiteListData;
    private RelativeLayout rlWhiteListBottom;
    private ImageView ivButton;
    private RelativeLayout rlAddButton;
    private SwipeListView swWhiteList;
    private MessageListListener listener;
    private int prevID;
    private boolean isRadio;

    public WhiteListList() {
        super();
    }
    @SuppressLint("ValidFragment")
    public WhiteListList(MainActivity act) {
        super(act);
    }
    @SuppressLint("ValidFragment")
    public WhiteListList(MainActivity act, WhiteListPage page) {
        super(act);
        this.page = page;
        header = getMainActivity().getHeader();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isRadio = false;
        try{
            isRadio = getArguments().getBoolean("isRadio");
        }catch (NullPointerException e){
            isRadio = false;
        }
        try{
            prevID = getArguments().getInt("back");
        }catch (NullPointerException e){
            prevID = BasePage.Pages.mainPage;
        }

        list = act.getDBHelper().loadWhiteLists();
        adapter = new WhiteListAdapter(act, list);
        adapter.setRadioShow(isRadio);
    }

    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.whitelist, container, false);
        return rView;
    }

    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        rlWhiteListEmpty = (RelativeLayout) rView.findViewById(R.id.rlWhiteListEmpty);
        rlWhiteListData = (RelativeLayout) rView.findViewById(R.id.rlWhiteListData);
        rlWhiteListBottom = (RelativeLayout) rView.findViewById(R.id.rlWhiteListBottom);
        ivButton = (ImageView) rView.findViewById(R.id.ivWhiteListAllMessage);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rlWhiteListEmpty.setLayoutParams(p);
        rlWhiteListData.setLayoutParams(p);
        rlAddButton = (RelativeLayout) rView.findViewById(R.id.rlWhiteListAddButton);
        rlAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAdd();
                adapter.notifyDataSetUpdated();
            }
        });
        swWhiteList = (SwipeListView) rView.findViewById(R.id.lvWhiteList);
        swWhiteList.setAdapter(adapter);
        swWhiteList.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onOpened(int position, boolean toRight) {
                View v = (View) swWhiteList.getChildAt(position - swWhiteList.getFirstVisiblePosition());
                RelativeLayout llBack = (RelativeLayout) v.findViewById(R.id.whitelist_item_back);
                llBack.setVisibility(View.VISIBLE);
            }

            @Override
            public void onClosed(int position, boolean fromRight) {
                View v = (View) swWhiteList.getChildAt(position - swWhiteList.getFirstVisiblePosition());
                RelativeLayout llBack = (RelativeLayout) v.findViewById(R.id.whitelist_item_back);
                llBack.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onListChanged() {
            }

            @Override
            public void onMove(int position, float x) {
            }

            @Override
            public void onStartOpen(int position, int action, boolean right) {
                swWhiteList.closeOpenedItems();
            }

            @Override
            public void onStartClose(int position, boolean right) {
            }

            @Override
            public void onClickFrontView(int position) {
                int id = ((WhiteListCard) ((WhiteListAdapter) swWhiteList.getAdapter()).getData().get(position)).getID();
                if (!isRadio) {
                    listener.onEdit(id);
                    adapter.notifyDataSetUpdated();
                } else {
                    listener.onSelected(id);
                }
            }

            @Override
            public void onClickBackView(int position) {
                int id = ((WhiteListCard) ((WhiteListAdapter) swWhiteList.getAdapter()).getData().get(position)).getID();
                act.getDBHelper().deleteWhiteList(id);
                adapter.notifyDataSetUpdated();
            }

            @Override
            public void onDismiss(int[] reverseSortedPositions) {
            }
        });
        swWhiteList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int idd = ((WhiteListCard) ((WhiteListAdapter) swWhiteList.getAdapter()).getData().get(position)).getID();
                listener.onEdit(idd);
                adapter.notifyDataSetUpdated();
                return true;
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        //adapter.notifyDataSetUpdated();
        showEmpty(getMainActivity().isWhiteListEmpty());

        if(isEmpty) {
            rlWhiteListEmpty.setVisibility(View.VISIBLE);
            rlWhiteListData.setVisibility(View.INVISIBLE);
            ivButton.setVisibility(View.VISIBLE);
        }
        else{
            rlWhiteListEmpty.setVisibility(View.INVISIBLE);
            rlWhiteListData.setVisibility(View.VISIBLE);
            ivButton.setVisibility(View.INVISIBLE);
        }

        header.setTextHeader(getMainActivity().getResources().getString(R.string.whitelist));
        header.setInvisibleAll();
        header.setVisibleBack(true);
        header.setOnBackListener(new OnBackListener() {
            @Override
            public void onBack() {
                switch (prevID){
                    case BasePage.Pages.mainPage:{
                        BasePage p = getMainActivity().createPageFromID(prevID);
                        getMainActivity().getSupportFragmentManager().beginTransaction().
                                //addToBackStack(null).
                                remove(page.getCurrent()).
                                add(R.id.container, p).
                                commit();
                        /*getMainActivity().getSupportFragmentManager().beginTransaction().
                                add(R.id.container, p).
                                commit();*/
                        break;
                    }
                    case BasePage.Pages.queitTimePage:{
                        getMainActivity().getHeader().setVisibleBack(false);
                        getMainActivity().getHeader().setVisibleCancel(true);
                        getMainActivity().getSupportFragmentManager().beginTransaction().
                                //addToBackStack(null).
                                remove(page.getCurrent()).
                                show(QueitTimePage.instance).
                                commit();
                        break;
                    }
                    case BasePage.Pages.areaCard:{
                        getMainActivity().getHeader().setVisibleBack(false);
                        getMainActivity().getHeader().setVisibleCancel(true);
                        getMainActivity().getSupportFragmentManager().beginTransaction().
                                //addToBackStack(null).
                                remove(page.getCurrent()).
                                show(AreaCard.instance).
                                commit();
                        break;
                    }

                }
            }
        });
    }

    public void showEmpty(boolean isWhiteListListEmpty){
        isEmpty = isWhiteListListEmpty;
    }
    public void setOnMessageListListener(MessageListListener aListener){
        listener = aListener;
    }
    public int getCount(){return adapter.getCount();}
    private class WhiteListAdapter extends FilteredAdapter {
        private boolean isShow;
        public WhiteListAdapter(Context context, BaseObjectList list) {
            super(context, list);
        }
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final WhiteListCard card = (WhiteListCard) getData().get(position);
            view = this.getInflater().inflate(R.layout.whitelist_item, parent, false);
            TextView tvText = (TextView) view.findViewById(R.id.tvTextWhiteList);
            tvText.setText(card.getName());
            final ImageView ivOn = (ImageView) view.findViewById(R.id.ivWhiteListOn);
            final ImageView ivOff = (ImageView) view.findViewById(R.id.ivWhiteListOff);
            if(isShow) {
                if (card.getState()) {
                    ivOn.setVisibility(View.VISIBLE);
                    ivOff.setVisibility(View.INVISIBLE);
                } else {
                    ivOn.setVisibility(View.INVISIBLE);
                    ivOff.setVisibility(View.VISIBLE);
                }
                ivOn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OffAll();
                        card.setState(false);
                        notifyDataSetChanged();
                    }
                });
                ivOff.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OffAll();
                        getMainActivity().selectWhiteList(card);
                        card.setState(true);
                        notifyDataSetChanged();
                    }
                });
            }
            else{
                ivOn.setVisibility(View.INVISIBLE);
                ivOff.setVisibility(View.INVISIBLE);
            }
            return view;
        }
        public void OffAll(){
            for(int i=0;i<this.getCount();i++){
                WhiteListCard c = (WhiteListCard)getItem(i);
                c.setState(false);
            }
            notifyDataSetChanged();
        }
        public void setRadioShow(boolean show){
            isShow = show;
        }
        public void notifyDataSetUpdated(){
            list = MainActivity.getMainActivity().getDBHelper().loadWhiteLists();
            data = list;
            super.notifyDataSetChanged();
        }
    }
}


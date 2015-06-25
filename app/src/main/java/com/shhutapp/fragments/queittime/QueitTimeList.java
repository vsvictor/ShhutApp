package com.shhutapp.fragments.queittime;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.data.BaseObjectList;
import com.shhutapp.data.FilteredAdapter;
import com.shhutapp.data.QueitCard;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.fragments.Header;
import com.shhutapp.fragments.OnBackListener;
import com.shhutapp.fragments.messages.MessageListListener;
import com.shhutapp.pages.BasePage;
import com.shhutapp.pages.QueitTimePage;

/**
 * Created by victor on 16.05.15.
 */
public class QueitTimeList extends BaseFragments {
    private QueitTimePage page;
    private BaseObjectList list;
    private boolean isEmpty;
    private Header header;
    private QueitTimeAdapter adapter;
    private ImageView ivButton;
    private RelativeLayout rlAddButton;
    private SwipeListView swQueitTime;
    private MessageListListener listener;
    private RelativeLayout rlQueitTimeData;
    private RelativeLayout rlQueitTimeEmpty;
    private boolean isRadio;

    public QueitTimeList() {
        super();
    }

    public QueitTimeList(MainActivity act) {
        super(act);
    }

    public QueitTimeList(MainActivity act, QueitTimePage page) {
        super(act);
        this.page = page;
        header = getMainActivity().getHeader();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isRadio = false;
        list = act.getDBHelper().loadQueitCard();
        adapter = new QueitTimeAdapter(act, list);
    }

    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.queittime_list, container, false);
        return rView;
    }

    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        header.setHeight(56);
        header.setInvisibleAll();
        header.setLeftText(72);

        header.setTextHeader(getMainActivity().getResources().getString(R.string.queittime));
        header.setInvisibleAll();
        header.setVisibleBack(true);
        header.setOnBackListener(new OnBackListener() {
            @Override
            public void onBack() {
                if(page.getPrevID() == BasePage.Pages.mainPage) {
                    getMainActivity().getHeader().setVisibleBack(false);
                    BasePage p = getMainActivity().createPageFromID(page.getPrevID());
                    Bundle args = new Bundle();
                    args.putInt("back", p.getID());
                    p.setArguments(args);
                    getMainActivity().getSupportFragmentManager().beginTransaction().remove(page.getCurrent()).commit();
                    getMainActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, p).commit();
                }
                else{
                    getMainActivity().getSupportFragmentManager().beginTransaction().remove(page.getCurrent()).commit();
                    getMainActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, page.getPrev()).commit();
                }
            }
        });



        rlQueitTimeData = (RelativeLayout) rView.findViewById(R.id.rlQueitTimeData);
        rlQueitTimeEmpty = (RelativeLayout) rView.findViewById(R.id.rlQueitTimeEmpty);

        ivButton = (ImageView) rView.findViewById(R.id.ivQueitTimeAllMessage);
        rlAddButton = (RelativeLayout) rView.findViewById(R.id.rlQueitAddButton);
        rlAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAdd();
                adapter.notifyDataSetUpdated();
            }
        });
        swQueitTime = (SwipeListView) rView.findViewById(R.id.lvQueitTime);
        swQueitTime.setAdapter(adapter);
        swQueitTime.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onOpened(int position, boolean toRight) {
                View v = (View) swQueitTime.getChildAt(position - swQueitTime.getFirstVisiblePosition());
                RelativeLayout llBack = (RelativeLayout) v.findViewById(R.id.queittime_item_back);
                llBack.setVisibility(View.VISIBLE);
            }

            @Override
            public void onClosed(int position, boolean fromRight) {
                View v = (View) swQueitTime.getChildAt(position - swQueitTime.getFirstVisiblePosition());
                RelativeLayout llBack = (RelativeLayout) v.findViewById(R.id.queittime_item_back);
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
                swQueitTime.closeOpenedItems();
            }

            @Override
            public void onStartClose(int position, boolean right) {
            }

            @Override
            public void onClickFrontView(int position) {
/*              int id = ((WhiteListCard) ((QueitTimeAdapter) swQueitTime.getAdapter()).getData().get(position)).getID();
                listener.onEdit(id);
                adapter.notifyDataSetUpdated();*/
                QueitCard card = (QueitCard) ((QueitTimeAdapter) swQueitTime.getAdapter()).getData().get(position);
                if(isRadio){
                    getMainActivity().selectQueitCard(card);
                }
                else {
                    listener.onEdit(card.getID());
                }
            }

            @Override
            public void onClickBackView(int position) {
                int id = ((QueitCard) ((QueitTimeAdapter) swQueitTime.getAdapter()).getData().get(position)).getID();
                act.getDBHelper().deleteQueitCard(id);
                adapter.notifyDataSetUpdated();
            }

            @Override
            public void onDismiss(int[] reverseSortedPositions) {
            }
        });

    }
    @Override
    public void onResume(){
        super.onResume();
        showEmpty(getMainActivity().isQueitTimeEmpty());

        if(isEmpty){
            rlQueitTimeEmpty.setVisibility(View.VISIBLE);
            rlQueitTimeData.setVisibility(View.INVISIBLE);
        }
        else {
            rlQueitTimeEmpty.setVisibility(View.INVISIBLE);
            rlQueitTimeData.setVisibility(View.VISIBLE);
        }
        ivButton.setVisibility(isEmpty ? View.VISIBLE : View.INVISIBLE);

        adapter.notifyDataSetUpdated();
    }
    public void showEmpty(boolean isWhiteListListEmpty){
        isEmpty = isWhiteListListEmpty;
    }
    public void setOnMessageListListener(MessageListListener aListener){
        listener = aListener;
    }
    public int getCount(){return adapter.getCount();}

    private class QueitTimeAdapter extends FilteredAdapter {
        private boolean isShow;
        public QueitTimeAdapter(Context context, BaseObjectList list) {
            super(context, list);
        }
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final QueitCard card = (QueitCard) getData().get(position);
            view = this.getInflater().inflate(R.layout.queittime_item, parent, false);
            TextView tvTimeQueit = (TextView) view.findViewById(R.id.tvTextQueit);
            TextView tvDaysQueit = (TextView) view.findViewById(R.id.tvDaysQueit);
            tvTimeQueit.setText(card.timeToText());
            tvDaysQueit.setText(card.daysToText());

            ImageView ivDaysOff = (ImageView)view.findViewById(R.id.ivQueitTimeDaysOff);
            ImageView ivDaysOn = (ImageView)view.findViewById(R.id.ivQueitTimeDaysOn);

            ImageView ivWLOff = (ImageView)view.findViewById(R.id.ivQueitTimeWLOff);
            ImageView ivWLOn = (ImageView)view.findViewById(R.id.ivQueitTimeWLOn);

            ImageView ivMsgOff = (ImageView)view.findViewById(R.id.ivQueitTimeMsgOff);
            ImageView ivMsgOn = (ImageView)view.findViewById(R.id.ivQueitTimeMsgOn);

            ImageView ivCardOff = (ImageView)view.findViewById(R.id.ivQueitTimeOff);
            ImageView ivCardOn = (ImageView)view.findViewById(R.id.ivQueitTimeOn);

            ivDaysOff.setVisibility(card.isAllDays()?View.VISIBLE:View.INVISIBLE);
            ivDaysOn.setVisibility(!card.isAllDays()?View.VISIBLE:View.INVISIBLE);

            ivWLOff.setVisibility(card.isWhiteList(getMainActivity().getDB())?View.VISIBLE:View.INVISIBLE);
            ivWLOn.setVisibility(!card.isWhiteList(getMainActivity().getDB())?View.VISIBLE:View.INVISIBLE);

            ivMsgOff.setVisibility(card.isMessage(getMainActivity().getDB())?View.VISIBLE:View.INVISIBLE);
            ivMsgOn.setVisibility(!card.isMessage(getMainActivity().getDB())?View.VISIBLE:View.INVISIBLE);

            ivCardOff.setVisibility(!card.isOn()?View.VISIBLE:View.INVISIBLE);
            ivCardOn.setVisibility(card.isOn()?View.VISIBLE:View.INVISIBLE);

            return view;
        }
        public void notifyDataSetUpdated(){
            list = MainActivity.getMainActivity().getDBHelper().loadQueitCard();
            data = list;
            super.notifyDataSetChanged();
        }
    }
    @Override
    public void onHiddenChanged(boolean hidden){
        super.onHiddenChanged(hidden);
        adapter.notifyDataSetUpdated();
        if(!hidden){
            header.setInvisibleAll();
            header.setTextHeader(getMainActivity().getResources().getString(R.string.queittime));
            header.setVisibleBack(true);
        }
    }
}


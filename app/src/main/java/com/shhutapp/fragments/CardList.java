package com.shhutapp.fragments;

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
import com.shhutapp.data.GeoCard;
import com.shhutapp.data.QueitCard;
import com.shhutapp.data.adapters.CardAdapter;
import com.shhutapp.pages.AreaPage;
import com.shhutapp.pages.BasePage;

public class CardList extends  BaseFragments {
    private BasePage page;
    private RelativeLayout rlCardListEmpty;
    private RelativeLayout rlCardListData;
    private RelativeLayout rlAddLocation;
    private boolean isEmpty;
    private SwipeListView lvCardList;
    private OnDeleteCard listener;
    private RelativeLayout rlCardDeletePanel;
    private TextView tvCardName;
    private TextView tvCardDeleteCancel;

    public CardList(){
        super();
        rView = null;
        act = null;
        this.page = null;
    }
    public  CardList(MainActivity act){
        super(act);
        rView = null;
        this.page = null;
    }
    public  CardList(MainActivity act, BasePage page){
        super(act);
        rView = null;
        this.page = page;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState){
        rView = inf.inflate(R.layout.card_list, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        boolean bob = getMainActivity().isCardListEmty();
        showEmpty(bob);
        rlCardListEmpty = (RelativeLayout) rView.findViewById(R.id.rlCardListEmpty);
        rlCardListData  = (RelativeLayout) rView.findViewById(R.id.rlCardListData);

        if(isEmpty){
            rlCardListEmpty.setVisibility(View.VISIBLE);
            rlCardListData.setVisibility(View.INVISIBLE);
        }
        else {
            rlCardListEmpty.setVisibility(View.INVISIBLE);
            rlCardListData.setVisibility(View.VISIBLE);
        }

        rlAddLocation = (RelativeLayout) rView.findViewById(R.id.rlAddLocation);
        rlAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AreaPage ap = new AreaPage(getMainActivity());
                Bundle args = new Bundle();
                args.putInt("back", BasePage.Pages.mainPage);
                ap.setArguments(args);
                getMainActivity().getSupportFragmentManager().beginTransaction().remove(page).commit();
                getMainActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, ap).commit();

            }
        });
        BaseObjectList list = getMainActivity().getDBHelper().loadGeoCards();
        final CardAdapter adapter = new CardAdapter(getMainActivity(),list);
        lvCardList = (SwipeListView) rView.findViewById(R.id.lvCardList);
        lvCardList.setAdapter(adapter);
        lvCardList.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onOpened(int position, boolean toRight) {
                GeoCard card = (GeoCard) ((CardAdapter) lvCardList.getAdapter()).getData().get(position);
                tvCardName.setText(card.getName()+" "+getMainActivity().getResources().getString(R.string.deleted));
                getMainActivity().getDBHelper().deleteGeoCard(card.getID());
                adapter.notifyDataSetUpdated();
                rlCardDeletePanel.setVisibility(View.VISIBLE);
            }

            @Override
            public void onClosed(int position, boolean fromRight) {
            }

            @Override
            public void onListChanged() {
            }

            @Override
            public void onMove(int position, float x) {
            }

            @Override
            public void onStartOpen(int position, int action, boolean right) {
            }

            @Override
            public void onStartClose(int position, boolean right) {
            }

            @Override
            public void onClickFrontView(int position) {
            }

            @Override
            public void onClickBackView(int position) {
            }

            @Override
            public void onDismiss(int[] reverseSortedPositions) {
            }
        });
        rlCardDeletePanel = (RelativeLayout) rView.findViewById(R.id.cardListDeletePanel);
        rlCardDeletePanel.setVisibility(View.INVISIBLE);
        tvCardName = (TextView) rView.findViewById(R.id.deletedCardName);
        tvCardDeleteCancel = (TextView) rView.findViewById(R.id.deletedCancel);
        tvCardDeleteCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().getDBHelper().unDelete();
                adapter.notifyDataSetUpdated();
                rlCardDeletePanel.setVisibility(View.INVISIBLE);
            }
        });
    }
    public void showEmpty(boolean isCardListEmpty){
        isEmpty = isCardListEmpty;
    }
    public void setOnCardDeleteListener(OnDeleteCard ll){
        listener = ll;
    }
}

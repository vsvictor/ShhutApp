package com.shhutapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.pages.AreaPage;
import com.shhutapp.pages.BasePage;

public class CardList extends  BaseFragments {
    private BasePage page;
    private RelativeLayout rlCardListEmpty;
    private RelativeLayout rlCardListData;
    private RelativeLayout rlAddLocation;
    private boolean isEmpty;
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
        showEmpty(getMainActivity().isCardListEmty());
        rlCardListEmpty = (RelativeLayout) rView.findViewById(R.id.rlCardListEmpty);
        rlCardListData  = (RelativeLayout) rView.findViewById(R.id.rlCardListData);
        rlCardListEmpty.setVisibility(!isEmpty?View.VISIBLE:View.INVISIBLE);
        rlCardListData.setVisibility(isEmpty?View.VISIBLE:View.INVISIBLE);
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
    }
    public void showEmpty(boolean isCardListEmpty){
        isEmpty = isCardListEmpty;
    }
}

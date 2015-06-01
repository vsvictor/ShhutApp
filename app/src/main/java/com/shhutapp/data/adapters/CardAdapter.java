package com.shhutapp.data.adapters;

import com.shhutapp.MainActivity;
import com.shhutapp.data.BaseObjectList;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.graphics.Typeface;

import com.shhutapp.R;
import com.shhutapp.data.FilteredAdapter;
import com.shhutapp.data.GeoCard;

public class CardAdapter extends FilteredAdapter{
    public CardAdapter(BaseObjectList list) {
        super(list);
    }
    public CardAdapter(Context context, BaseObjectList list) {
        super(context, list);
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        GeoCard card = (GeoCard) getData().get(position);
        view = this.getInflater().inflate(R.layout.card_list_item, null);

        ImageView ivHere = (ImageView) view.findViewById(R.id.ivHere);
        ImageView ivHours = (ImageView) view.findViewById(R.id.ivHours);
        RelativeLayout rlWhite = (RelativeLayout) view.findViewById(R.id.rlWhiteListSymbol);
        TextView tvName = (TextView) view.findViewById(R.id.tvCardName);
        tvName.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvName.setText(card.getName());
        TextView tvAddress = (TextView) view.findViewById(R.id.tvAddressText);
        tvAddress.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvAddress.setText(card.getAddress());
        if(card.getTypeActivation() == 1){
            ivHere.setVisibility(View.VISIBLE);
            ivHours.setVisibility(View.INVISIBLE);
            rlWhite.setVisibility(View.INVISIBLE);
        }
        else{
            ivHere.setVisibility(View.INVISIBLE);
            ivHours.setVisibility(View.VISIBLE);
            rlWhite.setVisibility(View.INVISIBLE);
        }
        TextView tvActivation = (TextView) view.findViewById(R.id.tvActivateText);
        tvActivation.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvActivation.setText(card.getActivationName());
        return view;
    }
}

package com.shhutapp.data.adapters;

import com.shhutapp.MainActivity;
import com.shhutapp.data.BaseObject;
import com.shhutapp.data.BaseObjectList;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
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
    private ImageView ivOn;
    private ImageView ivOff;
    private GeoCard card;
    private RelativeLayout rlOnOff;

    public CardAdapter(BaseObjectList list) {
        super(list);
    }
    public CardAdapter(Context context, BaseObjectList list) {
        super(context, list);
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        card = (GeoCard) getData().get(position);
        view = this.getInflater().inflate(R.layout.card_list_item, null);
        view.setBackground(new BitmapDrawable(card.getBackground()));
        ImageView ivHere = (ImageView) view.findViewById(R.id.ivHere);
        ImageView ivHours = (ImageView) view.findViewById(R.id.ivHours);
        RelativeLayout rlWhite = (RelativeLayout) view.findViewById(R.id.rlWhiteListSymbol);
        TextView tvName = (TextView) view.findViewById(R.id.tvCardName);
        tvName.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvName.setText(card.getName());
        TextView tvAddress = (TextView) view.findViewById(R.id.tvAddressText);
        tvAddress.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvAddress.setText(card.getAddress());
        ivOn = (ImageView) view.findViewById(R.id.ivGeoOn);
        ivOff = (ImageView) view.findViewById(R.id.ivGeoOff);
        rlOnOff = (RelativeLayout) view.findViewById(R.id.rlGeoOnOff);
        rlOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(card.isOn()) Off();
                else On();
                card.updateOnOff(MainActivity.getMainActivity().getDB());
            }
        });
        if(card.isOn()) On();
        else Off();
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
    private void On(){
        ivOn.setVisibility(View.VISIBLE);
        ivOff.setVisibility(View.INVISIBLE);
        card.setOnoff(true);
    }
    private void Off(){
        ivOn.setVisibility(View.INVISIBLE);
        ivOff.setVisibility(View.VISIBLE);
        card.setOnoff(false);
    }
    public void notifyDataSetUpdated(){
        list = MainActivity.getMainActivity().getDBHelper().loadGeoCards();
        data = list;
        super.notifyDataSetChanged();
    }
    public void delete(GeoCard card){
        GeoCard obj = (GeoCard)getData().find(card.getID());
        list.remove(obj);
        data.remove(obj);
        notifyDataSetChanged();
    }
}

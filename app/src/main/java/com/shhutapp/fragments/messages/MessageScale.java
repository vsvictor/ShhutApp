package com.shhutapp.fragments.messages;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.controls.TimeBarHours;
import com.shhutapp.data.SMSCard;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.pages.MessagePage;

/**
 * Created by victor on 03.05.15.
 */
public class MessageScale extends BaseFragments {
    private MessagePage page;
    private TimeBarHours tb;
    private TextView tvCount;
    private SMSCard card;
    private int prog;
    public MessageScale(){
        super();
    }
    @SuppressLint("ValidFragment")
    public MessageScale(MainActivity act){
        super(act);
    }
    @SuppressLint("ValidFragment")
    public MessageScale(MainActivity act, MessagePage page) {
        super(act);
        this.page = page;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState){
        rView = inf.inflate(R.layout.scale_messages, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved){
        super.onViewCreated(view,saved);
        tb = (TimeBarHours) rView.findViewById(R.id.tbMessageTime);
        tb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("Time:", "" + progress);
                prog = progress;
                tvCount.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                card = getMainActivity().getDBHelper().loadMessage(page.getSelectedID());
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                card.setTime(prog);
                card.save(getMainActivity().getDB());
            }
        });
        tvCount = (TextView) rView.findViewById(R.id.tvMessageScaleCount);
    }
    public void  setTime(int time){
        tb.setTime(time);
        tvCount.setText(String.valueOf(time));
    }
}

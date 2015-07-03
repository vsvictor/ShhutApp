package com.shhutapp.start;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.controls.AlphaTearCircle;
import com.shhutapp.pages.BasePage;
import com.shhutapp.utils.Convertor;

/**
 * Created by victor on 12.06.15.
 */
public class StartHelpSixth extends BasePage {
    private TextView tvHelp6Header;
    private TextView tvHelp6Text;
    private TextView tvProceed6;

    public StartHelpSixth(){
        super(MainActivity.getMainActivity());
    }
    @SuppressLint("ValidFragment")
    public StartHelpSixth(MainActivity act){
        super(act);
    }
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rootView = inf.inflate(R.layout.starthelpsixth, container, false);
        return rootView;
    }
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        AlphaTearCircle circ = (AlphaTearCircle) rootView.findViewById(R.id.rlHelp6Background);
        int radius = (int)Convertor.convertDpToPixel(55, getMainActivity());
        Bitmap b1 = BitmapFactory.decodeResource(getMainActivity().getResources(), R.drawable.add_location);
        Bitmap b2 = Bitmap.createScaledBitmap(b1,radius,radius,false);
        circ.setBack(Color.argb(153, 0, 0, 0));
        int h_offset = (int) Convertor.convertDpToPixel(497, getMainActivity());
        int w_offset = (int)Convertor.convertDpToPixel(16+72+20+72+20+8+72+9, getMainActivity());
        circ.setCenter(new Point(w_offset, h_offset));
        circ.setRadius(radius);
        circ.setBitmap(b2);

        tvHelp6Header = (TextView) rootView.findViewById(R.id.tvHelpSixth);
        tvHelp6Header.setTypeface(Typeface.createFromAsset(getMainActivity().getAssets(), "fonts/Roboto-Medium.ttf"));
        tvHelp6Text = (TextView) rootView.findViewById(R.id.tvHelp6Text);
        tvHelp6Text.setTypeface(Typeface.createFromAsset(getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvProceed6 = (TextView) rootView.findViewById(R.id.tvProceed6);
        tvProceed6.setTypeface(Typeface.createFromAsset(getMainActivity().getAssets(), "fonts/Roboto-Medium.ttf"));
        tvProceed6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main, new StartHelpEighth(getMainActivity())).commit();
            }
        });

    }
    public void onResume(){
        super.onResume();
    }
    @Override
    public void onPause(){
        super.onPause();
    }
    @Override
    public int getID() {
        return Pages.startHelpSecond;
    }
}

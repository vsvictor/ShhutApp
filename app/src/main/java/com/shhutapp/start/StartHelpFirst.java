package com.shhutapp.start;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.pages.BasePage;
import com.shhutapp.utils.Convertor;

import org.w3c.dom.Text;

/**
 * Created by victor on 12.06.15.
 */
public class StartHelpFirst extends BasePage {
    private TextView tvHelpCongrat;
    private TextView tvHelpCongratText;
    private TextView tvProceed1;
    private RelativeLayout rlHelpBack1;

    public StartHelpFirst(){
        super(MainActivity.getMainActivity());
    }
    @SuppressLint("ValidFragment")
    public StartHelpFirst(MainActivity act){
        super(act);
    }
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rootView = inf.inflate(R.layout.starthelpfirst, container, false);
        return rootView;
    }
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        Bitmap b1 = BitmapFactory.decodeResource(getMainActivity().getResources(), R.drawable.help_filter_1);
        Bitmap b2 = Bitmap.createScaledBitmap(b1, (int) Convertor.convertDpToPixel(360, getMainActivity()), (int) Convertor.convertDpToPixel(640, getMainActivity()), false);
        Drawable dr = new BitmapDrawable(b2);
        rlHelpBack1 = (RelativeLayout)rootView.findViewById(R.id.rlHelpBack1);
        rlHelpBack1.setBackground(dr);
        tvHelpCongrat = (TextView) rootView.findViewById(R.id.tvHelpCongrat);
        tvHelpCongrat.setTypeface(Typeface.createFromAsset(getMainActivity().getAssets(), "fonts/Roboto-Medium.ttf"));
        tvHelpCongratText = (TextView) rootView.findViewById(R.id.tvHelpCongratText);
        tvHelpCongratText.setTypeface(Typeface.createFromAsset(getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvProceed1 = (TextView) rootView.findViewById(R.id.tvProceed1);
        tvProceed1.setTypeface(Typeface.createFromAsset(getMainActivity().getAssets(), "fonts/Roboto-Medium.ttf"));
        tvProceed1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main, new StartHelpSecond(getMainActivity())).commit();
            }
        });

    }
    @Override
    public int getID() {
        return Pages.startHelpFirst;
    }
}

package com.shhutapp.start;

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
import com.shhutapp.pages.SettingsPage;
import com.shhutapp.utils.Convertor;

import org.w3c.dom.Text;

/**
 * Created by victor on 12.06.15.
 */
public class StartHelpEighth extends BasePage {
    private TextView tvHelp8Text;
    private TextView tvProceed8;
    private RelativeLayout rlHelpBack8;
    private RelativeLayout rlHelp8Top;

    public StartHelpEighth(){
        super(MainActivity.getMainActivity());
    }
    public StartHelpEighth(MainActivity act){
        super(act);
    }
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rootView = inf.inflate(R.layout.starthelpeighth, container, false);
        return rootView;
    }
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
/*
        Bitmap b1 = BitmapFactory.decodeResource(getMainActivity().getResources(), R.drawable.help_filter_1);
        Bitmap b2 = Bitmap.createScaledBitmap(b1, (int) Convertor.convertDpToPixel(360, getMainActivity()), (int) Convertor.convertDpToPixel(640, getMainActivity()), false);
        Drawable dr = new BitmapDrawable(b2);
        rlHelpBack8 = (RelativeLayout)rootView.findViewById(R.id.rlHelpBack8);
        rlHelpBack8.setBackground(dr);
*/
        tvHelp8Text = (TextView) rootView.findViewById(R.id.tvHelp8_text);
        tvHelp8Text.setTypeface(Typeface.createFromAsset(getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvProceed8 = (TextView) rootView.findViewById(R.id.tvProceed8);
        tvProceed8.setTypeface(Typeface.createFromAsset(getMainActivity().getAssets(), "fonts/Roboto-Medium.ttf"));
        tvProceed8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().getSupportFragmentManager().beginTransaction().remove(getCurrent()).commit();
            }
        });



    }
    @Override
    public int getID() {
        return Pages.startHelpFirst;
    }
}

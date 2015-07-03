package com.shhutapp.start;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
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
import com.shhutapp.controls.AlphaTearCircle;
import com.shhutapp.controls.AlphaTearRectRounted;
import com.shhutapp.pages.BasePage;
import com.shhutapp.utils.Convertor;

/**
 * Created by victor on 12.06.15.
 */
public class StartHelpSeventh extends BasePage {
    private TextView tvHelp7Header;
    private TextView tvHelp7Text;
    private TextView tvProceed7;
    private RelativeLayout rlHelpBack7;

    public StartHelpSeventh(){
        super(MainActivity.getMainActivity());
    }
    @SuppressLint("ValidFragment")
    public StartHelpSeventh(MainActivity act){
        super(act);
    }
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rootView = inf.inflate(R.layout.starthelpseventh, container, false);
        return rootView;
    }
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);

        Bitmap b1 = BitmapFactory.decodeResource(getMainActivity().getResources(), R.drawable.help_filter_2);
        Bitmap b2 = Bitmap.createScaledBitmap(b1, (int) Convertor.convertDpToPixel(360, getMainActivity()), (int) Convertor.convertDpToPixel(640, getMainActivity()), false);
        Drawable dr = new BitmapDrawable(b2);
        rlHelpBack7 = (RelativeLayout)rootView.findViewById(R.id.rlHelpBack7);
        rlHelpBack7.setBackground(dr);


        tvHelp7Header = (TextView) rootView.findViewById(R.id.tvHelpSeventh);
        tvHelp7Header.setTypeface(Typeface.createFromAsset(getMainActivity().getAssets(), "fonts/Roboto-Medium.ttf"));
        tvHelp7Text = (TextView) rootView.findViewById(R.id.tvHelp7Text);
        tvHelp7Text.setTypeface(Typeface.createFromAsset(getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvProceed7 = (TextView) rootView.findViewById(R.id.tvProceed7);
        tvProceed7.setTypeface(Typeface.createFromAsset(getMainActivity().getAssets(), "fonts/Roboto-Medium.ttf"));
        tvProceed7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main, new StartHelpFourth(getMainActivity())).commit();
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
    private Bitmap createBitmap(int w, int h){
        Bitmap res = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(res);
        Paint p = new Paint();
        p.setColor(getMainActivity().getResources().getColor(R.color.blue_body));
        canvas.drawRoundRect(new RectF(0,0,w,h),10,10,p);
        return res;
    }
    @Override
    public int getID() {
        return Pages.startHelpSecond;
    }
}

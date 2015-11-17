package com.shhutapp.fragments.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.shhutapp.MainActivity;
import com.shhutapp.R;
import com.shhutapp.data.BaseObject;
import com.shhutapp.data.BaseObjectList;
import com.shhutapp.data.IntStringPair;
import com.shhutapp.data.StringStringPair;
import com.shhutapp.fragments.BaseFragments;
import com.shhutapp.pages.BasePage;
import com.shhutapp.utils.Convertor;

import java.util.zip.Inflater;

/**
 * Created by victor on 30.06.15.
 */
public class MainSettings extends BaseFragments {
    private BasePage prev;
    private ImageView ivVibrate;
    private ImageView ivColoc;
    private RelativeLayout rlYellowBaks;
    private SeekBar sbVolume;
    private CheckBox cbOnVolume;
    private CheckBox cbVibrate;
/*    private RelativeLayout cbOnVolume;
    private RelativeLayout cbVibrate;
    private boolean bOnVolume;
    private boolean bVibrate;*/
    private RelativeLayout langBar;
    private RelativeLayout teamBar;
    private ExpandableListView exLanguage;
    private BaseObjectList langGroups;
    private BaseObjectList langs;
    private ExAdapter langAdapter;
    private ExpandableListView exTeam;
    private BaseObjectList teamGroups;
    private BaseObjectList teams;
    private ExAdapter teamAdapter;
    private RelativeLayout rlAbout;
    private  int iProgress;
    private OnAbout onAbout;

    public MainSettings(){
        super(MainActivity.getMainActivity());
    }
    @SuppressLint("ValidFragment")
    public MainSettings(MainActivity act){
        super(act);
    }
    @SuppressLint("ValidFragment")
    public MainSettings(MainActivity act, BasePage prev){
        super(act);
        this.prev = prev;
    }
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        langGroups = new BaseObjectList();
        langGroups.add(act.getSettings().getLanguage());
        langs = new BaseObjectList();
        langs.add(new StringStringPair("def", act.getResources().getString(R.string.use_default_lang)));
        langs.add(new StringStringPair("de", "Deutsch"));
        langs.add(new StringStringPair("en", "English"));
        langs.add(new StringStringPair("fr", "Français"));
        langs.add(new StringStringPair("ru", "Russian"));
        teamGroups = new BaseObjectList();
        teamGroups.add(act.getSettings().getTeam());
        teams = new BaseObjectList();
        teams.add(new IntStringPair(0,"Material Cyan"));
        teams.add(new IntStringPair(1,"Material Brief"));
        teams.add(new IntStringPair(2,"Custom team"));
    }
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.main_settings, container, false);
        return rView;
    }
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        ivVibrate = (ImageView) rView.findViewById(R.id.ivVibro);
        ivColoc = (ImageView) rView.findViewById(R.id.ivColoc);
        rlYellowBaks = (RelativeLayout) rView.findViewById(R.id.rlYellowBaks);
        rlYellowBaks.setVisibility(act.getSettings().isPurchased()?View.INVISIBLE:View.VISIBLE);
        sbVolume = (SeekBar) rView.findViewById(R.id.sbVolume);
        int thumbSize = (int)Convertor.convertDpToPixel(20,act);
        Bitmap bm = BitmapFactory.decodeResource(act.getResources(), R.drawable.thumb_settings);
        Bitmap th = Bitmap.createScaledBitmap(bm,thumbSize, thumbSize, false);
        Drawable thumb = new BitmapDrawable(th);
        sbVolume.setThumb(thumb);
        iProgress = act.getSettings().getDefaultCallVolume();
        ivVibrate.setVisibility(iProgress == 0 ? View.VISIBLE : View.INVISIBLE);
        ivColoc.setVisibility(iProgress>0?View.VISIBLE:View.INVISIBLE);
        sbVolume.setProgress(iProgress);
        sbVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                iProgress = progress;
                ivVibrate.setVisibility(iProgress == 0?View.VISIBLE:View.INVISIBLE);
                ivColoc.setVisibility(iProgress>0?View.VISIBLE:View.INVISIBLE);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                act.getSettings().setDefaultCallVolume(iProgress);
            }
        });
        int cbSize = (int)Convertor.convertDpToPixel(20,act);
        //Bitmap bm_cb1_on = BitmapFactory.decodeResource(act.getResources(), R.drawable.cb_on);
        //Bitmap bm_cb1_off = BitmapFactory.decodeResource(act.getResources(), R.drawable.cb_off);
        //Bitmap bm_cb2_on = Bitmap.createScaledBitmap(bm_cb1_on, cbSize, cbSize, false);
        //Bitmap bm_cb2_off = Bitmap.createScaledBitmap(bm_cb1_off,cbSize,cbSize,false);
        //final Drawable cb_on = new BitmapDrawable(bm_cb2_on);
        //final Drawable cb_off = new BitmapDrawable(bm_cb2_off);

        cbOnVolume = (CheckBox) rView.findViewById(R.id.cbOnVolumeButton);
        cbOnVolume.setChecked(act.getSettings().getDefaultOnVolumeButton());
        //cbOnVolume.setButtonDrawable(cbOnVolume.isChecked() ? cb_on : cb_off);
        cbOnVolume.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //cbOnVolume.setButtonDrawable(cbOnVolume.isChecked() ? cb_on : cb_off);
                act.getSettings().setOnVolumeButton(cbOnVolume.isChecked());
            }
        });
        cbVibrate = (CheckBox) rView.findViewById(R.id.cbVibrateOnActivate);
        cbVibrate.setChecked(act.getSettings().getVibtateOnActivate());
        //cbVibrate.setButtonDrawable(cbOnVolume.isChecked() ? cb_on : cb_off);
        cbVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //cbVibrate.setButtonDrawable(cbVibrate.isChecked() ? cb_on : cb_off);
                act.getSettings().setVibrateOnActivate(cbVibrate.isChecked());
            }
        });

        langBar = (RelativeLayout) rView.findViewById(R.id.rlLanguage);
        exLanguage = (ExpandableListView) rView.findViewById(R.id.exLanguage);
        langAdapter = new ExAdapter(act, langGroups, langs, langBar, 45,90);
        exLanguage.setAdapter(langAdapter);
        exLanguage.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                StringStringPair s = (StringStringPair) langs.get(childPosition);
                parent.collapseGroup(groupPosition);
                act.getSettings().setLanguage(s);
                reCreateLangAdapter();
                langBar.invalidate();
                return false;
            }
        });
        exTeam = (ExpandableListView) rView.findViewById(R.id.exTeam);
        teamBar = (RelativeLayout) rView.findViewById(R.id.rlTeam);
        teamAdapter = new ExAdapter(act, teamGroups, teams,teamBar,65,100);
        exTeam.setAdapter(teamAdapter);
        exTeam.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                IntStringPair s = (IntStringPair) teams.get(childPosition);
                parent.collapseGroup(groupPosition);
                act.getSettings().setTeam(s);
                reCreateTeamAdapter();
                teamBar.invalidate();
                return false;
            }
        });
        rlAbout = (RelativeLayout) rView.findViewById(R.id.rlAbout);
        rlAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAbout.onAbout();
            }
        });
    }
    private void reCreateLangAdapter(){
        langGroups = new BaseObjectList();
        langGroups.add(act.getSettings().getLanguage());
        langs = new BaseObjectList();
        langs.add(new StringStringPair("def", act.getResources().getString(R.string.use_default_lang)));
        langs.add(new StringStringPair("de", "Deutsch"));
        langs.add(new StringStringPair("en", "English"));
        langs.add(new StringStringPair("fr","Français"));
        langs.add(new StringStringPair("ru", "Russian"));
        langAdapter = new ExAdapter(act, langGroups, langs,langBar,45,90);
        exLanguage.setAdapter(langAdapter);
    }
    private void reCreateTeamAdapter(){
        teamGroups = new BaseObjectList();
        teamGroups.add(act.getSettings().getTeam());
        teams = new BaseObjectList();
        teams.add(new IntStringPair(0,"Material Cyan"));
        teams.add(new IntStringPair(1,"Material Brief"));
        teams.add(new IntStringPair(2, "Custom team"));
        exTeam.setAdapter(teamAdapter);
    }
    private class ExAdapter extends BaseExpandableListAdapter{
        private final Context context;
        private LayoutInflater inf;
        private TextView tvLangGroupe;
        private BaseObjectList groups;
        private BaseObjectList childs;
        private RelativeLayout rl;
        private int collapsedHeight;
        private int expandedHeight;
        public ExAdapter(Context context, BaseObjectList groups, BaseObjectList childs, RelativeLayout rl, int collapsedHeight, int expandedHeight){
            this.context = context;
            inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.groups = (BaseObjectList) groups.clone();
            this.childs = (BaseObjectList) childs.clone();
            this.rl = rl;
            this.collapsedHeight = collapsedHeight;
            this.expandedHeight = expandedHeight;
        }
        @Override
        public int getGroupCount() {
            return groups.size();
        }
        @Override
        public int getChildrenCount(int groupPosition) {
            return childs.size();
        }
        @Override
        public Object getGroup(int groupPosition) {
            return groups.get(groupPosition);
        }
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childs.get(childPosition);
        }
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }
        @Override
        public boolean hasStableIds() {
            return false;
        }
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View groupView = inf.inflate(R.layout.lang_group, null);
            tvLangGroupe = (TextView) groupView.findViewById(R.id.tvLangGroupe);
            tvLangGroupe.setTypeface(Typeface.createFromAsset(getMainActivity().getAssets(), "fonts/Roboto-Light.ttf"));
            tvLangGroupe.setText((String)groups.get(groupPosition).getName());

            if(isExpanded){
                ViewGroup.LayoutParams lp = rl.getLayoutParams();
                lp.height = (int) Convertor.convertDpToPixel(expandedHeight,context);
                rl.setLayoutParams(lp);
            }
            else{
                ViewGroup.LayoutParams lp = rl.getLayoutParams();
                lp.height = (int) Convertor.convertDpToPixel(collapsedHeight,context);
                rl.setLayoutParams(lp);
            }
            rl.invalidate();
            return groupView;
        }
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView = inf.inflate(R.layout.lang_item, null);
            TextView tvLangItem = (TextView) itemView.findViewById(R.id.tvItem);
            tvLangItem.setTypeface(Typeface.createFromAsset(getMainActivity().getAssets(), "fonts/Roboto-Light.ttf"));
            tvLangItem.setText((String) childs.get(childPosition).getName());
            return itemView;
        }
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
        public BaseObjectList getChilds(){
            return childs;
        }
    }
    public void setOnAbout(OnAbout about){
        this.onAbout = about;
    }
}

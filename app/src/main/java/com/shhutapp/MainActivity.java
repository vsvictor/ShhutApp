package com.shhutapp;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.database.sqlite.SQLiteDatabase;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shhutapp.data.QueitCard;
import com.shhutapp.data.SMSCard;
import com.shhutapp.data.WhiteListCard;
import com.shhutapp.fragments.Header;
import com.shhutapp.fragments.area.AreaCard;
import com.shhutapp.help.Help;
import com.shhutapp.pages.AreaPage;
import com.shhutapp.pages.BasePage;
import com.shhutapp.pages.MainPage;
import com.shhutapp.data.DBHelper;
import com.shhutapp.pages.MessagePage;
import com.shhutapp.pages.QueitTimePage;
import com.shhutapp.pages.SettingsPage;
import com.shhutapp.pages.WhiteListPage;
import com.shhutapp.services.AppReceiver;
import com.shhutapp.services.CallReceiver;
import com.shhutapp.services.Carder;
import com.shhutapp.services.Finder;
import com.shhutapp.services.Locator;
import com.shhutapp.services.MessageReceiver;
import com.shhutapp.start.StartHelpEighth;
import com.shhutapp.start.StartHelpFirst;
import com.shhutapp.start.StartHelpSeventh;
import com.shhutapp.utils.Convertor;

import java.util.List;

public class MainActivity extends ActionBarActivity {
    private static MainActivity act;
    private LayoutInflater inf;
    private boolean isCardListEmty;
    private boolean isMessageListEmpty;
    private boolean isWhiteListEmpty;
    private boolean isQueitTimeEmpty;

    private static boolean isDream;

    private boolean helpInStart = true;

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    public static Header header;
    private RelativeLayout actionBar;

    private SMSCard selected_sms;
    private WhiteListCard selectetd_whitelist;
    private QueitCard selected_queit_card;
    private AppSettings settings;
    public static MainPage mpage;

    private CallReceiver calls;
    private MessageReceiver mess;
    private AppReceiver apps;
    private int old_statusbar_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.main);
        //turnGPSOn();

/*        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            old_statusbar_color = getWindow().getStatusBarColor();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.blue_action_bar));
        }*/
        replaceStatusBarColor();
        act = this;
        //isCardListEmty = false;
        //isMessageListEmpty = false;
        //isWhiteListEmpty = false;
        //isQueitTimeEmpty = false;
        actionBar = (RelativeLayout) this.findViewById(R.id.actionBar);
        inf = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
/*        if(isNetwork()) {
            Intent intent = new Intent(this, Locator.class);
            intent.putExtra("accuracy", settings.getDefaultAccuracy());
            startService(intent);
            startService(new Intent(this, Finder.class));
        }*/
        startService(new Intent(this, Carder.class));
        settings = new AppSettings(this);
        if (savedInstanceState == null) {
            header = new Header(this);
            getSupportFragmentManager().beginTransaction().add(R.id.header, header,"nodelete").commit();
            mpage = new MainPage(this);
            getSupportFragmentManager().beginTransaction().add(R.id.container, mpage).commit();
            //if(true){
            if(settings.isFirst()){
                setHelpInStart(true);
                StartHelpFirst help = new StartHelpFirst(this);//Help(this);
                getSupportFragmentManager().beginTransaction().add(R.id.main, help).commitAllowingStateLoss();
                settings.setFirst(false);
            }
        }
        clearQueitCard();
        clearWhiteList();
        clearSMS();

        calls = new CallReceiver();
        mess = new MessageReceiver();
        apps = new AppReceiver();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume(){
        super.onResume();
        IntentFilter callsFilter = new IntentFilter();
        callsFilter.addAction(Actions.Call);
        callsFilter.addAction(Actions.OutCall);
        registerReceiver(calls, callsFilter);
        IntentFilter msgFilter = new IntentFilter();
        msgFilter.addAction(Actions.Messages);
        registerReceiver(mess, msgFilter);
        IntentFilter appFilter = new IntentFilter();
        appFilter.addAction(Actions.NotifiReceive);
        appFilter.addAction(Actions.NotifiRegistration);
        registerReceiver(apps,appFilter);
    }
    @Override
    public void onStop(){
        super.onStop();
        //turnGPSOff();
        unregisterReceiver(calls);
        unregisterReceiver(mess);
        unregisterReceiver(apps);
    }

    /*
    @Override
    public void onBackPressed(){
    }
*/
    public static MainActivity getMainActivity(){
        return act;
    }
    public BasePage createPageFromID(int id){
        BasePage page = null;
        switch (id){
            case BasePage.Pages.mainPage:{
                page = new MainPage(this);
                break;
            }
            case BasePage.Pages.messagePage:{
                page = new MessagePage(this);
                break;
            }
            case BasePage.Pages.whitelistPage:{
                page = new WhiteListPage(this);
                break;
            }
            case BasePage.Pages.queitTimePage:{
                page = new QueitTimePage(this);
                break;
            }
            case BasePage.Pages.areaPage:{
                page = new AreaPage(this);
                break;
            }
            case BasePage.Pages.areaCard:{
                page = new AreaCard(this);
                break;
            }
            case BasePage.Pages.settingsPage:{
                page = new SettingsPage(this);
                break;
            }

            default:{
                page = null;
                break;
            }
        }
        return page;
    }
    public LayoutInflater getInflater(){ return inf;}
    public Header getHeader(){
        return  header;
    }
    public MainPage getMainPage(){
        return mpage;
    }
    public boolean isCardListEmty(){
        Cursor c = getDB().query("cards", null, null, null, null, null, null);
        if(c.moveToFirst()) isCardListEmty = false;
        else isCardListEmty = true;
        return  isCardListEmty;
    }
    public boolean isMessageListEmpty(){
        Cursor c = getDB().query("sms", null, null, null, null, null, null);
        if(c.moveToFirst()) isMessageListEmpty = false;
        else isMessageListEmpty = true;
        return isMessageListEmpty;
    }
    public boolean isWhiteListEmpty(){
        Cursor c = getDB().query("white_list", null, null, null, null, null, null);
        if(c.moveToFirst()) isWhiteListEmpty = false;
        else isWhiteListEmpty = true;
        return isWhiteListEmpty;
    }
    public boolean isQueitTimeEmpty(){
        Cursor c = getDB().query("quieties", null, null, null, null, null, null);
        if(c.moveToFirst()) isQueitTimeEmpty = false;
        else isQueitTimeEmpty = true;
        return isQueitTimeEmpty;
    }
    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        super.onWindowAttributesChanged(params);
    }

    public void setActionBarGray(){
        actionBar.setBackgroundColor(this.getResources().getColor(R.color.gray));
    }
    public void setActionBarBlue(){
        actionBar.setBackgroundColor(this.getResources().getColor(R.color.blue_action_bar));
    }

    public SQLiteDatabase getDB() {
        return db;
    }
    public DBHelper getDBHelper(){
        return dbHelper;
    }
    public AppSettings getSettings(){
        return settings;
    }

    public void selectSMS(SMSCard card){
        this.selected_sms = new SMSCard(card.getID(), card.getName(), card.getText());
    }
    public SMSCard getSMS(){
        return this.selected_sms;
    }
    public void clearSMS(){
        this.selected_sms = null;
    }
    public void selectWhiteList(WhiteListCard card){
        this.selectetd_whitelist = new WhiteListCard(card.getID(), card.getName());
    }
    public WhiteListCard getWhiteList(){
        return this.selectetd_whitelist;
    }
    public void clearWhiteList(){
        this.selectetd_whitelist = null;
    }
    public void selectQueitCard(QueitCard card){
        this.selected_queit_card = new QueitCard(card.getID(), card.getName());
    }
    public QueitCard getQueitCard(){
        return selected_queit_card;
    }
    public void clearQueitCard(){
        this.selected_queit_card = null;
    }
    public void setDream(boolean b){
        isDream = b;
    }
    public static boolean isDream(){
        return isDream;
    }

    private boolean isNetwork(){
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
    public void turnGPSOn()
    {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        this.sendBroadcast(intent);

        String provider = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            this.sendBroadcast(poke);
        }
    }
    public void turnGPSOff()
    {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", false);
        this.sendBroadcast(intent);

        String provider = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(provider.contains("gps")){ //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            this.sendBroadcast(poke);
        }
    }
    public void setHelpInStart(boolean b){
        this.helpInStart = b;
    }
    public boolean isHelpInStart(){
        return helpInStart;
    }
    public void resumeStatusBarColor(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            old_statusbar_color = getWindow().getStatusBarColor();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(old_statusbar_color);
        }
    }
    public void replaceStatusBarColor(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            old_statusbar_color = getWindow().getStatusBarColor();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.blue_action_bar));
        }
    }

}

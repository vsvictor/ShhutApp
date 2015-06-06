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

import com.shhutapp.data.SMSCard;
import com.shhutapp.data.WhiteListCard;
import com.shhutapp.fragments.Header;
import com.shhutapp.help.Help;
import com.shhutapp.pages.BasePage;
import com.shhutapp.pages.MainPage;
import com.shhutapp.data.DBHelper;
import com.shhutapp.pages.MessagePage;
import com.shhutapp.pages.QueitTimePage;
import com.shhutapp.pages.WhiteListPage;
import com.shhutapp.services.Carder;
import com.shhutapp.services.Finder;
import com.shhutapp.services.Locator;
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

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private  Header header;
    private RelativeLayout actionBar;

    private SMSCard selected_sms;
    private WhiteListCard selectetd_whitelist;
    private AppSettings settings;
    private MainPage mpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.main);
        turnGPSOn();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.blue_action_bar));
        }
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
            getSupportFragmentManager().beginTransaction().add(R.id.header, header).commit();
            mpage = new MainPage(this);
            getSupportFragmentManager().beginTransaction().add(R.id.container, mpage).commit();
            if(false){
            //if(settings.isFirst()){
                Help help = new Help(this);
                getSupportFragmentManager().beginTransaction().add(R.id.main, help).commitAllowingStateLoss();

            }
        }
        clearSMS();
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
        String sw = String.valueOf(Convertor.convertPixelsToDp(480, this));
        String sh = String.valueOf(Convertor.convertPixelsToDp(800, this));
        Log.i("Size", sw + "х" + sh);
        //Toast.makeText(this, sw+"х"+sh, Toast.LENGTH_LONG);
//        AppEventsLogger.activateApp(this);
    }
    @Override
    public void onStop(){
        super.onStop();
        turnGPSOff();
//        AppEventsLogger.deactivateApp(this);
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
        else isCardListEmty = false;
        return  isCardListEmty;
    }
    public boolean isMessageListEmpty(){
        Cursor c = getDB().query("sms", null, null, null, null, null, null);
        if(c.moveToFirst()) isMessageListEmpty = false;
        else isMessageListEmpty = false;
        return isMessageListEmpty;
    }
    public boolean isWhiteListEmpty(){
        Cursor c = getDB().query("white_list", null, null, null, null, null, null);
        if(c.moveToFirst()) isWhiteListEmpty = false;
        else isWhiteListEmpty = false;
        return isWhiteListEmpty;
    }
    public boolean isQueitTimeEmpty(){
        Cursor c = getDB().query("quieties", null, null, null, null, null, null);
        if(c.moveToFirst()) isQueitTimeEmpty = false;
        else isQueitTimeEmpty = false;
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
}

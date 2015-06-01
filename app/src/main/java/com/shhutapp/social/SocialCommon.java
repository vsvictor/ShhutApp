package com.shhutapp.social;

import android.net.Uri;

import com.shhutapp.MainActivity;
import com.shhutapp.R;

/**
 * Created by victor on 27.05.15.
 */
public class SocialCommon {
    private static String url = "http://shhutapp.com";
    private static String name = "ShhutApp";
    private static String desc = "ShhutApp very good application!";
    public static String getText(){
        return MainActivity.getMainActivity().getResources().getString(R.string.social_text);
    }
    public static Uri getUri(){
        return Uri.parse(url);
    }
    public static String getUrl(){
        return url;
    }
    public static String getName(){
        return name;
    }
    public static String getDescription(){
        return desc;
    }
}


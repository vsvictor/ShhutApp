package com.shhutapp.utils;

import android.graphics.Point;

import com.google.android.gms.maps.model.LatLng;
import com.shhutapp.geo.maparea.MapAreasConstants;

/**
 * Created by victor on 29.05.15.
 */
public class Geo {
    public static double distance(LatLng StartP, LatLng EndP) {
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return MapAreasConstants.RADIUS_OF_EARTH_METERS * c;
    }
    public static double distance(Point p1, Point p2){
        float x = (p2.x-p1.x)*(p2.x-p1.x);
        float y = (p2.y-p1.y)*(p2.y-p1.y);
        return Math.sqrt(x+y);
    }

}

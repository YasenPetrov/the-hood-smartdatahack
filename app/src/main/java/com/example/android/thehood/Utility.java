package com.example.android.thehood;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseGeoPoint;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by yasen on 18/02/15.
 */
public class Utility {

    public ParseGeoPoint geoPointFromLatLng(LatLng latLng) {
        return new ParseGeoPoint(latLng.latitude, latLng.longitude);
    }

    static String formatDate(Date date) {
        return DateFormat.getDateInstance().format(date);
    }
}

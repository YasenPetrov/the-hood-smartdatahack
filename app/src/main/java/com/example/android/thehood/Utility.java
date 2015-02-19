package com.example.android.thehood;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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

    public static String getPreferredDistanceUnits(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String unitType = prefs.getString(
                context.getString(R.string.pref_units_key),
                context.getString(R.string.pref_units_imperial));
        if (unitType.equals(R.string.pref_units_imperial)) {
            return "mi";
        }
        return "km";
    }

    static String formatDate(Date date) {
        return DateFormat.getDateInstance().format(date);
    }
}

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
    private static final String MILES = "mi";
    private static final String KILOMETERS = "km";
    private static final double MILES_TO_KM = 1.609344;

    public ParseGeoPoint geoPointFromLatLng(LatLng latLng) {
        return new ParseGeoPoint(latLng.latitude, latLng.longitude);
    }

    public static String getPreferredDistanceUnits(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String unitType = prefs.getString(
                context.getString(R.string.pref_units_key),
                context.getString(R.string.pref_units_imperial));
        if (unitType.equals(context.getString(R.string.pref_units_imperial))) {
            return MILES;
        }
        return KILOMETERS;
    }

    static String formatDate(Date date) {
        return DateFormat.getDateTimeInstance().format(date);
    }

    public static double formatDistance(Context context, String radiusString) {
        if (radiusString.isEmpty()){
            return 0.0;
        }
        if (getPreferredDistanceUnits(context) == MILES){
            return Double.parseDouble(radiusString) * MILES_TO_KM;
        }
        //Kilometers
        return Double.parseDouble(radiusString);
    }
}

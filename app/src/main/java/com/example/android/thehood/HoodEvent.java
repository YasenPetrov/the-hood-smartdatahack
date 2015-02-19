package com.example.android.thehood;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by Elitsa on 19.2.2015 г..
 */
@ParseClassName("Events")
public class HoodEvent extends ParseObject{
    public String getText() {
        return getString("text");
    }

    public void setText(String value) {
        put("text", value);
    }

    public ParseUser getAuthor() {
        return getParseUser("user");
    }

    public void setAuthor(ParseUser value) {
        put("user", value);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }

    public void setLocation(ParseGeoPoint value) {
        put("location", value);
    }

    public void setRadius(int R)
    {
        put("radius",R);
    }
    public int getRadius()
    {
        return getInt("radius");
    }

    public static ParseQuery<HoodEvent> getQuery() {
        return ParseQuery.getQuery(HoodEvent.class);
    }
}

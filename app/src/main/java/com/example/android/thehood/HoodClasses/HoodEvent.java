package com.example.android.thehood.HoodClasses;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by Elitsa on 19.2.2015 г..
 */
@ParseClassName("Event")
public class HoodEvent extends ParseObject {
    public String getText() {
        return getString("description");
    }

    public void setText(String value) {
        put("description", value);
    }

    public ParseUser getAuthor() {
        return getParseUser("author");
    }

    public void setAuthor(ParseUser value) {
        put("author", value);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }

    public void setLocation(ParseGeoPoint value) {
        put("location", value);
    }

    public void setRadius(int R) {
        put("visibility_radius", R);
    }

    public int getRadius() {
        return getInt("visibility_radius");
    }

    public static ParseQuery<HoodEvent> getQuery() {
        return ParseQuery.getQuery(HoodEvent.class);
    }
    public String getTitle() {return getString("title");}

    public void addComment(ParseObject c) {
        add("comments", c);
    }


}

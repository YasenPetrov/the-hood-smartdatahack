package com.example.android.thehood;

/**
 * Created by stiliyan on 15-2-17.
 */
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;

//The Class for our posts
@ParseClassName("Post")
public class HoodPost extends ParseObject {

    public void setTitle(String title) { put("title", title);}
    public String getTitle() {return getString("title");}

    public String getDescription() {
        return getString("description");
    }
    public void setDescription(String value) {
        put("description", value);
    }

    public ParseUser getAuthor() {
        return getParseUser("author");
    }
    public void setUser(ParseUser value) {
        put("author", value);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }
    public void setLocation(ParseGeoPoint value) {
        put("location", value);
    }

    public void setRadius(int R)
    {
        put("visibility_radius",R);
    }
    public int getRadius()
    {
        return getInt("visibility_radius");
    }

    public Date getStartTime () { return getDate("created_at");}

    public Date getEndTime () { return getDate("ends_at"); }
    public void setEndTime(Date d) { put("ends_at", d); }

    public void addComment(ParseObject c) {
        add("comments", c);
    }

    public static ParseQuery<HoodPost> getQuery() {
        return ParseQuery.getQuery(HoodPost.class);
    }
}

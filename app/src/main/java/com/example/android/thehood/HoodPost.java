package com.example.android.thehood;

/**
 * Created by stiliyan on 15-2-17.
 */
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

//The Class for our posts
@ParseClassName("Posts")
public class HoodPost extends ParseObject {

    public String getText() {
        return getString("text");
    }

    public void setText(String value) {
        put("text", value);
    }

    public ParseUser getUser() {
        return getParseUser("user");
    }

    public void setUser(ParseUser value) {
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

    public static ParseQuery<HoodPost> getQuery() {
        return ParseQuery.getQuery(HoodPost.class);
    }
}

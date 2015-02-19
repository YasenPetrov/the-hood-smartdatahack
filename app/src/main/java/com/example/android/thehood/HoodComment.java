package com.example.android.thehood;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by yasen on 19/02/15.
 */
@ParseClassName("Comment")
public class HoodComment extends ParseObject {

    public ParseUser getAuthor() {
        return getParseUser("author");
    }

    public void setAuthor(ParseUser value) {
        put("author", value);
    }

    public ParseObject getPost() {
        return getParseObject("post");
    }

    public void setPost(ParseObject post) {
        put("post", post);
    }

    public String getText() {
        return getString("text");
    }

    public void setText(String text) {
        put("text", text);
    }

    public Date getCreatedAt() {
        return getDate("createdAt");
    }

    public Date getUpdatedAt() {
        return getDate("updatedAt");
    }

    public static ParseQuery<HoodPost> getQuery() {
        return ParseQuery.getQuery(HoodPost.class);
    }
}

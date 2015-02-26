package com.example.android.thehood.HoodClasses;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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

    public static ParseQuery<HoodComment> getQuery() {
        return ParseQuery.getQuery(HoodComment.class);
    }
}

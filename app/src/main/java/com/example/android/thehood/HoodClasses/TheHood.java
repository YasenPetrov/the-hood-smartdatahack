package com.example.android.thehood.HoodClasses;

/**
 * Created by yasen on 16/02/15.
 */
import android.app.Application;

import com.example.android.thehood.R;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseTwitterUtils;

public class TheHood extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(HoodPost.class);
        ParseObject.registerSubclass(HoodEvent.class);
        ParseObject.registerSubclass(HoodComment.class);
        // Required - Initialize the Parse SDK
        Parse.initialize(this, getString(R.string.parse_app_id),
                getString(R.string.parse_client_key));

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        // Optional - If you don't want to allow Twitter login, you can
        // remove this line (and other related ParseTwitterUtils calls)
        ParseTwitterUtils.initialize(getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret));
    }
}

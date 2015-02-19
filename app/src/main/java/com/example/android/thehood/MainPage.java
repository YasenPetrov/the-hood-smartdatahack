package com.example.android.thehood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.parse.ParseQueryAdapter;


public class MainPage extends Activity {
    private String LOG_TAG = MainPage.class.getSimpleName();

    private Button mEventButton;
    private Button mPostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        //Loads all posts
        ParseQueryAdapter<HoodPost> adapter = new ParseQueryAdapter<HoodPost>(this, HoodPost.class);
        adapter.setTextKey("title");
        ListView postsListView = (ListView) this.findViewById(R.id.posts_listview);
        postsListView.setAdapter(adapter);

        mEventButton = (Button) findViewById(R.id.event_button);
        mEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postIntent = new Intent(v.getContext(), PostEventActivity.class);
                startActivity(postIntent);
            }
        });

        mPostButton = (Button) findViewById(R.id.post_button);
        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postIntent = new Intent(v.getContext(), PostMessageActivity.class);
                startActivity(postIntent);
            }
        });

        Button viewPostsButton = (Button) findViewById(R.id.view_posts_button);
        viewPostsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewPostsIntent = new Intent(v.getContext(), ViewPostsActivity.class);
                startActivity(viewPostsIntent);
            }
        });

    }

    @Override
    protected void onResume() {
        ParseQueryAdapter<HoodPost> adapter = new ParseQueryAdapter<HoodPost>(this, HoodPost.class);
        adapter.setTextKey("title");
        ListView postListView = (ListView) this.findViewById(R.id.posts_listview);
        postListView.setAdapter(adapter);
        Log.v(LOG_TAG, "Resumed");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.v(LOG_TAG, "Paused");
        super.onPause();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG,"onPostCreate");
        super.onPostCreate(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

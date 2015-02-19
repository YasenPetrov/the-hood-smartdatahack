package com.example.android.thehood;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewPostsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewPostsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
/**
 * A placeholder fragment containing a simple view.
 */
public class  ViewPostsFragment extends android.support.v4.app.Fragment {

    private final String LOG_TAG = "ViewPostFragment says: ";
    // A temp variable for the visibility of posts, we should really get it from sharedPrefs
    private int max_post_dist = 1;
    private ParseQueryAdapter<HoodPost> parseQueryAdapter;

    public ViewPostsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_posts, container, false);

        // Make a custom query
        ParseQueryAdapter.QueryFactory<HoodPost> factory =
                new ParseQueryAdapter.QueryFactory<HoodPost>() {
                    public ParseQuery<HoodPost> create() {
                        ParseGeoPoint myLoc = ParseUser.getCurrentUser().getParseGeoPoint("address");
                        ParseQuery<HoodPost> query = HoodPost.getQuery();
                        query.whereWithinKilometers("location", myLoc, max_post_dist);
                        query.include("author");
                        query.include("text");
                        query.orderByDescending("startTime");
//                        mapQuery.setLimit(max_post_dist);
                        return query;
                    }
                };

        parseQueryAdapter = new ParseQueryAdapter<HoodPost>(getActivity(), HoodPost.class) {
            @Override
            public View getItemView(HoodPost post, View view, ViewGroup parent) {
                if (view == null) {
                    view = View.inflate(getContext(), R.layout.hood_post_item, null);
                }
                TextView postTextView = (TextView) view.findViewById(R.id.text_view);
                TextView authorView = (TextView) view.findViewById(R.id.author_view);
                TextView createdAtView = (TextView) view.findViewById(R.id.created_at_view);

                ParseUser author = post.getAuthor();
                String name = "";
                try {
                    name = author.fetchIfNeeded().getString("name");

                } catch (ParseException e) {
                    Log.v(LOG_TAG, e.toString());
                    e.printStackTrace();
                }

                postTextView.setText(post.getDescription());
                authorView.setText(name);
                createdAtView.setText(Utility.formatDate(post.getCreatedAt()));
                return view;
            }
        };
        parseQueryAdapter.setTextKey("title");
        ListView postsListView = (ListView) rootView.findViewById(R.id.posts_listview);
        postsListView.setAdapter(parseQueryAdapter);
        return rootView;
    }


    private void doMapQuery() {
        // 1
        ParseGeoPoint myLoc = ParseUser.getCurrentUser().getParseGeoPoint("address");
//        if (myLoc == null) {
//            cleanUpMarkers(new HashSet<String>());
//            return;
//        }

        ParseQuery<HoodPost> mapQuery = HoodPost.getQuery();
        // 4
        mapQuery.whereWithinKilometers("location", myLoc, max_post_dist);
        // 5
        mapQuery.include("author");
        mapQuery.include("text");
        mapQuery.orderByDescending("startTime");
        mapQuery.setLimit(max_post_dist);
        // 6
        mapQuery.findInBackground(new FindCallback<HoodPost>() {
            @Override
            public void done(List<HoodPost> objects, ParseException e) {
                // Handle the results
            }
        });
    }
}
package com.example.android.thehood;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    public PopupWindow popWindow;
    private final String LOG_TAG = "ViewPostFragment says: ";
    // A temp variable for the visibility of posts, we should really get it from sharedPrefs
    private int max_post_dist = 1;
    private ParseQueryAdapter<HoodPost> postQueryAdapter;
    private ParseUser mCurrentUser = ParseUser.getCurrentUser();

    public ViewPostsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_posts, container, false);
        ListView commentsListView = (ListView) rootView.findViewById(R.id.posts_listview);

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

        postQueryAdapter = new ParseQueryAdapter<HoodPost>(getActivity(), HoodPost.class) {
            @Override
            public View getItemView(final HoodPost post, View view, ViewGroup parent) {
                if (view == null) {
                    view = View.inflate(getContext(), R.layout.hood_post_item, null);
                }
                TextView postTextView = (TextView) view.findViewById(R.id.text_view);
                TextView authorView = (TextView) view.findViewById(R.id.author_view);
                TextView createdAtView = (TextView) view.findViewById(R.id.created_at_view);
                Button viewCommentsButton = (Button) view.findViewById(R.id.view_comments_button);

                ParseUser author = post.getAuthor();
                final ParseUser currentUser = ParseUser.getCurrentUser();

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

                viewCommentsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        HoodComment comment = new HoodComment();
//
//                        comment.setText(commentView.getText().toString());
//                        comment.setAuthor(currentUser);
//                        comment.setPost(post);
//                        post.addComment(comment);
//                        currentUser.add("comments", comment);
//                        ArrayList<ParseObject> objectsToSave = new ArrayList<ParseObject>();
//                        objectsToSave.add(post);
//                        objectsToSave.add(comment);
//                        objectsToSave.add(currentUser);
//                        try {
//                            ParseObject.saveAll(objectsToSave);
//
//                        } catch(ParseException e) {
//                            e.printStackTrace();
//                        }
//                        commentView.setText("");
//                        displayCommentDialog(post);
                        showCommentsPopup(v, post);

                    }
                });
                return view;
            }
        };
        postQueryAdapter.setTextKey("title");
        ListView postsListView = (ListView) rootView.findViewById(R.id.posts_listview);
        postsListView.setAdapter(postQueryAdapter);
        return rootView;
    }


//
    private ParseQueryAdapter<HoodComment> makeCommentQueryAdapter(final HoodPost post) {
        // Make query factory
        ParseQueryAdapter.QueryFactory<HoodComment> factory =
                new ParseQueryAdapter.QueryFactory<HoodComment>() {
                    public ParseQuery<HoodComment> create() {
                        ParseQuery<HoodComment> query = HoodComment.getQuery();
                        query.whereEqualTo("post", post);
                        return query;
                    }
                };
        // Create adapter with our custom factory
        ParseQueryAdapter<HoodComment> adapter = new ParseQueryAdapter<HoodComment>(getActivity(),
                factory) {
            @Override
            public View getItemView(final HoodComment comment, View view, ViewGroup parent) {
                if (view == null) {
                    view = View.inflate(getContext(), R.layout.hood_comment_item, null);
                }
                TextView commentTextView = (TextView) view.findViewById(R.id.comment_text_view);
                TextView authorTextView = (TextView) view.findViewById(
                        R.id.comment_author_textview);
                TextView datePublishedTextView = (TextView) view.findViewById(
                        R.id.comment_date_textview);

                Date createdAt;
                String authorName = "";
                try {
                    authorName = comment.getAuthor().fetchIfNeeded().getString("name");

                } catch (ParseException e) {
                    Log.v(LOG_TAG, e.toString());
                    e.printStackTrace();
                }
                try {
                    createdAt = comment.getCreatedAt();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    createdAt = Calendar.getInstance().getTime();
                }
                Log.v(LOG_TAG, createdAt.toString());
                if(createdAt == null) {
                    Log.v(LOG_TAG, createdAt.toString());
                    createdAt = Calendar.getInstance().getTime();
                }
                commentTextView.setText(comment.getString("text"));
                authorTextView.setText(authorName);
                datePublishedTextView.setText(Utility.formatDate(createdAt));
                return view;
            }
        };
        return adapter;
    }

    public void showCommentsPopup(View v, final HoodPost post){

        LayoutInflater layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflate the custom popup layout
        final View inflatedView = layoutInflater.inflate(R.layout.comments_popup_layout, null,false);
//        inflatedView.setBackgroundColor(getResources().getColor(R.color.background_material_dark));

        // find the ListView in the popup layout
        final ListView listView = (ListView)inflatedView.findViewById(R.id.comments_listview);
        // find the comment input box
        final EditText inputCommentTextView = (EditText)inflatedView.findViewById(R.id.input_comment_text_view);
        // find the "Comment" button
        final Button commentButton = (Button) inflatedView.findViewById(R.id.comment_button);
        // set button behaviour
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = inputCommentTextView.getText().toString();
                Log.v(LOG_TAG, commentText);
                if(!commentText.isEmpty()) {
                    // save the post
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    HoodComment comment = new HoodComment();
                    comment.setAuthor(currentUser);
                    comment.setPost(post);
                    comment.setText(commentText);
                    post.addComment(comment);
                    currentUser.add("comments", comment);
                    ArrayList<ParseObject> objectsToSave = new ArrayList<ParseObject>();
                    objectsToSave.add(post);
                    objectsToSave.add(comment);
                    objectsToSave.add(currentUser);
                    try {
                        ParseObject.saveAll(objectsToSave);

                    } catch(ParseException e) {
                        e.printStackTrace();
                    }

                    // empty input EditText
                    inputCommentTextView.setText("");
                    // redisplay posts
                    listView.setAdapter(makeCommentQueryAdapter(post));
                }
            }
        });

        // get device size
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        int deviceHeight = size.y;


        // fill the data to the list items
        listView.setAdapter(makeCommentQueryAdapter(post));


        // set height depends on the device size
        popWindow = new PopupWindow(inflatedView, size.x - 50,size.y - 300, true);
        // set a background drawable with rounders corners
        popWindow.setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.abc_popup_background_mtrl_mult));
        // make it focusable to show the keyboard to enter in `EditText`
        popWindow.setFocusable(true);
        // make it outside touchable to dismiss the popup window
        popWindow.setOutsideTouchable(true);
        // show the popup at bottom of the screen and set some margin at bottom ie,
        popWindow.showAtLocation(v, Gravity.BOTTOM, 0,100);
    }

//    private void doMapQuery() {
//        // 1
//        ParseGeoPoint myLoc = ParseUser.getCurrentUser().getParseGeoPoint("address");
////        if (myLoc == null) {
////            cleanUpMarkers(new HashSet<String>());
////            return;
////        }
//
//        ParseQuery<HoodPost> mapQuery = HoodPost.getQuery();
//        // 4
//        mapQuery.whereWithinKilometers("location", myLoc, max_post_dist);
//        // 5
//        mapQuery.include("author");
//        mapQuery.include("text");
//        mapQuery.orderByDescending("startTime");
//        mapQuery.setLimit(max_post_dist);
//        // 6
//        mapQuery.findInBackground(new FindCallback<HoodPost>() {
//            @Override
//            public void done(List<HoodPost> objects, ParseException e) {
//                // Handle the results
//            }
//        });
//    }


    }
package com.example.android.thehood;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostMessageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PostMessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostMessageFragment extends android.support.v4.app.Fragment {

    private static final String LOG_TAG = "PostFragment says: ";

    private SupportMapFragment mapFragment;
    private EditText pickStartTimeButton;
    private EditText pickEndTimeButton;
    private EditText pickStartDateButton;
    private EditText pickEndDateButton;
    private GoogleMap mMap;
    // Variables to store the event details
    private LatLng eventLatLng;

    public PostMessageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_message, container, false);
        registerViews(rootView);
        return rootView;
    }


    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.post_mapMessage);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.post_mapMessage, mapFragment).commit();
        }
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                mMap = map;
                ParseGeoPoint eventGeoPoint = ParseUser.getCurrentUser().getParseGeoPoint("Address");
                eventLatLng = new LatLng(eventGeoPoint.getLatitude(), eventGeoPoint.getLongitude());
                if (savedInstanceState != null) {
                    if (savedInstanceState.containsKey("eventLatLng")){
                        eventLatLng = savedInstanceState.getParcelable("eventLatLng");
                    }
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLatLng, (float) 14.5));
                mMap.addMarker(new MarkerOptions().position(eventLatLng));

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        mMap.clear();
                        eventLatLng = latLng;
                        mMap.addMarker(new MarkerOptions().position(latLng));
                    }
                });
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("eventLatLng", eventLatLng);
        super.onSaveInstanceState(outState);
    }

    private void registerViews(View v) {
        Button CreatePostButton = (Button) v.findViewById(R.id.create_event_buttonMessage);

        CreatePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(createPost())
                        getActivity().finish();
                } catch (ParseException e) {
                    Log.v(LOG_TAG, "Post was not created");
                    e.printStackTrace();
                }
            }
        });

    }

// A method used in the picker dialogs to set the labels ot the textViews
    private static void updateLabel(EditText et, String newText) {
        et.setText(newText);
    }

    private boolean createPost() throws ParseException {
        String title = ((TextView) getActivity().findViewById(R.id.title_input_fieldMessage))
                .getText().toString();
        String description = ((TextView) getActivity()
                .findViewById(R.id.description_input_fieldMessage))
                .getText().toString();
        String radiusString = ((TextView) getActivity().findViewById(R.id.radius_input_fieldMessage))
                .getText().toString();

        boolean valid = validatePostData(title, radiusString, description);

        if(valid) {
            ParseUser currentUser = ParseUser.getCurrentUser();
            int radius = Integer.parseInt(radiusString);

            // Make a new event, add it to the current user's posts_and_events
            ParseObject post = new ParseObject("Post");

            post.put("location", new ParseGeoPoint(eventLatLng.latitude, eventLatLng.longitude));
            post.put("title", title);
            post.put("text", description);
            post.put("visibility_radius", radius);
            post.put("author", currentUser);
            currentUser.add("posts_and_events", post);
            post.save();
            return true;
        }
        return false;
    }

    private boolean validatePostData(String title, String radius, String description) {
        if(title.isEmpty()) {
            Toast.makeText(getActivity(), "An event without a title? Come on...", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        if(radius.isEmpty()) {
            Toast.makeText(getActivity(),"Enter a radius, por favor",Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        // TODO: Decide on a maximum radius and perform a validation on that
        return true;
    }
    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */

}
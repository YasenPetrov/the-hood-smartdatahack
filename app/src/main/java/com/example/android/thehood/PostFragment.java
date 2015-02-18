package com.example.android.thehood;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.util.Calendar;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends android.support.v4.app.Fragment {

    private final String LOG_TAG = "PostFragment says: ";

    private Calendar fromDate;
    private Calendar toDate;
    private Calendar fromTime;
    private Calendar toTime;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private LatLng eventLatLng;

    public PostFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(LOG_TAG, Calendar.getInstance().toString());
        View rootView = inflater.inflate(R.layout.fragment_post, container, false);
        registerViews(rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.post_map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.post_map, mapFragment).commit();
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
        Button pickStartTimeButton = (Button) v.findViewById(R.id.pick_start_time_button);
        Button pickEndTimeButton = (Button) v.findViewById(R.id.pick_end_time_button);
        Button pickStartDateButton = (Button) v.findViewById(R.id.pick_start_date_button);
        Button pickEndDateButton = (Button) v.findViewById(R.id.pick_end_date_button);

        pickStartTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });
        pickEndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });

        pickStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        pickEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }


    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */

}

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
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
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
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends android.support.v4.app.Fragment {

    private static final String LOG_TAG = "PostFragment says: ";

    private static Calendar startDate = Calendar.getInstance();
    private static Calendar endDate = Calendar.getInstance();
    private SupportMapFragment mapFragment;
    private EditText pickStartTimeButton;
    private EditText pickEndTimeButton;
    private EditText pickStartDateButton;
    private EditText pickEndDateButton;

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
    public void onActivityCreated(Bundle savedInstanceState) {
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
                ParseGeoPoint eventGeoPoint = ParseUser.getCurrentUser().getParseGeoPoint("Address");
                LatLng eventLatLng = new LatLng(eventGeoPoint.getLatitude(), eventGeoPoint.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLatLng, (float) 14.5));
                map.addMarker(new MarkerOptions().position(eventLatLng));
            }
        });
    }

    private void registerViews(View v) {
        pickStartTimeButton = (EditText) v.findViewById(R.id.pick_start_time_button);
        pickEndTimeButton = (EditText) v.findViewById(R.id.pick_end_time_button);
        pickStartDateButton = (EditText) v.findViewById(R.id.pick_start_date_button);
        pickEndDateButton = (EditText) v.findViewById(R.id.pick_end_date_button);

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

        // Get the calling view, so we can set its text when we update the time
        private View viewCalledFrom;

        public TimePickerFragment(View v) {
            super();
            viewCalledFrom = v;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current set time as the default values for the picker
            final Calendar c;
            switch (viewCalledFrom.getId()) {
                case R.id.pick_start_time_button:
                    Log.v(LOG_TAG, "start time1");
                    c = startDate;
                    break;
                case R.id.pick_end_time_button:
                    Log.v(LOG_TAG, "end time!");
                    c = endDate;
                    break;
                default:
                    Log.v(LOG_TAG, "wtf?!");
                    c = Calendar.getInstance();
            }

            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            switch (viewCalledFrom.getId()) {
                case R.id.pick_start_time_button:
                    startDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    startDate.set(Calendar.MINUTE, minute);
                    Log.v(LOG_TAG, startDate.toString());
                    break;
                case R.id.pick_end_time_button:
                    endDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    endDate.set(Calendar.MINUTE, minute);
                    break;
                default:
                    Log.v(LOG_TAG, "What the actual fuck?!@!");
            }
            String newText = "" + hourOfDay + ":" + minute;
            updateLabel((EditText) viewCalledFrom, newText);
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        // Get the calling view, so we can set its text when we update the date
        private View viewCalledFrom;

        public DatePickerFragment() {
            super();
        }

        public DatePickerFragment(View v) {
            super();
            viewCalledFrom = v;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current set date as the default date in the picker
            final Calendar c;
            switch (viewCalledFrom.getId()) {
                case R.id.pick_start_date_button:
                    c = startDate;
                    break;
                case R.id.pick_end_date_button:
                    c = endDate;
                    break;
                default:
                    c = Calendar.getInstance();
            }

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            switch (viewCalledFrom.getId()) {
                case R.id.pick_start_date_button:
                    startDate.set(Calendar.YEAR, year);
                    startDate.set(Calendar.MONTH, month);
                    startDate.set(Calendar.DAY_OF_MONTH, day);
                    break;
                case R.id.pick_end_date_button:
                    endDate.set(Calendar.YEAR, year);
                    endDate.set(Calendar.MONTH, month);
                    endDate.set(Calendar.DAY_OF_MONTH, day);
                    break;
            }
            String newText = "" + year + "/" + (month+1) + "/" + day;
            updateLabel((EditText) viewCalledFrom, newText);
        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment(v);
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(v);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    // A method used in the picker dialogs to set the labels ot the textViews
    private static void updateLabel(EditText et, String newText) {
        et.setText(newText);
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */

}

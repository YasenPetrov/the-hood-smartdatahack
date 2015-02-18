package com.example.android.thehood;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;


public class UserAddressInput extends FragmentActivity {

    private String LOG_TAG = UserAddressInput.class.getSimpleName();
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ParseUser currentUser;
    private Button mSubmitLocationButton;
    private Button mGPSLocationButton;
    LatLng savedMarkerCoordinates;
    private final float MY_LOCATION_ZOOM = (float) 15.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentUser = ParseUser.getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_address_input);
        setUpMapIfNeeded();

        // Get submit button and make it invisible until a user puts a marker
        mSubmitLocationButton = (Button) findViewById(R.id.submit_location_button);
        mSubmitLocationButton.setVisibility(View.INVISIBLE);
        mGPSLocationButton = (Button) findViewById(R.id.gps_location_button);
        mGPSLocationButton.setVisibility(View.INVISIBLE);
        //GPS stuff
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if ( manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    Location location = manager.getLastKnownLocation(manager.PASSIVE_PROVIDER);
                    if (location != null) {
                        double longitude = location.getLongitude();
                        double latitude = location.getLatitude();
                        LatLng latLng = new LatLng(latitude, longitude);
                        savedMarkerCoordinates = latLng;
                        mMap.clear();
                        // Add a marker at the clicked location
                        mMap.addMarker(new MarkerOptions().position(latLng));
                        setUpSubmitButton(latLng);

                        //final ParseGeoPoint userAddress = new ParseGeoPoint(latitude, longitude);
                        Log.v(LOG_TAG, "Lat from GPS: " + String.valueOf(latitude));
                        Log.v(LOG_TAG, "Lon from GPS: " + String.valueOf(longitude));
                        //currentUser.put("Address", userAddress);
                        //currentUser.saveInBackground();
                        //Intent intent = new Intent(UserAddressInput.this, MainPage.class);
                        //UserAddressInput.this.finish();
                        //startActivity(intent);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, MY_LOCATION_ZOOM));
                    } else {
                        Log.v(LOG_TAG, "No last known location from GPS, try again.");
                         //does not get zoomed in to user loc
                    }
                    return true;
                };
            });
        }

        if(savedInstanceState!=null){
            if(savedInstanceState.containsKey("savedMarkerCoordinates")){
                savedMarkerCoordinates = savedInstanceState.getParcelable("savedMarkerCoordinates");
                if(savedMarkerCoordinates != null){
                    mMap.addMarker(new MarkerOptions().position(savedMarkerCoordinates));
                    setUpSubmitButton(savedMarkerCoordinates);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("savedMarkerCoordinates", savedMarkerCoordinates);
        super.onSaveInstanceState(outState);
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
//        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // remove last marker
                mMap.clear();
                // Add a marker at the clicked location
                mMap.addMarker(new MarkerOptions().position(latLng));

                //saving coordinates for bundle
                savedMarkerCoordinates = latLng;
                // Make a GeoPoint object to pass to the Parse server
                // Make button visible
                setUpSubmitButton(latLng);

            }
        });
    }

    private void setUpSubmitButton(LatLng latLng){
        final ParseGeoPoint userAddress = new ParseGeoPoint(latLng.latitude, latLng.longitude);

        mSubmitLocationButton.setVisibility(View.VISIBLE);
        mSubmitLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the current location as the user's address
                currentUser.put("Address", userAddress);
                currentUser.saveInBackground();
                Intent intent = new Intent(v.getContext(), MainPage.class);
                UserAddressInput.this.finish();
                startActivity(intent);
            }
        });
    }
    private void setUpGPSButton(){
        mGPSLocationButton.setVisibility(View.INVISIBLE);
        mGPSLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
                //check if GPS enabled TODO
                //manager.requestSingleUpdate();
                Location location = manager.getLastKnownLocation(manager.PASSIVE_PROVIDER);
                if (location != null) {
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    final ParseGeoPoint userAddress = new ParseGeoPoint(latitude, longitude);
                    Log.v(LOG_TAG, "Lat from GPS: " + String.valueOf(latitude));
                    Log.v(LOG_TAG, "Lon from GPS: " + String.valueOf(longitude));
                    currentUser.put("Address", userAddress);
                    currentUser.saveInBackground();
                    Intent intent = new Intent(v.getContext(), MainPage.class);
                    UserAddressInput.this.finish();
                    startActivity(intent);
                } else {
                    Log.v(LOG_TAG, "No last known location from GPS, try again.");
                }
            }
        });
    }
}

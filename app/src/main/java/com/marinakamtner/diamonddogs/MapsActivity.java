package com.marinakamtner.diamonddogs;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity {

    private GoogleMap map; // Might be null if Google Play services APK is not available.
    String latitude;
    String longitude;
    String finanzamtName;
    String finanzamtAddress;
    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        latitude = intent.getStringExtra(DetailActivity.LATITUDE);
        longitude = intent.getStringExtra(DetailActivity.LONGITUDE);
        finanzamtName = intent.getStringExtra(DetailActivity.FINANZAMTNAME);
        finanzamtAddress = intent.getStringExtra(DetailActivity.FINANZAMTADDRESS);
        latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the img_fa if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the img_fa has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #map} is not null.
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
        // Do a null check to confirm that we have not already instantiated the img_fa.
        if (map == null) {
            // Try to obtain the img_fa from the SupportMapFragment.
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the img_fa.
            if (map != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #map} is not null.
     */
    private void setUpMap() {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        map.addMarker(new MarkerOptions()
                .position(latLng)
                .title(finanzamtName)
                .snippet(finanzamtAddress));
    }

}

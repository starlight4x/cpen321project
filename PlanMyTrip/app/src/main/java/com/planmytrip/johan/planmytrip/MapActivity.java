package com.planmytrip.johan.planmytrip;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerClickListener{
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private TranslinkHandler translinkHandler;
    private Location mLastLocation;

    private final int[] MAP_TYPES = {
            GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE};
    private int curMapTypeIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        translinkHandler = new TranslinkHandler(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void submitCode(String lat, String lon) {
            if (isNetworkAvailable()) {
                translinkHandler.getNearestStops(lat, lon);

            } else {
               Toast.makeText(getApplicationContext(),"NO NETWORK AVAILABLE",Toast.LENGTH_SHORT).show(); //toast that no network is available
            }
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        initListeners();
    }

    public void nearestStopsQueryReturned(ArrayList<Stop> result, String errorMsg){
        if(errorMsg != null){
            Toast.makeText(getApplicationContext(), "haha you suck", Toast.LENGTH_SHORT).show();
        }
        else {
            setMarkers(result);
        }
    }

    private void setMarkers(ArrayList<Stop> result) {
        //Toast.makeText(getApplicationContext(), "blah", Toast.LENGTH_SHORT).show();
        double a, b;
        for(int i = 0; i < result.size(); i++) {
            a = Double.parseDouble(result.get(i).getLatitude());
            b = Double.parseDouble(result.get(i).getLongitude());
            MarkerOptions options = new MarkerOptions().position(new LatLng(a, b));
            options.title(result.get(i).getStopCode() + "\n" + result.get(i).getName());

            options.icon(BitmapDescriptorFactory.defaultMarker());
            mMap.addMarker(options);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            //Toast.makeText(getApplicationContext(), mLastLocation.getLatitude() + " " + mLastLocation.getLongitude(), Toast.LENGTH_SHORT).show();
            initCamera(mLastLocation);
            if(mLastLocation != null) {
                DecimalFormat df = new DecimalFormat("#.####");
                df.setRoundingMode(RoundingMode.CEILING);
                String la = df.format(mLastLocation.getLatitude());
                String lo = df.format(mLastLocation.getLongitude());

                submitCode(la, lo);
                Toast.makeText(getApplicationContext(), la + " " + lo, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please turn on Location Service", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getApplicationContext(), "connection failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "connection failed", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {

        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {

        super.onStop();
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
    }

    private void initCamera( Location location ) {
        CameraPosition position = CameraPosition.builder()
                .target( new LatLng( location.getLatitude(),
                        location.getLongitude() ) )
                .zoom( 16f )
                .bearing( 0.0f )
                .tilt( 0.0f )
                .build();

        mMap.animateCamera( CameraUpdateFactory
                .newCameraPosition( position ), null );

        mMap.setMapType( MAP_TYPES[curMapTypeIndex] );
        mMap.setTrafficEnabled( true );
        mMap.setMyLocationEnabled( true );
        mMap.getUiSettings().setZoomControlsEnabled( true );

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText( this, "Clicked on marker", Toast.LENGTH_SHORT ).show();

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    private void initListeners() {
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener( this );
    }
}

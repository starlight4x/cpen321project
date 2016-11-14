package com.planmytrip.johan.planmytrip;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.view.Menu;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import android.view.Window;
import android.graphics.Color;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerClickListener {

    private String stopNumber; //stores the stop number entered by user
    private RelativeLayout loadingPanel; // loading circle
    private TranslinkHandler transHandler;
    //below is for sliding menu
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle = "Time Your Trip";
    private String[] osArray = { "Find Bus Stops Around You", "View Your Location", "Show Bus Routes", "Show Skytrain"};

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
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
        setContentView(R.layout.activity_main);

        if(mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setTitle(mActivityTitle);

        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.GONE);
        transHandler = new TranslinkHandler(this); //initialize new translink handler class
        //below is for sliding menu
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
    private void addDrawerItems() {

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
/*
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(osArray[position].equals("View Your Location")) {
                    Toast.makeText(MainActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, MapActivity.class); //pass the intent to TranslinkUI class

                    startActivity(intent);
                }
            }
        });
        */
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
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

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //----------------------------------------------------------//
    //create the searchView layout
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchView searchview= (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchview.setOnQueryTextListener(this);
        searchview.setQueryHint("Enter Stopcode...");
        searchview.setBackgroundColor(Color.WHITE);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * *SearchView
     * @param query
     * @return
     */
  @Override
    public boolean onQueryTextSubmit(String query){
      //initialize http request based on user's stopcode input
     submitCode(query);

      return false;
  }

   @Override
   public boolean onQueryTextChange(String newText) {

       return false;
   }

    //function that checks if the Wifi is available on the phone
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

    public boolean isInteger(String a){
        int counter = 0;
        for(int i = 0; i < a.length(); i++){
            if(Character.getNumericValue(a.charAt(i)) >= 0 && Character.getNumericValue(a.charAt(i)) <= 9 ){
                counter++;
                continue;
            }
            else
                return  false;
        }
        if(counter == a.length())
            return true;
        else
            return false;
    }

    //helper function that initializes an http request for buses based on user input
    public void submitCode(String code) {
        if(code.length() == 5 && isInteger(code)){
            if (isNetworkAvailable()) {
                stopNumber = code; //store the user's input in the global variable
                transHandler.getNextBuses(code); //initialize a get bus http request based on input
                loadingPanel.setVisibility(View.VISIBLE); //set the loading wheel to visible
            }
            else {
                showError("NO NETWORK AVAILABLE"); //toast that no network is available
            }
        }
        else{
            showError("INVALID BUS STOP NUMBER");
        }

    }
    public void showError(String msg) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }

    //function that is called by the TranslinkHandler class when an http request is successfully returned
    public void nextBusesQueryReturned(ArrayList<Bus> result, String errorMsg){
        loadingPanel.setVisibility(View.GONE); //remove the loading wheel
        if(errorMsg != null){
            showError(errorMsg); //show the error returned by the request
        }
        else {
            Intent intent = new Intent(this, TranslinkUI.class); //pass the intent to TranslinkUI class
            intent.putExtra("busStopNo", stopNumber); //store the stop number the user entered
            intent.putExtra("busList", result); //pass the array of buses that the http request returned
            startActivity(intent); //transition to the TranslinkUI activity
        }
    }

        //------------------------------MAP STUFF (JAMES) ---------------------------------------------//
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

                    submitArea(la, lo);
                    Toast.makeText(getApplicationContext(), la + " " + lo, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Please turn on Location Service", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {

        }

        @Override
        public void onInfoWindowClick(Marker marker) {
            submitCode(marker.getTitle());
        }

        @Override
        public boolean onMarkerClick(Marker marker) {
            marker.showInfoWindow();
            return true;
        }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        initListeners();
    }

    public void submitArea(String lat, String lon) {
        if (isNetworkAvailable()) {
            transHandler.getNearestStops(lat, lon);

        } else {
            Toast.makeText(getApplicationContext(),"NO NETWORK AVAILABLE",Toast.LENGTH_SHORT).show(); //toast that no network is available
        }
    }

    public void nearestStopsQueryReturned(ArrayList<Stop> result, String errorMsg){
        if(errorMsg != null){
            Toast.makeText(getApplicationContext(), "Cannot obtain current location", Toast.LENGTH_SHORT).show();
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
            options.title(result.get(i).getStopCode());

            options.icon(BitmapDescriptorFactory.defaultMarker());
            mMap.addMarker(options);
        }
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
    private void initListeners() {
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener( this );
    }


}

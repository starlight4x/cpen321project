package com.planmytrip.johan.planmytrip;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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

import java.util.ArrayList;
import android.view.Window;
import android.graphics.Color;
public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private String stopNumber; //stores the stop number entered by user
    private RelativeLayout loadingPanel; // loading circle
    private TranslinkHandler transHandler;
    //below is for sliding menu
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setTitle("Time Your Trip");

        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.GONE);
        transHandler = new TranslinkHandler(this); //initialize new translink handler class
        //below is for sliding menu
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
    private void addDrawerItems() {
        String[] osArray = { "Android", "iOS", "Windows", "OS X", "Linux" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
            }
        });
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
}

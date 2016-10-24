package com.example.johan.planmytrip;

/**
 * Created by Navjashan on 23/10/2016.
 */


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TranslinkUI extends AppCompatActivity {

    private TextView text_view;
    private EditText text_input1;
    private ArrayList<String> nextBuses;
    private ArrayList<String> nearestStops;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.translinkui_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);

        text_view = (TextView) this.findViewById(R.id.text_field);
        text_input1 = (EditText) this.findViewById(R.id.text_input1);
        //text_view.setText("Blabla");
        nearestStops = new ArrayList<String>();
        nearestStops.add("Loading...");

        text_view.setText(nearestStops.toString());
        new TranslinkHandler(this).getNextBuses(12345);
        //getNextBuses();

    }

    public void routeStopsQueryReturned(String result, String errorMsg){
        if(errorMsg != null){
            text_view.setText(errorMsg);
        }
        else {
            text_view.setText(result);
        }
    }

    public void nearestStopsQueryReturned(ArrayList<String> result, String errorMsg){
        if(errorMsg != null){
            text_view.setText(errorMsg);
        }
        else {
            text_view.setText(result.toString());
        }
    }

    public void nextBusesQueryReturned(ArrayList<Bus> result, String errorMsg){
        if(errorMsg != null){
            text_view.setText(errorMsg);
        }
        else {
            text_view.setText(result.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

        return super.onOptionsItemSelected(item);
    }


}

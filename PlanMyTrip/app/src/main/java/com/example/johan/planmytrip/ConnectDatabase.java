package com.example.johan.planmytrip;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 01/11/2016.
 */

public class ConnectDatabase extends AppCompatActivity{
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);

        //Intent myIntent = getIntent();
        //Bus bus = (Bus)myIntent.getSerializableExtra("selectedBus");
        Intent myIntent = getIntent(); // gets the previously created intent
        String selRoute = myIntent.getStringExtra("selectedRoute"); // will return "FirstKeyValue"
        this.listView = (ListView) findViewById(R.id.listView);
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        List<String> stops = databaseAccess.getStops(selRoute);
        databaseAccess.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stops);
        this.listView.setAdapter(adapter);
    }


}
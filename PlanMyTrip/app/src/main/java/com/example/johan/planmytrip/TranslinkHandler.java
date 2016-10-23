package com.example.johan.planmytrip;


import android.support.v7.app.AppCompatActivity;

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

/**
 * Created by johan on 20.10.2016.
 */

public class TranslinkHandler {


    private AppCompatActivity context;


    public TranslinkHandler(AppCompatActivity activity){
        this.context = activity;
    }

    public void getNextBuses(){

        String url = "http://api.translink.ca/rttiapi/v1/stops/60980/estimates?apikey=1Y8IBRRxW0yYIhxyWswH";
        myJSONArrayRequest(url, 1);

    }

    public void getNearestStops(){

        String url = "http://api.translink.ca/rttiapi/v1/stops?apikey=1Y8IBRRxW0yYIhxyWswH&lat=49.187706&long=-122.850060";
        myJSONArrayRequest(url, 2);

    }

    private void getNearestStopsReturned(JSONArray response, String errorMsg){
        System.out.print(response.toString());
        if (errorMsg == null) {
            ArrayList<String> nearestStops = new ArrayList<String>();
            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonobject;
                try {
                    jsonobject = response.getJSONObject(i);
                    String stopNo = jsonobject.getString("StopNo");
                    String stopName = jsonobject.getString("Name");
                    String distance = jsonobject.getString("Distance");
                    String result = stopNo + " "+ stopName + " " + distance;
                    nearestStops.add(result);

                } catch (JSONException e) {
                    System.out.print("Error parsing JSONArray" + e.toString());
                }
            }

            System.out.print(nearestStops.toString());
            ((MainActivity)context).nearestStopsQueryReturned(nearestStops, null);



        }
        else{
            ((MainActivity)context).nearestStopsQueryReturned(null, errorMsg);

        }


    }
    private void getNextBusesReturned(JSONArray response, String errorMsg){

        if (errorMsg == null) {
            ArrayList<String> nextBuses = new ArrayList<String>();
            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonobject;
                try {
                    jsonobject = response.getJSONObject(i);
                    String routeNo = jsonobject.getString("RouteNo");
                    JSONArray busArray = jsonobject.getJSONArray("Schedules");
                    for (int j = 0; j <1; j++) {
                        JSONObject jsonobjectBusInfo;
                        try {
                            jsonobjectBusInfo = busArray.getJSONObject(j);
                            String destination = jsonobjectBusInfo.getString("Destination");
                            String expectedLeaveTime = jsonobjectBusInfo.getString("ExpectedLeaveTime");
                            String resultString = routeNo +" " + destination + " " + expectedLeaveTime;
                            nextBuses.add(resultString);

                        } catch (JSONException e) {
                            System.out.print("Error parsing JSONArray" + e.toString());
                        }

                    }


                } catch (JSONException e) {
                    System.out.print("Error parsing JSONArray" + e.toString());
                }
            }

            System.out.print(nextBuses.toString());
            ((MainActivity)context).nextBusesQueryReturned(nextBuses, null);



        }
        else{
            ((MainActivity)context).nextBusesQueryReturned(null, errorMsg);

        }

    }



    private void translinkRequestResponded(int inputID, JSONArray jsonArray, JSONObject jsonObject, String errorMsg){

        switch(inputID){
            case 1:
                getNextBusesReturned(jsonArray, errorMsg);
                break;
            case 2:
                getNearestStopsReturned(jsonArray,errorMsg);
                //System.out.print("hallellef" + jsonArray.toString());

                break;
            default: break;
        }

    }



    private void myJSONObjectRequest(String url, final int inputID){

        RequestQueue queue = Volley.newRequestQueue(context);

        Response.Listener<JSONObject> myResponseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                translinkRequestResponded(inputID,null,response,null);
            }
        };

        Response.ErrorListener myErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                translinkRequestResponded(inputID,null,null,error.toString());
            }
        };


        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,myResponseListener,myErrorListener)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/JSON");
                return params;
            }

        };

        queue.add(jsonRequest);


    }


    private void myJSONArrayRequest(String url, final int inputID){

        RequestQueue queue = Volley.newRequestQueue(context);

        Response.Listener<JSONArray> myResponseListener = new Response.Listener<JSONArray>()
        {

            @Override
            public void onResponse(JSONArray response) {
                translinkRequestResponded(inputID,response,null,null);


            }

        };
        Response.ErrorListener myErrorListener = new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error) {
                translinkRequestResponded(inputID,null,null,error.toString());

            }

        };



        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, url, null,myResponseListener,myErrorListener)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/JSON");
                return params;
            }

        };

        queue.add(jsonRequest);


    }


}

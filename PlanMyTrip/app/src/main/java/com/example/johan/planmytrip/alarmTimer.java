package com.example.johan.planmytrip;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Navjashan on 29.10.2016.
 */

public class alarmTimer extends AppCompatActivity {

    private MediaPlayer mp = new MediaPlayer();
    private CountDownTimer timer;
    private TextView locationTextView;
    private double destLat;
    private double destLong;
    private String routeNo;
    private Handler myHandler;
    private Runnable runnable;
    private GPSHandler gpsHandler;
    private boolean hasPlayedAlarm = false;
    private boolean hasSetGPSTo10000 = false;
    private boolean hasSetGPSTo5000 = false;
    private boolean hasSetGPSTo3000 = false;
    private TextView timerTextView;
    boolean alarmEnabled = false;


    //private int totalTime = 10000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_timer);
        timerTextView = (TextView) findViewById(R.id.textView3);
        timerTextView.setText("Loading...");
        myHandler = new Handler();
        gpsHandler = new GPSHandler(this);
        locationTextView = (TextView) findViewById(R.id.locationTextView);


        mp = MediaPlayer.create(this, R.raw.sound);
        final Button stopAlarm = (Button) this.findViewById(R.id.button3);
        stopAlarm.setText("Set Alarm");
        stopAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alarmEnabled){
                    stopAlarm.setText("Set Alarm");
                    alarmEnabled = false;
                    mp.stop();
                }
                else {
                    stopAlarm.setText("Stop Alarm");
                    alarmEnabled = true;
                }
            }
        });


        Intent timeIntent  = getIntent();
        Stop start = (Stop)timeIntent.getSerializableExtra("startingStop");
        Stop destination = (Stop)timeIntent.getSerializableExtra("destination");
        routeNo = timeIntent.getStringExtra("selRoute");
        destLat = Double.parseDouble(destination.getLatitude());
        destLong = Double.parseDouble(destination.getLongitude());


        new TranslinkHandler(this).getEstimatedTimeFromGoogle(start.getLatitude(), start.getLongitude(),destination.getLatitude(),destination.getLongitude(), "now");

    }


    public void getNearestBusStopServingRouteReturned(String latitude, String longitude, String errorMsg){
        if (errorMsg == null){
            new TranslinkHandler(this).getEstimatedTimeFromGoogle(latitude,longitude,String.valueOf(destLat),String.valueOf(destLong), "now");
        }
        else{
            System.out.println(errorMsg);
        }
    }


    public void gotGPSUpdate(Location location){
        System.out.println("gotGPSUpdate");

        double distance = gpsHandler.distance(destLat,location.getLatitude(),destLong,location.getLongitude());

        if(distance <300){
            locationTextView.setText("Distance to destination: " + String.format("%.0f", distance) + " Meters");
            if (runnable != null) {
                myHandler.removeCallbacks(runnable);
            }
            if (!hasPlayedAlarm) {
                if (alarmEnabled) {
                    mp.start();
                    Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vib.vibrate(1500);

                    hasPlayedAlarm = true;
                }
            }
            if(!hasSetGPSTo3000) {
                gpsHandler.removeUpdates();
                gpsHandler.requestGPSUpdates(1000);
                hasSetGPSTo3000 = true;
            }

        }
        else if(distance < 500){
            locationTextView.setText("Distance to destination: " + distance + " Meters");
            if (runnable != null) {
                myHandler.removeCallbacks(runnable);
            }
            if(!hasSetGPSTo5000) {
                gpsHandler.removeUpdates();
                gpsHandler.requestGPSUpdates(5000);
                hasSetGPSTo5000 = true;
            }


        }
        else if ( distance < 1000){
            locationTextView.setText("Distance to destination: " + distance + " Meters");
            gpsHandler.removeUpdates();
            if (runnable != null) {
                myHandler.removeCallbacks(runnable);
            }
            if (!hasSetGPSTo10000) {
                gpsHandler.requestGPSUpdates(10000);
                hasSetGPSTo10000 = true;
            }

        }
        else{
            hasSetGPSTo10000 = false;
            hasSetGPSTo5000 = false;
            hasSetGPSTo3000 = false;
            gpsHandler.removeUpdates();
            new TranslinkHandler(this).getNearestBusStopServingRoute(location.getLatitude(),location.getLongitude(),routeNo);
        }
    }


    public void estimatedTimeReturned(String duration, String errorMsg) {
        if (errorMsg == null) {
            if (timer != null) {
                timer.cancel();
            }
            gpsHandler.removeUpdates();
            if (runnable != null) {
                myHandler.removeCallbacks(runnable);
            }
            setTimer(Integer.parseInt(duration) * 1000);
        }
        else{
            timerTextView.setText("A server error occured. " + errorMsg);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                System.out.println("In onRequestPermissionResult case 10");
                gpsHandler.requestGPSUpdates(5000);
                break;
            default:
                break;
        }
    }


    private void setTimer(final long countTime){

        this.timer = new CountDownTimer(countTime, 1000) {

            long nextGPSUpdate = countTime;

            public void onTick(long millisUntilFinished) {

                if (millisUntilFinished<nextGPSUpdate){


                    nextGPSUpdate = millisUntilFinished/2;

                    if(!(hasSetGPSTo10000||hasSetGPSTo3000||hasSetGPSTo5000)) {
                        runnable = new Runnable() {

                            @Override
                            public void run() {
                                gpsHandler.requestGPSUpdates(10000);
                            }
                        };

                        myHandler.postDelayed(runnable, nextGPSUpdate - 10000);
                    }

                }




                int totalSeconds =  (int)millisUntilFinished/1000;

                int hours =  (totalSeconds % 86400) / 3600;
                int minutes = (totalSeconds % 3600) / 60;
                int seconds = (totalSeconds % 60);

                if(hours > 0)
                    timerTextView.setText( hours + " hours\n"  + minutes + " minutes\n" + seconds + " seconds!" );

                else if(minutes > 0)
                    timerTextView.setText( minutes + " minutes\n" + seconds + " seconds!" );

                else
                    timerTextView.setText(seconds + " seconds!" );

            }



            public void onFinish() {
                timerTextView.setText("DONE!");
                //mp.start();
            }
        };

        timer.start();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        myHandler.removeCallbacks(runnable);
        timer.cancel();
        gpsHandler.removeUpdates();
    }



}
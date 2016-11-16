package com.planmytrip.johan.planmytrip;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;

/**
 * Created by johan on 15.11.2016.
 */


public class TimerService extends Service {


    private MediaPlayer mp = new MediaPlayer();
    private CountDownTimer timer;
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
    boolean alarmEnabled = false;

    private final IBinder mBinder = new MyBinder();
    private Messenger outMessenger;

    @Override
    public IBinder onBind(Intent arg0) {
        Bundle extras = arg0.getExtras();
        Log.d("service","onBind");
        // Get messager from the Activity
        if (extras != null) {
            Log.d("service","onBind with extra");
            outMessenger = (Messenger) extras.get("MESSENGER");

        }
        return mBinder;
    }

    public class MyBinder extends Binder {
        TimerService getService() {
            return TimerService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle extras = intent.getExtras();
        Log.d("service","onBind");
        // Get messager from the Activity
        if (extras != null) {
            Log.d("service","onBind with extra");
            outMessenger = (Messenger) extras.get("MESSENGER");
            myHandler = new Handler();
            gpsHandler = new GPSHandler(this);

            //Media Player to be used
            mp = MediaPlayer.create(this, R.raw.sound);

            Stop start = (Stop)intent.getSerializableExtra("startingStop");
            Stop destination = (Stop)intent.getSerializableExtra("destination");
            routeNo = intent.getStringExtra("selRoute");
            destLat = Double.parseDouble(destination.getLatitude());
            destLong = Double.parseDouble(destination.getLongitude());


            new TranslinkHandler(this).getEstimatedTimeFromGoogle(start.getLatitude(), start.getLongitude(),destination.getLatitude(),destination.getLongitude(), "now");

        }

        return super.onStartCommand(intent, flags, startId);
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
        try {
            Message message = new Message();
            message.arg1 = 2;
            message.obj = "Distance to destination: " + String.format("%.0f", distance) + " Meters";
            outMessenger.send(message);
        }
        catch(RemoteException e){

        }

        if(distance <300){
            //locationTextView.setText("Distance to destination: " + String.format("%.0f", distance) + " Meters");

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
            //locationTextView.setText("Distance to destination: " + distance + " Meters");
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
            //locationTextView.setText("Distance to destination: " + distance + " Meters");

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

    public void setAlarmEnabled(boolean alarmEnabled){
        this.alarmEnabled = alarmEnabled;
        if (!alarmEnabled){
            mp.stop();
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
            //timerTextView.setText("A server error occured. " + errorMsg);
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

                    try {
                        Message message = new Message();
                        message.arg1 = 1;
                        String time;
                        if(hours > 0){
                            time = hours + " hours\n"  + minutes + " minutes\n" + seconds + " seconds!";
                        }
                        else if(minutes > 0){
                            time = minutes + " minutes\n" + seconds + " seconds!";
                        }
                        else{
                            time = seconds + " seconds!";
                        }

                        message.obj =  time;
                        outMessenger.send(message);
                    }catch(RemoteException e){

                    }
                }





            public void onFinish() {
                try {
                    Message message = new Message();
                    message.arg1 = 1;
                    message.obj =  "Done!";
                    outMessenger.send(message);
                }catch(RemoteException e){

                }

                //mp.start();
            }
        };

        timer.start();

    }

    public void gpsProviderDisabled(){
        Message message = new Message();
        message.arg1 = 3;
        try {
            outMessenger.send(message);
        }
        catch (RemoteException e){

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        myHandler.removeCallbacks(runnable);
        timer.cancel();
        gpsHandler.removeUpdates();
    }
}

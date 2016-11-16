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
    Vibrator vib;
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
    boolean alarmEnabled = true;

    private final IBinder mBinder = new MyBinder();
    private Messenger outMessenger;

    @Override
    public IBinder onBind(Intent arg0) {
        Bundle extras = arg0.getExtras();
        Log.d("service", "onBind");
        // Get messager from the Activity
        if (extras != null) {
            Log.d("service", "onBind with extra");
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
        Log.d("service", "onBind");
        // Get messager from the Activity
        if (extras != null) {
            Log.d("service", "onBind with extra");
            myHandler = new Handler();
            gpsHandler = new GPSHandler(this);
            vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            //Media Player to be used
            mp = MediaPlayer.create(this, R.raw.sound);

            Stop start = (Stop) intent.getSerializableExtra("startingStop");
            Stop destination = (Stop) intent.getSerializableExtra("destination");
            routeNo = intent.getStringExtra("selRoute");
            destLat = Double.parseDouble(destination.getLatitude());
            destLong = Double.parseDouble(destination.getLongitude());


            new TranslinkHandler(this).getEstimatedTimeFromGoogle(start.getLatitude(), start.getLongitude(), destination.getLatitude(), destination.getLongitude(), "now");

        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void getNearestBusStopServingRouteReturned(String latitude, String longitude, String errorMsg) {
        if (errorMsg == null) {
            new TranslinkHandler(this).getEstimatedTimeFromGoogle(latitude, longitude, String.valueOf(destLat), String.valueOf(destLong), "now");
        } else {
            System.out.println(errorMsg);
        }
    }

    public void gotGPSUpdate(Location location) {
        System.out.println("gotGPSUpdate");

        double distance = gpsHandler.distance(destLat, location.getLatitude(), destLong, location.getLongitude());

        if (distance < 300) {
            sendMessage(2, "Distance to destination: " + String.format("%.0f", distance) + " Meters");

            if (runnable != null) {
                myHandler.removeCallbacks(runnable);
            }
            if (!hasPlayedAlarm) {
                if (alarmEnabled) {
                    mp.start();
                    vib.vibrate(5000);

                    hasPlayedAlarm = true;
                }
            }
            if (!hasSetGPSTo3000) {
                gpsHandler.removeUpdates();
                gpsHandler.requestGPSUpdates(500);
                hasSetGPSTo3000 = true;
            }

        } else if (distance < 500) {
            sendMessage(2, "Distance to destination: " + String.format("%.0f", distance) + " Meters");
            if (runnable != null) {
                myHandler.removeCallbacks(runnable);
            }
            if (!hasSetGPSTo5000) {
                gpsHandler.removeUpdates();
                gpsHandler.requestGPSUpdates(1000);
                hasSetGPSTo5000 = true;
            }


        } else if (distance < 1200) {
            sendMessage(2, "Distance to destination: " + String.format("%.0f", distance) + " Meters");

            gpsHandler.removeUpdates();
            if (runnable != null) {
                myHandler.removeCallbacks(runnable);
            }
            if (!hasSetGPSTo10000) {
                gpsHandler.requestGPSUpdates(3000);
                hasSetGPSTo10000 = true;
            }

        } else {
            hasSetGPSTo10000 = false;
            hasSetGPSTo5000 = false;
            hasSetGPSTo3000 = false;
            gpsHandler.removeUpdates();
            new TranslinkHandler(this).getNearestBusStopServingRoute(location.getLatitude(), location.getLongitude(), routeNo);
        }
    }

    public void setAlarmEnabled(boolean alarmEnabled) {
        this.alarmEnabled = alarmEnabled;
        if (!alarmEnabled) {
            mp.stop();
            vib.cancel();
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


            long nextGPSUpdate = Integer.parseInt(duration) * 1000 / 3;

            runnable = new Runnable() {

                @Override
                public void run() {
                    if (!gpsHandler.requestGPSUpdates(10000)) {
                        gpsProviderDisabled();
                    }
                }
            };

            myHandler.postDelayed(runnable, nextGPSUpdate - 10000);


        } else {
            //timerTextView.setText("A server error occured. " + errorMsg);
        }
    }

    private void setTimer(final long countTime) {

        this.timer = new CountDownTimer(countTime, 1000) {

            public void onTick(long millisUntilFinished) {

                int totalSeconds = (int) millisUntilFinished / 1000;

                int hours = (totalSeconds % 86400) / 3600;
                int minutes = (totalSeconds % 3600) / 60;
                int seconds = (totalSeconds % 60);

                String time;
                if (hours > 0) {
                    time = hours + " hours\n" + minutes + " minutes\n" + seconds + " seconds!";
                } else if (minutes > 0) {
                    time = minutes + " minutes\n" + seconds + " seconds!";
                } else {
                    time = seconds + " seconds!";
                }
                sendMessage(1, time);
            }


            public void onFinish() {
                sendMessage(1, "Done!");
            }
        };

        timer.start();

    }

    private void sendMessage(int code, String messageString) {
        try {
            Message message = new Message();
            message.arg1 = code;
            message.obj = messageString;
            outMessenger.send(message);
        } catch (RemoteException e) {

        }
    }

    public void gpsProviderDisabled() {
        sendMessage(3, "");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Service got destroyed");
        myHandler.removeCallbacks(runnable);
        if (timer != null) {
            timer.cancel();
        }
        gpsHandler.removeUpdates();
        mp.stop();
        vib.cancel();
    }
}

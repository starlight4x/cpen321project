package com.planmytrip.johan.planmytrip;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Navjashan on 29.10.2016.
 */

public class alarmTimer extends AppCompatActivity {

    private TextView locationTextView;
    private TextView timerTextView;
    boolean alarmEnabled = false;
    private Intent intentFromLastActivity;



    private TimerService myServiceBinder;
    public ServiceConnection myConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder binder) {
            myServiceBinder = ((TimerService.MyBinder) binder).getService();
            Log.d("ServiceConnection","connected");
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.d("ServiceConnection","disconnected");
            myServiceBinder = null;
        }
    };

    public Handler myHandler1 = new Handler() {
        public void handleMessage(Message message) {
            //Bundle data = message.getData();
            switch (message.arg1){
                case 1:
                    updateClock(message);
                    break;
                case 2:
                    updateDistance(message);
                    break;
                case 3:
                    requestGPS();
                    break;
                default:
                    break;
            }
        }
    };

    private void requestGPS(){
        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    }

    public void doBindService() {
        Intent intent = null;
        intent = new Intent(this, TimerService.class);
        Messenger messenger = new Messenger(myHandler1);
        intent.putExtra("MESSENGER", messenger);
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE);
    }

    public void doStartService(Intent timeIntent){
        Intent intent = new Intent(this, TimerService.class);
        Stop start = (Stop)timeIntent.getSerializableExtra("startingStop");
        Stop destination = (Stop)timeIntent.getSerializableExtra("destination");
        String routeNo = timeIntent.getStringExtra("selRoute");
        intent.putExtra("startingStop", start);
        intent.putExtra("destination", destination);
        intent.putExtra("selRoute", routeNo);

        startService(intent);

    }


    @Override
    protected void onResume() {

        Log.d("activity", "onResume");
        if (myServiceBinder == null) {
            doBindService();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d("activity", "onPause");
        if (myServiceBinder != null) {
            unbindService(myConnection);
            myServiceBinder = null;
        }
        super.onPause();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Handles the setting up part for the class
        super.onCreate(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
        }

        intentFromLastActivity  = getIntent();
        doStartService(intentFromLastActivity);
        doBindService();
        setContentView(R.layout.activity_alarm_timer);
        timerTextView = (TextView) findViewById(R.id.textView3);
        timerTextView.setText("Loading...");
        locationTextView = (TextView) findViewById(R.id.locationTextView);

        //Alarm configurations for setting and stopping it
        final Button stopAlarm = (Button) this.findViewById(R.id.button3);
        stopAlarm.setText("Set Alarm");
        stopAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alarmEnabled){
                    stopAlarm.setText("Set Alarm");
                    alarmEnabled = false;
                    myServiceBinder.setAlarmEnabled(false);
                }
                else {
                    stopAlarm.setText("Stop Alarm");
                    alarmEnabled = true;
                    myServiceBinder.setAlarmEnabled(true);
                }
            }
        });


    }

    public void updateClock(Message message){
        timerTextView.setText((String)message.obj);
    }

    public void updateDistance(Message message){
        locationTextView.setText((String)message.obj);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                System.out.println("In onRequestPermissionResult case 10");
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopService(new Intent(this,TimerService.class));
    }


}
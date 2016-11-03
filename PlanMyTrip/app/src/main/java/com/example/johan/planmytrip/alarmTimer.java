package com.example.johan.planmytrip;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Navjashan on 29.10.2016.
 */

public class alarmTimer extends AppCompatActivity {



    MediaPlayer mp = new MediaPlayer();

    //private int totalTime = 10000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_timer);

       // MediaPlayer mp = new MediaPlayer();
        mp = MediaPlayer.create(this, R.raw.sound);


        Button stopAlarm = (Button) this.findViewById(R.id.button3);
        stopAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
            }
        });

        Intent timeIntent  = getIntent();
        Stop start = (Stop)timeIntent.getSerializableExtra("startingStop");
        Stop destination = (Stop)timeIntent.getSerializableExtra("destination");
        System.out.println(start.getLatitude() + start.getLongitude());
        new TranslinkHandler(this).getEstimatedTimeFromGoogle(start.getLatitude(), start.getLongitude(),destination.getLatitude(),destination.getLongitude(), "now");
        //totalTime = timeIntent.getIntExtra("setTime", 0);
        //computeTime(totalTime);
    }
    public void estimatedTimeReturned(String duration) {
        Context context = getApplicationContext();
        int dur = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, duration, dur);
        toast.show();
        computeTime(Integer.parseInt(duration) * 1000);
    }

    int counter = 0;
    private void computeTime(int countTime){

        final TextView mTextField = (TextView) findViewById(R.id.textView3);


        new CountDownTimer(countTime, 1000) {

            public void onTick(long millisUntilFinished) {

                int totalSeconds =  (int)millisUntilFinished/1000;


                int hours = totalSeconds/(60 * 60);

                int minutes;
                if(hours > 0)
                minutes = totalSeconds / (hours * 60);

                else
                    minutes = totalSeconds / 60;

                int seconds = (totalSeconds % 60) ;
                /*
                 * 90 seconds => 0 hr
                 * =>
                 *
                 */
                if(hours > 0)
                mTextField.setText( hours + " hours\n"  + minutes + " minutes\n" + seconds + " seconds!!!" );

                else if(minutes > 0)
                    mTextField.setText( minutes + " minutes\n" + seconds + " seconds!!!" );

                else
                    mTextField.setText(seconds + " seconds!!!" );

            }


            public void onFinish() {
                mTextField.setText("NOW!");
                mp.start();
              }
        }.start();
    }



    }






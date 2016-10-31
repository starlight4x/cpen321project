package com.example.johan.planmytrip;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class alarmTimer extends AppCompatActivity {


    private int totalTime = 10000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_timer);
        Intent timeIntent  = getIntent();
        totalTime = timeIntent.getIntExtra("busStopNo", 0);
        computeTime(totalTime);
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
                mTextField.setText( hours + " hours\n"  + minutes + " minutes\n" + seconds + " seconds remaining!!!" );

                else if(minutes > 0)
                    mTextField.setText( minutes + " minutes\n" + seconds + " seconds remaining!!!" );

                else
                    mTextField.setText(seconds + " seconds remaining!!!" );

            }

            public void onFinish() {
                mTextField.setText("done!");
            }
        }.start();

    }
}






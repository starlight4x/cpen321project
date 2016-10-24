package com.example.johan.planmytrip;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.johan.planmytrip.R;
import com.example.johan.planmytrip.TranslinkHandler;


public class MainActivity extends AppCompatActivity {

    public final static String MESSAGE = "hi";
    public final static String send = "";
    public final static String initial = "http://api.translink.ca/rttiapi/v1/stops/";
    public final static String ending = "/estimates?apikey=1Y8IBRRxW0yYIhxyWswH";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void busStopNumber(View view){

        EditText editText = (EditText) findViewById(R.id.busStopNumber);
        String message1 = editText.getText().toString();

        if(message1.length() == 5 && isInteger(message1)){
            Intent intent = new Intent(this, TranslinkUI.class);
            //Intent intent1 = new Intent(this, TranslinkHandler.class);
            //String message = initial + message1 + ending;
            //intent1.putExtra(MESSAGE, message);
            startActivity(intent);
        }

        else{
            Context context = getApplicationContext();
            CharSequence text = "INVALID BUS STOP NUMBER";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }

    public boolean isInteger(String a){

        int counter = 0;

        for(int i = 0; i < a.length(); i++){

            if(a.charAt(0) >= 0 || a.charAt(0) <= 9 ){
                counter++;
                continue;
            }
            else
                return  false;
        }

        if(counter == a.length())   return true;
        else        return false;
    }
}

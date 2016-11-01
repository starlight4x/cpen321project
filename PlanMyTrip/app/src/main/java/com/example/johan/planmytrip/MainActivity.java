package com.example.johan.planmytrip;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.johan.planmytrip.R;
import com.example.johan.planmytrip.TranslinkHandler;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void busStopNumber(View view){

        EditText editText = (EditText) findViewById(R.id.busStopNumber);
        String message1 = editText.getText().toString();
        int sender = 12000;

        if(message1.length() == 5 && isInteger(message1)){

            //
            Intent intent = new Intent(this, TranslinkUI.class);
            intent.putExtra("busStopNo",message1);
            //Intent intent1 = new Intent(this, TranslinkHandler.class);
            //String message = initial + message1 + ending;
            //intent1.putExtra(MESSAGE, message);
            if (isNetworkAvailable()) {
                startActivity(intent);
            }
            else {
                Context context = getApplicationContext();
                CharSequence text = "No internet connection";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }
        }

        else{
            Context context = getApplicationContext();
            CharSequence text = "INVALID BUS STOP NUMBER";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }


    private boolean isInteger(String a){

        int counter = 0;

        for(int i = 0; i < a.length(); i++){

            if(Character.getNumericValue(a.charAt(i)) >= 0 && Character.getNumericValue(a.charAt(i)) <= 9 ){
                counter++;
                continue;
            }
            else
                return  false;
        }

        if(counter == a.length())
            return true;

        else
            return false;
    }
}

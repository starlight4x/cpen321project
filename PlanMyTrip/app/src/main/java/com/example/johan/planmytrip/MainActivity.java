package com.example.johan.planmytrip;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.opengl.Visibility;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import com.example.johan.planmytrip.R;
import com.example.johan.planmytrip.TranslinkHandler;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private String stopNumber;
    private RelativeLayout loadingPanel;
    Context ctx;
    TextView tvOutput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.GONE);

        ctx=this;
        tvOutput =(TextView) findViewById(R.id.busStopNumber);

    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        SearchView searchview= (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchview.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);

    }


    /**
     * *SearchView
     * @param query
     * @return
     */
  @Override
    public boolean onQueryTextSubmit(String query){return false;}

   @Override
   public boolean onQueryTextChange(String newText){
       tvOutput.setText(newText);
       return false;
   }

    public void nextButtonPressed(View view){

        EditText editText = (EditText) findViewById(R.id.busStopNumber);
        String message1 = editText.getText().toString();
        int sender = 12000;

        if(message1.length() == 5 && isInteger(message1)){

            if (isNetworkAvailable()) {
                stopNumber = message1;
                new TranslinkHandler(this).getNextBuses(message1);
                loadingPanel.setVisibility(View.VISIBLE);
            }
            else{
                Context context = getApplicationContext();
                CharSequence text = "NO NETWORK AVAILABLE";
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

    public void nextBusesQueryReturned(ArrayList<Bus> result, String errorMsg){
        loadingPanel.setVisibility(View.GONE);
        if(errorMsg != null){
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, errorMsg, duration);
            toast.show();
        }
        else {
            Intent intent = new Intent(this, TranslinkUI.class);
            intent.putExtra("busStopNo", stopNumber);
            intent.putExtra("busList", result);
            startActivity(intent);
        }
    }
}

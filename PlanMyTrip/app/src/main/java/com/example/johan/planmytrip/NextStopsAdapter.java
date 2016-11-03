package com.example.johan.planmytrip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by james on 01/11/2016.
 */

public class NextStopsAdapter extends BaseAdapter{


    Context context;
    ArrayList<Stop> busStops;
    private static LayoutInflater inflater = null;

    public NextStopsAdapter(Context context, ArrayList<Stop> stops) {
        // TODO Auto-gene
        // rated constructor stub
        this.context = context;
        this.busStops = stops;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return busStops.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return busStops.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.next_stop_row, null);
        TextView stopNameTextField = (TextView) vi.findViewById(R.id.stopName);
        stopNameTextField.setText(busStops.get(position).getName());
        return vi;
    }
}

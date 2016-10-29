package com.example.johan.planmytrip;

import java.util.ArrayList;

/**
 * Created by johan on 23.10.2016.
 */

public class Stop {
    private int stopNo;
    private String name;
    private double longitude;
    private double latitude;
    private ArrayList<Bus> nextBuses;

    public Stop(int stopNo, String name, double longitude, double latitude, ArrayList<Bus> nextBuses){
        this.stopNo = stopNo;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.nextBuses = nextBuses;
    }

}

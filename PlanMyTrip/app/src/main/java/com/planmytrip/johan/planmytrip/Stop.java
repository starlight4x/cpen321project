package com.planmytrip.johan.planmytrip;

import java.io.Serializable;

/**
 * Created by johan on 23.10.2016.
 */

public class Stop implements Serializable{
    private String stopCode;
    private String stopID;
    private String name;
    private String longitude;
    private String latitude;

    public Stop(String stopID, String stopCode, String name, String latitude, String longitude){
        this.stopCode = stopCode;
        this.stopID = stopID;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getStopCode() {
        return stopCode;
    }

    public String getStopID() {
        return stopID;
    }

    public String getName() {
        return name;
    }
}

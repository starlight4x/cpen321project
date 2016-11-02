package com.example.johan.planmytrip;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by johan on 23.10.2016.
 */

public class Bus implements Serializable, Comparable<Bus>
{
    private String routeName;
    private String busNo;
    private String estimatedLeaveTime;
    private String destination;

    public Bus(String routeName,String busNo,String estimatedLeaveTime, String destination){
        this.routeName = routeName;
        this.busNo = busNo;
        this.estimatedLeaveTime = estimatedLeaveTime;
        this.destination = destination;

    }

    public String getRouteName() {
        return routeName;
    }

    public String getBusNo() {
        return busNo;
    }

    public String getDestination() {
        return destination;
    }

    public String getEstimatedLeaveTime() {

        return estimatedLeaveTime;
    }

    @Override
    public String toString() {
        return  busNo + " " + destination + "          " + estimatedLeaveTime ;
    }

    @Override
    public int compareTo(Bus bus) {

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mma yyyy-MM-dd");

        try {
            Date date1 = sdf.parse(bus.getEstimatedLeaveTime());
            Date date2 = sdf.parse(getEstimatedLeaveTime());
            if (date1.compareTo(date2) <= 0) {
                return 1;
            }
            else{
                return -1;
            }
        }
        catch(ParseException e){
            System.out.println("Parse exception date!!!!");
            return 1;
        }

    }

}

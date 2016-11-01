package com.example.johan.planmytrip;

/**
 * Created by johan on 23.10.2016.
 */

public class Bus
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
}

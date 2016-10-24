package com.example.johan.planmytrip;

/**
 * Created by johan on 23.10.2016.
 */

public class Bus
{
    private String routeName;
    private int busNo;
    private String estimatedLeaveTime;
    private String destination;

    public Bus(String routeName,int busNo,String estimatedLeaveTime, String destination){
        this.routeName = routeName;
        this.busNo = busNo;
        this.estimatedLeaveTime = estimatedLeaveTime;
        this.destination = destination;

    }

    public String getRouteName() {
        return routeName;
    }

    public int getBusNo() {
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
        return "Bus{" +
                "routeName='" + routeName + '\'' +
                ", busNo=" + busNo +
                ", estimatedLeaveTime='" + estimatedLeaveTime + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }
}

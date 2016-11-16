package com.planmytrip.johan.planmytrip;

import android.location.LocationManager;

/**
 * Created by Navjashan on 15/11/2016.
 */

public class GPSchecker{

    public LocationManager locationManager;

    public GPSchecker(LocationManager locationManager){
        this.locationManager = locationManager;
    }


    public boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}
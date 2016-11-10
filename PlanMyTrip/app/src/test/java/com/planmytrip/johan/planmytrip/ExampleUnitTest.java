package com.planmytrip.johan.planmytrip;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void distance_funtion_isCorrect(){
        double lat1 = 49.222222;
        double lat2 = 49.222222;
        double lon1 = -113.333333;
        double lon2 = -113.333333;

        double res1 = new GPSHandler(new alarmTimer()).distance(lat1, lat2,lon1, lon2);

        double lat3 = 49.259354;
        double lat4 = 49.308240;
        double lon3 = -123.081697;
        double lon4 = -123.138493;

        double res2 = new GPSHandler(new alarmTimer()).distance(lat3, lat4,lon3, lon4);

        assertEquals(0.0, res1,0.1);
        assertEquals(6828.166, res2,10);


    }

}
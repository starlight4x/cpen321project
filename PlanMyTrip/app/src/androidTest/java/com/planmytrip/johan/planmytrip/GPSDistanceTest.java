package com.planmytrip.johan.planmytrip;

/**
 * Created by james on 10/11/2016.
 */

import static android.support.test.InstrumentationRegistry.getTargetContext;

import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class GPSDistanceTest {
    private GPSHandler handler;
    private SQLiteDatabase database;

    @Before
    public void setUp(){
        handler = new GPSHandler(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void distance_function_isCorrect(){
        double lat1 = 49.222222;
        double lat2 = 49.222222;
        double lon1 = -113.333333;
        double lon2 = -113.333333;

        double res1 = handler.distance(lat1, lat2,lon1, lon2);

        double lat3 = 49.259354;
        double lat4 = 49.308240;
        double lon3 = -123.081697;
        double lon4 = -123.138493;

        double res2 = handler.distance(lat3, lat4,lon3, lon4);

        assertEquals(0.0, res1,0.1);
        assertEquals(6828.166, res2,10);
    }
}
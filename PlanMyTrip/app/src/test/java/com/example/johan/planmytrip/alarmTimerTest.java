package com.example.johan.planmytrip;

import android.os.Bundle;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Navjashan on 09/11/2016.
 */

public class alarmTimerTest {

    private alarmTimer alarmTime = new alarmTimer();

    @Test
    public void AlarmSounds() throws InterruptedException {

        //Initial Conditions tested
        assertFalse(alarmTime.mpInfo());
        assertFalse(alarmTime.getAlarmEnabled());

    }
}

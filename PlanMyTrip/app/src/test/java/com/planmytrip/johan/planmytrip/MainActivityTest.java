package com.planmytrip.johan.planmytrip;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by james on 10/11/2016.
 */

public class MainActivityTest {
    private MainActivity tester = new MainActivity();

    @Test
    public void integerTest(){

        assertTrue(tester.isInteger("14235222"));
        assertFalse(tester.isInteger("a12sasd242342"));
        assertTrue(tester.isInteger("134234"));
        assertFalse(tester.isInteger("-234"));
        assertTrue(tester.isInteger("0"));
        assertFalse(tester.isInteger("PleaseFail"));

    }

}

package com.example.johan.planmytrip;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Navjashan on 09/11/2016.
 */

public class mainActivitytest {

    private MainActivity tester = new MainActivity();

    @Test
    public void integerTest(){

        assertTrue(tester.isInteger("14235222"));
        assertFalse(tester.isInteger("a12sasd242342"));
        assertTrue(tester.isInteger("134234"));
        assertFalse(tester.isInteger("-234"));
        assertTrue(tester.isInteger("0"));
        assertFalse(tester.isInteger("PleaseFail"));

        //Valid Bus Stop Numbers tester
        assertTrue(tester.isInteger("52365"));
        assertTrue(tester.isInteger("56919"));
        assertTrue(tester.isInteger("50268"));

    }

}

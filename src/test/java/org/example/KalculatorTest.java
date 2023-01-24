package org.example;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class KalculatorTest

    extends TestCase

{
    private final Kalculator Kal = new Kalculator();
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public KalculatorTest(String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( KalculatorTest.class );
    }


    @org.junit.Test
   void testApp()
    {
     assertEquals(false,Kal.isLetter("5"));
        assertEquals(true,Kal.isLetter("a"));
    }
}

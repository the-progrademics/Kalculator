package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for simple App.
 */
public class KalculatorTest

{
    private final Kalculator Kal = new Kalculator();

    @org.junit.Test
   public void testApp()
    {
     assertEquals(false,Kal.isLetter("5"));
        assertEquals(true,Kal.isLetter("a"));
    }
}

package com.isat.drinkmate;

import junit.framework.TestCase;

public class BacActivityTest extends TestCase {
	
	public void testBACAverageMale() 
	{
	    int ounces = 48;
	    double percent = 5;
	    double weight = 160;
	    int hoursDrinking = 2;
	    double result = 0.0825;
	    BacActivity activity = new BacActivity();
	    assertEquals(result, activity.getBac(ounces, percent, weight, hoursDrinking));
	}
	public void testBacNegativeValue()
	{
	    int ounces = 1;
	    double percent = 5;
	    double weight = 160;
	    int hoursDrinking = 1;
	    double result = 0;
	    BacActivity activity = new BacActivity();
	    assertEquals(result, activity.getBac(ounces, percent, weight, hoursDrinking));
	}
	public void testAverageGirl()
	{
	    int ounces = 32;
	    double percent = 5;
	    double weight = 130;
	    int hoursDrinking = 2;
	    double result = 0.062307692307692314;
	    BacActivity activity = new BacActivity();
	    assertEquals(result, activity.getBac(ounces, percent, weight, hoursDrinking));
	}
}

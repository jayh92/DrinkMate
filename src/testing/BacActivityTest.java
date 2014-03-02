package testing;

import com.isat.drinkmate.BacActivity;

import junit.framework.TestCase;

public class BacActivityTest extends TestCase {
	
	public void testBACAverageMale() 
	{
		int ounces = 48;
	    double percent = 5;
	    double weight = 160;
	    double hoursDrinking = 2;
	    double result = 0.1639655172413793;
	    String gender = "m";
	    BacActivity activity = new BacActivity();
	    assertEquals(result, activity.getBac(ounces, percent, weight, hoursDrinking, gender));
	}
	public void testBacNegativeValue()
	{
		int ounces = 1;
	    double percent = 5;
	    double weight = 160;
	    double hoursDrinking = 1;
	    double result = 0;
	    String gender = "m";
	    BacActivity activity = new BacActivity();
	    assertEquals(result, activity.getBac(ounces, percent, weight, hoursDrinking, gender));
	}
	public void testAverageGirl()
	{
		int ounces = 48;
	    double percent = 5;
	    double weight = 160;
	    double hoursDrinking = 2;
	    double result = 0.19959183673469386;
	    String gender = "f";
	    BacActivity activity = new BacActivity();
	    assertEquals(result, activity.getBac(ounces, percent, weight, hoursDrinking, gender));
	}
	public void testDoubleHours()
	{
		int ounces = 48;
	    double percent = 5;
	    double weight = 160;
	    double hoursDrinking = 2.5;
	    double result = 0.1564655172413793;
	    String gender = "m";
	    BacActivity activity = new BacActivity();
	    System.out.println(activity.getBac(ounces, percent, weight, hoursDrinking, gender));
	    assertEquals(result, activity.getBac(ounces, percent, weight, hoursDrinking, gender));
	}
}

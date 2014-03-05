package testing;

import com.isat.drinkmate.model.BacCalculator;

import junit.framework.TestCase;

public class BacActivityTest extends TestCase {
	
	private int ounces = 48;
	private double percent = 5, weight = 160, hoursDrinking = 2;
	private String male = "m", female = "f";
	private BacCalculator calcMale = new BacCalculator(ounces, percent, weight, hoursDrinking, male);
	private BacCalculator calcFemale = new BacCalculator(ounces, percent, weight, hoursDrinking, female);
	
	public void testBACAverageMale() 
	{
	    double result = 0.1639655172413793;
	    assertEquals(result, calcMale.getBac());
	}
	public void testBacNegativeValue()
	{
	    double result = 0;
	    BacCalculator test = new BacCalculator();
	    assertEquals(result, test.getBac());
	}
	public void testAverageFemale()
	{
	    double result = 0.19959183673469386;
	    assertEquals(result, calcFemale.getBac());
	}
}

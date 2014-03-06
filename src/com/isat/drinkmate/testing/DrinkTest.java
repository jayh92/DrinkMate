package com.isat.drinkmate.testing;

import com.isat.drinkmate.model.Drink;

import junit.framework.TestCase;
/*
 * DrinkTest tests the Drink.java class
 * 
 * NOTE: MUST ADD TO ANDROID MANIFEST FOR TESTING TO WORK!
 *         <instrumentation
		    android:name="android.test.InstrumentationTestRunner"
		    android:label="drinktest"
		    android:targetPackage="com.isat.drinkmate" />
 */
public class DrinkTest extends TestCase {
	
	// initialize variables to be used in testing
	private int id = 1;
	private String name = "Test Drink", 
			description = "Description Test",
			ingredients = "Ingredients Test";
	private Drink testDrink = new Drink(id, name, description, ingredients);
	
	/*** GETTERS TESTS ***/
	public void testGetDrinkName()
	{	
		String result = "Test Drink";
	    assertEquals(result, testDrink.getDrinkName());
	}
	public void testDrinkGetID()
	{
	    assertEquals(1, testDrink.getDrinkID());
	}
	public void testGetDrinkDescription()
	{
		String result = "Description Test";
	    assertEquals(result, testDrink.getDrinkDescription());
	}
	public void testGetDrinkIngredientString()
	{
		String result = "Ingredients Test";
	    assertEquals(result, testDrink.getCSVIngredient());
	}
	
	/*** SETTERS TESTS ***/
	public void testSetDrinkName()
	{
		testDrink.setDrinkName("test");
		assertEquals("test", testDrink.getDrinkName());
	}
	public void testSetDrinkID()
	{
		testDrink.setDrinkID(2);
		assertEquals(2, testDrink.getDrinkID());
	}
	public void testSetDrinkDescription()
	{
		testDrink.setDrinkDescription("test");
	    assertEquals("test", testDrink.getDrinkDescription());
	}
	public void tesSettDrinkIngredientString()
	{
		testDrink.setDrinkIngredients("test");
	    assertEquals("test", testDrink.getCSVIngredient());
	}
}

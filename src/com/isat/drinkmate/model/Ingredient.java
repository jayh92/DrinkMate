package com.isat.drinkmate.model;

/*
 * Class represents a single Ingredient to be used with our DrinkMate application
 * 
 * ISAT 480
 * Author(s): Jackson Phillips
 */

public class Ingredient {
	// set up private variables
	private int id;
	private double amount;
	private String name, type;
	private static final String[] ingredientTypes = {"Alcohol", "Garnish", "Mixer"};
	
	public Ingredient()
	{
		/*
		 * Default constructor - set values to null
		 */
		id = -1;
		name = "";
		type = "";
		amount = 0;
	}
	public Ingredient(int id, String name, String type, double amount)
	{
		/*
		 * Constructor if all parameters are known
		 * 
		 * @param int id - ingredient id
		 * @param String name - name of ingredient
		 * @param String type - type of ingredient
		 * @param double amount - amount to use
		 */
		//checkConstructorParameters(name, type, amount);
		this.id = id;
		this.name = name;
		this.type = type;
		this.amount = amount;
	}
	public Ingredient(String name, String type, double amount)
	{
		this.name = name;
		this.type = type;
		this.amount = amount;
	}
	public int getIngredientID()
	{
		/*
		 * Returns the ingredient ID
		 * 
		 * @return int ID
		 */
		return id;
	}
	public String getIngredientName()
	{
		/*
		 * Returns the name of the ingredient
		 * 
		 * @return String name
		 */
		return name;
	}
	public String getIngredientType()
	{
		/*
		 * Returns the type of the ingredient
		 * 
		 * @return String type
		 */
		return type;
	}
	public double getIngredientAmount()
	{
		/*
		 * Returns the amount to use of the ingredient
		 * 
		 * @return double amount
		 */
		return amount;
	}
	public void setIngredientID(int id)
	{
		/*
		 * Sets the ingredient ID
		 * 
		 * @param int id - id of ingredient
		 */
		this.id = id;
	}
	public void setIngredientName(String name)
	{
		/*
		 * Sets the ingredient name
		 * 
		 * @param String name
		 */
		this.name = name;
	}
	public void setIngredientType(String type)
	{
		/*
		 * Sets the ingredient type
		 * 
		 * @param String type
		 */
		this.type = type;
	}
	public void setIngredientAmount(double amount)
	{
		/*
		 * Sets the ingredient amount
		 * 
		 * @param double amount
		 */
		this.amount = amount;
	}
	public void checkConstructorParameters(String name, String type, double amount)
	{
		/*
		 * Checks to make sure everything is valid when creating a Ingredient
		 * 
		 * @param String name - name of ingredient
		 * @param String type - type of ingredient
		 * @param double amount - amount to use
		 */
		for (int i = 0; i < ingredientTypes.length; i++) // type check
			if (type.equalsIgnoreCase(ingredientTypes[i]))
					this.type = ingredientTypes[i];
		
		if (this.name.length() <= 0) // name check
			this.name = "No Ingredient Name";
		
		if (this.amount <= 0) // amount check1
			this.amount = 0;
	}
	public String getAllIngredientInfo() 
	{
		String res = "";
		res += this.name
				+ "(id = " + this.id + ")"
				+ " Type: " + this.type
				+ " Amount: " + String.valueOf(this.amount);
		
		return res;
	} 
}

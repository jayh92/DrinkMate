package com.isat.drinkmate.controller;

import java.util.ArrayList;
import java.util.Random;

import com.isat.drinkmate.model.Drink;
/*
 * RandomDrink.java
 * 
 * Class represents a Random Drink selector that uses other classes within our other packages
 * 
 * ISAT 480
 * Author(s): Jack Phillips
 */
public class RandomDrink {
	private ArrayList<Drink> drinks;
	private Drink randomDrink;
	
	/*
	 * Default Constructor
	 */
	public RandomDrink() {
		drinks = new ArrayList<Drink>();
		randomDrink = new Drink();
	}
	
	/*
	 * Overloaded Constructor
	 * 
	 * @param ArrayList<Drink> drinks
	 */
	public RandomDrink(ArrayList<Drink> drinks) {
		this.drinks = drinks;
		randomDrink = new Drink();
	}
	
	/*
	 * Sets the drink list for the object
	 * 
	 * @param ArrayList<Drink> drinks
	 */
	public void setDrinkList(ArrayList<Drink> drinks) {
		this.drinks = drinks;
	}
	
	/*
	 * Selects a random drink from the ArrayList<Drink>
	 * 
	 * @return Drink - Drink object made in com.isat.drinkmate.model
	 */
	public Drink getRandomDrink() {
		Random rand = new Random();
		randomDrink = drinks.get(rand.nextInt(drinks.size()));
		
		return randomDrink;
	}
	
	/*
	 * Returns the objects ArrayList<Drink>
	 * 
	 * @return ArrayList<Drink>
	 */
	public ArrayList<Drink> getDrinkList()
	{
		return drinks;
	}
}

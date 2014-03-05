package com.isat.drinkmate.model;

import java.util.ArrayList;
import java.util.Random;

public class RandomDrink {
	private ArrayList<Drink> drinks;
	private Drink randomDrink;
	
	public RandomDrink() {
		drinks = new ArrayList<Drink>();
		randomDrink = new Drink();
	}
	public RandomDrink(ArrayList<Drink> drinks) {
		this.drinks = drinks;
		randomDrink = new Drink();
	}
	
	public Drink getRandomDrink() {
		// get random drink
		Random rand = new Random();
		randomDrink = drinks.get(rand.nextInt(drinks.size()));
		
		return randomDrink;
	}
	public void setDrinkList(ArrayList<Drink> drinks) {
		this.drinks = drinks;
	}
}

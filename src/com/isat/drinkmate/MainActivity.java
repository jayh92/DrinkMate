package com.isat.drinkmate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.isat.drinkmate.model.Drink;
import com.isat.drinkmate.model.Ingredient;

import helper.DatabaseHelper;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public final static String EXTRA_MESSAGE = "INTENT FROM MAIN";
	String[] INGREDIENT_ARRAY = {
			"Coffee Liquer,Alcohol,.25",
			"Lemon Juice,Mixer,4",
			"Strawberry,Garnish,1",
			"Blended Whiskey,Alcohol,2",
			"Sugar Cube,Mixer,1",
			"Bitters,Mixer,1",
			"Sliced Lemon,Garnish,1",
			"Cherry,Garnish,1",
			"Sliced Orange,Garnish,1",
			"Vodka,Alcohol,32",
			"Fruit Punch,Mixer,64"
	};
	String[] DRINKS_ARRAY = {
			"Bahama Mama,A perfect drink for summer that is very popular,1,2,3",
			"Old Fashioned,Classic blended whiskey cocktail,4,5,6,7,8,9",
			"Jungle Juice,A necessity at any College party,10,11"
	};
	
	MultiSelectionSpinner spinner;
	
	DatabaseHelper db;
	ArrayList<Ingredient> ingredients;
	ArrayList<Drink> drinks;
	ArrayList<String> ingredientNameArray;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// initialize components
		db = new DatabaseHelper(getApplicationContext());
		ingredients = new ArrayList<Ingredient>();
		drinks = new ArrayList<Drink>();
		ingredientNameArray = new ArrayList<String>();
		
		// get ingredients from CSV string and add to DB
		for(int i = 0; i < INGREDIENT_ARRAY.length; i++) {
			Ingredient temp = makeIngredient(INGREDIENT_ARRAY[i]);
			//int tempID = db.createIngredient(temp);
			ingredients.add(temp);
			
		}
		
		// get drinks from CSV string and add to DB
		for (int i = 0; i < DRINKS_ARRAY.length; i++) {
			Drink temp = makeDrink(DRINKS_ARRAY[i]);
			drinks.add(temp);
		}
		
		// log display number of total ingredients
		int ingredientSize = db.getAllIngredients().size();
		Log.d("Ingredient count", "Ingredient Count: " + ingredientSize);
		
		// log display all ingredients
		List<Ingredient> allIng = db.getAllIngredients();
		for (Ingredient i : allIng) {
			//Log.d("Ingredient ", i.getAllIngredientInfo());
			ingredientNameArray.add(i.getIngredientName());
			//db.deleteIngredient(i.getIngredientID());
		}

		// spinner
		//spinner = (MultiSelectionSpinner)findViewById(R.id.mySpinner1);
		//spinner.setItems(ingredientNameArray);
		
		// log drink count and what drinks are present in db
		//Log.e("Drink Count", "Drink count: " + db.getDrinkCount());
		//Log.d("Get Drinks", "Getting all drinks");
		List<Drink> allDrinks = db.getAllDrinks();
//		for (Drink d : allDrinks) {
//			Log.d("Drink", d.getAllCSVDrinkInfo());
//			//db.deleteDrink(d.getDrinkID());
//		}
		// add id's to array list  vals
		for (int i = 0; i < allDrinks.size(); i++) {
			drinks.get(i).setDrinkID(allDrinks.get(i).getDrinkID());
			//Log.d("Drink: ", drinks.get(i).getAllInformation());
			drinks.get(i).setDrinkIngredients(drinks.get(i).getAllArrayIngredientInfo());
		}

		// close database
		db.close();
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
		System.out.println("onOptionsItemSelected");
	    switch (item.getItemId()) {
	        case R.id.action_search:
	        	System.out.println("SEARCH SELECTED");
	        	openSearch(item);
	            return true;
	        //case R.id.action_compose:
	           // composeMessage();
	         //   return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	public void openSearch(MenuItem searchItem)
	{
		System.out.println("OPEN SEARCH");
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
	}
	/*
	 * Start BacActivity for the BAC Calculator
	 * 
	 * @param View view
	 */
	
	public void startSearch(View v) {
		System.out.println("ON CLICK");
		Intent intent = new Intent(this, SearchActivity.class);
		
		intent.putStringArrayListExtra("ingredients", ingredientNameArray);
		startActivity(intent);
//		String s = spinner.getSelectedItemsAsString();
//		Toast.makeText(getApplicationContext(), s , Toast.LENGTH_LONG).show();
	}
	
	public void startBac(View view)
	{
		// changes the view to BAC Calculator
		Intent intent = new Intent(this, BacActivity.class);
		startActivity(intent);	
	}
	/*
	 * Start RandomActivity for the random drink selection
	 * 
	 * @param View view
	 */
	public void startRandom(View view)
	{
		// changes the view to Random Drink Selector
		Intent intent = new Intent(MainActivity.this, RandomActivity.class);
		Bundle b = new Bundle();
		b.putParcelableArrayList("extraextra", drinks);
		intent.putExtra("result.content", b);
		startActivity(intent);	
	}
	/*
	 * make ingredient from csv string
	 * 
	 * @param String ingredient
	 * @return Ingredient
	 */
	public Ingredient makeIngredient(String ingredient)
	{
		Ingredient tempIngredient = new Ingredient();
		String[] splitArray = ingredient.split(",");
		double amount = 0;
		
		// check to see if three values are present
		if (splitArray.length != 3)
			System.out.println("INVALID PARAM Length = " + ingredient.length());
		
		for (int i = 0; i < splitArray.length; i++) {
			String tempName = splitArray[0];
			String tempType = splitArray[1];
			// get and parse double amount
			if (i == 2) {
				try
				{
					amount = Double.parseDouble(splitArray[i]);
				} catch (Exception e) {
					System.out.println("Could not parse double");
				}
			}
			tempIngredient.setIngredientName(tempName);
			tempIngredient.setIngredientType(tempType);
			tempIngredient.setIngredientAmount(amount);
		}
//		System.out.println(tempIngredient.getIngredientName()
//				+ " " + tempIngredient.getIngredientType()
//				+ " " + tempIngredient.getIngredientAmount());
			
		return tempIngredient;
		
	}
	/*
	 * make drink from csv string
	 */
	public Drink makeDrink(String drink)
	{
		Drink tempDrink = new Drink();
		Ingredient tempIngredient = new Ingredient(); 
		ArrayList<Ingredient> drinkIngredients = new ArrayList<Ingredient>();
		
		// split csv string and set values
		String[] splitArr = drink.split(",");
		tempDrink.setDrinkName(splitArr[0]);
		tempDrink.setDrinkDescription(splitArr[1]);
		
		// loop and set ingredients for drink
		for (int i = 2; i < splitArr.length; i++) {
			if (i == splitArr.length - 1) {
				tempIngredient = db.getIngredient(Integer.parseInt(splitArr[i]));
				drinkIngredients.add(tempIngredient);
			}
			else {
				tempIngredient = db.getIngredient(Integer.parseInt(splitArr[i]));
				drinkIngredients.add(tempIngredient);
			}
		}
		tempDrink.setIngredientsArray(drinkIngredients);
			
		return tempDrink;
	}
	
	public ArrayList<Ingredient> getDBIngredients()
	{
		return ingredients;
	}
	public ArrayList<Drink> getDBDrinks()
	{
		return drinks;
	}
	/*
	 * NOT WORKING
	 */
	public ArrayList<Ingredient> parseIngredientCSV(String file_path)
	{
		ArrayList<Ingredient> allIngredients = new ArrayList<Ingredient>();
		String line = "";
		InputStream is = getClass().getClassLoader().getResourceAsStream("ingredient.csv");
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				Ingredient temp = makeIngredient(line);
				allIngredients.add(temp);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return allIngredients;
	}
}

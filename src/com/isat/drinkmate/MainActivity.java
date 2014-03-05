package com.isat.drinkmate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.isat.drinkmate.model.BacCalculator;
import com.isat.drinkmate.model.Drink;
import com.isat.drinkmate.model.Ingredient;
import com.isat.drinkmate.model.RandomDrink;

import helper.DatabaseHelper;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "INTENT FROM MAIN";
	String[] INGREDIENT_ARRAY = { "Coffee Liquer,Alcohol,.25",
			"Lemon Juice,Mixer,4", "Strawberry,Garnish,1",
			"Blended Whiskey,Alcohol,2", "Sugar Cube,Mixer,1",
			"Bitters,Mixer,1", "Sliced Lemon,Garnish,1", "Cherry,Garnish,1",
			"Sliced Orange,Garnish,1", "Vodka,Alcohol,32",
			"Fruit Punch,Mixer,64" };
	String[] DRINKS_ARRAY = {
			"Bahama Mama,A perfect drink for summer that is very popular,1,2,3",
			"Old Fashioned,Classic blended whiskey cocktail,4,5,6,7,8,9",
			"Jungle Juice,A necessity at any College party,10,11" };

	private MultiSelectionSpinner spinner;
	private DatabaseHelper db;
	private ArrayList<Ingredient> ingredients;
	private ArrayList<Drink> drinks;
	private ArrayList<String> ingredientNameArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// set up tabs
		TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup();
		
		// search tab
		TabSpec specSearch = tabHost.newTabSpec("Search");
		specSearch.setContent(R.id.search);
		specSearch.setIndicator("Search");

		// random tab
		TabSpec specRandom = tabHost.newTabSpec("Random");
		specRandom.setContent(R.id.random);
		specRandom.setIndicator("Random");
		
		// BAC tab
		TabSpec specBAC = tabHost.newTabSpec("BAC").setIndicator("BAC")
				.setContent(R.id.BAC);

		tabHost.addTab(specSearch);
		tabHost.addTab(specRandom);
		tabHost.addTab(specBAC);

		// initialize DB and ArrayLists
		db = new DatabaseHelper(getApplicationContext());
		ingredients = new ArrayList<Ingredient>();
		drinks = new ArrayList<Drink>();
		ingredientNameArray = new ArrayList<String>();

		// get ingredients from CSV string and add to DB
		for (int i = 0; i < INGREDIENT_ARRAY.length; i++) {
			Ingredient temp = makeIngredient(INGREDIENT_ARRAY[i]);
			// int tempID = db.createIngredient(temp);
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
			// Log.d("Ingredient ", i.getAllIngredientInfo());
			ingredientNameArray.add(i.getIngredientName());
			// db.deleteIngredient(i.getIngredientID());
		}

		// log drink count and what drinks are present in db
		// Log.e("Drink Count", "Drink count: " + db.getDrinkCount());
		// Log.d("Get Drinks", "Getting all drinks");
		List<Drink> allDrinks = db.getAllDrinks();
		// for (Drink d : allDrinks) {
		// Log.d("Drink", d.getAllCSVDrinkInfo());
		// //db.deleteDrink(d.getDrinkID());
		// }
		
		// add id's to array list vals
		for (int i = 0; i < allDrinks.size(); i++) {
			drinks.get(i).setDrinkID(allDrinks.get(i).getDrinkID());
			// Log.d("Drink: ", drinks.get(i).getAllInformation());
			drinks.get(i).setDrinkIngredients(
					drinks.get(i).getAllArrayIngredientInfo());
		}

		// close database
		db.close();

		// setup spinner for search
		spinner = (MultiSelectionSpinner) findViewById(R.id.mySpinner1);
		ingredientNameArray.add(0, "");
		spinner.setItems(ingredientNameArray);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/*
	 * Handles search listener
	 */
	public void searchClick(View v) {
		String s = spinner.getSelectedItemsAsString();
		String res = getSearchResult(s);
		Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
	}

	/*
	 * Generates a random drink and outputs it to view
	 */
	public void generateDrink(View view) {
		RandomDrink rand = new RandomDrink(drinks);
		Drink temp = rand.getRandomDrink();

		// get TextViews
		TextView drinkName = (TextView) findViewById(R.id.tvDrinkName);
		TextView description = (TextView) findViewById(R.id.tvDrinkDescription);
		TextView ingredients = (TextView) findViewById(R.id.tvDrinkIngredients);

		// format TextViews
		drinkName.setGravity(Gravity.CENTER_VERTICAL
				| Gravity.CENTER_HORIZONTAL);
		description.setGravity(Gravity.CENTER_VERTICAL
				| Gravity.CENTER_HORIZONTAL);
		ingredients.setGravity(Gravity.CENTER_VERTICAL
				| Gravity.CENTER_HORIZONTAL);

		// tell user a drink was found
		Toast.makeText(getApplicationContext(), temp.getDrinkName() + "!", Toast.LENGTH_LONG).show();
		
		// print out random drink
		drinkName.setText("Name\n" + temp.getDrinkName());
		description.setText("Description\n" + temp.getDrinkDescription());
		ingredients.setText(temp.getCSVIngredient());
	}

	/*
	 * Searches by ingredients for the most suitable drink
	 * 
	 * @param String query
	 */
	public String getSearchResult(String query) {
		int success = 0, temp = 0;
		String result = "";
		Drink drink = new Drink();
		String[] splitArr = query.split(",");
		
		// iterate through drinks and search for ingredients in split array
		try {
			if (query.length() <= 0)
				return "Please Select Ingredients";
			for (Drink d : drinks) {
				for (int i = 0; i < splitArr.length; i++) {
					if (d.getCSVIngredient().contains(splitArr[i])) {
						temp++;
					}
				}
				if (temp > success) {
					drink = d;
					temp = 0;
				}
			}
			result = drink.getDrinkName();
		} catch (Exception e) {
			System.out.println("ERROR: query to search");
		}
		
		// output to console for error checking
		System.out.println("Name: " + drink.getDrinkName());
		System.out.println("Description: " + drink.getDrinkDescription());
		System.out.println("Ingredient: " + drink.getCSVIngredient());

		try {
			// set name, description, ingredients on TextView
			TextView drinkName = (TextView) findViewById(R.id.tvNameSearch);
			TextView drinkDescription = (TextView) findViewById(R.id.tvDescriptionSearch);
			TextView drinkIngredients = (TextView) findViewById(R.id.tvIngredientSearch);

			// format text views
			drinkName.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
			drinkDescription.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
			drinkIngredients.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
			
			drinkName.setText(drink.getDrinkName());
			drinkDescription.setText(drink.getDrinkDescription());
			drinkIngredients.setText(drink.getCSVIngredient());
		} catch (Exception e) {
			System.out.println("Failed to set TextViews");
		}
		return "FOUND: " + result;
	}

	public void calculateBac(View ciew) {
		// initialize variables
		int ounces = 0;
		double totalHours = 0;
		double percent = 0, userWeight = 0;
		String userGender = "";
		BacCalculator calc;

		// pull in user input
		EditText oz = (EditText) findViewById(R.id.ouncesEt);
		EditText perc = (EditText) findViewById(R.id.percentEt);
		EditText gender = (EditText) findViewById(R.id.genderEt);
		EditText weight = (EditText) findViewById(R.id.weightEt);
		EditText hours = (EditText) findViewById(R.id.timeEt);

		// result of calculations TextView
		TextView res = (TextView) findViewById(R.id.resultTv);
		res.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

		// parse user inputs
		try {
			totalHours = Double.parseDouble(hours.getText().toString());
			ounces = Integer.parseInt(oz.getText().toString());
			percent = Double.parseDouble(perc.getText().toString());
			userWeight = Double.parseDouble(weight.getText().toString());
		} catch (NumberFormatException e) {
			System.out.println(e);
		}
		try {
			userGender = gender.getText().toString().substring(0, 1);
		} catch (Exception e) {
			System.out.println(e);
		}
		// calculate BAC
		calc = new BacCalculator(ounces, percent, userWeight, totalHours, userGender);
		
		// provide message to user and set text color
		double bacCheck = calc.getBac();
		if (bacCheck <= 0) {
			Toast.makeText(getApplicationContext(), "You're Sober", Toast.LENGTH_LONG).show();
			res.setTextColor(Color.WHITE);
		}
		else if (bacCheck >= 0 && bacCheck <= 0.08) {
			Toast.makeText(getApplicationContext(), "You're not above the legal limit", Toast.LENGTH_LONG).show();
			res.setTextColor(Color.BLUE);	
		}
		else if (bacCheck >= 0.08 && bacCheck <= .18) {
			Toast.makeText(getApplicationContext(), "DO NOT DRIVE!", Toast.LENGTH_LONG).show();
			res.setTextColor(Color.YELLOW);
		}
		else if (bacCheck >= .18) {
			Toast.makeText(getApplicationContext(), "SEEK MEDICAL ATTENTION!", Toast.LENGTH_LONG).show();
			res.setTextColor(Color.RED);
		}
		else
			Toast.makeText(getApplicationContext(), "Invalid Input", Toast.LENGTH_LONG).show();
		
		// set text on screen
		res.setText(calc.getBacString());
	}
	/*
	 * Clears the form of values for BAC Calculator
	 * 
	 * @param View view
	 */
	public void clearForm(View view)
	{
		// get values
		EditText oz = (EditText)findViewById(R.id.ouncesEt);
		EditText perc = (EditText)findViewById(R.id.percentEt);
		EditText gender = (EditText)findViewById(R.id.genderEt);
		EditText weight = (EditText)findViewById(R.id.weightEt);
		EditText hours = (EditText)findViewById(R.id.timeEt);
		TextView res = (TextView)findViewById(R.id.resultTv);
		
		// set them to blanks and set hints
		oz.setText("");
		perc.setText("");
		gender.setText("");
		weight.setText("");
		hours.setText("");
		res.setText("");
	}

	/*
	 * make ingredient from csv string
	 * 
	 * @param String ingredient
	 * 
	 * @return Ingredient
	 */
	public Ingredient makeIngredient(String ingredient) {
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
				try {
					amount = Double.parseDouble(splitArray[i]);
				} catch (Exception e) {
					System.out.println("Could not parse double");
				}
			}
			tempIngredient.setIngredientName(tempName);
			tempIngredient.setIngredientType(tempType);
			tempIngredient.setIngredientAmount(amount);
		}
		return tempIngredient;
	}

	/*
	 * make drink from csv string
	 * 
	 * @param String drink
	 * @return Drink
	 */
	public Drink makeDrink(String drink) {
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
				tempIngredient = db
						.getIngredient(Integer.parseInt(splitArr[i]));
				drinkIngredients.add(tempIngredient);
			} else {
				tempIngredient = db
						.getIngredient(Integer.parseInt(splitArr[i]));
				drinkIngredients.add(tempIngredient);
			}
		}
		tempDrink.setIngredientsArray(drinkIngredients);

		return tempDrink;
	}
	
	/*
	 * Returns ArrayList of ingredients
	 * 
	 * @return ArrayList<Ingredient>
	 */
	public ArrayList<Ingredient> getDBIngredients() {
		return ingredients;
	}
	/*
	 * Returns ArrayList of Drinks
	 * 
	 * @return ArrayList<Drink>
	 */
	public ArrayList<Drink> getDBDrinks() {
		return drinks;
	}

	/*
	 * NOT WORKING
	 */
	public ArrayList<Ingredient> parseIngredientCSV(String file_path) {
		ArrayList<Ingredient> allIngredients = new ArrayList<Ingredient>();
		String line = "";
		InputStream is = getClass().getClassLoader().getResourceAsStream(
				"ingredient.csv");

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

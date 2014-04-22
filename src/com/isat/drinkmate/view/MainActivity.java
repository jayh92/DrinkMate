package com.isat.drinkmate.view;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.isat.drinkmate.R;
import com.isat.drinkmate.controller.BacCalculator;
import com.isat.drinkmate.controller.DatabaseHelper;
import com.isat.drinkmate.controller.RandomDrink;
import com.isat.drinkmate.model.Drink;
import com.isat.drinkmate.model.Ingredient;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

/*
 * MainActivity.java - Main Driver class for the DrinkMate Application. This class sets up all the tabs and listeners
 * for on screen actions.
 * 
 * ISAT 480
 * @Authors: Jack Phillips, Jay Harris, Dan Silvernail
 */
public class MainActivity extends Activity implements SensorEventListener {

	// Ingredient Array - NOTE Drinks will reuse ingredients
	private String[] INGREDIENT_ARRAY = { "Coffee Liquer,Alcohol,.25", // Bahama Mama
			"Lemon Juice,Mixer,4", "Strawberry,Garnish,1", // Bahama Mama cont.
			"Blended Whiskey,Alcohol,2", "Sugar Cube,Mixer,1", // Old Fashioned
			"Bitters,Mixer,1", "Sliced Lemon,Garnish,1", "Cherry,Garnish,1","Sliced Orange,Garnish,1", // Old Fashioned cont.
			"Vodka,Alcohol,32", "Fruit Punch,Mixer,64", // Jungle Juice
			"Gin,Alcohol,2", "Tonic Water,Mixer,5","Lime Wedge,Garnish,1", // Gin and Tonic
			"Goldschlager,Alcohol,.5", "Jose Cuervo Tequila,Alcohol,.5",  // 49er Gold Rush
	 		"Alize,Alcohol,2", "Grand Marnier,Alcohol,1", // 3rd Wheel
	 		"Malt Liquor,Alcohol,35", "Orange Juice,Mixer,5" }; // Brass Monkey
	
	// Drink Array NAME, DESCRIPTION, INGREDIENT IDs
	private String[] DRINKS_ARRAY = {
			"Bahama Mama,A perfect drink for summer that is very popular,1,2,3",
			"Old Fashioned,Classic blended whiskey cocktail,4,5,6,7,8,9",
			"Jungle Juice,A necessity at any College party,10,11",
			"Gin and Tonic,Simple highball glass drink that tastes kind of like a Christmas tree,12,13,14",
			"49er Gold Rush,A Cinnamon shooter with extra kick,15,16",
			"3rd Wheel,Perfect for awkward nights out,17,18",
			"Brass Monkey,The funky monkey is back with this Beastie Boys drink,19,20", 
			"Garbage Pale,Everything in your cabinet goes in your glass!,1,3,5,10,12,15,16,17", 
			"Vodka Tonic,A variation on the Gin and Tonic this cocktail is one that is never going away,10,13,14",
			"Virgin Cocktail,Take up your Designated Driver duties this weekend,2,11,13" };

	private MultiSelectionSpinner spinner;	// ingredient spinner
	private Spinner drinkSpinner;		    // drink spinner
	private DatabaseHelper db;			    // Database
	private ArrayList<Ingredient> ingredients;
	private ArrayList<Drink> drinks;
	
	// used to populate spinner lists
	private ArrayList<String> ingredientNameArray, drinkNameArray;

	// sensor
	private SensorManager sensorManager;
	private boolean shake = false;
	private View view;
	private long lastUpdate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// set up tabs
		try {
			TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
			tabHost.setup();
	
			// search tab
			TabSpec specSearch = tabHost.newTabSpec("Search");
			specSearch.setIndicator("Search",
					getResources().getDrawable(R.drawable.ic_action_search));
			specSearch.setContent(R.id.search);
	
			// random tab
			TabSpec specRandom = tabHost.newTabSpec("Random");
			specRandom.setIndicator("Random", getResources().getDrawable(R.drawable.ic_action_shuffle));
			specRandom.setContent(R.id.random);
	
			// BAC tab
			TabSpec specBAC = tabHost
					.newTabSpec("BAC")
					.setIndicator("BAC",
							getResources().getDrawable(R.drawable.ic_action_error))
					.setContent(R.id.BAC);
			TextView bac_res = (TextView) findViewById(R.id.resultTv);
			bac_res.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
	
			// add tabs to tabHost
			tabHost.addTab(specSearch);
			tabHost.addTab(specRandom);
			tabHost.addTab(specBAC);
		}
		catch (Exception e) {
			Log.e("MainActivity:onCreate", "Error with initializing TabHost");
		}
		
		// sensor
		try {
			view = findViewById(R.id.random);
			sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
			lastUpdate = System.currentTimeMillis();
		}
		catch (Exception e) {
			Log.e("MainActivity:onCreate", "Error initializing Accelerometer Sensor");
		}
		
		// initialize DB and ArrayLists
		db = new DatabaseHelper(getApplicationContext());
		ingredients = new ArrayList<Ingredient>();
		drinks = new ArrayList<Drink>();
		ingredientNameArray = new ArrayList<String>();
		drinkNameArray = new ArrayList<String>();
		
		// Display DB info in log
		Log.i("DB INFO", "DRINK(" + DRINKS_ARRAY.length + "): " + db.getDrinkCount() + " ING(" + INGREDIENT_ARRAY.length + "): " + db.getIngredientCount());
		
		try {
			int countIngArr = 1; // used to set IDs
			int dbIngredientSize = db.getIngredientCount();
			
			// create ingredients from array and add to db
			for (int i = 0; i < INGREDIENT_ARRAY.length; i++) {
				Ingredient temp = makeIngredient(INGREDIENT_ARRAY[i]);
				temp.setIngredientID(countIngArr);
				countIngArr++;
				ingredients.add(temp);

				// add to db if necessary
				if ((countIngArr - 1) > dbIngredientSize) {
					db.createIngredient(temp);
				}
			}
			
			// get drinks from CSV string and add to DB
			for (int i = 0; i < DRINKS_ARRAY.length; i++) {
				Drink temp = makeDrink(DRINKS_ARRAY[i]);
				drinkNameArray.add(temp.getDrinkName());
				drinks.add(temp);
			}
		}
		catch (Exception e) {
			Log.e("MainActivity:onCreate", "Error using DRINK_ARRAY");
		}
		// populate name array
		try {
			for (Ingredient i : ingredients) {
				ingredientNameArray.add(i.getIngredientName());
			}
		}
		catch (Exception e) {
			Log.e("MainActivity:onCreate", "Error using INGREDIENT_ARRAY");
		}
		try {
			// set Drink IDs in ArrayList<Drink>
			int t = 1;
			int drinkSize = db.getDrinkCount();
			for (Drink d : drinks) {
				d.setDrinkID(t);
				Log.i("SET DRINK ID", d.getDrinkName() + d.getDrinkID() + " " + d.getCSVIngredient());
				t++;
				
				// add drink to DB if necessary
				if (drinks.size() > drinkSize) {
					db.createDrink(d);
				}
			}
		}
		catch (Exception e) {
			Log.e("MainActivity:onCreate", "Failed to create Drink");
		}
		
		// close database
		db.close();
		
		try {
			// setup spinner for search by ingredient
			spinner = (MultiSelectionSpinner) findViewById(R.id.mySpinner1); // ingredients
			ingredientNameArray.add(0, ""); // add blank ingredient for display purposes
			spinner.setItems(ingredientNameArray); // set MultiSelectionSpinner for ingredients
		}
		catch (Exception e) {
			Log.e("MainActivity:onCreate", "ERROR setting up Ingredient Spinner");
		}
		try {
		// setup spinner for browse by drink
		drinkNameArray.add(0, "");
		Collections.sort(drinkNameArray); // alphabetize
		drinkSpinner = (Spinner) findViewById(R.id.spinDrink);
		ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, drinkNameArray);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		drinkSpinner.setAdapter(dataAdapter);
		}
		catch (Exception e) {
			Log.e("MainActivity:onCreate", "ERROR setting up Drink Spinner");
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/*
	 * Handles search listener
	 * 
	 * @param View view
	 */
	public void searchClick(View view) {
		String res = "DRINK NOT FOUND";
		String sIngredient = spinner.getSelectedItemsAsString();
		String drinkStr = String.valueOf(drinkSpinner.getSelectedItem());
		
		// browse drink spinner
		if (drinkStr != null && drinkStr != "") {
			res = getDrinkSearchResult(String.valueOf(drinkSpinner.getSelectedItem()));
			drinkSpinner.setSelection(0);
		}
		// search by ingredients
		else {
			res = getSearchResult(sIngredient, spinner.getSelectedIndicies());
		}
		Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
	}
	/*
	 * Returns the result of a users Search Result and sets the TextView on screen
	 * 
	 * @param String name
	 * @return Drink name that is found
	 */
	public String getDrinkSearchResult(String name) {
		String result = "";
		for (Drink temp : drinks) {
			if (temp.getDrinkName().equalsIgnoreCase(name)) {
				result = temp.getDrinkName();
				try {
					// set name, description, ingredients on TextView
					TextView drinkName = (TextView) findViewById(R.id.tvNameSearch);
					TextView drinkDescription = (TextView) findViewById(R.id.tvDescriptionSearch);
					TextView drinkIngredients = (TextView) findViewById(R.id.tvIngredientSearch);
		
					// format text views
					drinkName.setGravity(Gravity.CENTER_VERTICAL
							| Gravity.CENTER_HORIZONTAL);
					drinkDescription.setGravity(Gravity.CENTER_VERTICAL
							| Gravity.CENTER_HORIZONTAL);
					drinkIngredients.setGravity(Gravity.CENTER_VERTICAL
							| Gravity.CENTER_HORIZONTAL);
		
					drinkName.setText(temp.getDrinkName());
					drinkDescription.setText("\n" + temp.getDrinkDescription());
					drinkIngredients.setText("\n" + temp.getAllArrayIngredientInfo());
				} catch (Exception e) {
					Log.e("getDrinkSearchResult ERROR", "Failed to set TextView for Search");
				}
			}
		}
		return result;
	}
	
	/*
	 * Generates a random drink and outputs it to view - used by RANDOM tab
	 * 
	 * @param View view
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
		Toast.makeText(getApplicationContext(), temp.getDrinkName() + "!",
				Toast.LENGTH_LONG).show();

		// print out random drink
		drinkName.setText(temp.getDrinkName());
		description.setText(temp.getDrinkDescription());
		ingredients.setText(temp.getAllArrayIngredientInfo());
	}

	/*
	 * Searches by ingredients for the most suitable drink - used by SEARCH tab
	 * 
	 * @param String query
	 * @return String result
	 */
	public String getSearchResult(String query, List<Integer> ingredientID) {
		int success = 0, temp = 0;
		String result = "";
		Drink drink = new Drink();
		String[] splitArr = query.split(",");

		// iterate through drinks and search for ingredients in split array
		try {
			if (query.length() <= 0)
				return "Please Select Ingredients";
			for (Drink d : drinks) {
				ArrayList<Ingredient> tempIngredients = d.getIngredientArrayList();
				for (int j = 0; j < tempIngredients.size(); j++) {
					for (Integer num : ingredientID) {
						if (tempIngredients.get(j).getIngredientID() == num) {
							temp++;
						}
					}
				}
				// set drink based on how many ingredients are checked for it
				if (temp > success) {
					drink = d;
					success = temp;
					temp = 0;
				}
			}
			result = drink.getDrinkName();
		} catch (Exception e) {
			Log.e("MainActivity:getSearchResult", "ERROR: query to search");
		}

		// output to log for error checking
		Log.i("MainActivity:getSearchResult", "Name: " + drink.getDrinkName());
		Log.i("MainActivity:getSearchResult","Description: " + drink.getDrinkDescription());
		Log.i("MainActivity:getSearchResult", "Ingredient: " + drink.getAllArrayIngredientInfo());

		try {
			// set name, description, ingredients on TextView
			TextView drinkName = (TextView) findViewById(R.id.tvNameSearch);
			TextView drinkDescription = (TextView) findViewById(R.id.tvDescriptionSearch);
			TextView drinkIngredients = (TextView) findViewById(R.id.tvIngredientSearch);

			// format text views
			drinkName.setGravity(Gravity.CENTER_VERTICAL
					| Gravity.CENTER_HORIZONTAL);
			drinkDescription.setGravity(Gravity.CENTER_VERTICAL
					| Gravity.CENTER_HORIZONTAL);
			drinkIngredients.setGravity(Gravity.CENTER_VERTICAL
					| Gravity.CENTER_HORIZONTAL);

			drinkName.setText(drink.getDrinkName());
			drinkDescription.setText("\n" + drink.getDrinkDescription());
			drinkIngredients.setText("\n" + drink.getAllArrayIngredientInfo());
		} catch (Exception e) {
			Log.e("getSearchResult ERROR", "Failed to set TextView for Search");
		}
		return "FOUND: " + result;
	}

	/*
	 * Calculates the BAC based on user input - used by BAC tab
	 * 
	 * @param View view
	 */
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

		// parse user inputs
		try {
			totalHours = Double.parseDouble(hours.getText().toString());
			ounces = Integer.parseInt(oz.getText().toString());
			percent = Double.parseDouble(perc.getText().toString());
			userWeight = Double.parseDouble(weight.getText().toString());
		} catch (NumberFormatException e) {
			Log.e("MainActivity:calculateBac() ERROR", "Could not parse numeric inputs");
		}
		try {
			userGender = gender.getText().toString().substring(0, 1);
		} catch (Exception e) {
			Log.e("MainActivity:calculateBac() ERROR", "Could not set user gender");
		}
		// calculate BAC
		calc = new BacCalculator(ounces, percent, userWeight, totalHours,
				userGender);

		// show result
		showBACResults(calc);
	}
	
	/*
	 * Set and display on screen the calculations of the BAC calculator
	 * 
	 * @param BacCalculator calc - object that represents user BAC calc
	 */
	public void showBACResults(BacCalculator calc) {
		try {
			// result of calculations TextView
			TextView res = (TextView) findViewById(R.id.resultTv);
			res.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
	
			// provide message to user and set text color
			double bacCheck = calc.getBac();
			if (bacCheck <= 0) {
				Toast.makeText(getApplicationContext(), "You're Sober",
						Toast.LENGTH_SHORT).show();
				res.setTextColor(Color.GREEN);
			} else if (bacCheck >= 0 && bacCheck <= 0.08) {
				Toast.makeText(getApplicationContext(),
						"You're not above the legal limit", Toast.LENGTH_LONG)
						.show();
				res.setTextColor(Color.BLUE);
			} else if (bacCheck >= 0.08 && bacCheck <= .18) {
				Toast.makeText(getApplicationContext(), "DO NOT DRIVE!",
						Toast.LENGTH_SHORT).show();
				res.setTextColor(Color.YELLOW);
			} else if (bacCheck >= .18) {
				Toast.makeText(getApplicationContext(), "SEEK MEDICAL ATTENTION!",
						Toast.LENGTH_SHORT).show();
				res.setTextColor(Color.RED);
			} else
				Toast.makeText(getApplicationContext(), "Invalid Input",
						Toast.LENGTH_LONG).show();
	
			// set text on screen
			res.setText(calc.getBacString());
		}
		catch (Exception e) {
			Log.e("MainActivity:showBACResults", "Failed to set BAC output on view");
		}
	}

	/*
	 * Clears the form of values for BAC Calculator - used by BAC tab
	 * 
	 * @param View view
	 */
	public void clearForm(View view) {
		try {
			// get values
			EditText oz = (EditText) findViewById(R.id.ouncesEt);
			EditText perc = (EditText) findViewById(R.id.percentEt);
			EditText gender = (EditText) findViewById(R.id.genderEt);
			EditText weight = (EditText) findViewById(R.id.weightEt);
			EditText hours = (EditText) findViewById(R.id.timeEt);
			TextView res = (TextView) findViewById(R.id.resultTv);
	
			// set them to blanks and set hints
			oz.setText("");
			perc.setText("");
			gender.setText("");
			weight.setText("");
			hours.setText("");
			res.setText("");
			res.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		}
		catch (Exception e) {
			Log.e("MainActivity:clearForm", "Failed to Clear BAC form");
		}
	}

	/*
	 * Creates ingredient from csv string
	 * 
	 * @param String ingredient
	 * @return Ingredient
	 */
	public Ingredient makeIngredient(String ingredient) {
		System.out.println("makeIng: " + ingredient);
		Ingredient tempIngredient = new Ingredient();
		String[] splitArray = ingredient.split(",");
		double amount = 0;

		// check to see if three values are present
		if (splitArray.length != 3) {
			Log.i("MainActivity:makeIngredient", "INVALID PARAM Length = " + ingredient.length());
		}

		for (int i = 0; i < splitArray.length; i++) {
			String tempName = splitArray[0];
			String tempType = splitArray[1];
			// get and parse double amount
			if (i == 2) {
				try {
					amount = Double.parseDouble(splitArray[i]);
				} catch (Exception e) {
					Log.e("MainActivity:makeIngredient", "Could not parse double ofr ingredient");
				}
			}
			tempIngredient.setIngredientName(tempName);
			tempIngredient.setIngredientType(tempType);
			tempIngredient.setIngredientAmount(amount);
		}
		return tempIngredient;
	}

	/*
	 * Creates a drink from csv string
	 * 
	 * @param String drink
	 * @return Drink
	 */
	public Drink makeDrink(String drink) {
		Drink tempDrink = new Drink();
		ArrayList<Ingredient> drinkIngredients = new ArrayList<Ingredient>();

		// split csv string and set values
		String[] splitArr = drink.split(",");
		tempDrink.setDrinkName(splitArr[0]);
		tempDrink.setDrinkDescription(splitArr[1]);
		
		// loop through split array
		for (int i = 2; i < splitArr.length; i++) {
			for (int index = 0; index < ingredients.size(); index++) {
				try {
					// parse csv array and check if ingredient IDs match
					int ingredientID = Integer.parseInt(splitArr[i]);
					if (ingredientID == ingredients.get(index).getIngredientID()) {
						drinkIngredients.add(ingredients.get(index));
					}
				}
				catch (Exception e) {
					Log.e("MainActivity:makeDrink", "Failed to identify specified drink ingredient IDs");
				}
			}
		}
		// set the drinks ingredient list
		tempDrink.setIngredientsArray(drinkIngredients);
		return tempDrink;
	}

	/** ACCELEROMETER SENSOR **/
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			getAccelerometer(event);
		}
	}

	private void getAccelerometer(SensorEvent event) {
		float[] values = event.values;
		// Movement
		float x = values[0];
		float y = values[1];
		float z = values[2];

		// compare to earths gravity so when at rest it does not activate listener
		float accelationSquareRoot = (x * x + y * y + z * z)
				/ (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
		long actualTime = System.currentTimeMillis();
		if (accelationSquareRoot >= 2) //
		{
			if (actualTime - lastUpdate < 200) {
				return;
			}
			lastUpdate = actualTime;
			if (shake) {
				Log.i("MainActivity:getAccelerometer", "Accelerometer set up for shake");

			} else {
				generateDrink(view);
			}
			shake = !shake;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// register this class as a listener for the orientation and accelerometer sensors
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		// unregister listener
		super.onPause();
		sensorManager.unregisterListener(this);
	}
	
	/** HELPER METHODS **/
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
	
}

package com.isat.drinkmate.view;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
//import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

public class MainActivity extends Activity implements SensorEventListener {

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
	private Spinner drinkSpinner;
	private DatabaseHelper db;
	private ArrayList<Ingredient> ingredients;
	private ArrayList<Drink> drinks;
	// used to populate spinner lists
	private ArrayList<String> ingredientNameArray, drinkNameArray;

	// sensor
	private SensorManager sensorManager;
	private boolean shake = false;
	private View view;
	private long lastUpdate;
	
	boolean createDb = false;
	boolean deleteDb = false;
	
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
			System.out.println("TabHost Error");
		}
		
		// sensor
		try {
			view = findViewById(R.id.random);
			sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
			lastUpdate = System.currentTimeMillis();
		}
		catch (Exception e) {
			System.out.println("Sensor Error");
		}
		
		// initialize DB and ArrayLists
		//db = new DatabaseHelper(getApplicationContext());
		ingredients = new ArrayList<Ingredient>();
		drinks = new ArrayList<Drink>();
		ingredientNameArray = new ArrayList<String>();
		drinkNameArray = new ArrayList<String>();
		
		//System.out.println("DRINK(" + DRINKS_ARRAY.length + "): " + db.getDrinkCount() + " ING(" + INGREDIENT_ARRAY.length + "): " + db.getIngredientCount());
		//db.deleteAllDrinks();
		//db.deleteAllIngredients();
		//System.out.println("DRINK(" + DRINKS_ARRAY.length + "): " + db.getDrinkCount() + " ING(" + INGREDIENT_ARRAY.length + "): " + db.getIngredientCount());
		
		try {
			int countIngArr = 1;
		// get ingredients from CSV string and add to DB
			for (int i = 0; i < INGREDIENT_ARRAY.length; i++) {
				Ingredient temp = makeIngredient(INGREDIENT_ARRAY[i]);
//				if (createDb || db.getIngredientCount() == 0){
//					System.out.println("CREATE INGREDIENT MAIN(0): " + temp.getIngredientName());
//					int tempID = db.createIngredient(temp);
//				}
//				if (db.getIngredientCount() <= countIngArr) {
//					System.out.println("CREATE INGREDIENT MAIN: " + temp.getIngredientName());
//					int tempID = db.createIngredient(temp);
//				}
//				else
//					countIngArr++;
				temp.setIngredientID(countIngArr);
				countIngArr++;
				System.out.println(temp.getIngredientName() + temp.getIngredientID());
				ingredients.add(temp);
			}
			
			//int countDrink = 0;
			// get drinks from CSV string and add to DB
			for (int i = 0; i < DRINKS_ARRAY.length; i++) {
				Drink temp = makeDrink(DRINKS_ARRAY[i]);
				
//				if (createDb || db.getDrinkCount() == 0){
//					System.out.println("CREATE DRINK MAIN(0): " + temp.getDrinkName());
//					int tempD = db.createDrink(temp);
//				}
//				if (db.getDrinkCount() <= countDrink) {
//					System.out.println("CREATE DRINK MAIN: " + temp.getDrinkName());
//					int tempD = db.createDrink(temp);
//				}
//				else
//					countDrink++;
				drinkNameArray.add(temp.getDrinkName());
				drinks.add(temp);
			}
		}
		catch (Exception e) {
			System.out.println("Error using ARRAYS");
		}
		// log display number of total ingredients
		//int ingredientSize = db.getIngredientCount();
		//Log.d("Ingredient count", "Ingredient Count: " + ingredientSize);
		try {
			int countIng = INGREDIENT_ARRAY.length;
			for (Ingredient i : ingredients) {
				ingredientNameArray.add(i.getIngredientName());
			// log display all ingredients
			//List<Ingredient> allIng = db.getAllIngredients();
			//for (Ingredient i : allIng) {
				// Log.d("Ingredient ", i.getAllIngredientInfo());
				//ingredientNameArray.add(i.getIngredientName());
//				if (deleteDb) {
//					db.deleteIngredient(i.getIngredientID());
//				}
//				if (countIng == 0) {
//					System.out.println("DELETE INGREDIENT MAIN");
//					db.deleteIngredient(i.getIngredientID());
//					allIng.remove(i);
//				}
//				else
//					countIng--;
			}
		}
		catch (Exception e) {
			System.out.println("INGREDIENT ARRAY ERROR");
		}
		//List<Drink> allDrinks;
		try {
			int t = 1;
			for (Drink d : drinks) {
				d.setDrinkID(t);
				System.out.println(d.getDrinkName() + d.getDrinkID() + " " + d.getCSVIngredient());
				t++;
			}
			// log drink count and what drinks are present in db
//		    Log.e("Drink Count", "Drink count: " + db.getDrinkCount());
//			 allDrinks = db.getAllDrinks();
//			 int count = DRINKS_ARRAY.length;
//			 for (Drink d : allDrinks) {
//				 if (deleteDb) {
//					 db.deleteDrink(d.getDrinkID());
//				 }
//				 if (count == 0) {
//					 System.out.println("DELETE DRINK MAIN");
//					 db.deleteDrink(d.getDrinkID());
//					 allDrinks.remove(d);
//				 }
//				 else
//					 count--;
//			 }
			// add id's to array list vals
//			for (int i = 0; i < allDrinks.size(); i++) {
//				System.out.println("ADDING IDS TO DRINKS");
//				drinks.get(i).setDrinkID(allDrinks.get(i).getDrinkID());
//			    Log.d("Drink: ", drinks.get(i).getAllInformation());
//				drinks.get(i).setDrinkIngredients(
//						drinks.get(i).getAllArrayIngredientInfo());
//			}
		}
		catch (Exception e) {
			System.out.println("MainActivity:OnCreate Drink error");
		}

		// close database
		//db.close();

		// setup spinner for search
		spinner = (MultiSelectionSpinner) findViewById(R.id.mySpinner1); // ingredients
		ingredientNameArray.add(0, ""); // add blank ingredient for display purposes
		spinner.setItems(ingredientNameArray);
		
		drinkNameArray.add(0, "");
		drinkSpinner = (Spinner) findViewById(R.id.spinDrink);
		ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, drinkNameArray);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		drinkSpinner.setAdapter(dataAdapter);
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
					System.out.println("Failed to set TextView for Search");
				}
			}
		}
		return result;
	}
	
	/*
	 * Generates a random drink and outputs it to view
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
	 * Searches by ingredients for the most suitable drink
	 * 
	 * @param String query
	 * 
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
//				for (int i = 0; i < splitArr.length; i++) {
//					// contains and remove white space
//					if (d.getCSVIngredient().contains(splitArr[i].replaceAll("^\\s+", ""))) 
//					{
//						System.out.println("found");
//						temp++;
//					}
//				}
				ArrayList<Ingredient> tempIngredients = d.getIngredientArrayList();
				for (int j = 0; j < tempIngredients.size(); j++) {
					for (Integer num : ingredientID) {
						if (tempIngredients.get(j).getIngredientID() == num) {
							temp++;
							System.out.println("FOUND: " + tempIngredients.get(j).getIngredientName());
						}
					}
				}
				if (temp > success) {
					drink = d;
					success = temp;
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
		System.out.println("Ingredient: " + drink.getAllArrayIngredientInfo());

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
			System.out.println("Failed to set TextView for Search");
		}
		return "FOUND: " + result;
	}

	/*
	 * Calculates the BAC based on user input
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

		// // result of calculations TextView
		// TextView res = (TextView) findViewById(R.id.resultTv);
		// res.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

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
		calc = new BacCalculator(ounces, percent, userWeight, totalHours,
				userGender);

		// show result
		showBACResults(calc);
	}

	public void showBACResults(BacCalculator calc) {

		// result of calculations TextView
		TextView res = (TextView) findViewById(R.id.resultTv);
		res.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

		// provide message to user and set text color
		double bacCheck = calc.getBac();
		if (bacCheck <= 0) {
			Toast.makeText(getApplicationContext(), "You're Sober",
					Toast.LENGTH_LONG).show();
			res.setTextColor(Color.GREEN);
		} else if (bacCheck >= 0 && bacCheck <= 0.08) {
			Toast.makeText(getApplicationContext(),
					"You're not above the legal limit", Toast.LENGTH_LONG)
					.show();
			res.setTextColor(Color.BLUE);
		} else if (bacCheck >= 0.08 && bacCheck <= .18) {
			Toast.makeText(getApplicationContext(), "DO NOT DRIVE!",
					Toast.LENGTH_LONG).show();
			res.setTextColor(Color.YELLOW);
		} else if (bacCheck >= .18) {
			Toast.makeText(getApplicationContext(), "SEEK MEDICAL ATTENTION!",
					Toast.LENGTH_LONG).show();
			res.setTextColor(Color.RED);
		} else
			Toast.makeText(getApplicationContext(), "Invalid Input",
					Toast.LENGTH_LONG).show();

		// set text on screen
		res.setText(calc.getBacString());
	}

	/*
	 * Clears the form of values for BAC Calculator
	 * 
	 * @param View view
	 */
	public void clearForm(View view) {
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

	/*
	 * Creates ingredient from csv string
	 * 
	 * @param String ingredient
	 * 
	 * @return Ingredient
	 */
	public Ingredient makeIngredient(String ingredient) {
		System.out.println("makeIng: " + ingredient);
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
					Log.e("Make ingredient","Could not parse double");
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
	 * 
	 * @return Drink
	 */
	public Drink makeDrink(String drink) {
		System.out.println("make drink " + drink);
		Drink tempDrink = new Drink();
		ArrayList<Ingredient> drinkIngredients = new ArrayList<Ingredient>();

		// split csv string and set values
		String[] splitArr = drink.split(",");
		tempDrink.setDrinkName(splitArr[0]);
		tempDrink.setDrinkDescription(splitArr[1]);
		System.out.println("Name: " + splitArr[0]);
		System.out.println("Desc: " + splitArr[1]);
		
		for (int i = 2; i < splitArr.length; i++) {
			for (int index = 0; index < ingredients.size(); index++) {
				try {
					// parse csv array and check if ingredient IDs match
					int ingredientID = Integer.parseInt(splitArr[i]);
					if (ingredientID == ingredients.get(index).getIngredientID()) {
						drinkIngredients.add(ingredients.get(index));
						System.out.println("args: " + ingredients.get(i).getIngredientName());
					}
				}
				catch (Exception e) {
					Log.e("makeDrink", "Failed to identify specified drink ingredient IDs");
				}
			}
		}
		
		// loop and set ingredients for drink WITH DB
//		for (int i = 2; i < splitArr.length; i++) {
//			if (i == splitArr.length - 1) {
//				tempIngredient = db.getIngredient(Integer.parseInt(splitArr[i]));
//				drinkIngredients.add(tempIngredient);
//			} else {
//				tempIngredient = db.getIngredient(Integer.parseInt(splitArr[i]));
//				drinkIngredients.add(tempIngredient);
//			}
//		}
		// set the drinks ingredient list
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
	 * parseIngredientCSV
	 * 
	 * Parses a csv file and creates appropriate Ingredient objects
	 * 
	 * @param String file_path
	 * 
	 * @return ArrayList<Ingredient>
	 * 
	 * NOT WORKING
	 */
	public ArrayList<Ingredient> parseIngredientCSV(String file_path) {
		ArrayList<Ingredient> allIngredients = new ArrayList<Ingredient>();
		String line = "";
		InputStream is = null;
		try {
			is = getClass().getClassLoader().getResourceAsStream(file_path);
		} catch (Exception e) {
			System.out.println("INPUT STREAM IS BAD");
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				Ingredient temp = makeIngredient(line);
				allIngredients.add(temp);
			}
		} catch (FileNotFoundException e) {
			System.out.println("FILE NOT FOUND");
		} catch (IOException e) {
			System.out.println("IO Exception");
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					System.out.println("FINAL CATCH");
				}
			}
		}
		return allIngredients;
	}

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
				Log.i("shake event", "Accelerometer set up for shake");

			} else {
				generateDrink(view);
			}
			shake = !shake;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// register this class as a listener for the orientation and
		// accelerometer sensors
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
}

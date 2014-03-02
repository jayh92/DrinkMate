package com.isat.drinkmate;

import java.util.ArrayList;
import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import com.isat.drinkmate.model.*;

public class RandomActivity extends Activity {
	
	private ArrayList<Drink> drinks;
	private Drink randomDrink;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_random);
		
		// get data passed by MainActivity
		Bundle d = this.getIntent().getBundleExtra("result.content");
		drinks = d.getParcelableArrayList("extraextra");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.random, menu);
		return true;
	}
	/*
	 * Generates a random Drink to be displayed
	 * 
	 * @param View view
	 */
	public void generateDrink(View view)
	{	
		// get random drink
		Random rand = new Random();
		randomDrink = drinks.get(rand.nextInt(drinks.size()));
		
		// get TextViews
		TextView drinkName = (TextView)findViewById(R.id.tvDrinkName);
		TextView description = (TextView)findViewById(R.id.tvDrinkDescription);
		TextView ingredients = (TextView)findViewById(R.id.tvDrinkIngredients);
		
		// format TextViews
		drinkName.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		description.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		ingredients.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		
		// print out random drink
		drinkName.setText("Name\n" + randomDrink.getDrinkName());
		description.setText("Description\n" + randomDrink.getDrinkDescription());
		ingredients.setText(randomDrink.getCSVIngredient());
	}
	/*
	 * Returns the array list of drinks
	 */
	public ArrayList<Drink> getDrinkList()
	{
		return drinks;
	}
}

package com.isat480.drinkmate;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class BacActivity extends Activity {

	public static final double MAN_CONSTANT = 9.0;
	public static final double 	WOMAN_CONSTANT = 7.5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bac);
		// Show the Up button in the action bar.
		setupActionBar();
		
		// get intent
		Intent intent = getIntent();	
		
		setContentView(R.layout.activity_bac);
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bac, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void calculateBac(View view)
	{
		// pull in user input
		EditText gender = (EditText)findViewById(R.id.genderEt);
		EditText weight = (EditText)findViewById(R.id.weightEt);
		EditText numDrinks = (EditText)findViewById(R.id.numDrinksEt);
		EditText hours = (EditText)findViewById(R.id.timeEt);
		
		// display input for testing purposes
		String result = gender.getText().toString();
		result += "\t" + weight.getText().toString();
		result += "\t" + numDrinks.getText().toString();
		result += "\t" + hours.getText().toString();
		
		TextView res = (TextView)findViewById(R.id.resultTv);
		res.setText("User Input: " + result);
		
		
	}
}

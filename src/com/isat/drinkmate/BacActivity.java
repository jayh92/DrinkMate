package com.isat.drinkmate;

import java.text.DecimalFormat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class BacActivity extends Activity {

	public static final double MAN_CONSTANT = .58;
	public static final double 	WOMAN_CONSTANT = .49;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bac);
		// Show the Up button in the action bar.
		setupActionBar();
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
	/*
	 * Listener for Calculate on activity_bac.xml
	 * 
	 * @param View view - default parameter
	 */
	public void calculateBac(View view)
	{
		// initialize variables
		int ounces = 0;
		double totalHours = 0;
		double bac, percent = 0, userWeight = 0;
		String userGender = "";
		
		// pull in user input
		EditText oz = (EditText)findViewById(R.id.ouncesEt);
		EditText perc = (EditText)findViewById(R.id.percentEt);
		EditText gender = (EditText)findViewById(R.id.genderEt);
		EditText weight = (EditText)findViewById(R.id.weightEt);
		EditText hours = (EditText)findViewById(R.id.timeEt);
		 
		// result of calculations TextView
		TextView res = (TextView)findViewById(R.id.resultTv);
		res.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		
		// parse user inputs
		try {
			//totalHours = Integer.parseInt(hours.getText().toString());
			totalHours = Double.parseDouble(hours.getText().toString());
			ounces = Integer.parseInt(oz.getText().toString());
			percent = Double.parseDouble(perc.getText().toString());
			userWeight = Double.parseDouble(weight.getText().toString());
		} catch (NumberFormatException e) {
			System.out.println(e);
		}
		try {
			userGender = gender.getText().toString().substring(0, 1); // gets first character
		} catch (Exception e) {
			System.out.println(e);
		}
		// calculate BAC
		bac = getBac(ounces, percent, userWeight, totalHours, userGender);
		
		// format to 3 decimal places
		DecimalFormat df = new DecimalFormat("#.###");
		bac = Double.valueOf(df.format(bac));
		
		// show response to BAC calculation
		if (bac <= 0) {
			res.setText("\tBAC: " + bac + "%\nThis is a negligible amount of alcohol.\nYou're slackin' hombre.");
		}
		else if(bac >= 0 && bac <= 0.08) {
			res.setText("BAC: " + bac + "%\nYou are not above the legal limit");
		}
		else if(bac >= 0.08 && bac <= .18) {
			res.setText("BAC: " + bac + "%\nYou are over the legal limit\nDO NOT DRIVE");
		}
		else if(bac >= .18) {
			res.setText("BAC: " + bac + "%\nYou have a dangerous level of booze in you.\nSeek medical attention.");
		}
		else
			res.setText("Invalid input");
	}
	/*
	 * Calculates the BAC of a user and returns the double value. It also checks for negative
	 * values.
	 * 
	 * @param int ounces - ounces of alcohol consumed
	 * @param double percent - alcohols percentage
	 * @param double weight - weight of user
	 * @param int hoursDrinking - time drinking
	 * @return double result
	 */
	public double getBac(int ounces, double percent, double weight, double hoursDrinking, String gender)
	{	
		double result = 0;
		
		// decipher gender
		if (gender.equalsIgnoreCase("m")) {
			result = (ounces * percent * 0.075) / (weight * MAN_CONSTANT) - (hoursDrinking * 0.015);
		}
		else if (gender.equalsIgnoreCase("f")) {
			result = (ounces * percent * 0.075) / (weight * WOMAN_CONSTANT) - (hoursDrinking * 0.015);
		}
		else {
			TextView res = (TextView)findViewById(R.id.resultTv);
			res.setText("Invalid Gender Selection");
		}
		// check for negative values
		if (result < 0)
			result = 0;
		
		return result;
	}
	/*
	 * Clears the form of values and sets hints
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
}

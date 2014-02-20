package com.isat.drinkmate;

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
		
		// get message from intent
		Intent intent = getIntent();
		//String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		
		//TextView tv = new TextView(this);
		//tv.setTextSize(40);
		//tv.setText(message);		
		
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
		double bac;
		
		// pull in user input
		EditText oz = (EditText)findViewById(R.id.ouncesEt);
		EditText perc = (EditText)findViewById(R.id.percentEt);
		EditText gender = (EditText)findViewById(R.id.genderEt);
		EditText weight = (EditText)findViewById(R.id.weightEt);
		EditText hours = (EditText)findViewById(R.id.timeEt);
		
		TextView res = (TextView)findViewById(R.id.resultTv);
		
		int totalHours = Integer.parseInt(hours.getText().toString());
		int ounces = Integer.parseInt(oz.getText().toString());
		double percent = Double.parseDouble(perc.getText().toString());
		double userWeight = Double.parseDouble(weight.getText().toString());
		String userGender = gender.getText().toString().substring(0, 1);
		
		bac = getBac(ounces, percent, userWeight, totalHours);
		
		if (bac <= 0) {
			res.setText("BAC: " + bac + "%\nThis is a negligible amount of alcohol.\nYou're slackin' hombre.");
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
	public double getBac(int ounces, double percent, double weight, int hoursDrinking)
	{	
		double result = (ounces * percent * 0.075 / weight) - (hoursDrinking * 0.015);
		if (result < 0)
			result = 0;
		return result;
	}
}

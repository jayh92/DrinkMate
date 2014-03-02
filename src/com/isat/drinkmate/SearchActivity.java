package com.isat.drinkmate;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class SearchActivity extends Activity {
	private ArrayList<String> ingredients;
	MultiSelectionSpinner spinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		Intent intent = getIntent();
		ingredients = intent.getStringArrayListExtra("ingredients");
		spinner = (MultiSelectionSpinner) findViewById(R.id.mySpinner1);
		ingredients.add(0, "");
		spinner.setItems(ingredients);
	}

	public void onClick(View v) {
		String s = spinner.getSelectedItemsAsString();
		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

}

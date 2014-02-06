package com.isat480.drinkmate;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	Button btnTest;
	TextView tvTest;
	ImageView imgView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		addListenerOnButton();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void addListenerOnButton() {
		btnTest = (Button) findViewById(R.id.btnButton);
		btnTest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tvTest = (TextView) findViewById(R.id.strTest);
				tvTest.setText("   Welcome to DrinkMate!\nWe're still working on things");
				imgView = (ImageView) findViewById(R.id.ivWrench);
				imgView.setVisibility(1);
			}
			
		});
	}
}

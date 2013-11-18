package com.example.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Main extends Activity {
	Button exitButton;
	Button chartButton;
	Button statsButton;
	Button currentStateButton;
	ImageView iView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		exitButton = (Button) findViewById(R.id.ButtonExit);
		chartButton = (Button) findViewById(R.id.ButtonCharts);
		statsButton = (Button) findViewById(R.id.ButtonStats);
		currentStateButton = (Button) findViewById(R.id.ButtonCurrentState);
		iView = (ImageView) findViewById(R.id.imageViewLogo);
		
		Bundle extras = getIntent().getExtras();
		String un = extras.getString("un");
		
		if(un.contentEquals("admin")){
			iView.setClickable(true);
			iView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent adminPanel = new Intent(
							"com.example.login.AdminPanel");
					startActivity(adminPanel);	
				}
			});
		}
		else {
			iView.setClickable(false);
		}
		exitButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		chartButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent chartActivityIntent = new Intent("com.example.login.MainMenu");
				startActivity(chartActivityIntent);
			}
		});
		currentStateButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent currentStateIntent = new Intent ("com.example.login.CurrentState");
				startActivity(currentStateIntent);
			}
		});
	}
	
	
}

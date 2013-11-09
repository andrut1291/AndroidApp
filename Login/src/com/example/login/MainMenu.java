package com.example.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainMenu extends Activity {
	Button adminPanel;
	Button chart1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		adminPanel = (Button) findViewById(R.id.buttonAdminPanel);
		chart1 = (Button) findViewById(R.id.buttonChart1);
		Bundle extras = getIntent().getExtras();
		String un = extras.getString("un");
		if (un.contentEquals(getString(R.string.admin))) {
			adminPanel.setVisibility(View.VISIBLE);
			adminPanel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent adminPanel = new Intent(
							"com.example.login.AdminPanel");
					startActivity(adminPanel);
				}
			});
		} else{
			adminPanel.setVisibility(View.INVISIBLE);
			adminPanel.setClickable(false);
		}
		chart1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent chart = new Intent("com.example.login.ChartFromButton2");
				startActivity(chart);
			}
		});
	}

}

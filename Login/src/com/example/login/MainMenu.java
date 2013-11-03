package com.example.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends Activity {
	Button adminPanel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		adminPanel=(Button) findViewById(R.id.buttonAdminPanel);
		adminPanel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent adminPanel = new Intent(
						"com.example.login.AdminPanel");
				startActivity(adminPanel);
			}
		});

	}

}

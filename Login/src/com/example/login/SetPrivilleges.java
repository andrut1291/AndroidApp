package com.example.login;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ToggleButton;
public class SetPrivilleges extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		String un;
		if(extras!=null)
			un = extras.getString("username");
		setContentView(R.layout.user_info);
		TextView userName;
		ToggleButton onOff;
		Button exit;
		Button delateAccount;
		
	}

}

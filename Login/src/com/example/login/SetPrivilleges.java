package com.example.login;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
public class SetPrivilleges extends Activity {
	TextView userName;
	ToggleButton onOff;
	Button exit;
	Button delateAccount;
	ProgressBar pb;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		final String un;
		final String accepted;
		//Wymaga do³o¿enia obs³ugi kiedy wyst¹pi b³¹d przekazania stringów
		accepted = extras.getString("accepted");
		un = extras.getString("username");
		pb=(ProgressBar)findViewById(R.id.progressBarpb);
		pb.setVisibility(View.GONE);
		exit = (Button) findViewById(R.id.buttonExit);
		userName = (TextView) findViewById(R.id.textViewUserName);
		userName.setText((CharSequence)un);
		delateAccount = (Button)findViewById(R.id.buttonDelateAccount);
		setContentView(R.layout.user_info);
		onOff = (ToggleButton)findViewById(R.id.toggleButtonSetPrivilleges);
		if(accepted.contentEquals("Nie"))
			onOff.setActivated(false);
		else
			onOff.setActivated(true);
		exit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		delateAccount.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pb.setVisibility(View.VISIBLE);
				new MyAsyncTaskDelate().execute(un,accepted);
				
			}
		});
		
		
	}
	private class MyAsyncTaskDelate extends AsyncTask<String,Integer,Void>{
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			pb.setVisibility(View.GONE);
			Toast.makeText(getApplicationContext(), "Konto zosta³o usuniête!", Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			// TODO Auto-generated method stub
			pb.setProgress(progress[0]);
		}
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				postData(params[0],params[1]);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		private void postData(String un, String accepted) throws NoSuchAlgorithmException {
			// TODO Auto-generated method stub
			// Create a new HttpClient and Post Header
						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httppost = new HttpPost("http://mandrusz.pusku.com/include/delate.php");
			 
						try {
							// Add your data
							ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
							nameValuePairs.add(new BasicNameValuePair("accepted",accepted));
							nameValuePairs.add(new BasicNameValuePair("un", un));
							httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			 
							// Execute HTTP Post Request
							httpclient.execute(httppost);

			 
						} catch (ClientProtocolException e) {
							// TODO Auto-generated catch block
						} catch (IOException e) {
							// TODO Auto-generated catch block
						}
		}
		
	}
	}



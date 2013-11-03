package com.example.login;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.*;
import android.widget.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class MainActivity extends Activity {
	TextView Register;
	Button LoginButton;
	EditText email;
	EditText password;
	String pass;
	String un;
	String htmlResponse;
	ProgressBar pb;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		LoginButton = (Button) findViewById(R.id.buttonLogin);
		email = (EditText) findViewById(R.id.editTextEmail);
		password = (EditText) findViewById(R.id.editTextPassword);
		Register = (TextView) findViewById(R.id.textViewRegister);
		pb = (ProgressBar) findViewById(R.id.progressBarLogin);
		pb.setVisibility(View.GONE);
		Register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent registerActivity = new Intent(
						"com.example.login.Register");
				startActivity(registerActivity);
			}
		});
		LoginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pass = password.getText().toString();
				un = email.getText().toString();
				if (password.getText().toString().length() < 1
						|| email.getText().toString().length() < 1) {
					// out of range
					Toast.makeText(MainActivity.this, "Nie wpisano danych!",
							Toast.LENGTH_LONG).show();
				} else {
					if (pass.contentEquals("admin")
							&& un.contentEquals("admin")) {
						Intent MainMenuActivity = new Intent(
								"com.example.login.MainMenu");
						startActivity(MainMenuActivity);
						finish();
					} else {
						pb.setVisibility(View.VISIBLE);
						MyAsyncTaskLogin Connection = new MyAsyncTaskLogin();
						try {
							htmlResponse = Connection.execute(pass, un).get()
									.toString();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if (htmlResponse.contentEquals("SUCCESS") == true) {
							Intent MainMenuActivity = new Intent(
									"com.example.login.MainMenu");
							startActivity(MainMenuActivity);
							finish();
						} else if (htmlResponse
								.contentEquals("FAILED_BAD_PASSWORD") == true) {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									MainActivity.this);
							builder.setMessage("B³¹d logowania, b³êdne has³o!")
									.setCancelable(false)
									.setPositiveButton(
											"OK",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {
													// do things
												}
											});
							AlertDialog alert = builder.create();
							alert.show();
						} else if (htmlResponse
								.contentEquals("FAILED_BAD_USER") == true) {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									MainActivity.this);
							builder.setMessage(
									"B³¹d logowania, brak u¿ytkownika w bazie!")
									.setCancelable(false)
									.setPositiveButton(
											"OK",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {
													// do things
												}
											});
							AlertDialog alert = builder.create();
							alert.show();
						}
					}
				}
			}
		});

	}

	private class MyAsyncTaskLogin extends AsyncTask<String,Integer, String> {

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			pb.setVisibility(View.GONE);
			//Toast.makeText(getApplicationContext(), "Logowanie!", Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			// TODO Auto-generated method stub
			pb.setProgress(progress[0]);
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return postData(params[0], params[1]);
		}

		private String postData(String param1, String param2) {
			// TODO Auto-generated method stub
			String htmlResp = "";
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://mandrusz.pusku.com/include/check.php");

			try {
				// Add your data
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				Hash HashPass = new Hash(pass);
				nameValuePairs.add(new BasicNameValuePair("pass",HashPass.getHash()));
				nameValuePairs.add(new BasicNameValuePair("un", un));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				htmlResp = EntityUtils.toString(response.getEntity());
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
			return htmlResp;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

package com.example.login;

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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Register extends Activity {
	TextView Login;
	EditText Password;
	EditText RepeatPass;
	EditText Username;
	Button Register;
	ProgressBar pb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		Login = (TextView) findViewById(R.id.textViewRegisterLogin);
		Password = (EditText) findViewById(R.id.editTextRegisterPassword);
		RepeatPass = (EditText) findViewById(R.id.editTextRepeatPassword);
		Username = (EditText) findViewById(R.id.editTextRegisterEmail);
		Register = (Button) findViewById(R.id.buttonRegisterRegister);
		pb = (ProgressBar) findViewById(R.id.progressBarRegister);
		pb.setVisibility(View.GONE);
		Register.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String un = Username.getText().toString();
				String pass = Password.getText().toString();
				String repeatPass = RepeatPass.getText().toString();
				if (Password.getText().toString().length() < 1
						|| Username.getText().toString().length() < 1
						|| RepeatPass.getText().toString().length() < 1) {
					// out of range
					Toast.makeText(Register.this, "Nie wpisano danych!",
							Toast.LENGTH_LONG).show();
				} else {
					if (repeatPass.contentEquals(pass) == true) {
						pb.setVisibility(View.VISIBLE);
						new MyAsyncTaskRegister().execute(pass, un);
					} else {
						Toast.makeText(Register.this,
								"Wpisane has³a ró¿ni¹ siê!", Toast.LENGTH_LONG)
								.show();
					}
				}

			}

		});
		Login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	private class MyAsyncTaskRegister extends AsyncTask<String, Integer, Void> {
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			pb.setVisibility(View.GONE);
			Toast.makeText(getApplicationContext(),
					"Dane do rejestracji zosta³y wys³ane!", Toast.LENGTH_LONG)
					.show();
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
				postData(params[0], params[1]);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		private void postData(String pass, String un)
				throws NoSuchAlgorithmException {
			// TODO Auto-generated method stub
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://mandrusz.pusku.com/include/insert.php");

			try {
				// Add your data
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				Hash HashPass = new Hash(pass);
				nameValuePairs.add(new BasicNameValuePair("pass", HashPass
						.getHash()));
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

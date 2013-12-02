package com.example.login;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CurrentState extends Activity {

	Button checkButton;
	Spinner inputSpinner;
	ImageView iView;
	TextView statusTextView;
	ProgressBar pb;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.current_state);
		checkButton = (Button) findViewById(R.id.buttonCheck);
		inputSpinner = (Spinner) findViewById(R.id.spinner1);
		iView = (ImageView) findViewById(R.id.imageViewLogo);
		statusTextView = (TextView) findViewById(R.id.textViewStatus);
		pb = (ProgressBar) findViewById(R.id.progressBarStats);
		pb.setVisibility(View.GONE);
		
		statusTextView.setText("");
		String[] inputs = { "Wejœcie cyfrowe nr. 1 (0-12V DC)",
				"Wejœcie cyfrowe nr. 2 (0-12V DC)",
				"Wejœcie cyfrowe nr. 3 (0-12V DC)",
				"Wejœcie cyfrowe nr. 4 (0-24V DC)" };
		ArrayAdapter<String> inputAdapter = new ArrayAdapter<String>(
				CurrentState.this, android.R.layout.simple_spinner_item, inputs);
		inputSpinner.setAdapter(inputAdapter);
		
		checkButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pb.setVisibility(View.VISIBLE);
				GetData checkState = new GetData();
				checkState.execute(new String[] {String.valueOf(inputSpinner.getSelectedItemPosition())});
				try {
					String state = checkState.get().toString();
					if(state.contentEquals("ERROR")){
						Toast.makeText(getApplicationContext(), "Urz¹dzenie obecnie jest OFFLINE !", Toast.LENGTH_LONG).show();
					}
					else if(state.contentEquals("0")==false){
						statusTextView.setText("Stan na wybranym wyjœciu wynosi: "+state+"[V]");
						iView.setImageResource(R.drawable.lamp1);
					}
					else{
						statusTextView.setText("Stan na wybranym wyjœciu wynosi: "+state+"[V]");
						iView.setImageResource(R.drawable.lamp2);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public class GetData extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String answer="";
			try {
				answer = postData();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return answer;
		}
		protected void onProgressUpdate(Integer... progress) {
			// TODO Auto-generated method stub
			pb.setProgress(progress[0]);
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			pb.setVisibility(View.GONE);
		}
		
		private String postData()
				throws NoSuchAlgorithmException {
			// TODO Auto-generated method stub
			// Create a new HttpClient and Post Header
			String answer = "ERROR";
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpIsActive = new HttpPost(
					"http://mandrusz.pusku.com/include/check_online.php");

			try {
				// Add your data
				// Execute HTTP Post Request
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				//nameValuePairs.add(new BasicNameValuePair("input", );
				//httpIsActive.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse answerResponse = httpclient.execute(httpIsActive);
				String response = EntityUtils.toString(answerResponse.getEntity());
				if(response.contentEquals("1")){
					HttpPost httpCurrentState = new HttpPost(
							"http://mandrusz.pusku.com/include/current_state.php");
					HttpResponse stateResponse = httpclient.execute(httpCurrentState);
					answer = EntityUtils.toString(stateResponse.getEntity());
					return answer;
				}
				else 
					return answer;
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
			return answer;
		}
	}
}



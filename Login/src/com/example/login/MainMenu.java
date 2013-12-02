package com.example.login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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

import android.R.anim;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.view.DragEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainMenu extends Activity {
	DatePicker datePicker;
	Button chart1;
	Spinner inputSpinner;
	SeekBar sBar;
	TimePicker timePicker;
	TextView seekBarValue;
	int numberOfMinutes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		// //////Ustawienia kalendaarza
		final Calendar cal = Calendar.getInstance();
		// /////////
		chart1 = (Button) findViewById(R.id.buttonChart1);
		inputSpinner = (Spinner) findViewById(R.id.spinnerChooseInput);
		timePicker = (TimePicker) findViewById(R.id.timePicker1);
		datePicker = (DatePicker) findViewById(R.id.datePicker1);
		datePicker.setMaxDate(cal.getTimeInMillis());
		timePicker.setIs24HourView(true);
		timePicker
				.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

					@Override
					public void onTimeChanged(TimePicker view, int hourOfDay,
							int minute) {
						// TODO Auto-generated method stub
						Time now = new Time();
						now.setToNow();
						if ((hourOfDay > now.hour) && (now.monthDay == datePicker.getDayOfMonth())) {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									MainMenu.this);
							builder.setMessage("Wpisano niepoprawn¹ godzinê !")
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
							timePicker.setCurrentHour(now.hour);
							timePicker.setCurrentMinute(now.minute);
						} else if ((minute > now.minute && hourOfDay == now.hour)
								&& datePicker.getDayOfMonth() == now.monthDay) {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									MainMenu.this);
							builder.setMessage(
									"Wpisano niepoprawn¹ godzinê!")
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
							timePicker.setCurrentHour(now.hour);
							timePicker.setCurrentMinute(now.minute);
						}
					}
				});
		seekBarValue = (TextView) findViewById(R.id.textViewSeekBarValue);
		sBar = (SeekBar) findViewById(R.id.seekBarTimeRange);
		sBar.setMax(15);
		sBar.setProgress(1);
		sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				numberOfMinutes = sBar.getProgress();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				seekBarValue.setText("" + progress + " min");

			}
		});

		String[] inputs = { "Wejœcie cyfrowe nr. 1 (0-12V DC)",
				"Wejœcie cyfrowe nr. 2 (0-12V DC)",
				"Wejœcie cyfrowe nr. 3 (0-12V DC)",
				"Wejœcie cyfrowe nr. 4 (0-24V DC)" };
		ArrayAdapter<String> inputAdapter = new ArrayAdapter<String>(
				MainMenu.this, android.R.layout.simple_spinner_item, inputs);
		inputSpinner.setAdapter(inputAdapter);
		chart1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
						timePicker.getCurrentHour()+1,timePicker.getCurrentMinute());
				Bundle extras = new Bundle();
				extras.putCharSequence("min", ""+(cal.getTimeInMillis()-3600000)/1000);
				long endTimestamp =(cal.getTimeInMillis()-3600000)/1000 + sBar.getProgress()*60;
				extras.putCharSequence("max", ""+endTimestamp);
				extras.putCharSequence("input",""+inputSpinner.getSelectedItemPosition());
				CheckData validateCheckData = new CheckData();
				validateCheckData.execute(extras.getCharSequence("min").toString(),extras.getCharSequence("max").toString());
				try {
					if(validateCheckData.get().toString().contains("0")==true)
					{
						Toast.makeText(getApplicationContext(), "Brak danych na serwerze w zadanym oknie czasowym", Toast.LENGTH_LONG).show();
					}
					else {
						Intent chart = new Intent("com.example.login.ChartFromButton2");
						chart.putExtras(extras);
						startActivity(chart);
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
	private class CheckData extends AsyncTask<String,Integer, String> {

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			//Toast.makeText(getApplicationContext(), "Logowanie!", Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			// TODO Auto-generated method stub
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
					"http://mandrusz.pusku.com/include/check_online_min_max.php");

			try {
				// Add your data
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("min",param1));
				nameValuePairs.add(new BasicNameValuePair("max", param2));
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

}

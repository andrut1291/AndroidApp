package com.example.login;

import java.util.Calendar;

import android.R.anim;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
				extras.putCharSequence("min", ""+cal.getTimeInMillis()/1000);
				long endTimestamp =cal.getTimeInMillis()/1000 + sBar.getProgress()*60;
				extras.putCharSequence("max", ""+endTimestamp);
				extras.putCharSequence("input",""+inputSpinner.getSelectedItemPosition());
				Intent chart = new Intent("com.example.login.ChartFromButton2");
				chart.putExtras(extras);
				startActivity(chart);
			}
		});
	}

}

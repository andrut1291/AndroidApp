package com.example.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidplot.xy.*;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * 
 * This example is based on:
 * - multitouch example, by David Buezas (david.buezas at gmail.com) and Michael 
 * 	from http://androidplot.com/wiki/A_Simple_XYPlot_with_multi-touch_zooming_and_scrolling
 * - AndroidPlot quickstart example http://androidplot.com/wiki/Quickstart
 * No license was given with this samples, but I assume that use it for free on BSD-like license ;-)
 * 
 * @author Marcin Lepicki (marcin.lepicki at flex-it.pl)
 *
 */
public class ChartFromButton1 extends Activity 
{
	private final static String url = "http://mandrusz.pusku.com/include/get_data.php";
	private String jsonResult;
	ProgressBar pb;
	public List<Number> time = new ArrayList<Number>();
	public List<Number> data = new ArrayList<Number>();

	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_button1);
		pb = (ProgressBar) findViewById(R.id.progressBarChart);
		pb.setVisibility(View.VISIBLE);
		JsonReadTask task = new JsonReadTask();
		task.execute(new String[] { url });
		try {
			fillArrays(task.get(), time, data);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MultitouchPlot multitouchPlot = (MultitouchPlot) findViewById(R.id.stepChartExamplePlot);
        // Create two arrays of y-values to plot:
        Number[] series1Numbers = {1, 8, 5, 2, 7, 4};
		Number[] series2Time = {4000001, 4000002, 4000003, 4000004, 4000005, 4000006};
        // Turn the above arrays into XYSeries:
		XYSeries series1 = new SimpleXYSeries(Arrays.asList(series2Time),Arrays.asList(series1Numbers),
                "Series2");                          // Set the display title of the series
		// Same as above, for series2
        // Create a formatter to use for drawing a series using LineAndPointRenderer:
        LineAndPointFormatter series1Format = new LineAndPointFormatter(
                Color.rgb(0, 200, 0),                   // line color
                Color.rgb(0, 100, 0),                   // point color
                Color.rgb(150, 190, 150),null);              // fill color (optional)
        multitouchPlot.addSeries(series1, series1Format);
    }
	private class JsonReadTask extends AsyncTask<String, Integer, String> {

		protected void onProgressUpdate(Integer... progress) {
			// TODO Auto-generated method stub
			pb.setProgress(progress[0]);
		}

		protected String doInBackground(String... params) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(params[0]);
			try {
				HttpResponse response = httpclient.execute(httppost);
				jsonResult = inputStreamToString(
						response.getEntity().getContent()).toString();
			}

			catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return jsonResult;
		}

		private StringBuilder inputStreamToString(InputStream is) {
			String rLine = "";
			StringBuilder answer = new StringBuilder();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			try {
				while ((rLine = rd.readLine()) != null) {
					answer.append(rLine);
				}
			}

			catch (IOException e) {
				// e.printStackTrace();
				Toast.makeText(getApplicationContext(),
						"Error..." + e.toString(), Toast.LENGTH_LONG).show();
			}
			return answer;
		}

		@Override
		protected void onPostExecute(String result) {
			pb.setVisibility(View.GONE);
		}
	}
	public void fillArrays(String input , List<Number> timestamp , List<Number> state){
		List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
		userList.clear();
		try {
			JSONObject jsonResponse = new JSONObject(input);
			JSONArray jsonTime = jsonResponse.optJSONArray("time");
			JSONArray jsonState = jsonResponse.optJSONArray("state");
			for (int i = 0; i < jsonTime.length(); i++) {
				JSONObject jsonChildNode = jsonTime.getJSONObject(i);
				time.add(jsonChildNode.optInt("Time"));
			}
			for (int i = 0; i < jsonState.length(); i++) {
				JSONObject jsonChildNode = jsonState.getJSONObject(i);
				String value = jsonChildNode.optString("State");
				if(value.contentEquals("0")){
					state.add(0);
				}
				else if(value.contentEquals("12"))
					state.add(12);
				else{
					Toast.makeText(getApplicationContext(), "Z³y format danych na serwerze",
							Toast.LENGTH_SHORT).show();
				}
			}
		} catch (JSONException e) {
			Toast.makeText(getApplicationContext(), "Error" + e.toString(),
					Toast.LENGTH_SHORT).show();
		}
	}
}
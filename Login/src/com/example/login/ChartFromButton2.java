/*
 * Copyright 2012 AndroidPlot.com
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.example.login;

import android.app.Activity;
import android.graphics.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.androidplot.xy.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChartFromButton2 extends Activity {
	private final static String url = "http://mandrusz.pusku.com/include/get.php";
	private String jsonResult;
	ProgressBar pb;
	String min;
	String max;
	String input;
	public List<Number> time = new ArrayList<Number>();
	public List<Number> data = new ArrayList<Number>();

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.chart_button1);
		Bundle extras = getIntent().getExtras();
		min = extras.getString("min");
		max = extras.getString("max");
		input = extras.getString("input");
		pb = (ProgressBar) findViewById(R.id.progressBarChart);
		pb.setVisibility(View.VISIBLE);
		MultitouchPlot mySimpleXYPlot = (MultitouchPlot) findViewById(R.id.stepChartExamplePlot);
		JsonReadTask task = new JsonReadTask();
		task.execute(new String[] { url, min, max, input });
		try {
			fillArrays(task.get(), time, data);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Obsluga braku danych
				XYSeries series2 = new SimpleXYSeries(time, data,
						"Wartość napięcia");
				mySimpleXYPlot.getGraphWidget().getGridBackgroundPaint()
						.setColor(Color.WHITE);
				mySimpleXYPlot.getGraphWidget().getDomainGridLinePaint()
						.setColor(Color.BLACK);
				// mySimpleXYPlot.getGraphWidget().getDomainGridLinePaint().setPathEffect(new
				// DashPathEffect(new float[] { 1, 1 }, 1));
				mySimpleXYPlot.getGraphWidget().getRangeGridLinePaint()
						.setColor(Color.BLACK);
				// mySimpleXYPlot.getGraphWidget().getRangeGridLinePaint().setPathEffect(new
				// DashPathEffect(new float[] { 1, 1 }, 1));
				mySimpleXYPlot.getGraphWidget().getDomainOriginLinePaint()
						.setColor(Color.BLACK);
				mySimpleXYPlot.getGraphWidget().getRangeOriginLinePaint()
						.setColor(Color.BLACK);
				mySimpleXYPlot.getGraphWidget().setMarginRight(5);
				Paint lineFill = new Paint();
				lineFill.setAlpha(200);
				lineFill.setShader(new LinearGradient(0, 0, 0, 250, Color.WHITE,
						Color.BLUE, Shader.TileMode.MIRROR));

				LineAndPointFormatter Format = new LineAndPointFormatter(Color.rgb(
						0, 200, 0), // line color
						Color.rgb(0, 100, 0), // point color
						Color.rgb(150, 190, 150), null); // fill color (optional)

				StepFormatter stepFormatter = new StepFormatter(Color.rgb(0, 0, 0),
						Color.BLUE);
				stepFormatter.getLinePaint().setStrokeWidth(1);
				stepFormatter.getLinePaint().setAntiAlias(false);
				stepFormatter.setFillPaint(lineFill);

				mySimpleXYPlot.addSeries(series2, stepFormatter);
				mySimpleXYPlot.getGraphWidget().setMarginTop(10);
				mySimpleXYPlot.setDomainValueFormat(new DecimalFormat("0"));
				mySimpleXYPlot.setRangeBoundaries(1, 3, BoundaryMode.FIXED);
				mySimpleXYPlot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 1);
				mySimpleXYPlot.setTicksPerRangeLabel(1);
				mySimpleXYPlot.setDomainStep(XYStepMode.SUBDIVIDE, time.size());
				//mySimpleXYPlot.setTicksPerDomainLabel(time.size());
				/*
				 * mySimpleXYPlot.setDomainValueFormat(new Format() {
				 * 
				 * // create a simple date format that draws on the year portion of
				 * our timestamp. // see
				 * http://download.oracle.com/javase/1.4.2/docs
				 * /api/java/text/SimpleDateFormat.html // for a full description of
				 * SimpleDateFormat. private SimpleDateFormat dateFormat = new
				 * SimpleDateFormat("MM-dd HH:mm");
				 * 
				 * @Override public StringBuffer format(Object obj, StringBuffer
				 * toAppendTo, FieldPosition pos) {
				 * 
				 * // because our timestamps are in seconds and SimpleDateFormat
				 * expects milliseconds // we multiply our timestamp by 1000: long
				 * timestamp = ((Number) obj).longValue() * 1000; Date date = new
				 * Date(timestamp); return dateFormat.format(date, toAppendTo, pos);
				 * }
				 * 
				 * @Override public Object parseObject(String source, ParsePosition
				 * pos) { return null;
				 * 
				 * } });
				 */
				mySimpleXYPlot.setRangeValueFormat(new Format() {
					@Override
					public StringBuffer format(Object obj, StringBuffer toAppendTo,
							FieldPosition pos) {
						Number num = (Number) obj;
						switch (num.intValue()) {
						case 1:
							toAppendTo.append("0 [V]");
							break;
						case 2:
							toAppendTo.append("12 [V]");
							break;
						case 3:
							toAppendTo.append("24 [V]");
							break;
						default:
							toAppendTo.append("Unknown");
							break;
						}
						return toAppendTo;
					}

					@Override
					public Object parseObject(String source, ParsePosition pos) {
						return null;
					}
				});
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
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("min", params[1]));
				nameValuePairs.add(new BasicNameValuePair("max", params[2]));
				nameValuePairs.add(new BasicNameValuePair("input", params[3]));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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
	}// end async task

	public void fillArrays(String input, List<Number> timestamp,
			List<Number> state) {
		List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
		userList.clear();
		try {
			JSONObject jsonResponse = new JSONObject(input);
			JSONArray jsonTime = jsonResponse.optJSONArray("time");
			JSONArray jsonState = jsonResponse.optJSONArray("state");
			for (int i = 0; i < jsonTime.length(); i++) {
				JSONObject jsonChildNode = jsonTime.getJSONObject(i);
				time.add(jsonChildNode.optInt("Time") - Long.valueOf(min));
			}
			for (int i = 0; i < jsonState.length(); i++) {
				JSONObject jsonChildNode = jsonState.getJSONObject(i);
				String value = jsonChildNode.optString("State");
				if (value.contentEquals("0")) {
					state.add(1);
				} else if (value.contentEquals("12"))
					state.add(2);
				else {
					Toast.makeText(getApplicationContext(),
							"Zły format danych na serwerze", Toast.LENGTH_SHORT)
							.show();
				}
			}
		} catch (JSONException e) {
			Toast.makeText(getApplicationContext(), "Error" + e.toString(),
					Toast.LENGTH_SHORT).show();
		}
	}
}
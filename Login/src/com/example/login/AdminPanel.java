package com.example.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class AdminPanel extends Activity {

	private String jsonResult;
	private String url = "http://mandrusz.pusku.com/include/get_users.php";
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_panel);
		listView = (ListView) findViewById(R.id.listViewUsers);
		accessWebService();
		// React to user clicks on item
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		     public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
		                             long id) {

		         // We know the View is a TextView so we can cast it
		         //TextView clickedView = (TextView) view;
		    	 HashMap<String,String> newHash = new HashMap();
		    	 newHash=(HashMap<String, String>) listView.getItemAtPosition(position);
		         Toast.makeText(AdminPanel.this, "Item with id ["+id+"] - Position ["+position+"] "+newHash.get("line1").toString(), Toast.LENGTH_SHORT).show();

		     }
		});
		
	}

	// Async Task to access the web
	private class JsonReadTask extends AsyncTask<String, Void, String> {
		@Override
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
			return null;
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
			ListDrwaer();
		}
	}// end async task

	public void accessWebService() {
		JsonReadTask task = new JsonReadTask();
		// passes values for the urls string array
		task.execute(new String[] { url });
	}

	// build hash set for list view
	public void ListDrwaer() {
		List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("users");

			for (int i = 0; i < jsonMainNode.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				String name = jsonChildNode.optString("User");
				String number = jsonChildNode.optString("Accepted");
				if (number.contentEquals("0"))
					number = "Nie";
				else
					number = "Tak";
				String outPut = "Zaakceptowano: " + number;
				userList.add(createEmployee("line1", name,"line2",outPut));
				SimpleAdapter simpleAdapter = new SimpleAdapter(this, userList,
						android.R.layout.simple_list_item_2, new String[] { "line1","line2" },
						new int[] { android.R.id.text1,android.R.id.text2 });
				listView.setAdapter(simpleAdapter);
			}
		} catch (JSONException e) {
			Toast.makeText(getApplicationContext(), "Error" + e.toString(),
					Toast.LENGTH_SHORT).show();
		}
	}

	private HashMap<String, String> createEmployee(String field1,String value1,String field2,String value2) {
		HashMap<String, String> User = new HashMap<String, String>();
		User.put(field1, value1);
		User.put(field2, value2);
		return User;
	}
	

}
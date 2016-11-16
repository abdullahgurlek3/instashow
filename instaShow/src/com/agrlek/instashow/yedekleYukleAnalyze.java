package com.agrlek.instashow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class yedekleYukleAnalyze extends ActionBarActivity implements OnClickListener {

	protected static LinkedHashMap<String, JSONObject> unFollowedMe;
	protected static LinkedHashMap<String, JSONObject> newFollowedMe;
	protected static LinkedHashMap<String, JSONObject> myUnfollows;
	protected static LinkedHashMap<String, JSONObject> newMyFollows;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		String fileName = extras.getString("fileName");
		Log.d("main", fileName);

		setContentView(R.layout.yedekleyukleanalyze);
		setTitle(getResources().getString(R.string.restore));

		final Button unFollowedMeButton = (Button) findViewById(R.id.unfollowedMeButton);
		final Button myUnfollowedButton = (Button) findViewById(R.id.myUnfollowedButton);
		final Button newMyFollowedButton = (Button) findViewById(R.id.newMyFollowedButton);
		final Button newFollowedMeButton = (Button) findViewById(R.id.newFollowedMeButton);

		String fullFileName = getCacheDir() + "/instaFame/" + fileName;

		Log.d("main", fullFileName);

		String readText = "";

		try {
			FileReader fr = new FileReader(new File(fullFileName));
			BufferedReader br = new BufferedReader(fr);

			String c;
			while ((c = br.readLine()) != null) {
				readText += c;
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		new AsyncTask<String, Void, ArrayList>() {
			ProgressDialog pd;

			@Override
			protected void onPreExecute() {

				pd = new ProgressDialog(yedekleYukleAnalyze.this);
				pd.setMessage(getResources().getString(R.string.calculating));
				pd.show();
			};

			@Override
			protected void onPostExecute(ArrayList result) {

				// takibi bırakanlar
				unFollowedMeButton.setText(getResources().getString(
						R.string.unfollowedMe)
						+ " (" + unFollowedMe.size() + ")");
				// takibi bıraktıklarım
				myUnfollowedButton.setText(getResources().getString(
						R.string.myUnfollowed)
						+ " (" + myUnfollows.size() + ")");

				// yeni takip edenler
				newFollowedMeButton.setText(getResources().getString(
						R.string.newFollowedMe)
						+ " (" + newFollowedMe.size() + ")");

				// yeni takip ettiklerim
				newMyFollowedButton.setText(getResources().getString(
						R.string.newMyFollowed)
						+ " (" + newMyFollows.size() + ")");

				unFollowedMeButton.setOnClickListener(yedekleYukleAnalyze.this);
				myUnfollowedButton.setOnClickListener(yedekleYukleAnalyze.this);
				newFollowedMeButton
						.setOnClickListener(yedekleYukleAnalyze.this);
				newMyFollowedButton
						.setOnClickListener(yedekleYukleAnalyze.this);

				pd.cancel();
			};

			@Override
			protected ArrayList doInBackground(String... readText) {
				// TODO Auto-generated method stub

				ArrayList r = new ArrayList();

				JSONObject js;
				try {
					js = new JSONObject(readText[0]);

					JSONArray followedAr = (JSONArray) js.get("followed");
					JSONArray followsAr = (JSONArray) js.get("follows");

					yedekleYukleAnalyze.unFollowedMe = calculateUnFollowedMe(followedAr);
					yedekleYukleAnalyze.newFollowedMe = calculateNewFollowMe(followedAr);

					yedekleYukleAnalyze.myUnfollows = calculateMyUnfollows(followsAr);
					yedekleYukleAnalyze.newMyFollows = calculateNewMyfollows(followsAr);

					Log.d("json", "unFollowedMe  : " + unFollowedMe.toString());
					Log.d("json", "newFollowedMe : " + newFollowedMe.toString());
					Log.d("json", "myUnfollows : " + myUnfollows.toString());
					Log.d("json", "newMyFollows : " + newMyFollows.toString());

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return r;
			}
		}.execute(readText);

		Log.d("main", "readText: " + readText);
	}

	private LinkedHashMap<String, JSONObject> calculateNewMyfollows(
			JSONArray followsAr) {
		LinkedHashMap<String, JSONObject> r = new LinkedHashMap<String, JSONObject>();
		Iterator<Entry<String, JSONObject>> it = MainActivity.followsAr
				.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, JSONObject> user = it.next();
			String username = user.getKey();
			if (userexists(followsAr, username)) {
			} else {
				r.put(username, user.getValue());
			}
		}
		return r;
	}

	private LinkedHashMap<String, JSONObject> calculateMyUnfollows(
			JSONArray followsAr) {

		LinkedHashMap<String, JSONObject> r = new LinkedHashMap<String, JSONObject>();

		for (int i = 0; i < followsAr.length(); i++) {
			try {
				JSONObject user = (JSONObject) followsAr.get(i);
				String username = user.getString("key");
				if (MainActivity.followsAr.containsKey(username)) {

				} else {
					r.put(username, user.getJSONObject("value"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return r;
	}

	private LinkedHashMap<String, JSONObject> calculateNewFollowMe(
			JSONArray followedAr) {
		LinkedHashMap<String, JSONObject> r = new LinkedHashMap<String, JSONObject>();
		Iterator<Entry<String, JSONObject>> it = MainActivity.followedAr
				.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, JSONObject> user = it.next();
			String username = user.getKey();
			if (userexists(followedAr, username)) {
			} else {
				r.put(username, user.getValue());
			}
		}
		return r;
	}

	private LinkedHashMap<String, JSONObject> calculateUnFollowedMe(
			JSONArray followedAr) {

		LinkedHashMap<String, JSONObject> r = new LinkedHashMap<String, JSONObject>();

		for (int i = 0; i < followedAr.length(); i++) {
			try {
				JSONObject user = (JSONObject) followedAr.get(i);
				String username = user.getString("key");
				if (MainActivity.followedAr.containsKey(username)) {

				} else {
					r.put(username, user.getJSONObject("value"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return r;

	}

	private boolean userexists(JSONArray jsonArray, String usernameToFind) {
		return jsonArray.toString().contains(
				"\"key\":\"" + usernameToFind + "\"");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		MainActivity.action = v.getId();
		Log.d("main", "tıklandı : " + MainActivity.action);

		startActivityForResult(
				new Intent(yedekleYukleAnalyze.this, lists.class), 333);

	}

}

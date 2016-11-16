package com.agrlek.instashow;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class download extends AsyncTask<Boolean, String, String[][]> implements
		OnClickListener {
	private final int MAINACTIVITY_RETURN = 100;
	protected Activity context;
	protected Intent notFoundIntent;
	protected ProgressDialog pdDialog;
	protected Intent listsIntent;
	protected Intent oauthIntent;

	public download(Activity mainActivity, Intent notFoundIntent,
			Intent listsIntent, Intent oauthIntent) {
		this.context = mainActivity;
		this.oauthIntent = oauthIntent;
		this.notFoundIntent = notFoundIntent;
		this.listsIntent = listsIntent;

		// TODO Auto-generated constructor stub
	}

	@Override
	protected String[][] doInBackground(Boolean... params) {
		// TODO Auto-generated method stub

		String followUrl = "https://api.instagram.com/v1/users/"
				+ MainActivity.user_id + "/follows?access_token="
				+ MainActivity.access_token;

		String followedUrl = "https://api.instagram.com/v1/users/"
				+ MainActivity.user_id + "/followed-by?access_token="
				+ MainActivity.access_token;

		String url = "https://api.instagram.com/v1/users/"
				+ MainActivity.user_id + "?access_token="
				+ MainActivity.access_token;
		;

		Log.d("main", "Profile url: " + url);
		Log.d("main", "Follow url: " + followUrl);
		Log.d("main", "Followed url: " + followedUrl);

		String[][] returnAr = { null, null, null };

		publishProgress(context.getResources().getString(
				R.string.downloadingProfile));

		cache cache = new cache(context);

		Boolean passJson = false;
		try {
			passJson = params[0];
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (passJson) {
			Log.d("main", "Json pass geçildi");
			returnAr[0] = cache.loadArray(MainActivity.PROFILE_TAG, context);
			returnAr[1] = cache.loadArray(MainActivity.FOLLOWERS_TAG, context);
			returnAr[2] = cache.loadArray(MainActivity.FOLLOWINGS_TAG, context);
		} else {
			if (!isCancelled()) {
				returnAr[0] = downloadU(url, false);

			}

			if (!isCancelled()) {
				publishProgress(context.getResources().getString(
						R.string.downloadingFollow));
				returnAr[1] = downloadU(followUrl, true);
			}

			if (!isCancelled()) {
				publishProgress(context.getResources().getString(
						R.string.downloadingFollowed));
				returnAr[2] = downloadU(followedUrl, true);
			}

			if (!isCancelled()) {
				cache.saveArray(returnAr[0], MainActivity.PROFILE_TAG, context);
				cache.saveArray(returnAr[1], MainActivity.FOLLOWERS_TAG,
						context);
				cache.saveArray(returnAr[2], MainActivity.FOLLOWINGS_TAG,
						context);

				cache.setStringProperty(MainActivity.CACHE_LOCK, "ok");
			}
		}
		return returnAr;
	}

	int say = 2;

	String[] downloadU(String url, boolean devam) {
		String text = "";

		ArrayList<String> arrayList = new ArrayList<String>();

		boolean systemDevam = false;
		// TODO Auto-generated method stub
		do {
			if (isCancelled()) {
				systemDevam = false;
				Log.d("main", "process iptal edildi");
			} else {
				try {
					text = "";
					Log.d("main", "indiriliyor " + url);
					URL u = new URL(url);
					HttpURLConnection conn = (HttpURLConnection) u
							.openConnection();
					conn.connect();

					if (conn.getResponseCode() == 200) {
						InputStream stream = conn.getInputStream();
						int s;
						while ((s = stream.read()) != -1) {
							text = text + (char) s;
						}
					} else {

						InputStream stream = conn.getErrorStream();

						int s;
						while ((s = stream.read()) != -1) {
							text = text + (char) s;
						}

						text = conn.getResponseCode() + "|" + text;

						Log.d("main", "error  " + text);
					}

				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.d("main", "Bağlantı hatası");
					e.printStackTrace();
					text = "baglantihatasi";
				}

				try {
					JSONObject jo = new JSONObject(text);
					JSONObject pg = jo.getJSONObject("pagination");

					Log.d("main", "pagination : " + pg.getClass());

					systemDevam = true;
					url = pg.getString("next_url");
					publishProgress(context.getResources().getString(
							R.string.pageText)
							+ " : "
							+ say
							+ " "
							+ context.getResources().getString(
									R.string.pageDownloading));
					say++;

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					systemDevam = false;
				}
				Log.d("main", text);
				arrayList.add(text);
			}
		} while (systemDevam && devam);

		String[] returnAr = new String[arrayList.size()];
		return arrayList.toArray(returnAr);
	}

	@Override
	protected void onProgressUpdate(String... values) {
		Log.d("main", "load :" + values[0]);
		// TODO Auto-generated method stub
		if (values[0].equals("alert")) {
		} else {
			pdDialog.setMessage(values[0]);
		}
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(String[][] pass) {
		String s = pass[0][0];

		LinkedHashMap<String, JSONObject> followsAr = new LinkedHashMap<String, JSONObject>();
		LinkedHashMap<String, JSONObject> followedAr = new LinkedHashMap<String, JSONObject>();
		LinkedHashMap<String, JSONObject> fansAr = new LinkedHashMap<String, JSONObject>();
		LinkedHashMap<String, JSONObject> mutualAr = new LinkedHashMap<String, JSONObject>();
		LinkedHashMap<String, JSONObject> nonFollowersAr = new LinkedHashMap<String, JSONObject>();

		followsAr = jsonToLinkedHashMap(pass[1]);
		ArrayList<LinkedHashMap<String, JSONObject>> followedAndFans = jsonToLinkedHashMapWithNonfollowers(
				pass[2], followsAr);

		TextView followerCount = (TextView) context
				.findViewById(R.id.friendFollowerCount);

		TextView followsCount = (TextView) context
				.findViewById(R.id.friendFollowsCount);
		TextView mediaCount = (TextView) context
				.findViewById(R.id.friendMediaCount);

		followedAr = followedAndFans.get(0);
		fansAr = followedAndFans.get(1);
		mutualAr = followedAndFans.get(2);
		nonFollowersAr = followedAndFans.get(3);

		MainActivity.followedAr = followedAr;
		MainActivity.followsAr = followsAr;
		MainActivity.fansAr = fansAr;
		MainActivity.mutualsAr = mutualAr;
		MainActivity.nonFollowersAr = nonFollowersAr;

		Log.d("main", followedAndFans.get(1).toString());
		Log.d("main", "followsAr: " + followsAr.size());
		Log.d("main", "followedAr: " + followedAr.size());

		ImageView im = (ImageView) context.findViewById(R.id.profilePicture);
		TextView t = (TextView) context.findViewById(R.id.text);
		// Button followingButton = (Button) context
		// .findViewById(R.id.followingButton);
		// Button followedButton = (Button) context
		// .findViewById(R.id.followedButton);
		Button fansButton = (Button) context.findViewById(R.id.fanButton);
		Button mutualButton = (Button) context.findViewById(R.id.mutualButton);

		// Button medyaButton = (Button) context.findViewById(R.id.medya);
		Button yedekleButton = (Button) context
				.findViewById(R.id.yedekle_yukle);
		Button nonFollowersButton = (Button) context
				.findViewById(R.id.nonFollowerButton);

		// followingButton.setOnClickListener(this);
		// followedButton.setOnClickListener(this);
		fansButton.setOnClickListener(this);
		nonFollowersButton.setOnClickListener(this);
		mutualButton.setOnClickListener(this);
		// medyaButton.setOnClickListener(this);
		yedekleButton.setOnClickListener(this);

		// TODO Auto-generated method stub
		if (s.equals("baglantihatasi")) {
			Log.d("main", "popup gösteriliyor");
			context.startActivityForResult(notFoundIntent, MAINACTIVITY_RETURN);
		} else if (s.startsWith("400")) {
			Log.d("main", "oauth hatası gösteriliyor");
			context.startActivityForResult(oauthIntent, MAINACTIVITY_RETURN);
		} else {
			Log.d("main", s);

			try {
				JSONObject js = new JSONObject(s);
				JSONObject data = js.getJSONObject("data");

				int id = data.getInt("id");

				String profile_picture = data.getString("profile_picture");
				String username = data.getString("username");

				new changePicture(im, context, username)
						.execute(profile_picture);
				String full_name = data.getString("full_name");
				String website = data.getString("website");
				String bio = data.getString("bio");

				t.setText(id + "\n" + username + "\n" + full_name + website
						+ bio);

				JSONObject counts = data.getJSONObject("counts");

				String follows = counts.getString("follows");
				String media = counts.getString("media");
				String followed_by = counts.getString("followed_by");

				followsCount.setText(follows);
				followerCount.setText(followed_by);
				mediaCount.setText(media);

				// followingButton.setText(context.getResources().getString(
				// R.string.following)
				// + "(" + follows + ")");

				// followedButton.setText(context.getResources().getString(
				// R.string.followed)
				// + "(" + followed_by + ")");

				// medyaButton.setText(context.getResources().getString(
				// R.string.medya)
				// + "(" + media + ")");

				fansButton.setText(context.getResources().getString(
						R.string.fans)
						+ "(" + fansAr.size() + ")");

				mutualButton.setText(context.getResources().getString(
						R.string.mutual)
						+ "(" + mutualAr.size() + ")");

				nonFollowersButton.setText(context.getResources().getString(
						R.string.nonFollower)
						+ "(" + nonFollowersAr.size() + ")");

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// new recentMedia(context, notFoundIntent, listsIntent, oauthIntent)
		// .execute();

		pdDialog.cancel();
	}

	protected ArrayList<LinkedHashMap<String, JSONObject>> jsonToLinkedHashMapWithNonfollowers(
			String[] strings, LinkedHashMap<String, JSONObject> followsAr) {
		// TODO Auto-generated method stub
		ArrayList<LinkedHashMap<String, JSONObject>> returnAr = new ArrayList<LinkedHashMap<String, JSONObject>>();

		LinkedHashMap<String, JSONObject> followeds = new LinkedHashMap<String, JSONObject>();
		LinkedHashMap<String, JSONObject> fans = new LinkedHashMap<String, JSONObject>();
		LinkedHashMap<String, JSONObject> mutuals = new LinkedHashMap<String, JSONObject>();
		LinkedHashMap<String, JSONObject> nonFollowers = new LinkedHashMap<String, JSONObject>();

		for (String text : strings) {
			try {
				JSONObject js = new JSONObject(text);
				JSONArray data = js.getJSONArray("data");
				for (int i = 0; i < data.length(); i++) {
					JSONObject user = data.getJSONObject(i);
					String userName = user.getString("username");
					followeds.put(userName, user);

					// Log.d("main", "say -> " + user);
					// Log.d("main", "say -> " + followeds.size());

					if (followsAr.containsKey(userName)) {
						mutuals.put(userName, user);

						if (this.getClass().getName()
								.matches(".*?downloadFriend")) {
							if (MainActivity.followsAr.containsKey(userName)) {
								user.put("followingDurum",
										MainActivity.meFollowing);
							}
						} else {
							user.put("followingDurum", MainActivity.meFollowing);
						}
					} else {
						fans.put(userName, user);
					}

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		for (Entry<String, JSONObject> user : followsAr.entrySet()) {
			String userName = user.getKey();
			if (!followeds.containsKey(userName)) {
				JSONObject userValue = user.getValue();
				String className = this.getClass().getName();
				if (className.matches(".*?downloadFriend")) {

				} else {
					try {
						userValue.put("followingDurum", 1);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				nonFollowers.put(userName, userValue);
			}
			Log.d("main", "key -> " + userName);
		}

		returnAr.add(followeds);
		returnAr.add(fans);
		returnAr.add(mutuals);
		returnAr.add(nonFollowers);

		return returnAr;

	}

	protected LinkedHashMap<String, JSONObject> jsonToLinkedHashMap(
			String[] strings) {
		// TODO Auto-generated method stub
		LinkedHashMap<String, JSONObject> returnAr = new LinkedHashMap<String, JSONObject>();
		for (String text : strings) {
			try {
				JSONObject js = new JSONObject(text);
				JSONArray data = js.getJSONArray("data");
				for (int i = 0; i < data.length(); i++) {
					JSONObject user = data.getJSONObject(i);
					returnAr.put(user.getString("username"), user);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return returnAr;
	}

	@Override
	protected void onPreExecute() {
		pdDialog = new ProgressDialog(context);
		pdDialog.setCancelable(false);
		pdDialog.show();
		super.onPreExecute();
	}

	@Override
	public void onClick(View v) {
		MainActivity.action = v.getId();
		Log.d("main", "tıklandı : " + MainActivity.action);
		if (v.getId() == R.id.yedekle_yukle) {
			context.startActivityForResult(new Intent(context,
					yedekleYukle.class), MAINACTIVITY_RETURN);
		} else {
			context.startActivityForResult(listsIntent, MAINACTIVITY_RETURN);
		}
	}

	private LinkedHashMap<String, JSONObject> UserSign(
			LinkedHashMap<String, JSONObject> LinkedHashMap) {
		Iterator<Entry<String, JSONObject>> it = LinkedHashMap.entrySet()
				.iterator();
		while (it.hasNext()) {
			Entry<String, JSONObject> user = it.next();
			JSONObject value = user.getValue();
			try {
				value.put("followingDurum", 1);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			user.setValue(value);
		}

		return LinkedHashMap;
	}

}

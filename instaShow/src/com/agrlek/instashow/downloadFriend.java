package com.agrlek.instashow;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class downloadFriend extends download implements OnCancelListener {

	static final int LISTACTIVITY_RETURN = 700;
	public static int LISTACTIVITY_RETURN_BLOCK = 888;

	public downloadFriend(Activity mainActivity, Intent notFoundIntent,
			Intent listsIntent, Intent oauthIntent) {
		super(mainActivity, notFoundIntent, listsIntent, oauthIntent);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onPreExecute() {
		pdDialog = new ProgressDialog(context);
		pdDialog.setOnCancelListener(this);
		pdDialog.show();
		// TODO Auto-generated method stub
	}

	@Override
	protected String[][] doInBackground(Boolean... params) {
		String followUrl = "https://api.instagram.com/v1/users/"
				+ friendProfil.userID + "/follows?access_token="
				+ MainActivity.access_token;

		String followedUrl = "https://api.instagram.com/v1/users/"
				+ friendProfil.userID + "/followed-by?access_token="
				+ MainActivity.access_token;

		String url = "https://api.instagram.com/v1/users/"
				+ friendProfil.userID + "?access_token="
				+ MainActivity.access_token;

		String relationshipUrl = "https://api.instagram.com/v1/users/"
				+ friendProfil.userID + "/relationship?access_token="
				+ MainActivity.access_token;

		String[][] returnAr = { null, null, null, null };

		cache cache = new cache(context);

		Boolean passJson = false;
		try {
			passJson = params[0];
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (passJson) {
			Log.d("main", "Json pass geçildi");
			returnAr[0] = cache.loadArray(friendProfil.PROFILE_TAG + "_"
					+ friendProfil.userID, context);
			returnAr[1] = cache.loadArray(friendProfil.FOLLOWERS_TAG + "_"
					+ friendProfil.userID, context);

			returnAr[2] = cache.loadArray(friendProfil.FOLLOWINGS_TAG + "_"
					+ friendProfil.userID, context);

			returnAr[3] = cache.loadArray(friendProfil.RELATIONSHIP_TAG + "_"
					+ friendProfil.userID, context);

		} else {

			if (!isCancelled()) {
				returnAr[0] = downloadU(url, false);

				JSONObject js;

				try {
					js = new JSONObject(returnAr[0][0]);
					JSONObject data = js.getJSONObject("data");

					JSONObject counts = data.getJSONObject("counts");
					// String follows = counts.getString("follows");
					int followers = counts.getInt("follows");
					int followings = counts.getInt("followed_by");

					if (MainActivity.LOCK_MAXIMUM_PAGE) {
						if ((followers + followings) > 1000) {
							MainActivity.LOCK_MAXIMUM_PAGE = true;
							pdDialog.cancel();
							Log.d("main", "maksimum lock");
							context.startActivityForResult(new Intent(context,
									maksimumPage.class),
									MainActivity.MAKSIMUM_PAGE_RESULT);
							cancel(true);
						}
					} else {
						MainActivity.LOCK_MAXIMUM_PAGE = true;
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

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

				publishProgress(context.getResources().getString(
						R.string.downloadingRelationship));
				;
				returnAr[3] = downloadU(relationshipUrl, true);
			}

			if ((!isCancelled()) & (!returnAr[0][0].startsWith("400"))) {
				cache.saveArray(returnAr[0], friendProfil.PROFILE_TAG + "_"
						+ friendProfil.userID, context);
				cache.saveArray(returnAr[1], friendProfil.FOLLOWERS_TAG + "_"
						+ friendProfil.userID, context);
				cache.saveArray(returnAr[2], friendProfil.FOLLOWINGS_TAG + "_"
						+ friendProfil.userID, context);
				cache.saveArray(returnAr[3], friendProfil.RELATIONSHIP_TAG
						+ "_" + friendProfil.userID, context);

				cache.setStringProperty(friendProfil.CACHE_LOCK + "_"
						+ friendProfil.userID, "ok");

				Log.d("main", "friendShip:Cache_lock yazıldı");
			}

			Log.d("main", "friendShip:" + returnAr[0][0]);
		}

		// TODO Auto-generated method stub
		return returnAr;
	}

	@Override
	protected void onPostExecute(String[][] pass) {
		if (isCancelled()) {

		} else {
			String s = pass[0][0];
			LinkedHashMap<String, JSONObject> followsAr = new LinkedHashMap<String, JSONObject>();
			LinkedHashMap<String, JSONObject> followedAr = new LinkedHashMap<String, JSONObject>();
			LinkedHashMap<String, JSONObject> fansAr = new LinkedHashMap<String, JSONObject>();
			LinkedHashMap<String, JSONObject> mutualAr = new LinkedHashMap<String, JSONObject>();
			LinkedHashMap<String, JSONObject> nonFollowersAr = new LinkedHashMap<String, JSONObject>();

			followsAr = jsonToLinkedHashMap(pass[1]);
			ArrayList<LinkedHashMap<String, JSONObject>> followedAndFans = jsonToLinkedHashMapWithNonfollowers(
					pass[2], followsAr);

			followedAr = followedAndFans.get(0);
			fansAr = followedAndFans.get(1);
			mutualAr = followedAndFans.get(2);
			nonFollowersAr = followedAndFans.get(3);

			if (!isCancelled()) {

				friendProfil.followedAr = followedAr;
				friendProfil.followsAr = followsAr;
				friendProfil.fansAr = fansAr;
				friendProfil.mutualsAr = mutualAr;
				friendProfil.nonFollowersAr = nonFollowersAr;
			}
			ArrayList<LinkedHashMap<String, JSONObject>> youWithMeFollowers = youWithMeCalculate(
					followedAr, followsAr, fansAr, mutualAr, nonFollowersAr);

			LinkedHashMap<String, JSONObject> followedArWithMe = youWithMeFollowers
					.get(0);
			LinkedHashMap<String, JSONObject> followsArWithMe = youWithMeFollowers
					.get(1);
			LinkedHashMap<String, JSONObject> fansArWithMe = youWithMeFollowers
					.get(2);
			if (!isCancelled()) {

				friendProfil.followedArWithMe = followedArWithMe;
				friendProfil.followsArWithMe = followsArWithMe;
				friendProfil.fansArWithMe = fansArWithMe;

			}
			// Log.d("main", followedAndFans.get(1).toString());
			// Log.d("main", "followsAr: " + followsAr.size());
			// Log.d("main", "followedAr: " + followedAr.size());

			ImageView im = (ImageView) context
					.findViewById(R.id.friendProfilePicture);
			TextView t = (TextView) context.findViewById(R.id.friendText);

			TextView followerCount = (TextView) context
					.findViewById(R.id.friendFollowerCount);
			TextView followsCount = (TextView) context
					.findViewById(R.id.friendFollowsCount);
			TextView mediaCount = (TextView) context
					.findViewById(R.id.friendMediaCount);

			// TODO Auto-generated method stub
			if (s.equals("baglantihatasi")) {
				Log.d("main", "popup gösteriliyor");
				context.startActivityForResult(notFoundIntent,
						LISTACTIVITY_RETURN);
			} else if (s.startsWith("400")) {
				Log.d("main", "  hata : " + s);
				String[] ar = s.split("\\|");
				try {
					Log.d("main", "ar 1 : " + ar[1]);

					JSONObject jsError = new JSONObject(ar[1]);
					String error_type = jsError.getJSONObject("meta")
							.getString("error_type");
					String error_message = jsError.getJSONObject("meta")
							.getString("error_message");

					Log.d("main", "error type : " + error_type);
					Log.d("main", "error message : " + error_message);

					if (error_type.equals("APINotAllowedError")) {
						context.startActivityForResult(new Intent(context,
								block.class), LISTACTIVITY_RETURN_BLOCK);
					} else if (error_type.equals("APINotFoundError")) {
						context.startActivityForResult(new Intent(context,
								friendNotFound.class),
								LISTACTIVITY_RETURN_BLOCK);
					} else {
						context.startActivityForResult(oauthIntent,
								LISTACTIVITY_RETURN);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Log.d("main", s);

				try {

					Button fansButton = (Button) context
							.findViewById(R.id.friendFanButton);

					Button mutualButton = (Button) context
							.findViewById(R.id.friendMutualButton);

					// Button medyaButton = (Button) context
					// .findViewById(R.id.friendMedya);

					// Button yedekleButton = (Button) context
					// .findViewById(R.id.friendYedekle_yukle);

					Button nonFollowersButton = (Button) context
							.findViewById(R.id.friendNonFollowerButton);

					Button fansWithMeButton = (Button) context
							.findViewById(R.id.friendFanButtonWithMe);

					Button followedWithMeButton = (Button) context
							.findViewById(R.id.friendFollowedButtonWithme);

					Button followsWithMeButton = (Button) context
							.findViewById(R.id.friendNonFollowerButtonWithMe);

					
					Button instagramGoruntuleButton = (Button) context
							.findViewById(R.id.instagramGoruntule);

					Button friendProfilFollowButton = (Button) context
							.findViewById(R.id.friendProfilFollow);

					Button friendProfilButton = (Button) context
							.findViewById(R.id.friendProfilFollow2);

					fansButton.setOnClickListener(this);
					nonFollowersButton.setOnClickListener(this);
					mutualButton.setOnClickListener(this);
					// medyaButton.setOnClickListener(this);
					// yedekleButton.setOnClickListener(this);
					fansWithMeButton.setOnClickListener(this);
					followedWithMeButton.setOnClickListener(this);
					followsWithMeButton.setOnClickListener(this);

					JSONObject js = new JSONObject(s);
					JSONObject data = js.getJSONObject("data");

					int id = data.getInt("id");

					String profile_picture = data.getString("profile_picture");
					String username = data.getString("username");

					new changePicture(im, context, "/" + username)
							.execute(profile_picture);
					String full_name = data.getString("full_name");
					String website = data.getString("website");
					String bio = data.getString("bio");

					t.setText(id + "\n" + username + "\n" + full_name + "\n"
							+ website + "\n" + bio);

					JSONObject counts = data.getJSONObject("counts");

					String follows = counts.getString("follows");
					String media = counts.getString("media");
					String followed_by = counts.getString("followed_by");

					followsCount.setText(follows);
					followerCount.setText(followed_by);
					mediaCount.setText(media);

					String relationshipText = pass[3][0];
					JSONObject jsonRelationShip = new JSONObject(
							relationshipText);

					String outgoing_status = jsonRelationShip.getJSONObject(
							"data").getString("outgoing_status");
					String incoming_status = jsonRelationShip.getJSONObject(
							"data").getString("incoming_status");
					String target_user_is_private = jsonRelationShip
							.getJSONObject("data").getString(
									"target_user_is_private");
					if (outgoing_status.equals("follows")) {
					} else if (outgoing_status.equals("requested")) {
						friendProfilFollowButton.setText(context.getResources()
								.getString(R.string.sendRequest));
						friendProfilFollowButton
								.setBackgroundResource(R.drawable.instanonfollow);
					} else {
						friendProfilFollowButton
								.setCompoundDrawablesWithIntrinsicBounds(
										android.R.drawable.presence_invisible,
										0, 0, 0);
						friendProfilFollowButton
								.setBackgroundResource(R.drawable.instanonfollow);
						friendProfilFollowButton.setText(context.getResources()
								.getString(R.string.follow));
					}

					if (incoming_status.equals("none")) {
						friendProfilButton.setText(context.getResources()
								.getString(R.string.nonFollowedMe));
						friendProfilButton
								.setBackgroundResource(R.drawable.instanonfollow);
					} else if (incoming_status.equals("requested_by")) {
						friendProfilButton.setText(R.string.sendRequest);
					} else if (incoming_status.equals("followed_by")) {
						friendProfilButton.setText(context.getResources()
								.getString(R.string.followedMe));
						friendProfilButton
								.setBackgroundResource(R.drawable.instafollow);
					} else {
						friendProfilButton.setText("Sizi Engelledi");
					}

					instagramGoruntuleButton
							.setOnClickListener(new instagramEvent(context,
									username, id));

					friendProfilFollowButton
							.setOnClickListener(new instagramEvent(context,
									username, id));

					friendProfilButton.setOnClickListener(new instagramEvent(
							context, username, id));

					// followingButton.setText(context.getResources().getString(
					// R.string.following)
					// + "(" + follows + ")");

					// followedButton.setText(context.getResources().getString(
					// R.string.followed)
					// + "(" + followed_by + ")");

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
							R.string.friendFans)
							+ "(" + fansAr.size() + ")");

					mutualButton.setText(context.getResources().getString(
							R.string.friendMutual)
							+ "(" + mutualAr.size() + ")");

					nonFollowersButton.setText(context.getResources()
							.getString(R.string.friendNonFollower)
							+ "("
							+ nonFollowersAr.size() + ")");

					followedWithMeButton.setText(context.getResources()
							.getString(R.string.friendFollowedWithMe)
							+ "("
							+ followedArWithMe.size() + ")");

					followsWithMeButton.setText(context.getResources()
							.getString(R.string.friendFollowsWithMe)
							+ "("
							+ followsArWithMe.size() + ")");

					fansWithMeButton.setText(context.getResources().getString(
							R.string.friendFansWithMe)
							+ "(" + fansArWithMe.size() + ")");

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			pdDialog.cancel();
			// TODO Auto-generated method stub
		}
	}

	private ArrayList<LinkedHashMap<String, JSONObject>> youWithMeCalculate(
			LinkedHashMap<String, JSONObject> followedAr,
			LinkedHashMap<String, JSONObject> followsAr,
			LinkedHashMap<String, JSONObject> fansAr,
			LinkedHashMap<String, JSONObject> mutualAr,
			LinkedHashMap<String, JSONObject> nonFollowersAr) {
		// TODO Auto-generated method stub
		ArrayList<LinkedHashMap<String, JSONObject>> returnAr = new ArrayList<LinkedHashMap<String, JSONObject>>();

		LinkedHashMap<String, JSONObject> followedArWithMe = new LinkedHashMap<String, JSONObject>();
		LinkedHashMap<String, JSONObject> followsArWithMe = new LinkedHashMap<String, JSONObject>();
		LinkedHashMap<String, JSONObject> fansArWithMe = new LinkedHashMap<String, JSONObject>();

		for (JSONObject data : followedAr.values()) {
			try {
				String userName = data.getString("username");
				if (MainActivity.followedAr.containsKey(userName)) {
					Log.d("friend", "followedwithMe " + userName);
					followedArWithMe.put(userName, data);

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		for (JSONObject data : followsAr.values()) {
			try {
				String userName = data.getString("username");
				if (MainActivity.followsAr.containsKey(userName)) {
					Log.d("friend", "followswithMe " + userName);
					followsArWithMe.put(userName, data);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		for (JSONObject data : fansAr.values()) {
			try {
				String userName = data.getString("username");
				if (MainActivity.fansAr.containsKey(userName)) {
					Log.d("friend", "fanswithMe : " + userName);
					fansArWithMe.put(userName, data);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		returnAr.add(followedArWithMe);
		returnAr.add(followsArWithMe);
		returnAr.add(fansArWithMe);
		return returnAr;
	}

	@Override
	String[] downloadU(String url, boolean devam) {
		// TODO Auto-generated method stub
		return super.downloadU(url, devam);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		MainActivity.action = v.getId();
		Log.d("main", "tıklandı : " + MainActivity.action);

		context.startActivityForResult(listsIntent, LISTACTIVITY_RETURN);
		// TODO Auto-generated method stub
	}

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		context.finish();
		super.onCancelled();
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub
		Log.d("main", "downloadFriend iptal edildi");
		cancel(true);
	}
}

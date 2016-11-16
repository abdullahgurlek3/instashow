package com.agrlek.instashow;

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

public class downloadFriendLight extends download implements OnCancelListener {

	private final int LISTACTIVITY_RETURN = 700;

	public downloadFriendLight(Activity mainActivity, Intent notFoundIntent,
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

		String url = "https://api.instagram.com/v1/users/"
				+ friendProfil.userID + "?access_token="
				+ MainActivity.access_token;


		String relationshipUrl = "https://api.instagram.com/v1/users/"
				+ friendProfil.userID + "/relationship?access_token="
				+ MainActivity.access_token;

		
		String[][] returnAr = { null, null, null, null};

		cache cache = new cache(context);

		if (!isCancelled()) {
			returnAr[0] = downloadU(url, false);

			publishProgress(context.getResources().getString(
					R.string.downloadingRelationship));
			;
			returnAr[3] = downloadU(relationshipUrl, true);

		}

		Log.d("main", "friendShip:" + returnAr[0][0]);
		// TODO Auto-generated method stub
		return returnAr;
	}

	@Override
	protected void onPostExecute(String[][] pass) {
		if (isCancelled()) {

		} else {
			String s = pass[0][0];

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
			} else if (s.equals("oauthhatasi")) {
				Log.d("main", "oauth hatası gösteriliyor");
				context.startActivityForResult(oauthIntent, LISTACTIVITY_RETURN);
			} else {
				Log.d("main", s);
				try {
					Button fansButton = (Button) context
							.findViewById(R.id.friendFanButton);

					Button mutualButton = (Button) context
							.findViewById(R.id.friendMutualButton);

		//			Button medyaButton = (Button) context
			//				.findViewById(R.id.friendMedya);

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
					
					fansButton.setVisibility(View.GONE);
					nonFollowersButton.setVisibility(View.GONE);

					mutualButton.setVisibility(View.GONE);

					// medyaButton.setVisibility(View.GONE);
					//yedekleButton.setVisibility(View.GONE);
					fansWithMeButton.setVisibility(View.GONE);
					followedWithMeButton.setVisibility(View.GONE);
					followsWithMeButton.setVisibility(View.GONE);

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
						friendProfilFollowButton.setText("İstek Gönderildi");
						friendProfilFollowButton
								.setBackgroundResource(R.drawable.instanonfollow);
					} else {
						friendProfilFollowButton
								.setCompoundDrawablesWithIntrinsicBounds(
										android.R.drawable.presence_invisible,
										0, 0, 0);
						friendProfilFollowButton
								.setBackgroundResource(R.drawable.instanonfollow);
						friendProfilFollowButton.setText("Takip Et");
					}

					if (incoming_status.equals("none")) {
						friendProfilButton.setText("Takip Etmiyor");
						friendProfilButton
								.setBackgroundResource(R.drawable.instanonfollow);
					} else if (incoming_status.equals("requested_by")) {
						friendProfilButton.setText("Takip isteği Gönderdi");
					} else if (incoming_status.equals("followed_by")) {
						friendProfilButton.setText("Takip Ediyor");
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

					//medyaButton.setText(context.getResources().getString(
					//		R.string.medya)
					//		+ "(" + media + ")");

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			pdDialog.cancel();
			// TODO Auto-generated method stub
		}
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
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub
		Log.d("main", "downloadFriend iptal edildi");
		cancel(true);
		// context.finish();
	}
}

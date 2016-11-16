package com.agrlek.instashow;

import java.util.LinkedHashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class friendProfil extends Activity {

	public static Integer userID;
	public static String userName;
	public static LinkedHashMap<String, JSONObject> followedAr;
	public static LinkedHashMap<String, JSONObject> followsAr;
	public static LinkedHashMap<String, JSONObject> mutualsAr;
	public static LinkedHashMap<String, JSONObject> nonFollowersAr;
	public static LinkedHashMap<String, JSONObject> fansAr;

	public static String CACHE_LOCK = "friend_cache_lock";
	public static int REQUEST_DIALOG_RUN = 541;
	public static int RETURN_DIALOG_RUN = 600;
	protected static int RETURN_DIALOG_RUN_JSONPASS_REPLACE = 542;

	public static final String FOLLOWERS_TAG = "friendFollowers";
	public static final String PROFILE_TAG = "friendProfile";
	public static final String FOLLOWINGS_TAG = "friendFollowings";
	public static final String RELATIONSHIP_TAG="friendRelationship";

	static LinkedHashMap<String, JSONObject> followedArWithMe;
	static LinkedHashMap<String, JSONObject> followsArWithMe;
	static LinkedHashMap<String, JSONObject> fansArWithMe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		Bundle extras = getIntent().getExtras();
		friendProfil.userName = extras.getString("userName");
		friendProfil.userID = extras.getInt("userID");
		boolean light = extras.getBoolean("light");

		Log.d("main", "friend Username: " + friendProfil.userName);
		setContentView(R.layout.profilfriend);
		super.onCreate(savedInstanceState);

		cache cache = new cache(this);
		if (cache.checkStringProperty(friendProfil.CACHE_LOCK + "_"
				+ friendProfil.userID)) {
			Log.d("main", "Cache bulundu ");
			startActivityForResult(new Intent(this, dialogRun.class),
					friendProfil.REQUEST_DIALOG_RUN);
		} else {
			if (light) {
				new downloadFriendLight(this, new Intent(this, notfound.class),
						new Intent(this, lists.class), new Intent(this,
								webview.class)).execute();
			} else {
				new downloadFriend(this, new Intent(this, notfound.class),
						new Intent(this, lists.class), new Intent(this,
								webview.class)).execute();
			}
		}
		

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("main", "friendProfil : requestCode: " + requestCode);
		Log.d("main", "friendProfil : resultCode: " + resultCode);

		if (requestCode == friendProfil.REQUEST_DIALOG_RUN
				&& resultCode == friendProfil.RETURN_DIALOG_RUN) {
			new downloadFriend(this, new Intent(this, notfound.class),
					new Intent(this, lists.class), new Intent(this,
							webview.class)).execute(true);
		}

		if (requestCode == friendProfil.REQUEST_DIALOG_RUN
				&& resultCode == friendProfil.RETURN_DIALOG_RUN_JSONPASS_REPLACE) {
			new downloadFriend(this, new Intent(this, notfound.class),
					new Intent(this, lists.class), new Intent(this,
							webview.class)).execute();
		}

		if (requestCode == MainActivity.MAKSIMUM_PAGE_RESULT
				| requestCode == downloadFriend.LISTACTIVITY_RETURN_BLOCK) {
			finish();
		}

		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
}

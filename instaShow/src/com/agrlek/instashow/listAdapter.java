package com.agrlek.instashow;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class listAdapter implements ListAdapter, OnClickListener {

	private Context context;
	private HashMap<String, JSONObject> data;
	private ArrayList<String> dataList;
	private Integer selectedUserId;
	private String action;

	public listAdapter(Context lists) {
		context = lists;
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String key = dataList.get(position);
		JSONObject object = data.get(key);

		Log.d("main", "user : " + object);

		LayoutInflater li = ((Activity) context).getLayoutInflater();
		View lt = li.inflate(R.layout.listitem, null);

		TextView ad = (TextView) lt.findViewById(R.id.itemAd);

		ImageView im = (ImageView) lt.findViewById(R.id.itemPic);
		Button goruntuleButton = (Button) lt
				.findViewById(R.id.instagramGoruntule);
		// Button birakButton = (Button) lt.findViewById(R.id.instagramBirak);
		Button profilButton = (Button) lt.findViewById(R.id.friendProfil);

		Button followButton = (Button) lt.findViewById(R.id.friendProfilFollow);

		String username = null;
		Integer userID = null;

		
		Integer followingDurum = 0;

		try {
			username = object.getString("username");
			userID = object.getInt("id");
			followingDurum = object.getInt("followingDurum");
		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		selectedUserId = userID;

		followButton.setOnClickListener(new instagramEvent(context,
				username, userID));
		if (followingDurum == MainActivity.meFollowing) {
			followButton.setBackgroundResource(R.drawable.instafollow);
			action = "unfollow";
		} else if (followingDurum == MainActivity.itFollow) {
			followButton
					.setBackgroundResource(android.R.drawable.screen_background_dark);
		} else {
			action = "follow";
			followButton.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.presence_invisible,  0);
			followButton.setBackgroundResource(R.drawable.instanonfollow);
			followButton.setText(R.string.followIt);
		}

		goruntuleButton.setOnClickListener(new instagramEvent(context,
				username, userID));
		profilButton.setOnClickListener(new instagramEvent(context, username,
				userID));

		try {
			new changePicture(im, context, "/" + key).execute(object
					.getString("profile_picture"));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			ad.setText(object.getString("full_name") + " / " + username);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return lt;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setData(HashMap<String, JSONObject> data2) {
		// TODO Auto-generated method stub
		this.data = data2;
		dataList = new ArrayList<String>(data.keySet());
	}

	@Override
	public void onClick(View v) {
		// unfollow

		Log.d("main", "unfollow : https://api.instagram.com/v1/users/"
				+ selectedUserId + "/relationship?access_token="
				+ MainActivity.access_token + "&action=" + this.action);
	}

}

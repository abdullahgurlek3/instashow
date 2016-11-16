package com.agrlek.instashow;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class listTask extends AsyncTask<Integer, Integer, Integer[]> implements
		OnClickListener {

	private lists context;
	private ProgressDialog p;
	private listAdapter adapter;
	private ListView listView;
	private Set<Entry<String, JSONObject>> currentSet;
	private LinkedHashMap<String, JSONObject> currentData;
	private Iterator<Entry<String, JSONObject>> it;
	int itSay = 0;
	private Button loadingButton;

	public listTask(lists lists, listAdapter adapter) {
		this.context = lists;
		this.adapter = adapter;
	}

	@Override
	protected Integer[] doInBackground(Integer... params) {
		// TODO Auto-generated method stub

		Integer[] returnAr = params;

		return returnAr;

	}

	@Override
	protected void onPreExecute() {
		p = new ProgressDialog(this.context);
		p.setCancelable(false);
		p.show();
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(Integer[] pass) {
		Integer clickId = pass[0];
		LinkedHashMap<String, JSONObject> data = null;
		if (clickId == R.id.nonFollowerButton) {
			context.setTitle(context.getResources().getString(
					R.string.titleNonFollowers));
			data = MainActivity.nonFollowersAr;
		} else if (clickId == R.id.mutualButton) {
			data = MainActivity.mutualsAr;
			context.setTitle(context.getResources().getString(
					R.string.titleMutuals));
		} else if (clickId == R.id.fanButton) {
			data = MainActivity.fansAr;
			context.setTitle(context.getResources().getString(
					R.string.titleFans));
		} else if (clickId == R.id.friendFanButton) {
			data = friendProfil.fansAr;
			context.setTitle(friendProfil.userName + " : "
					+ context.getResources().getString(R.string.titleFansYou));
		} else if (clickId == R.id.friendMutualButton) {
			data = friendProfil.mutualsAr;
			context.setTitle(friendProfil.userName
					+ " : "
					+ context.getResources()
							.getString(R.string.titleMutualsYou));
		} else if (clickId == R.id.friendNonFollowerButton) {
			data = friendProfil.nonFollowersAr;
			context.setTitle(friendProfil.userName
					+ " : "
					+ context.getResources().getString(
							R.string.titleNonFollowersYou));
		} else if (clickId == R.id.friendNonFollowerButtonWithMe) {
			data = friendProfil.followsArWithMe;
			context.setTitle(friendProfil.userName
					+ " : "
					+ context.getResources().getString(
							R.string.titleNonFollowersWithMe));
		} else if (clickId == R.id.friendFollowedButtonWithme) {
			data = friendProfil.followedArWithMe;
			context.setTitle(friendProfil.userName
					+ " : "
					+ context.getResources().getString(
							R.string.titleFollowedWithMe));
		} else if (clickId == R.id.friendFanButtonWithMe) {
			data = friendProfil.fansArWithMe;
			context.setTitle(friendProfil.userName
					+ " : "
					+ context.getResources()
							.getString(R.string.titleFansWithMe));
		} else if (clickId == R.id.myUnfollowedButton) {
			context.setTitle(context.getResources().getString(
					R.string.myUnfollowed));
			data = yedekleYukleAnalyze.myUnfollows;
		} else if (clickId == R.id.newFollowedMeButton) {
			data = yedekleYukleAnalyze.newFollowedMe;
			context.setTitle(context.getResources().getString(
					R.string.newFollowedMe));
		} else if (clickId == R.id.newMyFollowedButton) {
			data = yedekleYukleAnalyze.newMyFollows;
			context.setTitle(context.getResources().getString(
					R.string.newMyFollowed));
		} else if (clickId == R.id.unfollowedMeButton) {
			data = yedekleYukleAnalyze.unFollowedMe;
			context.setTitle(context.getResources().getString(
					R.string.unfollowedMe));
		}

		else {
			try {
				throw new Exception("Tıklanan buton boş");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		currentSet = data.entrySet();
		currentData = new LinkedHashMap<String, JSONObject>();

		listView = (ListView) context.findViewById(R.id.listview);

		loadingButton = new Button(context);
		loadingButton.setBackgroundResource(R.drawable.instanonfollow);
		loadingButton.setText(context.getResources().getString(
				R.string.loadmore));
		loadingButton.setOnClickListener(this);
		listView.addFooterView(loadingButton);

		it = currentSet.iterator();

		while (it.hasNext() && itSay < 10) {
			Entry<String, JSONObject> next = it.next();
			currentData.put(next.getKey(), next.getValue());
			itSay++;
		}

		if (!it.hasNext()) {
			loadingButton.setVisibility(View.INVISIBLE);
		}

		adapter.setData(currentData);
		listView.setAdapter(adapter);
		Log.d("main", "jsonArray: " + data);

		p.cancel();
		p.dismiss();
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {

		itSay = 0;
		while (it.hasNext() && itSay < 5) {
			Entry<String, JSONObject> next = it.next();
			currentData.put(next.getKey(), next.getValue());
			itSay++;
		}

		if (!it.hasNext()) {
			loadingButton.setVisibility(View.INVISIBLE);
		}

		adapter.setData(currentData);
		listView.invalidateViews();

	}
}

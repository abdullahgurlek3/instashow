package com.agrlek.instashow;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

public class lists extends ActionBarActivity {

	private AsyncTask<Integer, Integer, Integer[]> listTaskProcess;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.lists);
		listAdapter adapter = new listAdapter(this);


		listTaskProcess = new listTask(this, adapter)
				.execute(MainActivity.action);
		
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		Log.d("main", " ListsActiity : " + requestCode);
		super.onActivityResult(requestCode, resultCode, arg2);
	}


	@Override
	public void onBackPressed() {
		listTaskProcess.cancel(true);
		Log.d("main", "listTask durduruldu");
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

}

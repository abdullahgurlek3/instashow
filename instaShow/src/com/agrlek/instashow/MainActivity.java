package com.agrlek.instashow;

import java.util.LinkedHashMap;
import java.util.Locale;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	public static String user_id;
	public static String access_token = "1366434179.661ca2d.e2a8b95d4c7c43a1aa1af6050e40f974";
	public static String client_id = "661ca2dee3a44d8196736d251d957910";
	public static LinkedHashMap<String, JSONObject> followedAr;
	public static LinkedHashMap<String, JSONObject> followsAr;
	public static LinkedHashMap<String, JSONObject> fansAr;
	public static LinkedHashMap<String, JSONObject> mutualsAr;
	public static LinkedHashMap<String, JSONObject> nonFollowersAr;
	public static int action;

	public static String CACHE_LOCK = "cache_lock";
	public static int MAKSIMUM_PAGE_RESULT = 777;
	public static boolean LOCK_MAXIMUM_PAGE = true;
	public static Integer meFollowing = 1;
	public static Integer itFollow = 2;

	public static final String FOLLOWERS_TAG = "followers";
	public static final String PROFILE_TAG = "profile";
	public static final String FOLLOWINGS_TAG = "followings";

	private final int MAINACTIVITY_RETURN_REQUEST = 100;
	private final int MAINACTIVITY_RETURN_RESULT = 300;

	private final int MAINACTIVITY_EXIT_REQUEST = 100;
	private final int MAINACTIVITY_EXIT_RESULT = 320;
	private final int MAINACTIVITY_WEBWIEV_REQUEST = 400;
	private int DIALOGRUN_CODE = 500;
	private AsyncTask<Boolean, String, String[][]> downloadProcess;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("main","locate : "+ Locale.getDefault().getDisplayLanguage());
		// getSupportActionBar().setDisplayShowHomeEnabled(true);
		// getSupportActionBar().setLogo(R.drawable.ic_launcher);
		// getSupportActionBar().setDisplayUseLogoEnabled(true);

		setContentView(R.layout.activity_main);

		// database db = new database(this, "db.db", null, 1);
		//
		// SQLiteDatabase dbx = db.getWritableDatabase();
		//
		// // Create a new map of values, where column names are the keys
		// ContentValues values = new ContentValues();
		// values.put("id", 1);
		

		// dbx.insert("xx", null, values);
		//
		if (MainActivity.user_id == null) {
			startWebWiew();
		} else {
			start();
		}

	}

	private void startWebWiew() {
		// TODO Auto-generated method stub
		startActivityForResult(new Intent(this, webview.class), 400);

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	private void start() {
		// TODO Auto-generated method stub
		Log.d("main", "Başladı");

		cache cache = new cache(this);
		Log.d("main",
				"profile cache durum : " + cache.checkStringProperty("profile"));

		if (cache.checkStringProperty(MainActivity.CACHE_LOCK)) {
			Log.d("main", "Cache bulundu ");
			startActivityForResult(new Intent(this, dialogRun.class),
					DIALOGRUN_CODE);
		} else {
			downloadProcess = new download(this, new Intent(this,
					notfound.class), new Intent(this, lists.class), new Intent(
					this, webview.class)).execute(false);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == R.id.action_refresh) {
			start();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		super.onActivityResult(requestCode, resultCode, arg2);
		Log.d("main", "requestCode: " + requestCode);
		Log.d("main", "resultCode: " + resultCode);

		// TODO Auto-generated method stub
		if (requestCode == MAINACTIVITY_RETURN_REQUEST
				&& resultCode == MAINACTIVITY_RETURN_RESULT) {
			start();
		}

		if (requestCode == MAINACTIVITY_WEBWIEV_REQUEST) {
			start();
		}

		
		if ((requestCode == DIALOGRUN_CODE && resultCode == friendProfil.RETURN_DIALOG_RUN_JSONPASS_REPLACE)
				| requestCode == MAKSIMUM_PAGE_RESULT) {
			downloadProcess = new download(this, new Intent(this,
					notfound.class), new Intent(this, lists.class), new Intent(
					this, webview.class)).execute(false);
		}
		if ((requestCode == DIALOGRUN_CODE && resultCode == friendProfil.RETURN_DIALOG_RUN)) {
			downloadProcess = new download(this, new Intent(this,
					notfound.class), new Intent(this, lists.class), new Intent(
					this, webview.class)).execute(true);
		}

		if (requestCode == MAINACTIVITY_EXIT_REQUEST
				&& resultCode == MAINACTIVITY_EXIT_RESULT) {
			Log.d("main", "çıkış yapıldı");
			Toast t = Toast.makeText(this, "Güle Güle ", Toast.LENGTH_LONG);
			t.show();
			finish();
		}
	}

	@Override
	public void onBackPressed() {

		final MainActivity context = this;
		// TODO Auto-generated method stub
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent resultIntent = new Intent();

				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					// Yes button clicked
					// new cache(context).clear(context);
					finish();
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					break;
				}

			}
		};

		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Do you Close?")
				.setPositiveButton("Yes", dialogClickListener)
				.setNegativeButton("No", dialogClickListener).create().show();

		// TODO Auto-generated method stub

	}
}

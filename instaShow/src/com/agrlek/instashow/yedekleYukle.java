package com.agrlek.instashow;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.Toast;

public class yedekleYukle extends ActionBarActivity implements OnClickListener,
		OnCheckedChangeListener {

	private Button devamButton;

	private RadioGroup rg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yedekleyukle);

		Button yedekle = (Button) findViewById(R.id.yedekle);
		Button yukle = (Button) findViewById(R.id.yukle);
		yedekle.setOnClickListener(this);
		yukle.setOnClickListener(this);

	
		setTitle(getResources().getString(R.string.yedekle_yukle));
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.yedekle) {
			yedekle();
		}
		if (v.getId() == R.id.yukle) {
			yukle();
		}
		try {
			if (v.getId() == devamButton.getId()) {
				Log.d("main", "devambutton tıklandı");

				int radioButtonID = rg.getCheckedRadioButtonId();
				View radioButton = rg.findViewById(radioButtonID);
				int idx = rg.indexOfChild(radioButton);

				RadioButton r = (RadioButton) rg.getChildAt(idx);
				String selectedtext = r.getText().toString();

				Bundle b = new Bundle();

				b.putString("fileName", selectedtext);
				Intent intent = new Intent(this, yedekleYukleAnalyze.class);
				intent.putExtras(b);
				startActivityForResult(intent, 664);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isExternalStorageAvailable() {

		String state = Environment.getExternalStorageState();
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			// Something else is wrong. It may be one of many other states, but
			// all we need
			// to know is we can neither read nor write
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}

		if (mExternalStorageAvailable == true
				&& mExternalStorageWriteable == true) {
			return true;
		} else {
			return false;
		}
	}

	private void yukle() {
		LinearLayout l = new LinearLayout(this);

		l.setOrientation(LinearLayout.VERTICAL);
		l.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.WHITE));

		l.setPadding(10, 10, 10, 10);

		File dir = null;
		// if (isExternalStorageAvailable()) {
		// dir = Environment.getExternalStorageDirectory();
		// } else {
		dir = yedekleYukle.this.getCacheDir();
		// }

		File fullDir = new File(dir.getAbsolutePath() + "/instaFame/");

		if (!fullDir.exists()) {
			fullDir.mkdir();
		}

		File[] listDir = fullDir.listFiles();

		ScrollView sc = new ScrollView(this);

		rg = new RadioGroup(this);

		rg.setOnCheckedChangeListener(this);
		for (File file : listDir) {
			if (file.isFile()) {
				RadioButton b = new RadioButton(this);
				b.setText(file.getName());
				rg.addView(b);
			}
		}

		devamButton = new Button(this);
		devamButton.setBackgroundResource(R.drawable.instago);
		devamButton.setText("Devam ..");
		devamButton.setEnabled(false);
		devamButton.setOnClickListener(this);
		sc.addView(rg);

		l.addView(devamButton);

		l.addView(sc);

		Display display = getWindowManager().getDefaultDisplay();

		PopupWindow pop = new PopupWindow(l, display.getWidth(),
				display.getHeight());

		pop.showAtLocation(l, Gravity.CENTER, 0, 0);
	}

	private void yedekle() {

		new AsyncTask<Void, Integer, Void>() {
			private ProgressDialog pd;

			protected void onPreExecute() {
				pd = new ProgressDialog(yedekleYukle.this);
				pd.setProgress(0);
				pd.setMax(1000);
				pd.show();
			};

			protected void onPostExecute(Void result) {
				pd.cancel();
			};

			@Override
			protected void onProgressUpdate(Integer... values) {
				// TODO Auto-generated method stub
				switch (values[0]) {
				case 1: {
					pd.setMessage(getResources().getString(R.string.initiating));
					break;
				}
				case 2: {
					pd.setMessage(getResources()
							.getString(R.string.calculating));
					break;
				}
				case 3: {
					pd.setMessage(getResources().getString(R.string.saving));
					File dir = null;
					// if (isExternalStorageAvailable()) {
					// dir = Environment.getExternalStorageDirectory();
					// } else {
					dir = yedekleYukle.this.getCacheDir();
					// }

					if (dir.exists()) {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy_MMM_dd_HH_mm_ss");
						Calendar cal = Calendar.getInstance();

						File fullDir = new File(dir.getAbsolutePath()
								+ "/instaFame/");
						if (!fullDir.exists()) {
							fullDir.mkdir();
							Log.d("main",
									"dizin oluşturuldu "
											+ fullDir.getAbsolutePath());
						}

						String writeFilename = fullDir.getAbsolutePath() + "/"
								+ sdf.format(cal.getTime()) + ".ins";

						File f = new File(writeFilename);

						Iterator<Entry<String, JSONObject>> it = MainActivity.followedAr
								.entrySet().iterator();

						JSONObject jsFollowed = new JSONObject();
						JSONArray jsFollowedData = new JSONArray();
						while (it.hasNext()) {
							Entry<String, JSONObject> next = it.next();
							JSONObject jsAlt = new JSONObject();
							try {
								jsAlt.put("key", next.getKey());
								jsAlt.put("value", next.getValue());
								jsFollowedData.put(jsAlt);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						JSONObject jsFollows = new JSONObject();
						JSONArray jsFollowsData = new JSONArray();

						it = MainActivity.followsAr.entrySet().iterator();
						while (it.hasNext()) {
							Entry<String, JSONObject> next = it.next();
							JSONObject jsAlt = new JSONObject();
							try {
								jsAlt.put("key", next.getKey());
								jsAlt.put("value", next.getValue());
								jsFollowsData.put(jsAlt);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						try {
							jsFollowed.put("followed", jsFollowedData);
							jsFollowed.put("follows", jsFollowsData);

							FileWriter fw = new FileWriter(f);
							fw.write(jsFollowed.toString());
							fw.close();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						Toast t = Toast.makeText(yedekleYukle.this,
								f.getAbsolutePath(), Toast.LENGTH_LONG);
						t.show();

					} else {
						try {
							throw new IOException();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					break;
				}

				}

				super.onProgressUpdate(values);
			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				for (int i = 0; i < 4; i++) {
					publishProgress(i);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return null;
			}
		}.execute();

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		if (checkedId != -1) {
			devamButton.setEnabled(true);
			Log.d("main", " radio : " + checkedId);
		}
	}
}

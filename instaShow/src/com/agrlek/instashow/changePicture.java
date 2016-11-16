package com.agrlek.instashow;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class changePicture extends AsyncTask<String, Integer, Bitmap> {

	private ImageView imgView;
	private Context context;
	private String cachePath;

	public changePicture(ImageView im, Context context, String cachePath) {
		imgView = im;
		this.context = context;
		this.cachePath = cachePath;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		Bitmap b = null;
		publishProgress(0);
		try {
			bitmapCache bt = new bitmapCache(context);
			if (bt.checkCache(cachePath)) {
				Log.d("main", "Bitmap cache : read  : " + params[0]);
				publishProgress(1);
				b = bt.readCache(cachePath);
			} else {
				Log.d("main", "indiriliyor : " + params[0]);
				URL url = new URL(params[0]);
				URLConnection conn = url.openConnection();
				publishProgress(1);
				b = BitmapFactory.decodeStream(conn.getInputStream());
				bt.writeCache(cachePath, b, Bitmap.CompressFormat.JPEG);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		publishProgress(2);

		// TODO Auto-generated method stub
		return b;
	}


	@Override
	protected void onProgressUpdate(Integer... values) {
		// imgView.setImageResource(R.drawable.abc_btn_radio_material);

		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		imgView.setImageBitmap(result);
		super.onPostExecute(result);
	}

}

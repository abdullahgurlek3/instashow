package com.agrlek.instashow;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

public class bitmapCache {

	private Context context;

	public bitmapCache(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	void getCache() {
		File dir = new File(context.getCacheDir(), "test");
		if (dir.exists()) {
			for (File f : dir.listFiles()) {
				Log.d("main", f.getAbsolutePath());
			}
		}
	}

	void writeCache(String path, Bitmap bitmap, CompressFormat jpeg) {
		try {
			String fullPath = context.getCacheDir() + path;
			FileOutputStream fos = new FileOutputStream(new File(fullPath));
			bitmap.compress(jpeg, 100, fos);
			fos.close();
			Log.d("main", fullPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	Bitmap readCache(String path) {
		String fullPath = context.getCacheDir() + path;
		return BitmapFactory.decodeFile(fullPath);
	}

	boolean checkCache(String path) {
		String fullPath = context.getCacheDir() + path;
		File f = new File(fullPath);
		return f.exists();
	}

}

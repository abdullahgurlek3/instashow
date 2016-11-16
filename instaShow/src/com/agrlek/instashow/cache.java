package com.agrlek.instashow;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class cache {
	private ContextWrapper context;

	public cache(Activity context2) {
		this.context = context2;
		// TODO Auto-generated constructor stub
	}

	public String getStringProperty(String key) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		String res = null;
		if (prefs != null) {
			res = prefs.getString(key, null);
			Log.d("cache", "Cacheden okundu : " + key);
		}
		return res;
	}

	public boolean checkStringProperty(String key) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean r = false;
		if (prefs != null) {
			r = prefs.contains(key);
			Log.d("cache", "Cache durumu : " + key + " : " + r);
		}
		return r;
	}

	public boolean delete(String key) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		if (prefs != null) {
			SharedPreferences.Editor editor = prefs.edit();
			Log.d("cache", "silindi : " + key);
		}
		return false;
	}

	public void setStringProperty(String key, String value) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		if (prefs != null) {
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(key, value);
			editor.commit();
			Log.d("cache", "Cacheye yazıldı : " + key);
		}
	}

	public boolean saveArray(String[] array, String arrayName, Context mContext) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(arrayName + "_size", array.length);
		for (int i = 0; i < array.length; i++)
			editor.putString(arrayName + "_" + i, array[i]);
		return editor.commit();
	}

	public String[] loadArray(String arrayName, Context mContext) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		int size = prefs.getInt(arrayName + "_size", 0);
		String array[] = new String[size];
		for (int i = 0; i < size; i++)
			array[i] = prefs.getString(arrayName + "_" + i, null);
		return array;
	}

	boolean clear(Context mContext) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.edit().clear().commit();
	}

}

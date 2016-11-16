package com.agrlek.instashow;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class block extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ImageView im = new ImageView(this);
		im.setImageResource(R.drawable.noentry);
		setContentView(im);

		Toast t = Toast.makeText(this, "Giriş izniniz yok", Toast.LENGTH_LONG);
		t.show();

	}
}

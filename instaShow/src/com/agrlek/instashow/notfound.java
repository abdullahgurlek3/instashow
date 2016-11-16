package com.agrlek.instashow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class notfound extends Activity implements OnClickListener {
	private final int MAINACTIVITY_RETURN_RESULT = 300;
	private final int MAINACTIVITY_EXIT_RESULT = 320;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nf);

		Toast t = Toast.makeText(this, R.string.downloadFailed,
				Toast.LENGTH_LONG);
		t.show();
		
		ImageButton i = (ImageButton) findViewById(R.id.refresh);
		i.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent resultIntent = new Intent();
		setResult(MAINACTIVITY_RETURN_RESULT, resultIntent);
		finish();
	}

	@Override
	public void onBackPressed() {
		Intent resultIntent = new Intent();
		setResult(MAINACTIVITY_EXIT_RESULT, resultIntent);
		finish();
		// TODO Auto-generated method stub
	}
}

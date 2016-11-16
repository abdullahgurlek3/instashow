package com.agrlek.instashow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class dialogRun extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("main", "dialogrun başlatınldı");
		start();
	}

	public void start() {
		final cache cache = new cache(this);

		// TODO Auto-generated method stub
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent resultIntent = new Intent();

				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					// Yes button clicked
					setResult(friendProfil.RETURN_DIALOG_RUN_JSONPASS_REPLACE,
							resultIntent);
					// cache.delete("passJson");
					finish();
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					setResult(friendProfil.RETURN_DIALOG_RUN, resultIntent);
					// cache.setStringProperty("passJson", "ok");

					finish();
					break;
				}

			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getResources().getString(R.string.reDownload))
				.setPositiveButton("Yes", dialogClickListener)
				.setNegativeButton("No", dialogClickListener).create().show();
	}

}

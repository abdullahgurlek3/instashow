package com.agrlek.instashow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class maksimumPage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					// Yes button clicked
					MainActivity.LOCK_MAXIMUM_PAGE = false;
					startFriendProfil();
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					MainActivity.LOCK_MAXIMUM_PAGE = true;
					finishAc();
					break;
				}

			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getResources().getString(R.string.limitExcepted))
				.setPositiveButton("Yes", dialogClickListener)
				.setNegativeButton("No", dialogClickListener).create().show();

		// TODO Auto-generated method stub
	}

	public void finishAc() {
		finish();
	}

	public void startFriendProfil() {
		new downloadFriend(this, new Intent(this, notfound.class), new Intent(
				this, lists.class), new Intent(this, webview.class)).execute();
	}

}

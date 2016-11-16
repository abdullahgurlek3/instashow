package com.agrlek.instashow;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class instagramEvent implements OnClickListener {

	private Context context;
	private String userName;
	private int userID;

	public instagramEvent(Context context, String userName, int userId) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.userName = userName;
		this.userID = userId;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.instagramGoruntule|v.getId() == R.id.friendProfilFollow|v.getId() == R.id.friendProfilFollow2) {

			// TODO Auto-generated method stub
			Uri uri = Uri.parse("http://instagram.com/_u/" + userName);
			Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

			likeIng.setPackage("com.instagram.android");

			try {
				context.startActivity(likeIng);
			} catch (Exception e) {
				context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
			}
		} else if (v.getId() == R.id.friendProfil) {

			// TODO Auto-generated method stub
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent resultIntent = new Intent();

					Bundle b = new Bundle();
					b.putString("userName", userName);
					b.putInt("userID", userID);

					switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						b.putBoolean("light", true);
						Intent friendProfilLightIntent = new Intent(context,
								friendProfil.class);

						friendProfilLightIntent.putExtras(b);
						context.startActivity(friendProfilLightIntent);
						break;

					case DialogInterface.BUTTON_NEGATIVE:
						b.putBoolean("light", false);
						Intent friendProfilIntent = new Intent(context,
								friendProfil.class);
						friendProfilIntent.putExtras(b);
						context.startActivity(friendProfilIntent);

						break;
					}

				}
			};

			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage("Do you Close?")
					.setPositiveButton(
							context.getResources().getString(
									R.string.showProfile),
							dialogClickListener)
					.setNegativeButton(
							context.getResources().getString(
									R.string.showDetailedProfile),
							dialogClickListener).create().show();

		}

	}

	void finish() {

	}
}

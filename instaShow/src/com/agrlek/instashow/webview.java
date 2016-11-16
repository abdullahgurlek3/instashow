package com.agrlek.instashow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class webview extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		final WebView w = new WebView(this);
		w.clearCache(true);

		CookieSyncManager.createInstance(this);
		CookieSyncManager.getInstance().startSync();

		// CookieManager cm=CookieManager.getInstance();
		// cm.removeAllCookie();

		String url = "https://www.instagram.com/oauth/authorize/?client_id="
				+ MainActivity.client_id
				+ "&redirect_uri=http://www.g-tekno.com/index.php&response_type=token&likes+comments+relationships+basic";
		w.loadUrl(url);

		w.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.d("main", "url gidiliyor " + url);

				Pattern pattern = Pattern.compile("access_token=.*");
				Matcher matcher = pattern.matcher(url);
				if (matcher.find()) {
					String tokenText = matcher.group();
					Log.d("main", "tokenText : " + tokenText);
					String[] tokenAr = tokenText.split("=");
					MainActivity.access_token = tokenAr[1];
					MainActivity.user_id = "self";

					finish();
				} else {
					w.loadUrl(url);
				}
				return true;
			};

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				if (errorCode == ERROR_TIMEOUT) {
					view.stopLoading(); // may not be needed
					String timeoutMessageHtml = "Connection Error. Please reconnect internet. ";
					view.loadData(timeoutMessageHtml, "text/html", "utf-8");
				}
			}
		});
		setContentView(w);
	}
}

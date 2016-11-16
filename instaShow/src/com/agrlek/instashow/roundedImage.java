package com.agrlek.instashow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class roundedImage extends ImageView {

	public static float radius = 64.0f;

	public roundedImage(Context context) {
		super(context);
	}

	public roundedImage(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public roundedImage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// float radius = 36.0f;
		Path clipPath = new Path();
		RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
		clipPath.addRoundRect(rect, radius, radius, Path.Direction.CW);
		canvas.clipPath(clipPath);
		Log.d("main", "yuvarlandı");
		super.onDraw(canvas);
	}
}

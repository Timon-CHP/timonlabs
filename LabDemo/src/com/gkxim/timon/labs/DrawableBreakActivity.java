/**
 * File: DrawableBreakActivity.java
 * Creator: Timon.Trinh (timon@gkxim.com)
 * Date: 17-12-2012
 * 
 */
package com.gkxim.timon.labs;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 *
 */
public class DrawableBreakActivity extends Activity {

	private GridView mGrid = null;
	private ImageAdapter mDrawableAdapter = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawable_break);
		mGrid = (GridView) findViewById(R.id.gv_drawblebreak);
		if (mGrid == null) {
			mGrid = new GridView(this);
			setContentView(mGrid);
		}
		mDrawableAdapter = new ImageAdapter(this);
		mGrid.setAdapter(mDrawableAdapter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		mDrawableAdapter.addBitmap(BitmapFactory.decodeResource(getResources(),
				R.drawable.yellow_30));
		mDrawableAdapter.addBitmap(BitmapFactory.decodeResource(getResources(),
				R.drawable.yellow_40));
		mDrawableAdapter.addBitmap(BitmapFactory.decodeResource(getResources(),
				R.drawable.yellow_60));
		
		mDrawableAdapter.addDrawable(getResources().getDrawable(
				R.drawable.yellow_30_hdpi));
		mDrawableAdapter.addDrawable(getResources().getDrawable(
				R.drawable.yellow_40_hdpi));
		mDrawableAdapter.addDrawable(getResources().getDrawable(
				R.drawable.yellow_60_hdpi));
		
		mDrawableAdapter.addDrawable(getResources().getDrawable(
				R.drawable.yellow));
		mDrawableAdapter.addDrawable(getResources().getDrawable(
				R.drawable.yellow_dither));
		mDrawableAdapter.addDrawable(getResources().getDrawable(
				R.drawable.ic_launcher));
		
		mDrawableAdapter.addDrawable(getResources().getDrawable(
				R.drawable.yellow_hdpi));
		mDrawableAdapter.addDrawable(getResources().getDrawable(
				R.drawable.yellow_hdpi_dither));
		super.onStart();
	}

	public class ImageAdapter extends BaseAdapter {

		private ArrayList<Bitmap> mBitmaps;
		private ArrayList<Drawable> mDrawables = null;

		public ImageAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			// return mThumbIds.length;
			int size = 0;
			if (mBitmaps != null) {
				size += mBitmaps.size();
			}
			if (mDrawables != null) {
				size += mDrawables.size();
			}
			return size;
		}

		public Bitmap getItem(int position) {
			if (mBitmaps == null) {
				return BitmapFactory.decodeResource(getResources(),
						R.drawable.ic_launcher);
			}
			return mBitmaps.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) {
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
				imageView.setAdjustViewBounds(true);
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(10, 10, 10, 10);
				imageView.setBackgroundColor(Color.DKGRAY);
			} else {
				imageView = (ImageView) convertView;
			}
			if (position < mBitmaps.size()) {
				imageView.setImageBitmap(mBitmaps.get(position));
			} else {
				Drawable drbl = mDrawables.get(position - mBitmaps.size());
				imageView.setImageDrawable(drbl);
//				imageView.setImageDrawable(null);
//				imageView.setBackgroundDrawable(drbl);
			}
			return imageView;
		}

		public void addBitmap(Bitmap bitmap) {
			if (mBitmaps == null) {
				mBitmaps = new ArrayList<Bitmap>();
			}
			mBitmaps.add(bitmap);
		}

		public void addDrawable(Drawable drawable) {
			if (drawable == null) {
				return;
			}
			if (mDrawables == null) {
				mDrawables = new ArrayList<Drawable>();
			}
			mDrawables.add(drawable);
		}

		public void clear() {
			if (mBitmaps != null) {
				mBitmaps.clear();
			}
		}

		private Context mContext;

	}
}

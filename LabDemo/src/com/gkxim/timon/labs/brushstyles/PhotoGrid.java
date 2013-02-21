package com.gkxim.timon.labs.brushstyles;

import java.util.ArrayList;

import com.gkxim.timon.labs.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

public class PhotoGrid extends Activity {

	private static final int CONST_BMP_SIZE = 200;
	private ImageAdapter mPhotoAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.photogrid);

		GridView g = (GridView) findViewById(R.id.myGrid);
		g.setNumColumns(2);
		mPhotoAdapter = new ImageAdapter(this);
		g.setAdapter(mPhotoAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// add bitmap
		Bitmap normalbitmap = Bitmap.createBitmap(CONST_BMP_SIZE,
				CONST_BMP_SIZE, Bitmap.Config.ARGB_8888);
		Canvas c1 = new Canvas(normalbitmap);
		Paint p1 = new Paint();
		p1.setColor(Color.WHITE);
		c1.drawRect(
				new Rect(0, 0, normalbitmap.getWidth(), normalbitmap
						.getHeight()), p1);
		
		//Bmp 02
		int[] color01 = new int[normalbitmap.getWidth()*normalbitmap.getHeight()];
		BrusheAlgorithms.alg01_alphaWhite(color01, normalbitmap.getWidth(),
				normalbitmap.getHeight(), 0xf0);
		Bitmap alphaBmp = Bitmap.createBitmap(color01,
				normalbitmap.getWidth(), normalbitmap.getHeight(),
				Bitmap.Config.ARGB_8888);

		//Bmp 03
		Bitmap lineBmp = Bitmap.createBitmap(normalbitmap);
		Canvas cblur = new Canvas(lineBmp);
		Paint pRed = new Paint();
		pRed.setColor(Color.RED);
		pRed.setStyle(Style.STROKE);
		pRed.setStrokeJoin(Join.MITER);
		pRed.setStrokeCap(Cap.ROUND);
		pRed.setStrokeMiter(10);
		pRed.setStrokeWidth(20);
		cblur.drawLine(10, 10, CONST_BMP_SIZE - 10, CONST_BMP_SIZE - 10, pRed);
		
		//Bmp04 - fastBlur
		Bitmap fastBlur = BrusheAlgorithms.alg02_BlurBitmapToBitmap(lineBmp, 20);
		
		mPhotoAdapter.addBitmap(normalbitmap);
		mPhotoAdapter.addBitmap(alphaBmp);
		mPhotoAdapter.addBitmap(lineBmp);
		mPhotoAdapter.addBitmap(fastBlur);

		
		mPhotoAdapter.addDrawable(BrusheAlgorithms.buildGardientDrawable(0xFF912ef5, 0xFF6300c8));
		mPhotoAdapter.addDrawable(BrusheAlgorithms.buildGardientDrawable(0xff61666d, 0xff343940));
		
		mPhotoAdapter.addDrawable(BrusheAlgorithms.buildGardientDrawable(0xFF52cad6, 0xFF259da9));
		mPhotoAdapter.addDrawable(BrusheAlgorithms.buildGardientDrawable(0xff61666d, 0xff343940));
		
		mPhotoAdapter.addDrawable(BrusheAlgorithms.buildGardientDrawable(0xff61666d, 0xff343940));
		mPhotoAdapter.addDrawable(BrusheAlgorithms.buildGardientDrawable(0xff61666d, 0xff343940));
		
		mPhotoAdapter.addDrawable(BrusheAlgorithms.buildGardientDrawable(0xFFc5da41, 0xFF97ac13));
		mPhotoAdapter.addDrawable(BrusheAlgorithms.buildGardientDrawable(0xff61666d, 0xff343940));
		
		mPhotoAdapter.addDrawable(BrusheAlgorithms.buildGardientDrawable(0xFF2eb3f5, 0xFF0086c8));
		mPhotoAdapter.addDrawable(BrusheAlgorithms.buildGardientDrawable(0xff61666d, 0xff343940));
		
		mPhotoAdapter.addDrawable(BrusheAlgorithms.buildGardientDrawable(0xFFf66b2e, 0xFFc83e00));
		mPhotoAdapter.addDrawable(BrusheAlgorithms.buildGardientDrawable(0xff61666d, 0xff343940));
		
		mPhotoAdapter.addDrawable(BrusheAlgorithms.buildGardientDrawable(0xFF2ef691, 0xFF00c863));
		mPhotoAdapter.addDrawable(BrusheAlgorithms.buildGardientDrawable(0xff61666d, 0xff343940));
		
		mPhotoAdapter.addDrawable(BrusheAlgorithms.buildGardientDrawable(0xFFf52e66, 0xFFc80038));
		mPhotoAdapter.addDrawable(BrusheAlgorithms.buildGardientDrawable(0xff61666d, 0xff343940));
		
		mPhotoAdapter.addDrawable(BrusheAlgorithms.buildGardientDrawable(0xFFd74dfa, 0xFFaa1fcc));
		mPhotoAdapter.addDrawable(BrusheAlgorithms.buildGardientDrawable(0xff61666d, 0xff343940));
		
		mPhotoAdapter.addDrawable(BrusheAlgorithms.buildGardientDrawable(0xFF707fcd, 0xFF43519f));
		mPhotoAdapter.addDrawable(BrusheAlgorithms.buildGardientDrawable(0xff61666d, 0xff343940));
		
		mPhotoAdapter.addDrawable(BrusheAlgorithms.buildGardientDrawable(0xFF50e6de, 0xFF23b8b0));
		mPhotoAdapter.addDrawable(BrusheAlgorithms.buildGardientDrawable(0xff61666d, 0xff343940));
		
		mPhotoAdapter.notifyDataSetChanged();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onAttachedToWindow()
	 */
	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		Window window = getWindow();
	    window.setFormat(PixelFormat.RGB_888);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "Add");
		menu.add(0, 2, 0, "Clean");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			mPhotoAdapter.addBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.ic_launcher));
			mPhotoAdapter.notifyDataSetChanged();
			break;
		case 2:
			mPhotoAdapter.clear();
			mPhotoAdapter.notifyDataSetChanged();
			break;
		}
		return super.onOptionsItemSelected(item);
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
				imageView.setLayoutParams(new GridView.LayoutParams(300, 200));
				imageView.setAdjustViewBounds(true);
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(10, 10, 10, 10);
			} else {
				imageView = (ImageView) convertView;
			}
			if (position < mBitmaps.size()) {
				imageView.setImageBitmap(mBitmaps.get(position));
			}else {
				Drawable drbl = mDrawables.get(position-mBitmaps.size());
				imageView.setImageDrawable(null);
				imageView.setBackgroundDrawable(drbl);
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
				mDrawables  = new ArrayList<Drawable>();
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

/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gkxim.timon.labs.brushstyles;

import com.gkxim.timon.labs.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class FingerPaint extends Activity
        implements ColorPickerDialog.OnColorChangedListener {    

    public static final int SQUARE_SIZE_SMALL = 50;
    public static final int SQUARE_SIZE_MED = 75;
    public static final int SQUARE_SIZE_LARGE = 100;
    protected int mSquaresize = SQUARE_SIZE_SMALL;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
        
        mEmboss = new EmbossMaskFilter(new float[] { 1, 1, 1 },
                                       0.4f, 6, 3.5f);

        mBlur = new BlurMaskFilter(50, BlurMaskFilter.Blur.INNER);
    }
    
    private Paint       mPaint;
    private MaskFilter  mEmboss;
    private MaskFilter  mBlur;
	protected Bitmap mPaintBitmap;
    
    public void colorChanged(int color) {
        mPaint.setColor(color);
    }

    public class MyView extends View {
        
        private static final float MINP = 0.25f;
        private static final float MAXP = 0.75f;
        
        private Bitmap  mBitmap;
        private Canvas  mCanvas;
        private Path    mPath;
        private Paint   mBitmapPaint;
		private Point mTouchPoint;
		private Rect mTouchSquare;
		
		private Paint mRectPaint;
		private Drawable mDrawablePattern;
        
        public MyView(Context c) {
            super(c);
            Display d = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
            mBitmap = Bitmap.createBitmap(d.getWidth(), d.getHeight(), Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            
            mTouchSquare = new Rect(0,0, mSquaresize ,mSquaresize);
            mPaintBitmap = Bitmap.createBitmap(mSquaresize, mSquaresize, Bitmap.Config.ARGB_8888);
            mTouchPoint = new Point();
            mRectPaint = new Paint();
//            mRectPaint.setAntiAlias(true);
//            mRectPaint.setDither(true);
            mRectPaint.setColor(0xFFFFFF00);
            mRectPaint.setStyle(Paint.Style.STROKE);
            mRectPaint.setStrokeCap(Paint.Cap.SQUARE);
            mRectPaint.setStrokeWidth(2);
            
            mDrawablePattern = getResources().getDrawable(R.drawable.ic_launcher);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
        }
        
        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(0xFFAAAAAA);
            
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
//            drawBitmapOnPath(canvas, mPath, mDrawablePattern);
            
//            drawBitmapAlongPath(mPath, mDrawablePattern);
            
            canvas.drawPath(mPath, mPaint);
            
            //touch point
            canvas.drawRect(mTouchSquare, mRectPaint);
        }
        
		private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;
        
        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }
        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                mX = x;
                mY = y;
            }
        }
        private void touch_up() {
            mPath.lineTo(mX, mY);
            // commit the path to our offscreen
//            mCanvas.drawPath(mPath, mPaint);
            drawBitmapOnPath(mCanvas, mPath, mDrawablePattern);
            // kill this so we don't double draw
            mPath.reset();
        }
        
        private void drawBitmapOnPath(Canvas c, Path path,
				Drawable texture) {
			if (c == null) {
				return;
			}
        	Paint p = new Paint();
			p.setStyle(Style.STROKE);
			p.setShader(new BitmapShader(drawableToBitmap(texture), TileMode.CLAMP, TileMode.CLAMP));
			p.setStrokeWidth(texture.getIntrinsicWidth());
			c.drawPath(path, p);
		}

		@Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                	mTouchPoint.set( (int)x, (int)y);
//                	mTouchSquare.set( (int)x, (int)y, (int)(x + mSquaresize), (int)(y+ mSquaresize));
                	centerlizeTouchSquare(x,y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
//                    mTouchSquare.set( (int)x, (int)y, (int)(x + mSquaresize), (int)(y+ mSquaresize));
                    centerlizeTouchSquare(x,y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }

		private void centerlizeTouchSquare(float xf, float yf) {
			int centerX = (int) xf;
			int centerY = (int) yf;
			int left = centerX - mSquaresize /2;
			int top = centerY - mSquaresize /2;
			int right = centerX + mSquaresize /2;
			int bottom = centerY + mSquaresize /2;
			
			mTouchSquare.set(left, top, right, bottom);
		}
    }
    
    private static final int COLOR_MENU_ID = Menu.FIRST;
    private static final int EMBOSS_MENU_ID = Menu.FIRST + 1;
    private static final int BLUR_MENU_ID = Menu.FIRST + 2;
    private static final int ERASE_MENU_ID = Menu.FIRST + 3;
    private static final int SRCATOP_MENU_ID = Menu.FIRST + 4;
    private static final int SMALL_MENU_ID = Menu.FIRST + 5;
    private static final int MEDIUM_MENU_ID = Menu.FIRST + 6;
    private static final int LARGE_MENU_ID = Menu.FIRST + 7;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        
        menu.add(0, COLOR_MENU_ID, 0, "Color").setShortcut('3', 'c');
        menu.add(0, SMALL_MENU_ID, 0, "SMALL");
        menu.add(0, MEDIUM_MENU_ID, 0, "MEDIUM");
        menu.add(0, LARGE_MENU_ID, 0, "LARGE");
//        menu.add(0, EMBOSS_MENU_ID, 0, "Emboss").setShortcut('4', 's');
//        menu.add(0, BLUR_MENU_ID, 0, "Blur").setShortcut('5', 'z');
//        menu.add(0, ERASE_MENU_ID, 0, "Erase").setShortcut('5', 'z');
//        menu.add(0, SRCATOP_MENU_ID, 0, "SrcATop").setShortcut('5', 'z');

        /****   Is this the mechanism to extend with filter effects?
        Intent intent = new Intent(null, getIntent().getData());
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        menu.addIntentOptions(
                              Menu.ALTERNATIVE, 0,
                              new ComponentName(this, NotesList.class),
                              null, intent, 0, null);
        *****/
        return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xFF);

        switch (item.getItemId()) {
            case COLOR_MENU_ID:
                new ColorPickerDialog(this, this, mPaint.getColor()).show();
                return true;
            case EMBOSS_MENU_ID:
                if (mPaint.getMaskFilter() != mEmboss) {
                    mPaint.setMaskFilter(mEmboss);
                } else {
                    mPaint.setMaskFilter(null);
                }
                return true;
            case BLUR_MENU_ID:
                if (mPaint.getMaskFilter() != mBlur) {
                    mPaint.setMaskFilter(mBlur);
                } else {
                    mPaint.setMaskFilter(null);
                }
                return true;
            case ERASE_MENU_ID:
                mPaint.setXfermode(new PorterDuffXfermode(
                                                        PorterDuff.Mode.CLEAR));
                return true;
            case SRCATOP_MENU_ID:
                mPaint.setXfermode(new PorterDuffXfermode(
                                                    PorterDuff.Mode.SRC_ATOP));
                mPaint.setAlpha(0x80);
                return true;
            case SMALL_MENU_ID:
            	mSquaresize = SQUARE_SIZE_SMALL;
            	mPaintBitmap = Bitmap.createBitmap(mSquaresize, mSquaresize, Bitmap.Config.ARGB_8888);
            	return true;
            case MEDIUM_MENU_ID:
            	mSquaresize = SQUARE_SIZE_MED;
            	mPaintBitmap = Bitmap.createBitmap(mSquaresize, mSquaresize, Bitmap.Config.ARGB_8888);
            	return true;
            case LARGE_MENU_ID:
            	mSquaresize = SQUARE_SIZE_LARGE;
            	mPaintBitmap = Bitmap.createBitmap(mSquaresize, mSquaresize, Bitmap.Config.ARGB_8888);
            	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
	public static Bitmap drawableToBitmap (Drawable drawable) {
	    if (drawable instanceof BitmapDrawable) {
	        return ((BitmapDrawable)drawable).getBitmap();
	    }

	    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap); 
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);

	    return bitmap;
	}
}

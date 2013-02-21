package com.gkxim.timon.labs;

import com.gkxim.timon.labs.brushstyles.FingerPaint;
import com.gkxim.timon.labs.brushstyles.PhotoGrid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LabDemoActivity extends Activity {
	public static final String LOG_TAG = "LABS";
	private int mIndex = 0;
	private Intent mXMLLayoutIntent;
	private Intent mInputEditorsIntent;
	private Intent mFingerPaintIntent;
	private Intent mPhotoGridIntent;
	private Intent mTestFontIntent;
	private Intent mDrawbleBreakIntent;
	private Intent mAnimationIntent;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

	}

	public void clickMe(View view) {
		if (view.getId() == R.id.btnchange) {
			String text = ((EditText) findViewById(R.id.edname)).getText()
					.toString();
			mIndex++;
			((TextView) findViewById(R.id.txthello)).setText(String.format(
					getResources().getString(R.string.myhello), text, mIndex));
		} else if (view.getId() == R.id.btn_load_layout) {
			if (mXMLLayoutIntent == null) {
				mXMLLayoutIntent = new Intent(this, MyXMLLayoutActivity.class);
			}
			mXMLLayoutIntent.putExtra(MyXMLLayoutActivity.XML_FILEPATH,
					Environment.getExternalStorageDirectory().getAbsolutePath()
							+ "/myfile.xml");
			startActivity(mXMLLayoutIntent);
		} else if (view.getId() == R.id.btn_open_inputeditor) {
			if (mInputEditorsIntent == null) {
				mInputEditorsIntent = new Intent(this, MyInputEditors.class);
			}
			startActivity(mInputEditorsIntent);
		} else if (view.getId() == R.id.btn_open_fingerpaint) {
			if (mFingerPaintIntent == null) {
				mFingerPaintIntent = new Intent(this, FingerPaint.class);
			}
			startActivity(mFingerPaintIntent);
		} else if (view.getId() == R.id.btn_open_photogrid) {
			if (mPhotoGridIntent == null) {
				mPhotoGridIntent = new Intent(this, PhotoGrid.class);
			}
			startActivity(mPhotoGridIntent);
		} else if (view.getId() == R.id.btn_open_testfont) {
			if (mTestFontIntent == null) {
				mTestFontIntent = new Intent(this, FontTestActivity.class);
			}
			startActivity(mTestFontIntent);
		} else if (view.getId() == R.id.btn_open_drawablebreak) {
			if (mDrawbleBreakIntent == null) {
				mDrawbleBreakIntent = new Intent(this,
						DrawableBreakActivity.class);
			}
			startActivity(mDrawbleBreakIntent);
		} else if (view.getId() == R.id.btn_open_animation) {
			if (mAnimationIntent == null) {
				mAnimationIntent = new Intent(this,
						AnimationMainActivity.class);
			}
			startActivity(mAnimationIntent);
		}
	}
}
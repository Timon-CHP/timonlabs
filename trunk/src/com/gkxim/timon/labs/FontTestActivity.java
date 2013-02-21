package com.gkxim.timon.labs;

import com.gkxim.timon.utils.MyLogger;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class FontTestActivity extends Activity {

	public static final String FONT_NAME_THANHNIEN = "thanhnien_regular.ttf";
	private static final String TAG = "FontTestActivity";
	private EditText mEditText;
	private Spinner mSpinner;
	private CheckBox mCBBold;
	private CheckBox mCBItalic;
	private Spinner mMultipleLineSpace;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_font_test);

		mSpinner = (Spinner) findViewById(R.id.spinner1);
		if (mSpinner != null) {
			mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					if (arg1 != null) {
						CharSequence text = ((TextView) arg1).getText();
						if (text != null) {
							setEditTextFont(text.toString());
						}
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});
		}
		mMultipleLineSpace = (Spinner) findViewById(R.id.mulLines);
		if (mMultipleLineSpace != null) {
			mMultipleLineSpace
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							if (arg1 != null) {
								CharSequence text = ((TextView) arg1).getText();
								if (text != null) {
									float value = Float.parseFloat((String) text);
									mEditText.setLineSpacing(0, value);
								}
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
						}
					});
		}
		mEditText = (EditText) findViewById(R.id.etFontTest);
		if (mEditText != null && mEditText.getText().length() == 0) {
			mEditText.setTextColor(Color.BLACK);
			mEditText.setBackgroundColor(Color.WHITE);
			mEditText.setText(getInfoDebug() + "\r\n\r\n" + getResources().getString(R.string.story_text));
			setEditTextFont(FONT_NAME_THANHNIEN);
		}
		mCBBold = (CheckBox) findViewById(R.id.fontBold);
		if (mCBBold != null) {
			mCBBold.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					Typeface tf = mEditText.getTypeface();
					if (isChecked) {
						if (mCBItalic != null && mCBItalic.isChecked()) {
							mEditText.setTypeface(tf, Typeface.BOLD_ITALIC);
						} else {
							mEditText.setTypeface(tf, Typeface.BOLD);
						}
					} else {
						if (mCBItalic != null && mCBItalic.isChecked()) {
							mEditText.setTypeface(tf, Typeface.NORMAL
									| Typeface.ITALIC);
						} else {
							mEditText.setTypeface(tf, Typeface.NORMAL);
						}
					}
				}
			});
		}
		mCBItalic = (CheckBox) findViewById(R.id.fontItalic);
		if (mCBItalic != null) {
			mCBItalic.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					Typeface tf = mEditText.getTypeface();
					if (isChecked) {
						if (mCBBold != null && mCBBold.isChecked()) {
							mEditText.setTypeface(tf, Typeface.BOLD_ITALIC);
						} else {
							mEditText.setTypeface(tf, Typeface.ITALIC);
						}
					} else {
						if (mCBBold != null && mCBBold.isChecked()) {
							mEditText.setTypeface(tf, Typeface.NORMAL
									| Typeface.BOLD);
						} else {
							mEditText.setTypeface(tf, Typeface.NORMAL);
						}
					}
				}
			});
		}
	}

	private void setEditTextFont(String fontName) {
		Typeface fontface = Typeface.createFromAsset(getAssets(), fontName);
		if (fontface != null && mEditText != null) {
			mEditText.setTypeface(fontface);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_font_test, menu);
		return true;
	}

	private String getInfoDebug() {
		StringBuilder sb = new StringBuilder();

		Configuration conf = getResources().getConfiguration();
		int iscreenlayout = conf.screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;
		String screenlayout = "";
		if (iscreenlayout == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
			screenlayout = "normal";
		} else if (iscreenlayout == Configuration.SCREENLAYOUT_SIZE_LARGE) {
			screenlayout = "large";
		} else if (iscreenlayout == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
			screenlayout = "xlarge";
		} else {
			screenlayout = "small";
		}
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		String densityQualifier = "";
		if (metrics.densityDpi == DisplayMetrics.DENSITY_LOW) {
			densityQualifier = "ldpi";
		} else if (metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM) {
			densityQualifier = "mdpi";
		} else if (metrics.densityDpi == DisplayMetrics.DENSITY_HIGH) {
			densityQualifier = "hdpi";
		} else if (metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {
			densityQualifier = "xhdpi";
		} else {
			densityQualifier = "default-" + metrics.densityDpi;
		}
		sb.append("showInfoDebug: " + "[{" + " locale.getDisplayLanguage(): "
				+ conf.locale.getDisplayLanguage()
				+ ", conf.locale.getLanguage(): "
				+ conf.locale.getLanguage()
				+ ", conf.screenLayout: "
				+ screenlayout
				+ ", conf.orientation: "
				+ (conf.orientation == Configuration.ORIENTATION_PORTRAIT ? "portrait"
						: "landscape") + ", conf.fontScale: " + conf.fontScale
				+ ", density: " + metrics.density + ", scaledDensity: "
				+ metrics.scaledDensity + ", densityDpi: " + densityQualifier
				+ ", heightPixels: " + metrics.heightPixels + ", widthPixels: "
				+ metrics.widthPixels + ", xdpi: " + metrics.xdpi + ", ydpi: "
				+ metrics.ydpi

				+ "},  " + conf.toString() + "]");
		MyLogger.lf(this.getApplicationContext(), 1, TAG + "=>" + sb.toString());

		return sb.toString();
	}
}

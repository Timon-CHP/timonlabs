/**
 * File: AndroiderSamplesActivity.java
 * Creator: Timon.Trinh (timon@gkxim.com)
 * Date: 16-10-2013
 * 
 */
package com.gkxim.timon.labs;

import java.util.ArrayList;
import java.util.List;

import com.gkxim.timon.labs.androider.BlurBitmapActivity;
import com.gkxim.timon.labs.androider.ConvolutionBlurBitmapActivity;
import com.gkxim.timon.labs.androider.USBConnectActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/**
 *
 */
public class AndroiderSamplesActivity extends Activity {

	private static final int ANDROIDER_ID = 101;
	private static final String ANDROIDER_LABEL = "-> ";

	private LinearLayout mLLGroup;
	private View.OnClickListener mDefaultOnClickListener = getDefaultOnClickListener();
	private List<View> mViews;

	// open intents
	private Intent mUSBConnectIntent;
	protected Intent mBlurBitmapIntent;
	protected Intent mConvolutionBlurBitmapIntent;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_androider);
		mLLGroup = (LinearLayout) findViewById(R.id.llgroup);
		mViews = new ArrayList<View>();

		// add Buttons, no need to change anyhing in here
		addButtons(this, mLLGroup);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		View[] vs = new View[mViews.size()];
		listenViewClick(mViews.toArray(vs));
		super.onResume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		View[] vs = new View[mViews.size()];
		removeViewClickListener(mViews.toArray(vs));
		super.onStop();
	}

	private void listenViewClick(View... vs) {
		if (vs == null || vs.length <= 0) {
			return;
		}
		for (View v : vs) {
			if (v != null) {
				v.setOnClickListener(mDefaultOnClickListener);
			}
		}
	}

	private void removeViewClickListener(View... vs) {
		if (vs == null || vs.length <= 0) {
			return;
		}
		for (View v : vs) {
			if (v != null) {
				v.setOnClickListener(null);
			}
		}

	}

	private void addButton(Context context, ViewGroup vg, int id, String label) {
		if (context == null || vg == null) {
			return;
		}

		Button btn = new Button(context);
		btn.setText(label);
		btn.setId(id);
		btn.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));

		vg.addView(btn);
		mViews.add(btn);
	}

	// Add more buttons and process from here

	private void addButtons(Context context, ViewGroup vg) {
		
		addButton(context, vg, ANDROIDER_ID + 1, ANDROIDER_LABEL
				+ "USB Connect");
		addButton(context, vg, ANDROIDER_ID + 2, ANDROIDER_LABEL
				+ "Blur Bitmap");
		addButton(context, vg, ANDROIDER_ID + 3, ANDROIDER_LABEL
				+ "Convolution Blur Bitmap");
	}

	private OnClickListener getDefaultOnClickListener() {
		return (new OnClickListener() {

			@Override
			public void onClick(View view) {
				switch (view.getId()) {
					case (ANDROIDER_ID + 1):
						if (mUSBConnectIntent == null) {
							mUSBConnectIntent = new Intent(
									AndroiderSamplesActivity.this,
									USBConnectActivity.class);
						}
						// Don't forget to add Activity in XML.
						startActivity(mUSBConnectIntent);
						break;
						
					case (ANDROIDER_ID + 2):
						if (mBlurBitmapIntent == null) {
							mBlurBitmapIntent = new Intent(
									AndroiderSamplesActivity.this,
									BlurBitmapActivity.class);
						}
					// Don't forget to add Activity in XML.
					startActivity(mBlurBitmapIntent);
					break;
					
					case (ANDROIDER_ID + 3):
						if (mConvolutionBlurBitmapIntent == null) {
							mConvolutionBlurBitmapIntent = new Intent(
									AndroiderSamplesActivity.this,
									ConvolutionBlurBitmapActivity.class);
						}
					// Don't forget to add Activity in XML.
					startActivity(mConvolutionBlurBitmapIntent);
					break;
					default:
						break;
				}
			}
		});
	}

}

package com.gkxim.timon.labs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MyXMLLayoutActivity extends Activity {

	public static final String XML_FILEPATH = "file_path";
	private String mXMLFile;
	private View mView;
	private ViewGroup mViewGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d(LabDemoActivity.LOG_TAG, getClass().getSimpleName()
				+ "-> onCreate");

		Intent intentInput = getIntent();
		if (intentInput.hasExtra(XML_FILEPATH)) {
			mXMLFile = intentInput.getStringExtra(XML_FILEPATH);
			Log.d(LabDemoActivity.LOG_TAG, "file: " + mXMLFile);
			File f = new File(mXMLFile);
			if (f != null && f.exists()) {

				try {
//					FileInputStream fis = new FileInputStream(f);
					FileReader fr = new FileReader(f);
//					if (fis != null) {
					if (fr != null) {
						LayoutInflater linflate = getLayoutInflater();
						XmlPullParserFactory factory = XmlPullParserFactory
								.newInstance();
						XmlPullParser parse = factory.newPullParser();
//						parse.setInput(fis, "utf-8");
						parse.setInput(fr);
						
						mView  = linflate.inflate(parse, mViewGroup);
						setContentView(mView);
					}
				} catch (FileNotFoundException e) {
					Log.e(LabDemoActivity.LOG_TAG,
							"no file found: " + e.getMessage());
				} catch (XmlPullParserException e) {
					Log.e(LabDemoActivity.LOG_TAG,
							"XMLPullPaser failed: " + e.getMessage());
				} catch (InflateException e) {
					Log.e(LabDemoActivity.LOG_TAG,
							"InflateException failed: " + e.getMessage());
				}

			} else {
				Log.d(LabDemoActivity.LOG_TAG,
						"no file found.");
			}
		} else {
			Log.d(LabDemoActivity.LOG_TAG, "no file input");
			mXMLFile = null;
		}

	}

	public void clickMe(View view) {
		if (view.getClass().equals(Button.class)) {
			Log.d(LabDemoActivity.LOG_TAG, "button clicked");
		} else {
			Log.d(LabDemoActivity.LOG_TAG, "clicked on "
					+ view.getClass().getSimpleName());
		}

	}

}

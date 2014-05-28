package com.gkxim.timon.labs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import com.gkxim.timon.labs.brushstyles.FingerPaint;
import com.gkxim.timon.labs.brushstyles.PhotoGrid;

public class LabDemoActivity extends Activity {
    public static final String LOG_TAG = "LABS";
    private Intent mXMLLayoutIntent;
    private Intent mInputEditorsIntent;
    private Intent mFingerPaintIntent;
    private Intent mPhotoGridIntent;
    private Intent mTestFontIntent;
    private Intent mDrawbleBreakIntent;
    private Intent mAnimationIntent;
    private Intent mSocialAuth_ShareButtonIntent;
    private Intent mSocialAuth_ShareBarIntent;
    private Intent mSocialAuth_ShareCustomIntent;
    private Intent mAndroiderIntent;
    private Intent mOpenFullLinkIntent;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

    }

    public void clickMe(View view) {
        if (view.getId() == R.id.btn_load_layout) {
            if (mXMLLayoutIntent == null) {
                mXMLLayoutIntent = new Intent(this, MyXMLLayoutActivity.class);
            }
            mXMLLayoutIntent.putExtra(MyXMLLayoutActivity.XML_FILEPATH,
                    Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/myfile.xml"
            );
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
        } else if (view.getId() == R.id.btn_open_sharebutton) {
            if (mSocialAuth_ShareButtonIntent == null) {
                mSocialAuth_ShareButtonIntent = new Intent(this,
                        ShareButtonActivity.class);
            }
            startActivity(mSocialAuth_ShareButtonIntent);
        } else if (view.getId() == R.id.btn_open_sharebar) {
            if (mSocialAuth_ShareBarIntent == null) {
                mSocialAuth_ShareBarIntent = new Intent(this,
                        ShareBarActivity.class);
            }
            startActivity(mSocialAuth_ShareBarIntent);
        } else if (view.getId() == R.id.btn_open_sharecustom) {
            if (mSocialAuth_ShareCustomIntent == null) {
                mSocialAuth_ShareCustomIntent = new Intent(this,
                        ShareBarActivity.class);
            }
            startActivity(mSocialAuth_ShareCustomIntent);
        } else if (view.getId() == R.id.btn_open_usbdevice) {
            if (mAndroiderIntent == null) {
                mAndroiderIntent = new Intent(this,
                        AndroiderSamplesActivity.class);
            }
            startActivity(mAndroiderIntent);
        } else {
            if (view.getId() == R.id.btn_open_fulllinkwebview) {
                if (mOpenFullLinkIntent == null) {
                    mOpenFullLinkIntent = new Intent(this,
                            FullWebView.class);
                }
                AlertDialog.Builder aldlgbuilder = new AlertDialog.Builder(this);
                final EditText etv = new EditText(this);
                etv.setText(R.string.urldefault);
                AlertDialog aldlg = aldlgbuilder.setView(etv).setTitle("Url load")
                        .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (etv.getText() != null) {
                                    String url = etv.getText().toString();
                                    if (!TextUtils.isEmpty(url)) {
                                        mOpenFullLinkIntent.putExtra("url", url);
                                        startActivity(mOpenFullLinkIntent);
                                    }
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                aldlg.show();

            }
        }
    }

    public int addMethodForTest(int a, int b) {
        return ((a) + (b));
    }
}
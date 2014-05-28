package com.gkxim.timon.labs;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import com.gkxim.timon.labs.R;

/**
 * Created by HP on 5/26/2014.
 */
public class FullWebView extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullwebview);
        String url = "about:blank";
        if (getIntent().hasExtra("url")){
            url = getIntent().getStringExtra("url");
        }
        Log.i("com.gkxim.timon.labs.FullWebView", "Loading URL: " + url);
        WebView wv = (WebView)findViewById(R.id.webview);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl(url);
    }
}
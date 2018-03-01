package com.tinystartup.juniormaster.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.tinystartup.juniormaster.R;

public class TakeChallengeActivity extends AppCompatActivity {
    private static final String api_token = "o6H5cDMnDkTGlJCsMB0M4Ur69gUWRK3b";
    private static final String url = "http://demo.junior-master.markchen.cc/challenge";

    private int mID;
    private WebView mWebView;

    @JavascriptInterface
    public String getUrl() {
        return  url + Integer.toString(mID) + ".json";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mID = getIntent().getIntExtra("id", 0);

        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.addJavascriptInterface(this, "challenge");
        mWebView.loadUrl("file:///android_asset/challenge.html");
    }
}

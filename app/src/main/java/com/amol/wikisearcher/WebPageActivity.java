package com.amol.wikisearcher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebPageActivity extends AppCompatActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_page);
        webView = findViewById(R.id.webview);
        String pageId = getIntent().getStringExtra("pageId");
        webView.loadUrl("https://en.wikipedia.org/?curid="+pageId);
    }
}

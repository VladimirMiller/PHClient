package com.example.user.rhclient.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.user.rhclient.R;

public class WebViewActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://www.producthunt.com/posts/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent intent = getIntent();

        WebView webView = (WebView) findViewById(R.id.web_view);

        webView.loadUrl(BASE_URL + intent.getIntExtra("id", 1));
    }
}

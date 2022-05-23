package com.trng.znews.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.trng.znews.R;

public class NewsDetailActivity extends AppCompatActivity {
    WebView newsWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        newsWebView = findViewById(R.id.newsWebView);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String link = bundle.getString("link");
        Log.e("---------------------" , link);

        newsWebView.getSettings().setLoadsImagesAutomatically(true);
        newsWebView.getSettings().setJavaScriptEnabled(true);
        newsWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        newsWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }
        });
        newsWebView.loadUrl(link);
    }
}
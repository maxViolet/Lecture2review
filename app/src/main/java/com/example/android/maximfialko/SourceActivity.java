package com.example.android.maximfialko;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class SourceActivity extends AppCompatActivity {

    private WebView webView;
    private static final String URL_EXTRAS = "url_extras";
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);

        webView = (WebView) findViewById(R.id.web_source);

        webView.loadUrl(url);
    }


}

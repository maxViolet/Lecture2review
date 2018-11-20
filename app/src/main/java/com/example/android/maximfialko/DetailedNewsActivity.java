package com.example.android.maximfialko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class DetailedNewsActivity extends AppCompatActivity {

    private static final String URL_EXTRAS = "url_extras";

    public static void start(Activity activity, String url) {
        Intent intent = new Intent(activity, DetailedNewsActivity.class);
        intent.putExtra(URL_EXTRAS,url);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_news);
        setTheme(R.style.AppThemeNoActionBar);

        final String Url = getIntent().getStringExtra(URL_EXTRAS);

        final WebView webView = (WebView) findViewById(R.id.web_view);
        webView.loadUrl(Url);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_switch:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

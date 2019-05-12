package com.example.android.maximfialko.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.example.android.maximfialko.R;
import com.example.android.maximfialko.ui.about.AboutActivity;

public class GoToSourceActivity extends AppCompatActivity {

    private WebView webView;
    private static final String URL_EXTRAS = "url_extras";
    private String url;

    public static void start(Activity activity, String url) {
        Intent intent = new Intent(activity, GoToSourceActivity.class);
        intent.putExtra(URL_EXTRAS,url);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gotosource_activity);

        webView = findViewById(R.id.web_source);
        url = getIntent().getStringExtra(URL_EXTRAS);

        webView.loadUrl(url);
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

package com.example.android.maximfialko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.maximfialko.data.NewsItem;
import com.example.android.maximfialko.data.NewsItemList;

public class DetailedNewsActivity extends AppCompatActivity {

    private static final String ID_EXTRAS = "id_extras";
    private static final int detItem_id = 0;

    public static void start(Activity activity, int id) {
        Intent intent = new Intent(activity, DetailedNewsActivity.class);
        intent.putExtra(ID_EXTRAS,id);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_news);
        setTheme(R.style.AppThemeNoActionBar);

        final ImageView b_image = (ImageView) findViewById(R.id.iv_det_photo);
        final TextView b_title = (TextView) findViewById(R.id.tv_cont_title);
        final TextView b_date = (TextView) findViewById(R.id.tv_cont_date);
        final TextView b_fullText = (TextView) findViewById(R.id.tv_cont_fulltext);

        final int Id = getIntent().getIntExtra(ID_EXTRAS, detItem_id);

        NewsItem detNewsItem = NewsItemList.getNewsItemById(Id);

        assert detNewsItem != null;
        Glide.with(this).load(detNewsItem.getImageUrl()).into(b_image);
        b_title.setText(detNewsItem.getTitle());
//        b_date.setText(detNewsItem.getPublishDate());
        b_fullText.setText(detNewsItem.getFullText());

//        Toolbar det_toolbar = (Toolbar) findViewById(R.id.det_toolbar);
//        setSupportActionBar(det_toolbar);
    }

    @Override //для элемента меню
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override //для элемента меню
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_switch:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    private void displayUserInfo(User user) {
//        getSupportActionBar().setTitle(user.getName());
//    }
}

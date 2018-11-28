package com.example.android.maximfialko;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.maximfialko.data.Margins;
import com.example.android.maximfialko.data.NewsItemList;
import com.example.android.maximfialko.data.NewsAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewsListActivity extends AppCompatActivity{

    private NewsAdapter adapter;

    //переход на DetailedNewsActivity
    private final NewsAdapter.newItemClickListener clickListener = newsItem -> openDetailedNewsActivity(newsItem.getItemId());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list_activity);

        RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new NewsAdapter(this, NewsItemList.generateNews(), clickListener);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            rv.setLayoutManager(new LinearLayoutManager(this));
        } else {
            rv.setLayoutManager(new GridLayoutManager(this, 2));
        }

        rv.setAdapter(adapter);

        //add Margins to Recycler View
        Margins decoration = new Margins(24, 1);
        rv.addItemDecoration(decoration);
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

    public void openDetailedNewsActivity(int id) {
        DetailedNewsActivity.start(this, id);
    }
}



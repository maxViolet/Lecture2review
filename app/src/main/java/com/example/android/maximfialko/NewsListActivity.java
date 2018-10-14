package com.example.android.maximfialko;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.maximfialko.data.Margins;
import com.example.android.maximfialko.data.NewsItem;
import com.example.android.maximfialko.data.NewsItemList;
import com.example.android.maximfialko.data.NewsAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
public class NewsListActivity extends AppCompatActivity {

    private NewsAdapter adapter;

    //переход на DetailedNewsActivity
    private final NewsAdapter.OnItemClickListener clickListener = new NewsAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(NewsItem position) {
            openDetailedNewsActivity(position.getItemId());
       }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list_activity);

        RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new NewsAdapter(this, NewsItemList.generateNews(), clickListener);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);

        rv.setAdapter(adapter);

        //add Margins to RecyclerView
        Margins decoration = new Margins(32, 1);
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

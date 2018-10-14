package com.example.android.maximfialko;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import com.example.android.maximfialko.data.NewsItem;
import com.example.android.maximfialko.data.NewsItemList;
import com.example.android.maximfialko.data.RVAdapter;
import com.example.android.maximfialko.data.margins;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
public class NewsListActivity extends AppCompatActivity {

    private RVAdapter adapter;

    //переход на DetailedNewsActivity
    private final RVAdapter.OnItemClickListener clickListener = new RVAdapter.OnItemClickListener() {
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
        adapter = new RVAdapter(this, NewsItemList.generateNews(), clickListener);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);

        rv.setAdapter(adapter);

        //add margins to RecyclerView
        margins decoration = new margins(32, 1);
        rv.addItemDecoration(decoration);
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

    public void openDetailedNewsActivity(int id) {
        DetailedNewsActivity.start(this, id);
    }
}

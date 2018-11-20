package com.example.android.maximfialko;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.example.android.maximfialko.Utils.DensityPixelMath;
import com.example.android.maximfialko.Utils.Visibilty;
import com.example.android.maximfialko.data.Margins;
import com.example.android.maximfialko.data.NewsAdapter;
import com.example.android.maximfialko.data.NewsItem;
import com.example.android.maximfialko.data.NewsItemList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewsListActivity extends AppCompatActivity {

    @Nullable
    private NewsAdapter adapter;
    @Nullable
    private Disposable disposable;
    @Nullable
    private ProgressBar progress;
    @Nullable
    private RecyclerView rv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list_activity);

        progress = (ProgressBar) findViewById(R.id.progress);
        rv = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new NewsAdapter(this, new ArrayList<>(), clickListener);

        //ps to dp //util class
        DensityPixelMath DPmath = new DensityPixelMath(getApplicationContext());
        //add Margins to Recycler View
        Margins decoration = new Margins((int) DPmath.dpFromPx(45), 1);

        rv.addItemDecoration(decoration);
        rv.setAdapter(adapter);

        loadItems();

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            rv.setLayoutManager(new LinearLayoutManager(this));
        } else {
            rv.setLayoutManager(new GridLayoutManager(this, 2));
        }
    }

    //переход на DetailedNewsActivity
    private final NewsAdapter.OnItemClickListener clickListener = position -> openDetailedNewsActivity(position.getItemId());

    //асинхронная загрузка списка новостей
    private void loadItems() {
        showProgress(true);
        disposable = Observable/*.timer(9000, TimeUnit.MILLISECONDS)*/.fromCallable(() -> {
            Thread.sleep(2000);
            Log.d("addNews", "Generate news: " + Thread.currentThread().getName());
            //асинхронное получение данных из списка новостей
            return new NewListCallable(NewsItemList.generateNews());
        })
                //указываем поток, где будет исполняться код
                .subscribeOn(Schedulers.io())
//                .delay(2000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                //указываем поток, куда приходят данные, все что ниже ObserveOn будет выполняться в потоке, который здесь прописан
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(news -> {
                    Log.d("addNews", "Subscribe: " + Thread.currentThread().getName());
                    getAdapter().add(news.data);
                    Visibilty.setVisible(progress, false);
                    Visibilty.setVisible(rv, true);
                }, error -> Log.d("onError", error.toString()));
    }

    @Override
    protected void onStop() {
        super.onStop();
        showProgress(false);
        disposable.dispose();
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

    //метод перехода на активити DetailedNews
    public void openDetailedNewsActivity(int id) {
        DetailedNewsActivity.start(this, id);
    }

    //Callable обертка для метода NewsAdapter.add
    private static class NewListCallable implements Callable<List<NewsItem>> {
        private final List<NewsItem> data;

        public NewListCallable(List<NewsItem> data) {
            this.data = data;
        }

        @Override
        public List<NewsItem> call() throws Exception {
            return data;
        }
    }

    public NewsAdapter getAdapter() {
        return adapter;
    }

    public void showProgress(boolean show) {
        Visibilty.setVisible(progress, show);
        Visibilty.setVisible(rv, !show);
    }
}



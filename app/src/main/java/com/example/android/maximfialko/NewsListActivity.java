package com.example.android.maximfialko;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.android.maximfialko.Utils.DensityPixelMath;
import com.example.android.maximfialko.Utils.Margins;
import com.example.android.maximfialko.Utils.Visibility;
import com.example.android.maximfialko.data.NewsItem;
import com.example.android.maximfialko.network.RestApi;
import com.example.android.maximfialko.room.MapperDbToNewsItem;
import com.example.android.maximfialko.room.MapperDtoToDb;
import com.example.android.maximfialko.room.NewsItemRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewsListActivity extends AppCompatActivity {

    private NewsAdapter adapter;
    private ProgressBar progress;
    private RecyclerView rv;
    private View error;
    private Spinner spinner;
    private FloatingActionButton fabResfresh;

    private NewsItemRepository newsRepository;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_news);

        progress = (ProgressBar) findViewById(R.id.progress);
        rv = (RecyclerView) findViewById(R.id.recycler_view);
        error = (View) findViewById(R.id.lt_error);
        spinner = (Spinner) findViewById(R.id.spinner);

        adapter = new NewsAdapter(this, new ArrayList<>(), clickListener);

        newsRepository = new NewsItemRepository(getApplicationContext());

        subscribeToDataFromDb();

        setupSpinner();
        setupFab();

        //ps to dp //util class
        DensityPixelMath DPmath = new DensityPixelMath(getApplicationContext());
        //add Margins to Recycler View
        Margins decoration = new Margins((int) DPmath.dpFromPx(44), 1);

        rv.addItemDecoration(decoration);
        rv.setAdapter(adapter);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            rv.setLayoutManager(new LinearLayoutManager(this));
        } else {
            rv.setLayoutManager(new GridLayoutManager(this, 2));
        }
    }

    @Override
    protected void onStart() {
        Log.d("lifecycle", "_____________onSTART");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d("lifecycle", "_____________onSTOP");
        super.onStop();
        compositeDisposable.clear();
    }

    @Override
    protected void onResume() {
        Log.d("lifecycle", "_____________onRESUME");
        super.onResume();
        subscribeToDataFromDb();
    }

    @Override
    protected void onDestroy() {
        Log.d("lifecycle", "_____________onDESTROY");
        super.onDestroy();
    }

    private void setupSpinner() {
        ArrayAdapter<String> spinner_adapter =
                new ArrayAdapter<>(this, R.layout.spinner_item, getResources().getStringArray(R.array.categoryList));
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.d("room", "CATEGORY is selected in spinner");
                loadItemsToDb(spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    //асинхронная загрузка списка новостей
    private void loadItemsToDb(@NonNull String category) {
        Log.d("room", "loadNews START");
        showProgress(true);
        final Disposable searchDisposable = RestApi.getInstance()   //init Retrofit client
                .topStoriesEndpoint()   //return topStoriesEndpoint
                .getNews(category)      //@GET Single<TopStoriesResponse>, TopStoriesResponse = List<NewsItemDTO>
                .map(response -> MapperDtoToDb.map(response.getNews()))   //return List<NewsItemDB>
                .flatMapCompletable(NewsItemDB -> newsRepository.saveData(NewsItemDB))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        Log.d("room", "loadNews END");
        compositeDisposable.add(searchDisposable);
        showProgress(false);
        Visibility.setVisible(rv, true);
    }

    public void subscribeToDataFromDb() {
        Log.d("room", "subscribeToDataFromDb()");
        Disposable disposable = newsRepository.getDataObservable()
                .map(newsItemDBS -> MapperDbToNewsItem.map(newsItemDBS))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsItems -> setupNews(newsItems),
                        throwable -> Log.d("room", throwable.toString()));
        compositeDisposable.add(disposable);
    }

    private void setupNews(List<NewsItem> newsItems) {
        showProgress(false);
        Log.d("room", "UPDATE RV: setupNews");
        if (adapter != null) adapter.replaceItems(newsItems);
    }

    //метод перехода на активити DetailedNews

    public void openDetailedNewsActivity(int id) {
        DetailedNewsActivity.start(this, id);
    }
    //clickListener на DetailedNewsActivity через WebView

    private final NewsAdapter.newsItemClickListener clickListener = newsItem -> openDetailedNewsActivity(newsItem.getId());

    public void showProgress(boolean show) {
        Visibility.setVisible(progress, show);
        Visibility.setVisible(rv, !show);
        Visibility.setVisible(error, !show);
    }

    @Override //for AboutActivity MENU toolbar
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override //for AboutActivity MENU toolbar
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_switch:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setupFab() {
        fabResfresh = (FloatingActionButton) findViewById(R.id.fab_refresh);
        fabResfresh.setOnClickListener(v -> loadItemsToDb(spinner.getSelectedItem().toString()));
    }

    private void handleError(Throwable throwable) {
        if (throwable instanceof IOException) {
            return;
        }
        Visibility.setVisible(rv, false);
        Visibility.setVisible(progress, false);
        Visibility.setVisible(error, true);
    }
}



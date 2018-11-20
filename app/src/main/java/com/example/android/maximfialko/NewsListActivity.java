package com.example.android.maximfialko;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.maximfialko.Utils.DensityPixelMath;
import com.example.android.maximfialko.Utils.MapperDtoToNews;
import com.example.android.maximfialko.Utils.Visibilty;
import com.example.android.maximfialko.Utils.Margins;
import com.example.android.maximfialko.data.NewsAdapter;
import com.example.android.maximfialko.data.NewsItem;
import com.example.android.maximfialko.network.DTOmodels.NewsItemDTO;
import com.example.android.maximfialko.network.TopStoriesResponse;
import com.example.android.maximfialko.network.RestApi;

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
import retrofit2.Response;

public class NewsListActivity extends AppCompatActivity {

    private NewsAdapter adapter;
    private ProgressBar progress;
    private RecyclerView rv;
    private View error;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list_activity);

        progress = (ProgressBar) findViewById(R.id.progress);
        rv = (RecyclerView) findViewById(R.id.recycler_view);
        error = (View) findViewById(R.id.lt_error);

        adapter = new NewsAdapter(this, new ArrayList<>(), clickListener);

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
        super.onStart();
        //TODO: SpinnerAdapter for category usage
        loadItems("technology");
    }

    //переход на DetailedNewsActivity через WebView
    private final NewsAdapter.OnItemClickListener clickListener = position -> openDetailedNewsActivity(position.getTextUrl());

    //асинхронная загрузка списка новостей
    private void loadItems(@NonNull String category) {
        showProgress(true);
        final Disposable searchDisposable = RestApi.getInstance()
                .topStoriesEndpoint()
                .getNews(category)
                .map(response -> MapperDtoToNews.map(response.getNews()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsItems -> setupNews(newsItems), throwable -> handleError(throwable));
        compositeDisposable.add(searchDisposable);
    }

//    private void transformResponse(@NonNull Response<TopStoriesResponse<List<NewsItemDTO>>> response) {
//
//        final TopStoriesResponse<List<NewsItemDTO>> body = response.body();
//        assert body != null;
//        final List<NewsItemDTO> data = body.getData();
//
//        adapter.replaceItems(data);
//    }

    private void handleError(Throwable throwable) {
        if (throwable instanceof IOException) {
            showError(true);
            return;
        }
        showError(true);
    }

    private void setupNews(List<NewsItem> newsItems) {
        showProgress(false);
        updateItems(newsItems);
    }

    private void updateItems(@Nullable List<NewsItem> news) {
        if (adapter != null) adapter.replaceItems(news);

        Visibilty.setVisible(rv, true);
        Visibilty.setVisible(progress, false);
        Visibilty.setVisible(error, false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        showProgress(false);
        compositeDisposable.dispose();
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
    public void openDetailedNewsActivity(String url) {
        DetailedNewsActivity.start(this, url);
    }

    public NewsAdapter getAdapter() {
        return adapter;
    }

    public void showProgress(boolean show) {
        Visibilty.setVisible(progress, show);
        Visibilty.setVisible(rv, !show);
        Visibilty.setVisible(error, !show);
    }

    void showError(boolean show) {
        Visibilty.setVisible(progress, !show);
        Visibilty.setVisible(rv, !show);
        Visibilty.setVisible(error, show);
    }

//    public void showState(@NonNull State state) {
//
//        switch (state) {
//            case HasData:
//                viewError.setVisibility(View.GONE);
//                viewLoading.setVisibility(View.GONE);
//                viewNoData.setVisibility(View.GONE);
//
//                rvPhotos.setVisibility(View.VISIBLE);
//                break;
//
//            case HasNoData:
//                rvPhotos.setVisibility(View.GONE);
//                viewLoading.setVisibility(View.GONE);
//
//                viewError.setVisibility(View.VISIBLE);
//                viewNoData.setVisibility(View.VISIBLE);
//                break;
//            case NetworkError:
//                rvPhotos.setVisibility(View.GONE);
//                viewLoading.setVisibility(View.GONE);
//                viewNoData.setVisibility(View.GONE);
//
//                tvError.setText(getText(R.string.error_network));
//                viewError.setVisibility(View.VISIBLE);
//                break;
//
//            case ServerError:
//                rvPhotos.setVisibility(View.GONE);
//                viewLoading.setVisibility(View.GONE);
//                viewNoData.setVisibility(View.GONE);
//
//                tvError.setText(getText(R.string.error_server));
//                viewError.setVisibility(View.VISIBLE);
//                break;
//            case Loading:
//                viewError.setVisibility(View.GONE);
//                rvPhotos.setVisibility(View.GONE);
//                viewNoData.setVisibility(View.GONE);
//
//                viewLoading.setVisibility(View.VISIBLE);
//                break;
//
//
//            default:
//                throw new IllegalArgumentException("Unknown state: " + state);
//        }
//    }

}



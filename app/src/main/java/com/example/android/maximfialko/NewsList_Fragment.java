package com.example.android.maximfialko;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.example.android.maximfialko.room.NewsItemDB;
import com.example.android.maximfialko.room.NewsItemRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.CompletableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class NewsList_Fragment extends android.app.Fragment {

    private Activity activity;
    private NewsAdapter adapter;
    private ProgressBar progress;
    private RecyclerView rv;
    private View error;
    private Spinner spinner;
    private FloatingActionButton fabResfresh;

    private NewsItemRepository newsRepository;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    static NewsList_Fragment newInstance() {
        return new NewsList_Fragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("lifecycle", "_____________onATTACH");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("lifecycle", "_____________onCREATEView");

        activity = getActivity();

        View view = inflater.inflate(R.layout.activity_list_news, null);

        progress = (ProgressBar) view.findViewById(R.id.progress);
        error = (View) view.findViewById(R.id.lt_error);
        spinner = (Spinner) view.findViewById(R.id.spinner);

        newsRepository = new NewsItemRepository(activity);

        subscribeToDataFromDb();

        setupSpinner();
        setupFab(view);
        setupRecycler(view);

        return view;
    }

    @Override
    public void onStart() {
        Log.d("lifecycle", "_____________onSTART");
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.d("lifecycle", "_____________onSTOP");
        super.onStop();
        compositeDisposable.clear();
        activity = null;
    }

    @Override
    public void onResume() {
        Log.d("lifecycle", "_____________onRESUME");
        super.onResume();
        subscribeToDataFromDb();
    }

    @Override
    public void onDestroy() {
        Log.d("lifecycle", "_____________onDESTROY");
        super.onDestroy();
    }

    public void setupRecycler(View view) {

        rv = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new NewsAdapter(view.getContext(), new ArrayList<>(), clickListener);

        //ps to dp //util class
        DensityPixelMath DPmath = new DensityPixelMath(view.getContext());
        //add Margins to Recycler View
        Margins decoration = new Margins((int) DPmath.dpFromPx(44), 1);

        rv.setAdapter(adapter);
        rv.addItemDecoration(decoration);

        if (view.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            rv.setLayoutManager(new LinearLayoutManager(view.getContext()));
        } else {
            rv.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        }

    }

    private void setupSpinner() {
        ArrayAdapter<String> spinner_adapter =
                new ArrayAdapter<>(activity, R.layout.spinner_item, getResources().getStringArray(R.array.categoryList));
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                .delay(2, TimeUnit.SECONDS)
                .map(response -> MapperDtoToDb.map(response.getNews()))   //return List<NewsItemDB>
                .flatMapCompletable(NewsItemDB -> newsRepository.saveData(NewsItemDB))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        Log.d("room", "loadNews END");
        compositeDisposable.add(searchDisposable);
        showProgress(false);
//        Visibility.setVisible(rv, true);
    }

    private void subscribeToDataFromDb() {
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
//        showProgress(false);
        Log.d("room", "UPDATE RV: setupNews");
        if (adapter != null) adapter.replaceItems(newsItems);
    }

    //метод перехода на активити DetailedNews
    public void openDetailedNewsActivity(int id) {
        DetailedNewsActivity.start(activity, id);
    }

    private final NewsAdapter.newsItemClickListener clickListener = newsItem -> openDetailedNewsActivity(newsItem.getId());

    public void setupFab(View view) {
        fabResfresh = (FloatingActionButton) view.findViewById(R.id.fab_refresh);
        fabResfresh.setOnClickListener(v -> loadItemsToDb(spinner.getSelectedItem().toString()));
    }

    public void showProgress(boolean show) {
        Visibility.setVisible(progress, show);
        Visibility.setVisible(rv, !show);
        Visibility.setVisible(error, !show);
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

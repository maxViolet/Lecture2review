package com.example.android.maximfialko;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.android.maximfialko.utils.DensityPixelMath;
import com.example.android.maximfialko.utils.Margins;
import com.example.android.maximfialko.utils.Visibility;
import com.example.android.maximfialko.data.NewsItem;
import com.example.android.maximfialko.network.RestApi;
import com.example.android.maximfialko.room.MapperDbToNewsItem;
import com.example.android.maximfialko.room.MapperDtoToDb;
import com.example.android.maximfialko.room.NewsItemRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NewsListFragment extends android.app.Fragment {

    static final String TAG_DETAIL_FRAGMENT = "detail_fragment";
    private static int MARGIN = 22;

    private boolean isTwoPanel;
    private Activity activityInstance;
    private NewsDetailFragment detailNews_fragment;
    private NewsAdapter adapter;
    private ProgressBar progress;
    private View progress_container;
    private RecyclerView recyclerView;
    private View recycler_container;
    private View error_container;
    private Spinner spinner;
    private FloatingActionButton fabRefresh;
    private NewsItemRepository newsRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public interface DetailFragmentListener {
        void openDetailFragment(int id);
    }

    static NewsListFragment newInstance() {
        return new NewsListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        Log.d("lifecycle", "NewsListFragment_____________onCREATE_VIEW");
        activityInstance = getActivity();

        View view = inflater.inflate(R.layout.fragment_list_news, null);

        progress = view.findViewById(R.id.progress);
        progress_container = view.findViewById(R.id.progress_container);
        recycler_container = view.findViewById(R.id.recycler_container);
        error_container = view.findViewById(R.id.error_container);
        spinner = view.findViewById(R.id.spinner);

        newsRepository = new NewsItemRepository(activityInstance);

        subscribeToDataFromDb();

        setupSpinner();
        setupFab(view);
        setupRecycler(view);

        isTwoPanel = view.findViewById(R.id.frame_detail) != null;
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Log.d("lifecycle", "listFragment_____________onACTIVITY_CREATED");
        //check for tablet twopanel mode
        isTwoPanel = ((MainActivity) getActivity()).getIsTwoPanel();
    }

    @Override
    public void onResume() {
//        Log.d("lifecycle", "listFragment_____________onRESUME");
        super.onResume();
        subscribeToDataFromDb();
    }

    @Override
    public void onStop() {
//        Log.d("lifecycle", "listFragment_____________onSTOP");
        super.onStop();
        compositeDisposable.clear();
        activityInstance = null;
    }

    public void setupRecycler(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new NewsAdapter(view.getContext(), new ArrayList<>(), clickListener);

        //ps to dp
        DensityPixelMath DPmath = new DensityPixelMath(view.getContext());
        //add Margins to Recycler View
        Margins decoration = new Margins((int) DPmath.dpFromPx(MARGIN), 1);

        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(decoration);
    }

    private void setupSpinner() {
        ArrayAdapter<String> spinner_adapter =
                new ArrayAdapter<>(activityInstance, R.layout.spinner_item, getResources().getStringArray(R.array.categoryList));
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
//                loadItemsToDb(spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void loadItemsToDb(@NonNull String category) {
        showProgress(true);
        final Disposable searchDisposable = RestApi.getInstance()
                .topStoriesEndpoint()
                .getNews(category)
                .delay(600, TimeUnit.MILLISECONDS)
                .map(response -> MapperDtoToDb.map(response.getNews()))
                .flatMapCompletable(NewsItemDB -> newsRepository.saveData(NewsItemDB))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    showNews(true);
                }, throwable -> showError(throwable));
        compositeDisposable.add(searchDisposable);
//        showProgress(false);
//        progress.setVisibility(View.GONE);
//        recyclerView.setVisibility(View.VISIBLE);
    }

    private void subscribeToDataFromDb() {
        Disposable disposable = newsRepository.getDataObservable()
                .map(newsItemDBS -> MapperDbToNewsItem.map(newsItemDBS))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsItems -> setupNews(newsItems),
                        throwable -> Log.d("room", throwable.toString()));
        compositeDisposable.add(disposable);
    }

    private void setupNews(List<NewsItem> newsItems) {
        if (adapter != null) adapter.replaceItems(newsItems);
    }

    public final NewsAdapter.newsItemClickListener clickListener = new NewsAdapter.newsItemClickListener() {
        @Override
        public void onItemClick(NewsItem newsItem) {
            NewsListFragment.this.openNewsDetails(newsItem.getId());
        }
    };

    public void setupFab(View view) {
        fabRefresh = view.findViewById(R.id.fab_refresh);
        fabRefresh.setOnClickListener(v -> loadItemsToDb(spinner.getSelectedItem().toString()));
    }

    public void showNews(boolean show) {
        Visibility.setVisible(progress_container, !show);
        Visibility.setVisible(recycler_container, show);
        Visibility.setVisible(error_container, !show);
    }

    public void showProgress(boolean show) {
        Visibility.setVisible(progress_container, show);
        Visibility.setVisible(recycler_container, !show);
        Visibility.setVisible(error_container, !show);
    }

    public void showError(Throwable throwable) {
        if (throwable instanceof IOException) {
            return;
        }
        Visibility.setVisible(progress_container, false);
        Visibility.setVisible(recycler_container, false);
        Visibility.setVisible(error_container, true);
    }

    public void openNewsDetails(int id) {
        if (isTwoPanel) {
            detailNews_fragment = NewsDetailFragment.newInstance(id);

            getFragmentManager().beginTransaction()
                    .replace(R.id.frame_detail, detailNews_fragment, TAG_DETAIL_FRAGMENT)
                    .commit();
        } else {
            detailNews_fragment = NewsDetailFragment.newInstance(id);

            getFragmentManager().beginTransaction()
                    .replace(R.id.frame_list, detailNews_fragment, TAG_DETAIL_FRAGMENT)
                    .addToBackStack(null)
                    .commit();
        }
    }

//    public int getFirstItemId() {
//        return adapter.getFirstItemId();
//    }
}

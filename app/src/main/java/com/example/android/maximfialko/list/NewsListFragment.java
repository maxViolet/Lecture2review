package com.example.android.maximfialko.list;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.example.android.maximfialko.detail.NewsDetailFragment;
import com.example.android.maximfialko.R;
import com.example.android.maximfialko.utils.DensityPixelMath;
import com.example.android.maximfialko.utils.HidingScrollListener;
import com.example.android.maximfialko.utils.ItemDecorator;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewsListFragment extends androidx.fragment.app.Fragment {

    private static final String TAG_DETAIL_FRAGMENT = "detail_fragment";
    private static final String KEY_RECYCLER_STATE = "recyclerviewState";
    private static final String POSITION = "position";
    private static final int MARGIN = 5;

    private boolean isTwoPanel;
    private Activity activityInstance;
    private NewsAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Parcelable stateParcelable;
    private int mScrollPosition = -1;

    private Toolbar mToolbar;
    private View progress_container;
    private View recycler_container;
    private View error_container;
    private Spinner spinner;
    private FloatingActionButton fabRefresh;
    private NewsItemRepository newsRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();


    static NewsListFragment newInstance() {
        return new NewsListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("stateSave", "onCREATEVIEW");
        View view = inflater.inflate(R.layout.list_news_fragment, null);

        isTwoPanel = view.findViewById(R.id.frame_detail) != null;
        activityInstance = getActivity();

        recyclerView = view.findViewById(R.id.recycler_view);
        progress_container = view.findViewById(R.id.progress_container);
        recycler_container = view.findViewById(R.id.recycler_container);
        error_container = view.findViewById(R.id.error_container);
        spinner = view.findViewById(R.id.spinner);

        newsRepository = new NewsItemRepository(activityInstance);

        setupRecycler(view, recyclerView);
        setupSpinner();
        setupFabRefresh(view);

        subscribeToDataFromDb();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //check for tablet twopanel mode
        isTwoPanel = ((MainActivity) getActivity()).getIsTwoPanel();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("stateSave", "onPAUSE");
        compositeDisposable.clear();
        activityInstance = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("stateSave", "onRESUME");
        subscribeToDataFromDb();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("stateSave", "onDESTROY_VIEW-------------------------------------------RIP");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // save list position
        int scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        stateParcelable = linearLayoutManager.onSaveInstanceState();
        outState.putParcelable(KEY_RECYCLER_STATE, stateParcelable);
        outState.putInt(POSITION, scrollPosition);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        // restore list position
        if (savedInstanceState != null) {
            stateParcelable = savedInstanceState.getParcelable(KEY_RECYCLER_STATE);
            mScrollPosition = savedInstanceState.getInt(POSITION);
        }
    }

    private void setupRecycler(View view, RecyclerView recyclerView) {
        adapter = new NewsAdapter(view.getContext(), new ArrayList<>(), itemClickListener);
        linearLayoutManager = new LinearLayoutManager(activityInstance);
        mToolbar = view.findViewById(R.id.collapsing_toolbar);
        //add space between recyclerView items
        ItemDecorator decoration = new ItemDecorator(DensityPixelMath.dpToPx(MARGIN), 1);

        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideToolbarNFab();
            }

            @Override
            public void onShow() {
                showToolbarNFab();
            }
        });
    }

    private void setupSpinner() {
        ArrayAdapter<String> spinner_adapter =
                new ArrayAdapter<>(activityInstance, R.layout.spinner_item, getResources().getStringArray(R.array.categoryList));
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
                .map(response -> MapperDtoToDb.map(response.getNews()))
                //save data to database
                .flatMapCompletable(NewsItemDB -> newsRepository.saveData(NewsItemDB))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> showNews(true), throwable -> showError(throwable));
        compositeDisposable.add(searchDisposable);
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
        // scroll list to saved position
        recyclerView.scrollToPosition(mScrollPosition);
    }

    private final NewsAdapter.newsItemClickListener itemClickListener = (NewsItem newsItem) -> NewsListFragment.this.openNewsDetails(newsItem.getId());

    private void setupFabRefresh(View view) {
        fabRefresh = view.findViewById(R.id.fab_options);
        fabRefresh.setOnClickListener(v -> loadItemsToDb(spinner.getSelectedItem().toString()));
    }

    private void showNews(boolean show) {
        Visibility.setVisible(progress_container, !show);
        Visibility.setVisible(recycler_container, show);
        Visibility.setVisible(error_container, !show);
    }

    private void showProgress(boolean show) {
        Visibility.setVisible(progress_container, show);
        Visibility.setVisible(recycler_container, !show);
        Visibility.setVisible(error_container, !show);
    }

    private void showError(Throwable throwable) {
        if (throwable instanceof IOException) {
            return;
        }
        Visibility.setVisible(progress_container, false);
        Visibility.setVisible(recycler_container, false);
        Visibility.setVisible(error_container, true);
    }

    private void openNewsDetails(int id) {
        NewsDetailFragment detailNews_fragment;
        if (isTwoPanel) {
            detailNews_fragment = NewsDetailFragment.newInstance(id);
            if (getFragmentManager() != null) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_detail, detailNews_fragment, TAG_DETAIL_FRAGMENT)
                        //                    .addToBackStack(TAG_DETAIL_FRAGMENT)
                        .commit();
            }
        } else {
            detailNews_fragment = NewsDetailFragment.newInstance(id);
            if (getFragmentManager() != null) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_list, detailNews_fragment, TAG_DETAIL_FRAGMENT)
                        .addToBackStack(TAG_DETAIL_FRAGMENT)
                        .commit();
            }
        }
    }

    private void hideToolbarNFab() {
        mToolbar.animate().translationY(-mToolbar.getHeight());

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fabRefresh.getLayoutParams();
        int fabBottomMargin = layoutParams.bottomMargin;
        fabRefresh.animate().translationY(+fabRefresh.getHeight() + fabBottomMargin).start();
    }

    private void showToolbarNFab() {
        mToolbar.animate().translationY(0);
        fabRefresh.animate().translationY(0);
    }
}

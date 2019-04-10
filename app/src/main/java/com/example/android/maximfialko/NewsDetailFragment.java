package com.example.android.maximfialko;

import android.app.Activity;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.maximfialko.room.NewsItemDB;
import com.example.android.maximfialko.room.NewsItemRepository;
import com.example.android.maximfialko.utils.DateUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.android.maximfialko.utils.DateUtils.formatDateFromDb;

public class NewsDetailFragment extends androidx.fragment.app.Fragment {

    public static final String ID_EXTRAS = "id_extras";
    private static int Id;

    private Activity activityInstance;
    private Boolean miniFabShow = false;

    private ImageView photo;
    private TextView category;
    private TextView date;
    private EditText title;
    private EditText fullText;
    private Button buttonSource;
    private Drawable originalDrawable;

    private FloatingActionButton fabOptions;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;

    private NewsItemDB newsItemDB;
    private NewsItemRepository newsRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static NewsDetailFragment newInstance(int id) {
        NewsDetailFragment detailNews_fragment = new NewsDetailFragment();
        Bundle arg = new Bundle();
        arg.putInt(ID_EXTRAS, id);
        detailNews_fragment.setArguments(arg);
        return detailNews_fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Id = getArguments().getInt(ID_EXTRAS);
//        Log.d("room", String.valueOf(Id));
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        activityInstance = getActivity();

        View view = inflater.inflate(R.layout.detailed_news_fragment, null);
        newsRepository = new NewsItemRepository(activityInstance);

        initViews(view);
        setupFabs(view);
        loadNewsItemFromDb(Id);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
        activityInstance = null;
    }

    private void initViews(View view) {
        category = view.findViewById(R.id.tv_cont_category);
        photo = view.findViewById(R.id.iv_det_photo);
        title = view.findViewById(R.id.tv_cont_title);
        date = view.findViewById(R.id.tv_cont_date);
        fullText = view.findViewById(R.id.tv_cont_fulltext);
        buttonSource = view.findViewById(R.id.button_goToSource);

//        makeLookLikeTextView(title);
//        makeLookLikeTextView(fullText);
    }

    private void loadNewsItemFromDb(int id) {
        Disposable disposable = newsRepository.getNewsById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(NewsItemDB -> newsItemDB = NewsItemDB)
                .subscribe(newsItemDB -> setupViews(newsItemDB),
                        throwable -> Log.d("room", throwable.toString()));

//        Log.d("room", "ID of NewsItemDB " + String.valueOf(id));
        compositeDisposable.add(disposable);
    }

    private void setupViews(NewsItemDB item) {
        Glide.with(activityInstance)
                .load(item.getImageUrl())
                .into(photo);

        category.setText(item.getCategory());
        title.setText(item.getTitle());
        date.setText(
                DateUtils.formatDateTime(
                        activityInstance,
                        formatDateFromDb(item.getPublishDate())
                )
        );
        fullText.setText(item.getPreviewText());
        buttonSource.setVisibility(View.VISIBLE);
        buttonSource.setOnClickListener(v -> openSourceActivity(item.getTextUrl()));
    }

    private void setupFabs(View view) {
        fabOptions = view.findViewById(R.id.fab_options);
        fab1 = view.findViewById(R.id.fab_1);
        fab2 = view.findViewById(R.id.fab_2);
        fab3 = view.findViewById(R.id.fab_3);

        setMiniFabClicklistener(fabOptions);
        setMiniFabClicklistener(fab1);
        setMiniFabClicklistener(fab2);
        setMiniFabClicklistener(fab3);
    }

    private void setMiniFabClicklistener(View view) {
        view.setClickable(true);
        switch (view.getId()) {
            case R.id.fab_options:
                view.setOnClickListener(v -> {
                    if (!miniFabShow) {
                        miniFabShow = true;
                        showMiniFabs();
                    } else {
                        miniFabShow = false;
                        hideMiniFabs();
                    }
                });
                break;
            case R.id.fab_1:
                view.setClickable(true);
                view.setOnClickListener(v -> deleteFromDb());
                break;
            case R.id.fab_2:
                view.setClickable(true);
                view.setOnClickListener(v -> openSourceActivity(newsItemDB.getTextUrl()));
                break;
            case R.id.fab_3:
                view.setClickable(true);
                view.setOnClickListener(v -> updateDb());
                makeLookLikeEditText(title, originalDrawable);
                makeLookLikeEditText(fullText, originalDrawable);
                break;
        }
    }

    private void deleteFromDb() {
        if (newsItemDB != null) {
            Disposable disposable = newsRepository.deleteNews(newsItemDB)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> activityInstance.getFragmentManager().popBackStack());
            compositeDisposable.add(disposable);
            activityInstance.onBackPressed();
        }
    }

    private void updateDb() {
        if (newsItemDB != null) {
            newsItemDB.setTitle(title.getText().toString());
            newsItemDB.setPreviewText(fullText.getText().toString());
            Disposable disposable = newsRepository.updateNews(newsItemDB)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();
            compositeDisposable.add(disposable);
        }
    }

    private void showMiniFabs() {
        final int radius = 135;
        final int fab1angle = 182;
        final int fab2angle = 225;
        final int fab3angle = 268;

        fab1.show();
        fab2.show();
        fab3.show();

        PointF positionFab1 = getMiniFabPosition(fabOptions.getX(), fabOptions.getY(), radius, fab1angle);
        fab1.animate().x(positionFab1.x).y(positionFab1.y).start();

        PointF positionFab2 = getMiniFabPosition(fabOptions.getX(), fabOptions.getY(), radius, fab2angle);
        fab2.animate().x(positionFab2.x).y(positionFab2.y).start();

        PointF positionFab3 = getMiniFabPosition(fabOptions.getX(), fabOptions.getY(), radius, fab3angle);
        fab3.animate().x(positionFab3.x).y(positionFab3.y).start();
    }

    private void hideMiniFabs() {
        fab1.animate().x(fabOptions.getX()).y(fabOptions.getY()).start();
        fab2.animate().x(fabOptions.getX()).y(fabOptions.getY()).start();
        fab3.animate().x(fabOptions.getX()).y(fabOptions.getY()).start();
        fab1.hide();
        fab2.hide();
        fab3.hide();
    }

    private PointF getMiniFabPosition(float centerX, float centerY, float radius, float angle) {
        return new PointF((float) (centerX + radius * Math.cos(Math.toRadians(angle))),
                (float) (centerY + radius * Math.sin(Math.toRadians(angle))));
    }

    private void openSourceActivity(String url) {
        GoToSourceActivity.start(activityInstance, url);
    }

    private void makeLookLikeTextView(EditText editText) {
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        editText.setLongClickable(false);
        editText.setBackground(null);
    }

    private void makeLookLikeEditText(EditText editText, Drawable original) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.setLongClickable(true);
        editText.setBackground(original);
    }
}

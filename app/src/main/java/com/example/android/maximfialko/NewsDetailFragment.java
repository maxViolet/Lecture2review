package com.example.android.maximfialko;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
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
import com.example.android.maximfialko.utils.DateUtils;
import com.example.android.maximfialko.room.NewsItemDB;
import com.example.android.maximfialko.room.NewsItemRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.android.maximfialko.utils.DateUtils.formatDateFromDb;

public class NewsDetailFragment extends android.app.Fragment {

    public static final String ID_EXTRAS = "id_extras";
    private static int Id;

    private Activity activity;
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

    private NewsItemDB item;
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
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("lifecycle", "detailFragment_____________onATTACH");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("lifecycle", "detailFragment_____________onCREATE");

        Id = getArguments().getInt(ID_EXTRAS);
        Log.d("room", String.valueOf(Id));
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("lifecycle", "detailFragment_____________onCREATE_VIEW");
        activity = getActivity();

        View view = inflater.inflate(R.layout.detailed_news_fragment, null);
        newsRepository = new NewsItemRepository(activity);

        initViews(view);
        setupFabs(view);
        loadNewsItemFromDb(Id);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("lifecycle", "detailFragment_____________onSTOP");

        compositeDisposable.clear();
        activity = null;
    }

    public void initViews(View view) {
        category = view.findViewById(R.id.tv_cont_category);
        photo = view.findViewById(R.id.iv_det_photo);
        title = view.findViewById(R.id.tv_cont_title);
        date = view.findViewById(R.id.tv_cont_date);
        fullText = view.findViewById(R.id.tv_cont_fulltext);
        buttonSource = view.findViewById(R.id.button_goToSource);

        makeLookLikeTextView(title);
        makeLookLikeTextView(fullText);
    }

    public void loadNewsItemFromDb(int id) {
        Disposable disposable = newsRepository.getNewsById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(NewsItemDB -> item = NewsItemDB)
                .subscribe(newsItemDB -> setupViews(newsItemDB),
                        throwable -> Log.d("room", throwable.toString()));

//        Log.d("room", "ID of NewsItemDB " + String.valueOf(id));
        compositeDisposable.add(disposable);
    }

    public void setupViews(NewsItemDB item) {

        Glide.with(activity)
                .load(item.getImageUrl())
                .into(photo);

        category.setText(item.getCategory());
        title.setText(item.getTitle());
        date.setText(
                DateUtils.formatDateTime(
                        activity,
                        formatDateFromDb(item.getPublishDate())
                )
        );
        fullText.setText(item.getPreviewText());
        buttonSource.setVisibility(View.VISIBLE);
        buttonSource.setOnClickListener(v -> openSourceActivity(item.getTextUrl()));
    }

    public void setupFabs(View view) {
        fabOptions = view.findViewById(R.id.fab_options);
//        fabOptions.show();
        fab1 = view.findViewById(R.id.fab_1);
        fab2 = view.findViewById(R.id.fab_2);
        fab3 = view.findViewById(R.id.fab_3);

        fabOptions.setOnClickListener(
                v -> {
                    if (!miniFabShow) {
                        miniFabShow = true;
                        showMiniFabs();
                    } else {
                        miniFabShow = false;
                        hideMiniFabs();
                    }
                }
        );

        setup_Fab1_Clicklistener();
        setup_Fab2_Clicklistener();
        setup_Fab3_Clicklistener();
    }

    public void setup_Fab1_Clicklistener() {
        fab1.setClickable(true);
        fab1.setOnClickListener(v -> deleteFromDb());
    }

    public void setup_Fab2_Clicklistener() {
        fab1.setClickable(true);
        fab1.setOnClickListener(v -> openSourceActivity(item.getTextUrl()));
    }

    public void setup_Fab3_Clicklistener() {
        fab1.setClickable(true);
        fab1.setOnClickListener(v -> updateDb());
        makeLookLikeEditText(title, originalDrawable);
        makeLookLikeEditText(fullText, originalDrawable);
    }

    private void deleteFromDb() {
        if (item != null) {
            Disposable disposable = newsRepository.deleteNews(item)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();
            compositeDisposable.add(disposable);
//            finish();
//            activity.getFragmentManager().popBackStack();
            activity.onBackPressed();
        }
    }

    private void updateDb() {
        if (item != null) {
            item.setTitle(title.getText().toString());
            item.setPreviewText(fullText.getText().toString());
            Disposable disposable = newsRepository.updateNews(item)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();
            compositeDisposable.add(disposable);
        }
    }

    public void showMiniFabs() {
        int radius = 144;
        int fab1angle = 185;
        int fab2angle = 225;
        int fab3angle = 265;
        PointF positionFab1 = getMiniFabPosition(fabOptions.getX(), fabOptions.getY(), radius, fab1angle);
//        Log.d("fab", positionFab1.x + " | " + positionFab1.y);
//        playMiniFabAnimation(fab1, positionFab1.x, positionFab1.y);

        PointF positionFab2 = getMiniFabPosition(fabOptions.getX(), fabOptions.getY(), radius, fab2angle);
//        Log.d("fab", positionFab2.x + " | " + positionFab2.y);
//        playMiniFabAnimation(fab1, positionFab2.x, positionFab2.y);

        PointF positionFab3 = getMiniFabPosition(fabOptions.getX(), fabOptions.getY(), radius, fab3angle);
//        Log.d("fab", positionFab3.x + " | " + positionFab3.y);
//        playMiniFabAnimation(fab1, positionFab3.x, positionFab3.y);

        fab1.setX(positionFab1.x); fab1.setY(positionFab1.y);
        fab2.setX(positionFab2.x); fab2.setY(positionFab2.y);
        fab3.setX(positionFab3.x); fab3.setY(positionFab3.y);

        fab1.show();
        fab2.show();
        fab3.show();

    }

    public void hideMiniFabs() {
        fab1.hide();
        fab2.hide();
        fab3.hide();
    }

    private PointF getMiniFabPosition(float centerX, float centerY, float radius, float angle) {

        return new PointF((float) (centerX + radius * Math.cos(Math.toRadians(angle))),
                (float) (centerY + radius * Math.sin(Math.toRadians(angle))));
    }

    private void playMiniFabAnimation(View view, Float x, Float y) {
        int ANIMATION_DURATION = 2;

        AnimatorSet animSet = new AnimatorSet();

        ValueAnimator buttonAnimatorX = ValueAnimator.ofFloat(fabOptions.getX(), x);
        buttonAnimatorX.addUpdateListener(animation -> {
            view.setX((float) animation.getAnimatedValue() - (float) view.getLayoutParams().width / 2);
            view.requestLayout();
        });
        buttonAnimatorX.setDuration(ANIMATION_DURATION);

        ValueAnimator buttonAnimatorY = ValueAnimator.ofFloat(fabOptions.getY(), y);
        buttonAnimatorY.addUpdateListener(animation -> {
            view.setY((float) animation.getAnimatedValue() - (float) view.getLayoutParams().width / 2);
            view.requestLayout();
        });
        buttonAnimatorY.setDuration(ANIMATION_DURATION);

        /*ValueAnimator buttonSizeAnimator = ValueAnimator.ofFloat(1, width);
        buttonSizeAnimator.addUpdateListener(animation -> {
            button.getLayoutParams().width = (int) animation.getAnimatedValue();
            button.getLayoutParams().height = (int) animation.getAnimatedValue();
            button.requestLayout();
        });
        buttonSizeAnimator.setDuration(ANIMATION_DURATION);*/

        animSet.playTogether(buttonAnimatorX, buttonAnimatorY /*buttonSizeAnimator*/);
    }

    public void openSourceActivity(String url) {
        GoToSourceActivity.start(activity, url);
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

//    public int getItemId() {
//        return item.getId();
//    }
}

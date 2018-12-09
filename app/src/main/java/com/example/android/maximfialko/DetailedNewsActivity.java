package com.example.android.maximfialko;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.maximfialko.room.NewsItemDB;
import com.example.android.maximfialko.room.NewsItemRepository;

public class DetailedNewsActivity extends AppCompatActivity {

    private static final String ID_EXTRAS = "id_extras";

    private Toolbar toolbar;
    private ImageView photo;
    private TextView category;
    private TextView title;
    private TextView date;
    private TextView fullText;
    private Button buttonSource;

    private NewsItemRepository newsRepository;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static void start(Activity activity, int id) {
        Intent intent = new Intent(activity, DetailedNewsActivity.class);
        intent.putExtra(ID_EXTRAS, id);
        Log.d("room", String.valueOf(id));
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_news);
        setTheme(R.style.AppThemeNoActionBar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("test");
        }

        newsRepository = new NewsItemRepository(getApplicationContext());
        initViews();
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            int Id = getIntent().getExtras().getInt(ID_EXTRAS);
            Log.d("room", String.valueOf(Id));
            loadNewsItemFromDb(Id);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.clear();
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

    public void initViews() {
        category = findViewById(R.id.tv_cont_category);
        photo = findViewById(R.id.iv_det_photo);
        title = findViewById(R.id.tv_cont_title);
        date = findViewById(R.id.tv_cont_date);
        fullText = findViewById(R.id.tv_cont_fulltext);
        buttonSource = findViewById(R.id.button_goToSource);
    }

    public void loadNewsItemFromDb(int id) {
        Disposable disposable = newsRepository.getNewsById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsItemDB -> setupViews(newsItemDB),
                        throwable -> Log.d("room", throwable.toString()));

        Log.d("room", "ID of NewsItemDB " + String.valueOf(id));
        compositeDisposable.add(disposable);
    }

    public void setupViews(NewsItemDB item) {

        Glide.with(getApplicationContext())
                .load(item.getImageUrl())
                .into(photo);

        category.setText(item.getCategory());
        title.setText(item.getTitle());
        date.setText(item.getPublishDate());
        fullText.setText(item.getPreviewText());

        buttonSource.setOnClickListener(v -> openSourceActivity(item.getTextUrl()));
    }

    public void openSourceActivity(String url) {

    }
}

package com.example.android.maximfialko;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class IntroActivity extends AppCompatActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final String SHARED_PREF = "COUNTER_SHARED_PREF";
    private static final String SHARED_PREF_KEY = "COUNTER";

    private boolean showLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLogo = initShowLogo();

        if (showLogo) {
            saveShowLogo(false); //false
            createSplashScreen();
            Disposable disposable = Completable.complete()
                    .delay(2, TimeUnit.SECONDS)
                    .subscribe(() -> startMainActivity());
            compositeDisposable.add(disposable);
        } else {
            saveShowLogo(true);
            startMainActivity();
        }
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.dispose();
    }

    private void saveShowLogo(boolean showLogo) {
        SharedPreferences sharePref = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharePref.edit();
        editor.putBoolean(SHARED_PREF_KEY, showLogo);
        editor.apply();
    }

    private boolean initShowLogo() {
        SharedPreferences sharePref = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        return sharePref.getBoolean(SHARED_PREF_KEY, true);
    }

    private void createSplashScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.new_york_intro);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        linearLayout.addView(imageView);
        setContentView(linearLayout);
    }
}

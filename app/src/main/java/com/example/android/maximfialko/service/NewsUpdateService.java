package com.example.android.maximfialko.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.android.maximfialko.R;
import com.example.android.maximfialko.network.RestApi;
import com.example.android.maximfialko.room.MapperDtoToDb;
import com.example.android.maximfialko.room.NewsItemRepository;
import com.example.android.maximfialko.utils.NotificationBuilder;
import com.example.android.maximfialko.utils.VersionControl;


import androidx.annotation.Nullable;
import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NewsUpdateService extends Service {

    private static final String LOG_TAG = "newsDownLoad_SERVICE";
    private static final int FG_ID = 1;
    private Disposable downloadDisposable;
    private NewsItemRepository newsRepository;

    public static void start(Context context) {
        Intent intent = new Intent(context, NewsUpdateService.class);

        if (VersionControl.atLeastOreo()) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBIND");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCREATE");
        Notification notification = NotificationBuilder.createForegroundNotification(this);
        startForeground(FG_ID, notification);
        newsRepository = new NewsItemRepository(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        Log.d(LOG_TAG, "onStartCOMMAND");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            downloadDisposable = RestApi.getInstance()
                    .topStoriesEndpoint()
                    .getNews("home")
                    .map(response -> MapperDtoToDb.map(response.getNews()))
                    //save to DB
                    .flatMapCompletable(NewsItemDB -> newsRepository.saveData(NewsItemDB))
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(() -> {
                        NotificationBuilder.showResultNotification(NewsUpdateService.this,
                                NewsUpdateService.this.getString(R.string.success_message));
                        NewsUpdateService.this.stopSelf();
                    }, throwable -> {
                        NotificationBuilder.showResultNotification(NewsUpdateService.this,
                                throwable.getClass().getSimpleName());
                        NewsUpdateService.this.stopSelf();
                    });
        }
        NewsUpdateService.this.stopSelf();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "onDESTROY");
        if (downloadDisposable != null && !downloadDisposable.isDisposed()) {
            downloadDisposable.dispose();
        }
    }
}

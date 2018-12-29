package com.example.android.maximfialko.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.android.maximfialko.network.RestApi;
import com.example.android.maximfialko.room.MapperDbToNewsItem;
import com.example.android.maximfialko.room.MapperDtoToDb;
import com.example.android.maximfialko.room.NewsItemRepository;
import com.example.android.maximfialko.utils.NotificationBuilder;
import com.example.android.maximfialko.utils.VersionControl;

import java.util.function.Consumer;

import androidx.annotation.Nullable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewsUpdateService extends Service {

    private static final String LOG_TAG = "newDownLoad_SERVICE";
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
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        Log.d(LOG_TAG, "onStartCOMMAND");

        downloadDisposable = RestApi.getInstance()                         //init Retrofit client
                .topStoriesEndpoint()                                      //return topStoriesEndpoint
                .getNews("home")                                   //@GET Single<TopStoriesResponse>, TopStoriesResponse = List<NewsItemDTO>
                .map(response -> MapperDtoToDb.map(response.getNews()))    //return List<NewsItemDB>
                .flatMapCompletable(NewsItemDB -> newsRepository.saveData(NewsItemDB))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe();
                /*.subscribe(new Consumer<Object>() {
                               NotificationUtils.showResultNotification(NewsLoadService.this,
                                NewsLoadService.this.getString(R.string.sucess_message));
                        NewsLoadService.this.stopSelf();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        NotificationUtils.showResultNotification(NewsLoadService.this,
                                throwable.getClass().getSimpleName());
                        NewsLoadService.this.stopSelf();
                );*/
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

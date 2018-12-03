package com.example.android.maximfialko.room;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class NewsItemRepository {

    public final Context mContext;

    public NewsItemRepository(Context mContext) {
        this.mContext = mContext;
    }

    public Completable saveData(final List<NewsItemDB> newsItemDBlist) {
        Log.d("room", "SAVE DATA to DB");
        return Completable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                AppDatabase db = AppDatabase.getAppDatabase(mContext);

                NewsItemDB[] items = newsItemDBlist.toArray(new NewsItemDB[newsItemDBlist.size()]);

                db.newsItemDAO().insertAll(items);
                return null;
            }
        });

    }

    //источник данных из бд, на который можно подписаться
    public Single<List<NewsItemDB>> getData() {
        return Single.fromCallable((Callable<List<NewsItemDB>>) () -> {
            AppDatabase db = AppDatabase.getAppDatabase(mContext);

            return db.newsItemDAO().getAll();
        });
    }
}

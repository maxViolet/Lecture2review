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

    //источник данных из бд, на который можно подписаться
    public Single<List<NewsItemDB>> getData() {
        return Single.fromCallable(() -> {
            AppDatabase db = AppDatabase.getAppDatabase(mContext);

            return db.newsItemDAO().getAll();
        });
    }

    public Completable saveData(final List<NewsItemDB> newsItemDBlist) {
        Log.d("room", "SAVE DATA to DB");
        return Completable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                AppDatabase db = AppDatabase.getAppDatabase(mContext);

                db.newsItemDAO().deleteAll();

                NewsItemDB[] items = newsItemDBlist.toArray(new NewsItemDB[newsItemDBlist.size()]);

                db.newsItemDAO().insertAll(items);
                return null;
            }
        });
    }

    public Observable<List<NewsItemDB>> getDataObservable() {
        AppDatabase db = AppDatabase.getAppDatabase(mContext);
        return db.newsItemDAO().getAllObservable();
    }

    public Single<NewsItemDB> getNewsById(int id) {
        return Single.fromCallable(new Callable<NewsItemDB>() {
            @Override
            public NewsItemDB call() throws Exception {
                AppDatabase db = AppDatabase.getAppDatabase(mContext);

                return db.newsItemDAO().findNewsById(id);
            }
        });
    }

    public Completable deleteNews(final NewsItemDB newsItemDB) {
        AppDatabase db = AppDatabase.getAppDatabase(mContext);
        return Completable.fromAction(() -> { db.newsItemDAO().delete(newsItemDB); });
    }

    public Completable updateNews(final NewsItemDB newsItemDB) {
        AppDatabase db = AppDatabase.getAppDatabase(mContext);
        return Completable.fromAction(() -> {db.newsItemDAO().updateNews(newsItemDB);});
    }
}

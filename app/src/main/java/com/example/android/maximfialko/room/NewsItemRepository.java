package com.example.android.maximfialko.room;

import android.content.Context;

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

//    private AppDatabase db;
//
//    public NewsItemRepository(Context mContext) {
//        db = AppDatabase.getAppDatabase(mContext);
//    }

    public Completable saveData(final List<NewsItemDB> newsItemDBlist) {
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

    public Single<List<NewsItemDB>> getData() {

        return Single.fromCallable(() -> {
            AppDatabase db = AppDatabase.getAppDatabase(mContext);

            return db.newsItemDAO().getAll();
        });
    }

    public Observable<List<NewsItemDB>> getNewsObservable() {
        AppDatabase db = AppDatabase.getAppDatabase(mContext);

        return db.newsItemDAO().getAllObservables();
    }
}

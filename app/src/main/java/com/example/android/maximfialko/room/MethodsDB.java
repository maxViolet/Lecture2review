package com.example.android.maximfialko.room;

import android.content.Context;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.internal.operators.completable.CompletableDisposeOn;

public class MethodsDB {

    public final Context mContext;

    public MethodsDB (Context mContext) {
        this.mContext = mContext;
    }

    Completable saveData (final List<NewsItem> newsItemList) {
        return Completable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                AppDatabase db = AppDatabase.getAppDatabase(mContext);

                NewsItem[] newsItems = newsItemList.toArray(new NewsItem[newsItemList.size()]);

                db.newsItemDAO().insertAll(newsItems);

                return null;
            }
        });
    }

    Single<List<NewsItem>> getData() {

        return Single.fromCallable(new Callable<List<NewsItem>>() {
            @Override
            public List<NewsItem> call() throws Exception {


                return null;
            }
        });
    }
}

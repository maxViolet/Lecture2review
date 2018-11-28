package com.example.android.maximfialko.room;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Observable;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface NewsItemAsyncDAO {

    @Query("SELECT * FROM newsItem")
    Observable<List<NewsItem>> getAll();

    @Insert(onConflict = REPLACE)
    void insertAll(NewsItem... newsItems);

    @Delete
    void delete(NewsItem newsItem);

    @Query("DELETE FROM newsItem")
    void deleteAll();

    @Query("SELECT * FROM newsItem WHERE id = :id")
    NewsItem findNewsById(int id);

    @Query("SELECT * FROM newsItem WHERE title IN (:titles)")
    Observable<List<NewsItem>> loadAllByTitles(String[] titles);
}

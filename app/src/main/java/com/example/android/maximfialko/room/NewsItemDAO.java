package com.example.android.maximfialko.room;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface NewsItemDAO {

    @Query("SELECT * FROM newsItem")
    List<NewsItem> getAll();

    @Insert(onConflict = REPLACE)
    void insertAll(NewsItem... newsItems);

    @Delete
    void delete(NewsItem newsItem);

    @Query("DELETE FROM newsItem")
    void deleteAll();

    @Query("SELECT * FROM newsItem WHERE id = :id")
    NewsItem findNewsById(int id);

    @Query("SELECT * FROM newsItem WHERE title LIKE :title LIMIT 1")
    NewsItem findNewsById(String title);

    @Query("SELECT * FROM newsItem WHERE title IN (:titles)")
    List<NewsItem> loadAllByTitles(String[] titles);

}

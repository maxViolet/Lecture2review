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

    @Query("SELECT * FROM newsItemDB")
    Observable<List<NewsItemDB>> getAll();

    @Insert(onConflict = REPLACE)
    void insertAll(NewsItemDB... newsItemDBs);

    @Delete
    void delete(NewsItemDB newsItemDB);

    @Query("DELETE FROM newsItemDB")
    void deleteAll();

    @Query("SELECT * FROM newsItemDB WHERE id = :id")
    NewsItemDB findNewsById(int id);

    @Query("SELECT * FROM newsItemDB WHERE title IN (:titles)")
    Observable<List<NewsItemDB>> loadAllByTitles(String[] titles);
}

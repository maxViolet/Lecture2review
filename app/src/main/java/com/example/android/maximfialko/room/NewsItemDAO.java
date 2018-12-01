package com.example.android.maximfialko.room;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Observable;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface NewsItemDAO {

    @Query("SELECT * FROM NewsItemDB")
    List<NewsItemDB> getAll();

    @Query("SELECT * FROM newsItemDB")
    Observable<List<NewsItemDB>> getAllObservables();

    @Insert(onConflict = REPLACE)
    void insertAll(NewsItemDB... newsItemDBs);

    @Delete
    void delete(NewsItemDB newsItemDB);

    @Query("DELETE FROM NewsItemDB")
    void deleteAll();

    @Query("SELECT * FROM NewsItemDB WHERE id = :id")
    NewsItemDB findNewsById(int id);

    @Query("SELECT * FROM NewsItemDB WHERE title LIKE :title LIMIT 1")
    NewsItemDB findNewsById(String title);

    @Query("SELECT * FROM NewsItemDB WHERE title IN (:titles)")
    List<NewsItemDB> loadAllByTitles(String[] titles);

}

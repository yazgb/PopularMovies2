package com.android.yaz.popularmovies.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie ORDER BY user_rating DESC")
    LiveData<PopularMovie[]> getAllMovies();

    @Insert
    void insert(PopularMovie movie);

    @Delete
    void delete(PopularMovie movie);

    @Query("SELECT * FROM movie WHERE id = :id")
    PopularMovie getMovie(String id);
}

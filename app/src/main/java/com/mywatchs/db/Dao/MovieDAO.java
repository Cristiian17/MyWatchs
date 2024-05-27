package com.mywatchs.db.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.mywatchs.db.Dao.entities.CompletedMovie;
import com.mywatchs.db.Dao.entities.DetachMovie;
import com.mywatchs.db.Dao.entities.ForWatchMovie;

import java.util.List;

@Dao
public interface MovieDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CompletedMovie movie);

    @Query("SELECT * FROM CompletedMovie")
    List<CompletedMovie> getAllCompeltedMovies();

    @Query("SELECT * FROM CompletedMovie WHERE id = :id")
    CompletedMovie getCompletedMovieById(long id);

    @Delete
    void delete(CompletedMovie movie);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DetachMovie movie);

    @Query("SELECT * FROM DetachMovie")
    List<DetachMovie> getAllDetachedMovies();

    @Query("SELECT * FROM DetachMovie WHERE id = :id")
    DetachMovie getDetachMovieById(long id);

    @Delete
    void delete(DetachMovie movie);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ForWatchMovie movie);

    @Query("SELECT * FROM ForWatchMovie")
    List<ForWatchMovie> getAllForWatchMovies();

    @Query("SELECT * FROM ForWatchMovie WHERE id = :id")
    DetachMovie getForWatchMovieById(long id);

    @Delete
    void delete(ForWatchMovie movie);
}

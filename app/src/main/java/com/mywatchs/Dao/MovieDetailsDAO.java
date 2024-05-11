package com.mywatchs.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import java.util.List;

@Dao
public interface MovieDetailsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MoviePOJO movie);

    @Query("SELECT * FROM moviepojo")
    List<MoviePOJO> getAll();

    @Query("SELECT * FROM moviepojo WHERE id = :id")
    MoviePOJO getById(long id);

    @Delete
    void delete(MoviePOJO movie);
}

package com.mywatchs.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;


@Dao
public interface SerieDetailsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SeriePOJO serie);

    @Query("SELECT * FROM SeriePOJO")
    List<SeriePOJO> getAll();

    @Query("SELECT * FROM SeriePOJO WHERE id = :id")
    SeriePOJO getById(long id);

    @Delete
    void delete(SeriePOJO serie);
}

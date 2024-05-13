package com.mywatchs.db.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mywatchs.db.Dao.entities.CompletedSerie;
import com.mywatchs.db.Dao.entities.DetachSerie;
import com.mywatchs.db.Dao.entities.ForWatchSerie;

import java.util.List;


@Dao
public interface SerieDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CompletedSerie serie);

    @Query("SELECT * FROM CompletedSerie")
    List<CompletedSerie> getAllCompeltedSeries();

    @Query("SELECT * FROM CompletedSerie WHERE id = :id")
    CompletedSerie getCompletedSerieById(long id);

    @Delete
    void deleteCompletedSerie(CompletedSerie serie);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DetachSerie serie);

    @Query("SELECT * FROM DetachSerie")
    List<DetachSerie> getAllDetachSerie();

    @Query("SELECT * FROM DetachSerie WHERE id = :id")
    CompletedSerie getDetachSerieById(long id);

    @Delete
    void deleteDetachSerie(DetachSerie serie);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ForWatchSerie serie);

    @Query("SELECT * FROM ForWatchSerie")
    List<ForWatchSerie> getAllForWatchSerie();

    @Query("SELECT * FROM ForWatchSerie WHERE id = :id")
    CompletedSerie getForWatchSerieById(long id);

    @Delete
    void deleteForWatchSerie(ForWatchSerie serie);
}

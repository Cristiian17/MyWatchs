package com.mywatchs.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.mywatchs.db.Dao.MovieDAO;
import com.mywatchs.db.Dao.SerieDAO;
import com.mywatchs.db.Dao.entities.CompletedMovie;
import com.mywatchs.db.Dao.entities.CompletedSerie;
import com.mywatchs.db.Dao.entities.DetachMovie;
import com.mywatchs.db.Dao.entities.DetachSerie;
import com.mywatchs.db.Dao.entities.ForWatchMovie;
import com.mywatchs.db.Dao.entities.ForWatchSerie;

@Database(entities = {CompletedMovie.class, CompletedSerie.class, DetachMovie.class, DetachSerie.class, ForWatchMovie.class, ForWatchSerie.class}, version = 1)
public abstract class MyBD extends RoomDatabase {
    public abstract MovieDAO movieDetailsDao();

    public abstract SerieDAO serieDetailsDao();
}
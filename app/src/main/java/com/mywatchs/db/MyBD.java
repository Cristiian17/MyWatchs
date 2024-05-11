package com.mywatchs.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.mywatchs.Dao.MovieDetailsDAO;
import com.mywatchs.Dao.MoviePOJO;
import com.mywatchs.Dao.SerieDetailsDAO;
import com.mywatchs.Dao.SeriePOJO;

@Database(entities = {MoviePOJO.class, SeriePOJO.class}, version = 1)
public abstract class MyBD extends RoomDatabase {
    public abstract MovieDetailsDAO movieDetailsDao();

    public abstract SerieDetailsDAO serieDetailsDao();
}

    /*// DAOs para las tablas adicionales
    public abstract FavMoviesDAO favMoviesDao();
    public abstract FavSeriesDAO favSeriesDao();
    public abstract ForWatchMovieDAO forWatchMovieDao();
    public abstract ForWatchSerieDAO forWatchSerieDao();
    public abstract DetachMovieDAO detachMovieDao();
    public abstract DetachSerieDAO detachSerieDao();*/

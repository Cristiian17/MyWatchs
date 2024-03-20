package com.mywatchs.dao;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.mywatchs.model.Movie;
import com.mywatchs.model.MovieResponse;
import com.mywatchs.model.Serie;
import com.mywatchs.model.SerieResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieDAO {

    private static final String TAG = MovieDAO.class.getSimpleName();
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0ZGE2OGRjODM2MWIxYjIwOGNiMGNjYzU2ZjRhMWU1ZCIsInN1YiI6IjY1ZjlhYmJkMGYyZmJkMDE3ZDhhZjU1ZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.hnLH39M5kQFCogPD_FWQuDUjcsZzzP0fQKeI4oOwD_8";

    public interface MovieDataCallback {
        void onSuccessMovies(List<Movie> movies);
        void onSuccessSeries(List<Serie> series);
        void onError(String errorMessage);
    }

    public static void getPopularMovies(final MovieDataCallback callback) {
        new AsyncTask<Void, Void, List<Movie>>() {
            @Override
            protected List<Movie> doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(BASE_URL+"movie/popular?language=es-ES&page=1")
                        .get()
                        .addHeader("accept", "application/json")
                        .addHeader("Authorization", TOKEN)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        Gson gson = new Gson();
                        MovieResponse movieResponse = gson.fromJson(responseData, MovieResponse.class);
                        return Arrays.asList(movieResponse.getResults());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Movie> movies) {
                super.onPostExecute(movies);
                if (movies != null) {
                    callback.onSuccessMovies(movies);
                } else {
                    callback.onError("Error fetching data from API");
                }
            }
        }.execute();
    }


    public static void getUpcomingMovies(final MovieDataCallback callback) {
        new AsyncTask<Void, Void, List<Movie>>() {
            @Override
            protected List<Movie> doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(BASE_URL+"trending/movie/day?language=en-US")
                        .get()
                        .addHeader("accept", "application/json")
                        .addHeader("Authorization", TOKEN)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        Gson gson = new Gson();
                        MovieResponse movieResponse = gson.fromJson(responseData, MovieResponse.class);
                        return Arrays.asList(movieResponse.getResults());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Movie> movies) {
                super.onPostExecute(movies);
                if (movies != null) {
                    callback.onSuccessMovies(movies);
                } else {
                    callback.onError("Error fetching data from API");
                }
            }
        }.execute();
    }

    public static void getTopRatedSeries(final MovieDataCallback callback) {
        new AsyncTask<Void, Void, List<Serie>>() {
            @Override
            protected List<Serie> doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(BASE_URL + "trending/tv/day?language=es-ES")
                        .get()
                        .addHeader("accept", "application/json")
                        .addHeader("Authorization", TOKEN)
                        .build();


                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        Gson gson = new Gson();
                        SerieResponse serieResponse = gson.fromJson(responseData, SerieResponse.class);
                        return Arrays.asList(serieResponse.getResults());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Serie> series) {
                super.onPostExecute(series);
                if (series != null) {
                    callback.onSuccessSeries(series);
                } else {
                    callback.onError("Error fetching data from API");
                }
            }
        }.execute();
    }
}


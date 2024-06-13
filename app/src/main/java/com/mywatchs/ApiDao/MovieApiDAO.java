package com.mywatchs.ApiDao;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.mywatchs.model.movie.Movie;
import com.mywatchs.model.movie.MovieResponse;
import com.mywatchs.model.serie.Serie;
import com.mywatchs.model.serie.SerieResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieApiDAO {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0ZGE2OGRjODM2MWIxYjIwOGNiMGNjYzU2ZjRhMWU1ZCIsInN1YiI6IjY1ZjlhYmJkMGYyZmJkMDE3ZDhhZjU1ZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.hnLH39M5kQFCogPD_FWQuDUjcsZzzP0fQKeI4oOwD_8";

    public interface MovieDataCallback {
        void onSuccessMovies(List<Movie> movies);
        void onSuccessSeries(List<Serie> series);
        void onError(String errorMessage);
    }

    public static void getMovies(final MovieDataCallback callback) {
        new AsyncTask<Void, Void, List<Movie>>() {
            @Override
            protected List<Movie> doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(BASE_URL+"discover/movie?include_adult=false&include_video=false&language=es-ES&page=1&sort_by=vote_count.desc")
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

    public static void getSeries(final MovieDataCallback callback) {
        new AsyncTask<Void, Void, List<Serie>>() {
            @Override
            protected List<Serie> doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(BASE_URL + "discover/tv?include_adult=false&include_null_first_air_dates=false&language=es-ES&page=1&sort_by=vote_count.desc")
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

    public static void getMoviesByPageAndNameAndGenre(int pageNumber, String name, Integer genreId, final MovieDataCallback callback) {
        new AsyncTask<Void, Void, List<Movie>>() {
            @Override
            protected List<Movie> doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();
                StringBuilder urlBuilder = new StringBuilder();
                if (name.isEmpty()) {
                    urlBuilder.append(BASE_URL).append("discover/movie?include_adult=false&include_video=false&language=es-ES&page=").append(pageNumber).append("&sort_by=vote_count.desc");
                } else {
                    urlBuilder.append(BASE_URL).append("search/movie?include_adult=false&language=es-ES&page=").append(pageNumber).append("&query=").append(name).append("&sort_by=vote_count.desc");
                }

                if (genreId != null) {
                    urlBuilder.append("&with_genres=").append(genreId);
                }

                Request request = new Request.Builder()
                        .url(urlBuilder.toString())
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

    public static void getSeriesByPageNameAndGenre(int pageNumber, String name, Integer genreId, final MovieDataCallback callback) {
        new AsyncTask<Void, Void, List<Serie>>() {
            @Override
            protected List<Serie> doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();
                StringBuilder urlBuilder = new StringBuilder();
                if (name.isEmpty()) {
                    urlBuilder.append(BASE_URL).append("discover/tv?include_adult=false&include_null_first_air_dates=false&language=es-ES&page=").append(pageNumber).append("&sort_by=vote_count.desc");
                } else {
                    urlBuilder.append(BASE_URL).append("search/tv?include_adult=false&language=es-ES&page=").append(pageNumber).append("&query=").append(name).append("&sort_by=vote_count.desc");
                }

                if (genreId != null) {
                    urlBuilder.append("&with_genres=").append(genreId);
                }

                Request request = new Request.Builder()
                        .url(urlBuilder.toString())
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


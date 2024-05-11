package com.mywatchs.ApiDao;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.mywatchs.model.genre.GenreResponse;
import com.mywatchs.model.genre.Genre;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GenresApiDAO {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0ZGE2OGRjODM2MWIxYjIwOGNiMGNjYzU2ZjRhMWU1ZCIsInN1YiI6IjY1ZjlhYmJkMGYyZmJkMDE3ZDhhZjU1ZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.hnLH39M5kQFCogPD_FWQuDUjcsZzzP0fQKeI4oOwD_8";

    public interface GenresDataCallback {
        void onSuccessGenres(List<Genre> genres);
        void onError(String errorMessage);
    }

    public static void getMoviesGenres(final GenresDataCallback callback) {
        new AsyncTask<Void, Void, List<Genre>>() {
            @Override
            protected List<Genre> doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(BASE_URL+"genre/movie/list?language=es")
                        .get()
                        .addHeader("accept", "application/json")
                        .addHeader("Authorization", TOKEN)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        Gson gson = new Gson();
                        GenreResponse genreResponse = gson.fromJson(responseData, GenreResponse.class);
                        return Arrays.asList(genreResponse.getResults());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Genre> genres) {
                super.onPostExecute(genres);
                if (genres != null) {
                    callback.onSuccessGenres(genres);
                } else {
                    callback.onError("Error fetching data from API");
                }
            }
        }.execute();
    }

    public static void getSeriesGenres(final GenresDataCallback callback) {
        new AsyncTask<Void, Void, List<Genre>>() {
            @Override
            protected List<Genre> doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(BASE_URL+"genre/tv/list?language=es")
                        .get()
                        .addHeader("accept", "application/json")
                        .addHeader("Authorization", TOKEN)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        Gson gson = new Gson();
                        GenreResponse genreResponse = gson.fromJson(responseData, GenreResponse.class);
                        return Arrays.asList(genreResponse.getResults());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Genre> genres) {
                super.onPostExecute(genres);
                if (genres != null) {
                    callback.onSuccessGenres(genres);
                } else {
                    callback.onError("Error fetching data from API");
                }
            }
        }.execute();
    }
}

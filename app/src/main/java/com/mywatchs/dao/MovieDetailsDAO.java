package com.mywatchs.dao;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.mywatchs.model.Movie;
import com.mywatchs.model.MovieDetails;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieDetailsDAO {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0ZGE2OGRjODM2MWIxYjIwOGNiMGNjYzU2ZjRhMWU1ZCIsInN1YiI6IjY1ZjlhYmJkMGYyZmJkMDE3ZDhhZjU1ZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.hnLH39M5kQFCogPD_FWQuDUjcsZzzP0fQKeI4oOwD_8";

    public interface MovieDataCallback {
        void onSuccessMovie(MovieDetails movieDetails);
        void onError(String errorMessage);
    }

    public static void getMovieDetails(final MovieDetailsDAO.MovieDataCallback callback, long id) {
        new AsyncTask<Void, Void, MovieDetails>() {
            @Override
            protected MovieDetails doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(BASE_URL + "movie/" + id + "?language=es-ES")
                        .get()
                        .addHeader("accept", "application/json")
                        .addHeader("Authorization", TOKEN)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        Gson gson = new Gson();
                        MovieDetails movieDetails = gson.fromJson(responseData, MovieDetails.class);
                        return movieDetails;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(MovieDetails movieDetails) {
                super.onPostExecute(movieDetails);
                if (movieDetails != null) {
                    callback.onSuccessMovie(movieDetails);
                } else {
                    callback.onError("Error fetching data from API");
                }
            }
        }.execute();
    }

}

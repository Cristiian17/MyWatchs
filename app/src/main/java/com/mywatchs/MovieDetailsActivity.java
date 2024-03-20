package com.mywatchs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mywatchs.adapter.MovieAdapter;
import com.mywatchs.dao.MovieDAO;
import com.mywatchs.dao.MovieDetailsDAO;
import com.mywatchs.model.Movie;
import com.mywatchs.model.MovieDetails;
import com.mywatchs.model.Serie;

import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity {
    private long id;
    private MovieDetailsDAO movieDetailsDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        id = getIntent().getLongExtra("id",1);
        movieDetailsDAO = new MovieDetailsDAO();
        getMovie();
    }



    private void getMovie() {
        movieDetailsDAO.getMovieDetails(new MovieDetailsDAO.MovieDataCallback() {

            @Override
            public void onSuccessMovie(MovieDetails movieDetails) {
                System.out.println(movieDetails.getTitle());


                String imageUrl = "https://image.tmdb.org/t/p/w500" + movieDetails.getPosterPath();

                ImageView imageView = findViewById(R.id.movieImage);
                Glide.with(getApplicationContext()).load(imageUrl).into(imageView);

                TextView overview = findViewById(R.id.tv_overview);
                overview.setText(movieDetails.getOverview());

                TextView title = findViewById(R.id.tv_title);
                title.setText(movieDetails.getTitle());

                TextView genre = findViewById(R.id.tv_genre);
                genre.setText(movieDetails.getGenres().toString());
            }

            @Override
            public void onError(String errorMessage) {
                // Manejar el error si es necesario
            }
        }, id);
    }



}
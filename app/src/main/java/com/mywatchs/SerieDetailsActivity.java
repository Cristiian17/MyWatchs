package com.mywatchs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mywatchs.dao.MovieDetailsDAO;
import com.mywatchs.model.movie.MovieDetails;
import com.mywatchs.model.serie.SerieDetails;

public class SerieDetailsActivity extends AppCompatActivity {

    private int id;
    private MovieDetailsDAO movieDetailsDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie_details);

        id = getIntent().getIntExtra("id",1);
        movieDetailsDAO = new MovieDetailsDAO();
        getSerie();
    }

    private void getSerie() {
        movieDetailsDAO.getSerieDetails(new MovieDetailsDAO.MovieDataCallback() {

            @Override
            public void onSuccessMovie(MovieDetails movieDetails) {

            }

            @Override
            public void onSuccessSerie(SerieDetails serieDetails) {
                System.out.println(serieDetails.getName());


                String imageUrl = "https://image.tmdb.org/t/p/w500" + serieDetails.getPosterPath();

                ImageView imageView = findViewById(R.id.serieImage);
                Glide.with(getApplicationContext()).load(imageUrl).into(imageView);

                TextView overview = findViewById(R.id.tv_serie_overview);
                overview.setText(serieDetails.getOverview());

                TextView title = findViewById(R.id.tv_serie_title);
                title.setText(serieDetails.getName());

                TextView genre = findViewById(R.id.tv_serie_genre);
                genre.setText(serieDetails.getGenres().toString());

            }

            @Override
            public void onError(String errorMessage) {
                // Manejar el error si es necesario
            }
        }, id);
    }
}
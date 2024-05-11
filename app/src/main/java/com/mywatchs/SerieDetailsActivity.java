package com.mywatchs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mywatchs.ApiDao.MovieDetailsApiDAO;
import com.mywatchs.Dao.MovieDetailsDAO;
import com.mywatchs.Dao.MoviePOJO;
import com.mywatchs.Dao.SerieDetailsDAO;
import com.mywatchs.Dao.SeriePOJO;
import com.mywatchs.db.MyBD;
import com.mywatchs.model.movie.MovieDetails;
import com.mywatchs.model.serie.SerieDetails;

import java.util.List;

public class SerieDetailsActivity extends AppCompatActivity {

    private int id;
    SerieDetails serieDetails;
    private MovieDetailsApiDAO movieDetailsApiDAO;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie_details);

        id = getIntent().getIntExtra("id",1);
        movieDetailsApiDAO = new MovieDetailsApiDAO();
        getSerie();
        findViewById(R.id.button2).setOnClickListener(this::addToFav);
    }
    @SuppressLint("StaticFieldLeak")
    private void addToFav(View view) {
        SeriePOJO s = new SeriePOJO(serieDetails.getId(), serieDetails.getName());

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MyBD myBD = Room.databaseBuilder(getApplicationContext(),
                        MyBD.class, "myDB").build();

                SerieDetailsDAO favMoviesDAO = myBD.serieDetailsDao();

                favMoviesDAO.insert(s);
                favMoviesDAO.getAll().forEach(m -> System.out.println(m.getName()));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Película agregada a favoritos", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    private void getSerie() {
        movieDetailsApiDAO.getSerieDetails(new MovieDetailsApiDAO.MovieDataCallback() {

            @Override
            public void onSuccessMovie(MovieDetails movieDetails) {

            }

            @Override
            public void onSuccessSerie(SerieDetails serieDetails) {
                SerieDetailsActivity.this.serieDetails = serieDetails;


                String imageUrl = "https://image.tmdb.org/t/p/w500" + serieDetails.getPosterPath();

                ImageView imageView = findViewById(R.id.serieImage);
                Glide.with(getApplicationContext()).load(imageUrl).into(imageView);

                TextView overview = findViewById(R.id.tv_serie_overview);
                overview.setText(serieDetails.getOverview());

                TextView title = findViewById(R.id.tv_serie_title);
                title.setText(serieDetails.getName());

                TextView genre = findViewById(R.id.tv_serie_genre);
                genre.setText(serieDetails.getGenres().toString());

                TextView airDate = findViewById(R.id.tv_airdate_series);
                airDate.setText(serieDetails.getFirstAirDate() +"/"+serieDetails.getLastAirDate());

                TextView voteAverage = findViewById(R.id.tv_voteAverage_serie);
                voteAverage.setText(""+serieDetails.getVoteAverage());

                TextView seasons = findViewById(R.id.tv_seasons_series);
                seasons.setText(serieDetails.getNumberOfSeasons()+" temporadas");



            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(getApplicationContext(), "Error al intentar obtener informacion sobre la serie", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, id);
    }
}
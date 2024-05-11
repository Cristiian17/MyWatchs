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
import com.mywatchs.db.MyBD;
import com.mywatchs.model.movie.MovieDetails;
import com.mywatchs.model.serie.SerieDetails;

import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity {
    private long id;
    private MovieDetailsApiDAO movieDetailsApiDAO;
    private MovieDetails movieDetails;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        id = getIntent().getLongExtra("id",1);
        movieDetailsApiDAO = new MovieDetailsApiDAO();
        getMovie();
        findViewById(R.id.button).setOnClickListener(this::addToFav);
    }

    @SuppressLint("StaticFieldLeak")
    private void addToFav(View view) {
        MoviePOJO m = new MoviePOJO(movieDetails.getId(), movieDetails.getTitle());

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MyBD myBD = Room.databaseBuilder(getApplicationContext(),
                        MyBD.class, "myDB").build();

                MovieDetailsDAO favMoviesDAO = myBD.movieDetailsDao();

                favMoviesDAO.insert(m);
                List<MoviePOJO> list = favMoviesDAO.getAll();
                list.forEach(m -> System.out.println(m.getName()));

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Película agregada a favoritos", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }


    private void getMovie() {
        movieDetailsApiDAO.getMovieDetails(new MovieDetailsApiDAO.MovieDataCallback() {

            @Override
            public void onSuccessMovie(MovieDetails movieDetails) {
                MovieDetailsActivity.this.movieDetails = movieDetails;
                String imageUrl = "https://image.tmdb.org/t/p/w500" + movieDetails.getPosterPath();

                ImageView imageView = findViewById(R.id.movieImage);
                Glide.with(getApplicationContext()).load(imageUrl).into(imageView);

                TextView overview = findViewById(R.id.tv_serie_overview);
                overview.setText(movieDetails.getOverview());

                TextView title = findViewById(R.id.tv_serie_title);
                title.setText(movieDetails.getTitle());

                TextView genre = findViewById(R.id.tv_serie_genre);
                genre.setText(movieDetails.getGenres().toString());
            }

            @Override
            public void onSuccessSerie(SerieDetails serieDetails) {

            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(getApplicationContext(), "Error al intentar obtener información sobre la película", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, id);
    }



}
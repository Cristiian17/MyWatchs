package com.mywatchs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mywatchs.ApiDao.MovieDetailsApiDAO;
import com.mywatchs.db.Dao.MovieDAO;
import com.mywatchs.db.Dao.SerieDAO;
import com.mywatchs.db.Dao.entities.CompletedMovie;
import com.mywatchs.db.Dao.entities.CompletedSerie;
import com.mywatchs.db.Dao.entities.DetachMovie;
import com.mywatchs.db.Dao.entities.DetachSerie;
import com.mywatchs.db.Dao.entities.ForWatchMovie;
import com.mywatchs.db.Dao.entities.ForWatchSerie;
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
        findViewById(R.id.movie_addBtn).setOnClickListener(this::addToFav);
    }

    @SuppressLint("StaticFieldLeak")
    private void addToFav(View view) {

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        Button buttonCompletada = new Button(this);
        buttonCompletada.setText("Completada");
        layout.addView(buttonCompletada);

        Button buttonVerMasTarde = new Button(this);
        buttonVerMasTarde.setText("Ver mas tarde");
        layout.addView(buttonVerMasTarde);

        Button buttonAbandonada = new Button(this);
        buttonAbandonada.setText("Abandonada");
        layout.addView(buttonAbandonada);

        setContentView(layout);
        buttonCompletada.setOnClickListener(v -> {
            CompletedMovie m = new CompletedMovie(movieDetails.getId(), movieDetails.getTitle(), movieDetails.getPosterPath());
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    MyBD myBD = Room.databaseBuilder(getApplicationContext(),
                            MyBD.class, "myDB").build();

                    MovieDAO movieDAO = myBD.movieDetailsDao();

                    movieDAO.insert(m);
                    movieDAO.getAllCompeltedMovies().forEach(m -> System.out.println(m.getName()));
                    return null;
                }
                @Override
                protected void onPostExecute(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Pelicula agregada a completada", Toast.LENGTH_SHORT).show();
                }
            }.execute();
            finish();
        });

        buttonVerMasTarde.setOnClickListener(v -> {
            ForWatchMovie m = new ForWatchMovie(movieDetails.getId(), movieDetails.getTitle(), movieDetails.getPosterPath());
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    MyBD myBD = Room.databaseBuilder(getApplicationContext(),
                            MyBD.class, "myDB").build();

                    MovieDAO movieDAO = myBD.movieDetailsDao();

                    movieDAO.insert(m);
                    movieDAO.getAllForWatchMovies().forEach(m -> System.out.println(m.getName()));
                    return null;
                }
                @Override
                protected void onPostExecute(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Pelicula agregada a ver mas tarde", Toast.LENGTH_SHORT).show();
                }
            }.execute();
            finish();
        });

        buttonAbandonada.setOnClickListener(v -> {
            DetachMovie m = new DetachMovie(movieDetails.getId(), movieDetails.getTitle(), movieDetails.getPosterPath());
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    MyBD myBD = Room.databaseBuilder(getApplicationContext(),
                            MyBD.class, "myDB").build();

                    MovieDAO movieDAO = myBD.movieDetailsDao();

                    movieDAO.insert(m);
                    movieDAO.getAllDetachedMovies().forEach(m -> System.out.println(m.getName()));
                    return null;
                }
                @Override
                protected void onPostExecute(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Pelicula agregada a abandonada", Toast.LENGTH_SHORT).show();
                }
            }.execute();
            finish();
        });
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
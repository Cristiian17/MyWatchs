package com.mywatchs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        Drawable searchIcon = searchItem.getIcon();
        if (searchIcon != null) {
            searchIcon.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            searchItem.setIcon(searchIcon);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Añadir a:");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        Button buttonCompletada = new Button(this);
        buttonCompletada.setText("Completada");
        layout.addView(buttonCompletada);

        Button buttonVerMasTarde = new Button(this);
        buttonVerMasTarde.setText("Ver más tarde");
        layout.addView(buttonVerMasTarde);

        Button buttonAbandonada = new Button(this);
        buttonAbandonada.setText("Abandonada");
        layout.addView(buttonAbandonada);

        Button buttonCancelar = new Button(this);
        buttonCancelar.setText("Cancelar");
        layout.addView(buttonCancelar);


        builder.setView(layout);


        AlertDialog dialog = builder.create();
        dialog.show();


        buttonCompletada.setOnClickListener(v -> {
            CompletedMovie m = new CompletedMovie(movieDetails.getId(), movieDetails.getTitle(), movieDetails.getPosterPath());
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    MyBD myBD = Room.databaseBuilder(getApplicationContext(),
                            MyBD.class, "myDB").build();

                    MovieDAO movieDAO = myBD.movieDetailsDao();

                    movieDAO.insert(m);
                    movieDAO.getAllCompeltedMovies().forEach(movie -> System.out.println(movie.getName()));
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    Toast.makeText(getApplicationContext(), m.getName() + " agregada a completada", Toast.LENGTH_SHORT).show();
                }
            }.execute();
            dialog.dismiss();
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
                    movieDAO.getAllForWatchMovies().forEach(movie -> System.out.println(movie.getName()));
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    Toast.makeText(getApplicationContext(), m.getName() + " agregada a ver más tarde", Toast.LENGTH_SHORT).show();
                }
            }.execute();
            dialog.dismiss();
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
                    movieDAO.getAllDetachedMovies().forEach(movie -> System.out.println(movie.getName()));
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    Toast.makeText(getApplicationContext(), m.getName() + " agregada a abandonada", Toast.LENGTH_SHORT).show();
                }
            }.execute();
            dialog.dismiss();
        });

        buttonCancelar.setOnClickListener(v -> dialog.dismiss());

        return super.onOptionsItemSelected(item);
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

                TextView time = findViewById(R.id.tv_time);
                time.setText(movieDetails.getRuntime() + "min");

                TextView releaseDate = findViewById(R.id.tv_releaseDate);
                releaseDate.setText("" + movieDetails.getReleaseDate());

                TextView voteAverage = findViewById(R.id.tv_voteAverage_movie);
                voteAverage.setText("" + movieDetails.getVoteAverage());

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
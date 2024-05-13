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
import com.mywatchs.db.Dao.entities.CompletedSerie;
import com.mywatchs.db.Dao.SerieDAO;
import com.mywatchs.db.Dao.entities.DetachSerie;
import com.mywatchs.db.Dao.entities.ForWatchSerie;
import com.mywatchs.db.MyBD;
import com.mywatchs.model.movie.MovieDetails;
import com.mywatchs.model.serie.SerieDetails;

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
        findViewById(R.id.series_addBtn).setOnClickListener(this::addToFav);
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
            CompletedSerie s = new CompletedSerie(serieDetails.getId(), serieDetails.getName(), serieDetails.getPosterPath());
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    MyBD myBD = Room.databaseBuilder(getApplicationContext(),
                            MyBD.class, "myDB").build();

                    SerieDAO favMoviesDAO = myBD.serieDetailsDao();

                    favMoviesDAO.insert(s);
                    favMoviesDAO.getAllCompeltedSeries().forEach(m -> System.out.println(m.getName()));
                    return null;
                }
                @Override
                protected void onPostExecute(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Serie agregada a completada", Toast.LENGTH_SHORT).show();
                }
            }.execute();
            finish();
        });

        buttonVerMasTarde.setOnClickListener(v -> {
            ForWatchSerie s = new ForWatchSerie(serieDetails.getId(), serieDetails.getName(), serieDetails.getPosterPath());
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    MyBD myBD = Room.databaseBuilder(getApplicationContext(),
                            MyBD.class, "myDB").build();

                    SerieDAO favMoviesDAO = myBD.serieDetailsDao();

                    favMoviesDAO.insert(s);
                    favMoviesDAO.getAllForWatchSerie().forEach(m -> System.out.println(m.getName()));
                    return null;
                }
                @Override
                protected void onPostExecute(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Serie agregada a ver mas tarde", Toast.LENGTH_SHORT).show();
                }
            }.execute();
            finish();
        });

        buttonAbandonada.setOnClickListener(v -> {
            DetachSerie s = new DetachSerie(serieDetails.getId(), serieDetails.getName(), serieDetails.getPosterPath());
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    MyBD myBD = Room.databaseBuilder(getApplicationContext(),
                            MyBD.class, "myDB").build();

                    SerieDAO favMoviesDAO = myBD.serieDetailsDao();

                    favMoviesDAO.insert(s);
                    favMoviesDAO.getAllDetachSerie().forEach(m -> System.out.println(m.getName()));
                    return null;
                }
                @Override
                protected void onPostExecute(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Serie agregada a abandonada", Toast.LENGTH_SHORT).show();
                }
            }.execute();
            finish();
        });
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
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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie_details);

        id = getIntent().getIntExtra("id",1);
        getSerie();
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
        builder.setTitle("Seleccionar estado");

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
            CompletedSerie s = new CompletedSerie(serieDetails.getId(), serieDetails.getName(), serieDetails.getPosterPath());
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    MyBD myBD = Room.databaseBuilder(getApplicationContext(),
                            MyBD.class, "myDB").build();

                    SerieDAO favMoviesDAO = myBD.serieDetailsDao();

                    favMoviesDAO.insert(s);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Serie agregada a completada", Toast.LENGTH_SHORT).show();
                }
            }.execute();
            dialog.dismiss();
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
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Serie agregada a ver más tarde", Toast.LENGTH_SHORT).show();
                }
            }.execute();
            dialog.dismiss();
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
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Serie agregada a abandonada", Toast.LENGTH_SHORT).show();
                }
            }.execute();
            dialog.dismiss();
        });
        buttonCancelar.setOnClickListener(v -> dialog.dismiss());
        return super.onOptionsItemSelected(item);
    }


    private void getSerie() {
        MovieDetailsApiDAO.getSerieDetails(new MovieDetailsApiDAO.MovieDataCallback() {

            @Override
            public void onSuccessMovie(MovieDetails movieDetails) {
            }

            @SuppressLint("SetTextI18n")
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
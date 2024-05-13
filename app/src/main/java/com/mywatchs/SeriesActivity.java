package com.mywatchs;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mywatchs.adapter.GenreAdapter;
import com.mywatchs.adapter.SerieAdapter;
import com.mywatchs.ApiDao.GenresApiDAO;
import com.mywatchs.ApiDao.MovieApiDAO;
import com.mywatchs.model.genre.Genre;
import com.mywatchs.model.movie.Movie;
import com.mywatchs.model.serie.Serie;

import java.util.ArrayList;
import java.util.List;

public class SeriesActivity extends AppCompatActivity {
    private MovieApiDAO movieApiDAO;
    private int page;
    private List<Serie> series;
    private RecyclerView view;
    private SerieAdapter adapter;
    String serieName;
    private boolean isLoading = false;
    private GenresApiDAO genresApiDAO;
    private List<Genre> genres;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series);

        movieApiDAO = new MovieApiDAO();
        genresApiDAO = new GenresApiDAO();
        view = findViewById(R.id.allSeriesView);
        page = 1;
        serieName = "";
        series = new ArrayList<>();
        genres = new ArrayList<>();
        createAdapter();
        getSeries(page);
        getGenres();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    private void createAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        view.setLayoutManager(gridLayoutManager);

        view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = gridLayoutManager.getChildCount();
                int totalItemCount = gridLayoutManager.getItemCount();
                int firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();

                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    loadMoreData();
                }
            }
        });
    }
    private void getGenres() {
        genresApiDAO.getSeriesGenres(new GenresApiDAO.GenresDataCallback() {
            @Override
            public void onSuccessGenres(List<Genre> genres) {
                SeriesActivity.this.genres = genres;
                RecyclerView genderView = findViewById(R.id.seriesGenresView);
                genderView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                GenreAdapter adapter = new GenreAdapter(genres, getApplicationContext());
                genderView.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {
                // Manejar el error si es necesario
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setQueryHint("Buscar serie");
        searchMenuItem.setIconTintList(ColorStateList.valueOf(Color.WHITE));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                serieName = newText;
                page = 1;
                movieApiDAO.getSeriesByPageAndName(page, serieName, new MovieApiDAO.MovieDataCallback() {
                    @Override
                    public void onSuccessMovies(List<Movie> movies) {
                    }

                    @Override
                    public void onSuccessSeries(List<Serie> series) {
                        SeriesActivity.this.series.clear();
                        SeriesActivity.this.series.addAll(series);
                        adapter.notifyDataSetChanged();
                        isLoading = false;
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Handle error if needed
                        isLoading = false;
                    }
                });
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    private void getSeries(int page) {
        movieApiDAO.getSeriesByPageAndName(page,serieName, new MovieApiDAO.MovieDataCallback() {
            @Override
            public void onSuccessMovies(List<Movie> movies) {

            }

            @Override
            public void onSuccessSeries(List<Serie> series) {
                SeriesActivity.this.series.addAll(series);


                adapter = new SerieAdapter(SeriesActivity.this.series, getApplicationContext(), position -> {
                    Serie serie = SeriesActivity.this.series.get(position);

                    Intent intent = new Intent(getApplicationContext(), SerieDetailsActivity.class);

                    intent.putExtra("id",serie.getId());
                    startActivity(intent);
                });

                view.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    private void loadMoreData() {
        isLoading = true;
        page++;
        movieApiDAO.getSeriesByPageAndName(page,serieName ,new MovieApiDAO.MovieDataCallback() {
            @Override
            public void onSuccessMovies(List<Movie> movies) {

            }

            @Override
            public void onSuccessSeries(List<Serie> series) {
                SeriesActivity.this.series.addAll(series);
                adapter.notifyDataSetChanged();
                isLoading = false;
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error if needed
                isLoading = false;
            }
        });
    }
}
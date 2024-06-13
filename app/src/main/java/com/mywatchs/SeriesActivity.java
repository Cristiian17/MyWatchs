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
    private String serieName;
    private boolean isLoading = false;
    private GenresApiDAO genresApiDAO;
    private Genre selectedGenre;

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
                RecyclerView genreView = findViewById(R.id.seriesGenresView);
                genreView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

                GenreAdapter.OnItemClickListener listener = genre -> {
                    if (selectedGenre == genre) {
                        selectedGenre = null;
                        getSeries(1);
                    } else {
                        selectedGenre = genre;
                        filterSeriesByGenre(genre);
                    }
                };

                GenreAdapter adapter = new GenreAdapter(genres, getApplicationContext(), listener);
                genreView.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {
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
                filterSeries();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void filterSeries() {
        series.clear();
        adapter.notifyDataSetChanged();
        loadSeries(page, serieName, selectedGenre);
    }

    private void filterSeriesByGenre(Genre genre) {
        series.clear();
        adapter.notifyDataSetChanged();
        loadSeries(1, serieName, genre);
    }

    private void getSeries(int page) {
        loadSeries(page, serieName, selectedGenre);
    }

    private void loadSeries(int page, String serieName, Genre genre) {
        isLoading = true;
        movieApiDAO.getSeriesByPageNameAndGenre(page, serieName, genre != null ? genre.getId() : null, new MovieApiDAO.MovieDataCallback() {
            @Override
            public void onSuccessMovies(List<Movie> movies) {
            }

            @Override
            public void onSuccessSeries(List<Serie> series) {
                SeriesActivity.this.series.addAll(series);
                if (adapter == null) {
                    adapter = new SerieAdapter(SeriesActivity.this.series, getApplicationContext(), position -> {
                        Serie serie = SeriesActivity.this.series.get(position);
                        Intent intent = new Intent(getApplicationContext(), SerieDetailsActivity.class);
                        intent.putExtra("id", serie.getId());
                        startActivity(intent);
                    });
                    view.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
                isLoading = false;
            }

            @Override
            public void onError(String errorMessage) {
                isLoading = false;
            }
        });
    }

    private void loadMoreData() {
        if (!isLoading) {
            page++;
            loadSeries(page, serieName, selectedGenre);
        }
    }
}

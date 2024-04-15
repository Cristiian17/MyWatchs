package com.mywatchs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.mywatchs.adapter.GenreAdapter;
import com.mywatchs.adapter.SerieAdapter;
import com.mywatchs.dao.GenresDAO;
import com.mywatchs.dao.MovieDAO;
import com.mywatchs.model.genre.Genre;
import com.mywatchs.model.movie.Movie;
import com.mywatchs.model.serie.Serie;

import java.util.ArrayList;
import java.util.List;

public class SeriesActivity extends AppCompatActivity {
    private MovieDAO movieDAO;
    private int page;
    private List<Serie> series;
    private RecyclerView view;
    private SerieAdapter adapter;
    private boolean isLoading = false;
    private GenresDAO genresDAO;
    private List<Genre> genres;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series);

        movieDAO = new MovieDAO();
        genresDAO = new GenresDAO();
        view = findViewById(R.id.allSeriesView);
        page = 1;
        series = new ArrayList<>();
        genres = new ArrayList<>();
        createAdapter();
        getSeries(page);
        getGenres();
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
        genresDAO.getSeriesGenres(new GenresDAO.GenresDataCallback() {
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


    private void getSeries(int page) {
        movieDAO.getSeriesByPage(page, new MovieDAO.MovieDataCallback() {
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
        movieDAO.getSeriesByPage(page, new MovieDAO.MovieDataCallback() {
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
package com.mywatchs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mywatchs.adapter.MovieAdapter;
import com.mywatchs.adapter.SerieAdapter;
import com.mywatchs.dao.MovieDAO;
import com.mywatchs.model.movie.Movie;
import com.mywatchs.model.serie.Serie;
import com.mywatchs.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class SeriesActivity extends AppCompatActivity {
    private MovieDAO movieDAO;
    private int page;
    private List<Serie> series;
    private RecyclerView view;
    private SerieAdapter adapter;
    private boolean isLoading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series);

        movieDAO = new MovieDAO();
        view = findViewById(R.id.allSeriesView);
        page = 1;
        series = new ArrayList<>();
        createAdapter();
        getSeries(page);
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
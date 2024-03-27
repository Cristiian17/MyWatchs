package com.mywatchs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mywatchs.adapter.MovieAdapter;
import com.mywatchs.dao.MovieDAO;
import com.mywatchs.model.movie.Movie;
import com.mywatchs.model.serie.Serie;
import com.mywatchs.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class MoviesActivity extends AppCompatActivity {

    private MovieDAO movieDAO;
    private int page;
    private List<Movie> movies;
    private RecyclerView view;
    private MovieAdapter adapter;
    private boolean isLoading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        movieDAO = new MovieDAO();
        view = findViewById(R.id.allMoviesView);
        page = 1;
        movies = new ArrayList<>();
        createAdapter();
        getMovies(page);
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


    private void getMovies(int page) {
        movieDAO.getMoviesByPage(page, new MovieDAO.MovieDataCallback() {
            @Override
            public void onSuccessMovies(List<Movie> movies) {
                MoviesActivity.this.movies.addAll(movies);


                adapter = new MovieAdapter(MoviesActivity.this.movies, getApplicationContext(), position -> {
                Movie movie = MoviesActivity.this.movies.get(position);

                Intent intent = new Intent(getApplicationContext(), MovieDetailsActivity.class);

                intent.putExtra("id",movie.getId());
                startActivity(intent);
            });

                view.setAdapter(adapter);
            }

            @Override
            public void onSuccessSeries(List<Serie> series) {

            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    private void loadMoreData() {
        isLoading = true;
        page++;
        movieDAO.getMoviesByPage(page, new MovieDAO.MovieDataCallback() {
            @Override
            public void onSuccessMovies(List<Movie> movies) {
                MoviesActivity.this.movies.addAll(movies);
                adapter.notifyDataSetChanged();
                isLoading = false;
            }

            @Override
            public void onSuccessSeries(List<Serie> series) {
                // Handle series if needed
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error if needed
                isLoading = false;
            }
        });
    }
}
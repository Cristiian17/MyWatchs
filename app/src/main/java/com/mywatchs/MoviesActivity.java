package com.mywatchs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.mywatchs.adapter.GenreAdapter;
import com.mywatchs.adapter.MovieAdapter;
import com.mywatchs.dao.GenresDAO;
import com.mywatchs.dao.MovieDAO;
import com.mywatchs.model.genre.Genre;
import com.mywatchs.model.movie.Movie;
import com.mywatchs.model.serie.Serie;

import java.util.ArrayList;
import java.util.List;

public class MoviesActivity extends AppCompatActivity {

    private MovieDAO movieDAO;
    private GenresDAO genresDAO;
    private int page;
    private List<Movie> movies;
    private List<Genre> genres;
    private RecyclerView view;
    private MovieAdapter adapter;
    private boolean isLoading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        movieDAO = new MovieDAO();
        genresDAO = new GenresDAO();
        view = findViewById(R.id.allMoviesView);
        page = 1;
        movies = new ArrayList<>();
        genres = new ArrayList<>();
        createAdapter();
        getMovies(page);
        getGenres();
    }

    private void getGenres() {
        genresDAO.getMoviesGenres(new GenresDAO.GenresDataCallback() {
            @Override
            public void onSuccessGenres(List<Genre> genres) {
                MoviesActivity.this.genres = genres;
                RecyclerView genderView = findViewById(R.id.moviesGenresView);
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
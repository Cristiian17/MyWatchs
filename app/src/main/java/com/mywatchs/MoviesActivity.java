package com.mywatchs;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mywatchs.adapter.GenreAdapter;
import com.mywatchs.adapter.MovieAdapter;
import com.mywatchs.ApiDao.GenresApiDAO;
import com.mywatchs.ApiDao.MovieApiDAO;
import com.mywatchs.model.genre.Genre;
import com.mywatchs.model.movie.Movie;
import com.mywatchs.model.serie.Serie;

import java.util.ArrayList;
import java.util.List;

public class MoviesActivity extends AppCompatActivity {

    private MovieApiDAO movieApiDAO;
    private GenresApiDAO genresApiDAO;
    private int page;
    private List<Movie> movies;
    private String movieName;
    private RecyclerView view;
    private MovieAdapter adapter;
    private boolean isLoading = false;
    private Genre selectedGenre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        movieApiDAO = new MovieApiDAO();
        genresApiDAO = new GenresApiDAO();
        view = findViewById(R.id.allMoviesView);
        page = 1;
        movies = new ArrayList<>();
        movieName = "";
        createAdapter();
        getGenres();
        getMovies(page);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setQueryHint("Buscar pelicula");
        searchMenuItem.setIconTintList(ColorStateList.valueOf(Color.WHITE));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                movieName = newText;
                page = 1;
                filterMovies(selectedGenre);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void filterMovies(Genre genre) {
        movies.clear();
        adapter.notifyDataSetChanged();
        loadMovies(page, movieName, genre);
    }

    private void getGenres() {
        genresApiDAO.getMoviesGenres(new GenresApiDAO.GenresDataCallback() {
            @Override
            public void onSuccessGenres(List<Genre> genres) {
                RecyclerView genreView = findViewById(R.id.moviesGenresView);
                genreView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

                GenreAdapter.OnItemClickListener listener = genre -> {
                    if (selectedGenre == genre) {
                        selectedGenre = null;
                        getMovies(1);
                    } else {
                        selectedGenre = genre;
                        filterMovies(genre);
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
        loadMovies(page, movieName, selectedGenre);
    }

    private void loadMovies(int page, String movieName, Genre genre) {
        isLoading = true;
        movieApiDAO.getMoviesByPageAndNameAndGenre(page, movieName, genre != null ? genre.getId() : null, new MovieApiDAO.MovieDataCallback() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccessMovies(List<Movie> movies) {
                MoviesActivity.this.movies.addAll(movies);

                if (adapter == null) {
                    adapter = new MovieAdapter(MoviesActivity.this.movies, getApplicationContext(), position -> {
                        Movie movie = MoviesActivity.this.movies.get(position);
                        Intent intent = new Intent(getApplicationContext(), MovieDetailsActivity.class);
                        intent.putExtra("id", movie.getId());
                        startActivity(intent);
                    });
                    view.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }

                isLoading = false;
            }

            @Override
            public void onSuccessSeries(List<Serie> series) {
            }

            @Override
            public void onError(String errorMessage) {
                isLoading = false;
            }
        });
    }

    private void loadMoreData() {
        isLoading = true;
        page++;
        loadMovies(page, movieName, selectedGenre);
    }
}

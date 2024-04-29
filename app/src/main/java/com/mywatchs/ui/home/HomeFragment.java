package com.mywatchs.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mywatchs.MovieDetailsActivity;
import com.mywatchs.MoviesActivity;
import com.mywatchs.R;
import com.mywatchs.SerieDetailsActivity;
import com.mywatchs.SeriesActivity;
import com.mywatchs.adapter.MovieAdapter;
import com.mywatchs.adapter.SerieAdapter;
import com.mywatchs.dao.MovieDAO;
import com.mywatchs.databinding.FragmentHomeBinding;
import com.mywatchs.model.movie.Movie;
import com.mywatchs.model.serie.Serie;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private MovieDAO movieDAO;
    private List<Movie> movies;
    private List<Serie> series;
    private MovieAdapter adapter;
    private List<String> genders;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setHasOptionsMenu(true);

        movieDAO = new MovieDAO();
        getPopularMovies();
        getPopularSeries();
        binding.btnAllMovies.setOnClickListener(this::seeAllMovies);
        binding.btnAllSeries.setOnClickListener(this::seeAllSeries);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.action_bar, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setQueryHint("Search here");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Aquí puedes manejar la acción cuando se envía una consulta de búsqueda
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Aquí puedes manejar la acción cuando el texto de búsqueda cambia
                return false;
            }
        });
    }

    private void seeAllSeries(View view) {
        Intent intent = new Intent(getContext(), SeriesActivity.class);
        startActivity(intent);
    }

    private void seeAllMovies(View view) {
        Intent intent = new Intent(getContext(), MoviesActivity.class);
        startActivity(intent);
    }

    private void getPopularSeries() {
        movieDAO.getSeries(new MovieDAO.MovieDataCallback() {
            @Override
            public void onSuccessMovies(List<Movie> movies) {

            }

            @Override
            public void onSuccessSeries(List<Serie> series) {
                HomeFragment.this.series = series;
                binding.seriesView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                SerieAdapter adapter = new SerieAdapter(HomeFragment.this.series, getContext(), position -> {

                    Serie serie = HomeFragment.this.series.get(position);

                    Intent intent = new Intent(getContext(), SerieDetailsActivity.class);

                    intent.putExtra("id",serie.getId());
                    startActivity(intent);
                });
                binding.seriesView.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    private void getPopularMovies() {
        movieDAO.getMovies(new MovieDAO.MovieDataCallback() {
            @Override
            public void onSuccessMovies(List<Movie> movies) {
                HomeFragment.this.movies = movies;
                binding.moviesView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                adapter = new MovieAdapter(HomeFragment.this.movies, getContext(), position -> {

                    Movie movie = HomeFragment.this.movies.get(position);

                    Intent intent = new Intent(getContext(), MovieDetailsActivity.class);

                    intent.putExtra("id",movie.getId());
                    startActivity(intent);
                });
                binding.moviesView.setAdapter(adapter);
            }

            @Override
            public void onSuccessSeries(List<Serie> series) {

            }

            @Override
            public void onError(String errorMessage) {
                // Manejar el error si es necesario
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
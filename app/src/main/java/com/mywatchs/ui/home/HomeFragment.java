package com.mywatchs.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mywatchs.MovieDetailsActivity;
import com.mywatchs.MoviesActivity;
import com.mywatchs.SerieDetailsActivity;
import com.mywatchs.SeriesActivity;
import com.mywatchs.adapter.GenreAdapter;
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

        movieDAO = new MovieDAO();
        setGenders();
        getPopularMovies();
        getPopularSeries();
        binding.btnAllMovies.setOnClickListener(this::seeAllMovies);
        binding.btnAllSeries.setOnClickListener(this::seeAllSeries);
        return root;
    }

    private void seeAllSeries(View view) {
        Intent intent = new Intent(getContext(), SeriesActivity.class);
        startActivity(intent);
    }

    private void seeAllMovies(View view) {
        Intent intent = new Intent(getContext(), MoviesActivity.class);
        startActivity(intent);
    }

    private void setGenders() {
        genders = Arrays.asList(
                "Drama",
                "Comedia",
                "Acción",
                "Aventura",
                "Ciencia ficción",
                "Fantasía",
                "Terror",
                "Misterio",
                "Crimen",
                "Animación",
                "Documental",
                "Romance",
                "Thriller",
                "Histórico",
                "Familiar"
        );

        binding.genderView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        GenreAdapter adapter = new GenreAdapter(genders, getContext());
        binding.genderView.setAdapter(adapter);

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
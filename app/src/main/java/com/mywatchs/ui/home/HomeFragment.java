package com.mywatchs.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mywatchs.MovieDetailsActivity;
import com.mywatchs.adapter.GenreAdapter;
import com.mywatchs.adapter.MovieAdapter;
import com.mywatchs.adapter.SerieAdapter;
import com.mywatchs.dao.MovieDAO;
import com.mywatchs.databinding.FragmentHomeBinding;
import com.mywatchs.model.Movie;
import com.mywatchs.model.Serie;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private MovieDAO movieDAO;
    private List<Movie> popularMovies;
    private List<Serie> popularSeries;
    MovieAdapter adapter;
    List<String> genders;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        movieDAO = new MovieDAO();
        setGenders();
        getPopularMovies();
        getPopularSeries();
        return root;
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
        movieDAO.getTopRatedSeries(new MovieDAO.MovieDataCallback() {
            @Override
            public void onSuccessMovies(List<Movie> movies) {

            }

            @Override
            public void onSuccessSeries(List<Serie> series) {
                popularSeries = series;
                binding.seriesView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                SerieAdapter adapter = new SerieAdapter(popularSeries, getContext());
                binding.seriesView.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    private void getPopularMovies() {
        movieDAO.getPopularMovies(new MovieDAO.MovieDataCallback() {
            @Override
            public void onSuccessMovies(List<Movie> movies) {
                popularMovies = movies;
                binding.moviesView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                adapter = new MovieAdapter(popularMovies, getContext(), position -> {

                    Movie movie = popularMovies.get(position);

                    Intent intent = new Intent(getContext(), MovieDetailsActivity.class);

                    intent.putExtra("id",movie.getId());
                    intent.putExtra("movie", movie);
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
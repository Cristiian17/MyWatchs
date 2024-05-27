package com.mywatchs.ui.moviesViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.mywatchs.MovieDetailsActivity;
import com.mywatchs.adapter.CompletedMoviesAdapter;
import com.mywatchs.databinding.FragmentCompletedMoviesBinding;
import com.mywatchs.db.Dao.MovieDAO;
import com.mywatchs.db.Dao.entities.CompletedMovie;
import com.mywatchs.db.MyBD;
import com.mywatchs.ui.seriesViewPager.CompletedSeriesFragment;

import java.util.ArrayList;
import java.util.List;
public class CompletedMoviesFragment extends Fragment {

    private FragmentCompletedMoviesBinding binding;
    private List<CompletedMovie> completedMovies;
    private CompletedMoviesAdapter adapter;
    private RecyclerView view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCompletedMoviesBinding.inflate(inflater, container, false);
        completedMovies = new ArrayList<>();
        view = binding.completedMoviesView;
        getAllMovies();
        createAdapter();
        return binding.getRoot();
    }
    @Override
    public void onResume() {
        super.onResume();
        getAllMovies();
    }

    private void createAdapter() {
        view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new CompletedMoviesAdapter(completedMovies, getContext(), new CompletedMoviesAdapter.MovieItemClickListener() {
            @Override
            public void onMovieItemClick(int position) {
                CompletedMovie movie = completedMovies.get(position);
                Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
                intent.putExtra("id", movie.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteButtonClick(int position) {
                CompletedMovie movie = completedMovies.get(position);
                deleteMovie(movie);
            }
        });
        view.setAdapter(adapter);
    }
    @SuppressLint("StaticFieldLeak")
    private void deleteMovie(CompletedMovie movie) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MyBD myBD = Room.databaseBuilder(getContext(),
                        MyBD.class, "myDB").build();

                MovieDAO movieDAO = myBD.movieDetailsDao();
                movieDAO.delete(movie);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                completedMovies.remove(movie);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }
    @SuppressLint("StaticFieldLeak")
    private void getAllMovies() {
        new AsyncTask<Void, Void, List<CompletedMovie>>() {
            @Override
            protected List<CompletedMovie> doInBackground(Void... voids) {
                MyBD myBD = Room.databaseBuilder(getContext(),
                        MyBD.class, "myDB").build();

                MovieDAO movieDAO = myBD.movieDetailsDao();
                List<CompletedMovie> completedMovies = movieDAO.getAllCompeltedMovies();
                return completedMovies;
            }

            @Override
            protected void onPostExecute(List<CompletedMovie> series) {
                CompletedMoviesFragment.this.completedMovies.clear();
                CompletedMoviesFragment.this.completedMovies.addAll(series);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

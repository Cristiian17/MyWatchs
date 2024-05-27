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
import com.mywatchs.adapter.ForWatchMoviesAdapter;
import com.mywatchs.databinding.FragmentToWatchMoviesBinding;
import com.mywatchs.db.Dao.MovieDAO;
import com.mywatchs.db.Dao.entities.ForWatchMovie;
import com.mywatchs.db.MyBD;

import java.util.ArrayList;
import java.util.List;

public class ToWatchMoviesFragment extends Fragment {

    private FragmentToWatchMoviesBinding binding;
    private List<ForWatchMovie> forWatchMovies;
    private ForWatchMoviesAdapter adapter;
    private RecyclerView view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentToWatchMoviesBinding.inflate(inflater, container, false);
        forWatchMovies = new ArrayList<>();
        view = binding.forWatchMoviesView;
        createAdapter();
        getAllMovies();
        return binding.getRoot();
    }
    @Override
    public void onResume() {
        super.onResume();
        getAllMovies();
    }
    private void createAdapter() {
        view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new ForWatchMoviesAdapter(forWatchMovies, getContext(), new ForWatchMoviesAdapter.MovieItemClickListener() {
            @Override
            public void onMovieItemClick(int position) {
                ForWatchMovie movie = forWatchMovies.get(position);
                Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
                intent.putExtra("id", movie.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteButtonClick(int position) {
                ForWatchMovie movie = forWatchMovies.get(position);
                deleteMovieFromDatabase(movie);
                forWatchMovies.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });
        view.setAdapter(adapter);
    }

    @SuppressLint("StaticFieldLeak")
    private void deleteMovieFromDatabase(ForWatchMovie movie) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MyBD myBD = Room.databaseBuilder(requireContext(), MyBD.class, "myDB").build();
                MovieDAO movieDAO = myBD.movieDetailsDao();
                movieDAO.delete(movie);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void getAllMovies() {
        new AsyncTask<Void, Void, List<ForWatchMovie>>() {
            @Override
            protected List<ForWatchMovie> doInBackground(Void... voids) {
                MyBD myBD = Room.databaseBuilder(requireContext(),
                        MyBD.class, "myDB").build();

                MovieDAO movieDAO = myBD.movieDetailsDao();
                return movieDAO.getAllForWatchMovies();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(List<ForWatchMovie> series) {
                ToWatchMoviesFragment.this.forWatchMovies.clear();
                ToWatchMoviesFragment.this.forWatchMovies.addAll(series);
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

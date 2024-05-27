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
import com.mywatchs.adapter.DetatchedMoviesAdapter;
import com.mywatchs.databinding.FragmentDiscardedMoviesBinding;
import com.mywatchs.db.Dao.MovieDAO;
import com.mywatchs.db.Dao.entities.DetachMovie;
import com.mywatchs.db.MyBD;

import java.util.ArrayList;
import java.util.List;

public class DiscardedMoviesFragment extends Fragment {

    private FragmentDiscardedMoviesBinding binding;
    private List<DetachMovie> detachMovies;
    private DetatchedMoviesAdapter adapter;
    private RecyclerView view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDiscardedMoviesBinding.inflate(inflater, container, false);
        detachMovies = new ArrayList<>();
        view = binding.detatchMoviesView;
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
        adapter = new DetatchedMoviesAdapter(detachMovies, getContext(), new DetatchedMoviesAdapter.MovieItemClickListener() {
            @Override
            public void onMovieItemClick(int position) {
                DetachMovie movie = detachMovies.get(position);
                Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
                intent.putExtra("id", movie.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteButtonClick(int position) {
                DetachMovie movie = detachMovies.get(position);
                deleteMovieFromDatabase(movie);
                detachMovies.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });
        view.setAdapter(adapter);
    }

    @SuppressLint("StaticFieldLeak")
    private void deleteMovieFromDatabase(DetachMovie movie) {
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
        new AsyncTask<Void, Void, List<DetachMovie>>() {
            @Override
            protected List<DetachMovie> doInBackground(Void... voids) {
                MyBD myBD = Room.databaseBuilder(requireContext(),
                        MyBD.class, "myDB").build();

                MovieDAO movieDAO = myBD.movieDetailsDao();
                return movieDAO.getAllDetachedMovies();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(List<DetachMovie> series) {
                DiscardedMoviesFragment.this.detachMovies.clear();
                DiscardedMoviesFragment.this.detachMovies.addAll(series);
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

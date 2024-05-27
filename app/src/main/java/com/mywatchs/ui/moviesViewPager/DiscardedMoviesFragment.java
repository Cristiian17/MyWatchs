package com.mywatchs.ui.moviesViewPager;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;
import com.mywatchs.databinding.FragmentDiscardedMoviesBinding;
import com.mywatchs.db.Dao.MovieDAO;
import com.mywatchs.db.Dao.entities.DetachMovie;
import com.mywatchs.db.MyBD;

import java.util.List;

public class DiscardedMoviesFragment extends Fragment {

    private FragmentDiscardedMoviesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDiscardedMoviesBinding.inflate(inflater, container, false);
        getAllMovies();
        return binding.getRoot();
    }
    @Override
    public void onResume() {
        super.onResume();
        getAllMovies();  // Recarga los datos cada vez que el fragmento se vuelve visible
    }

    @SuppressLint("StaticFieldLeak")
    private void getAllMovies() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                MyBD myBD = Room.databaseBuilder(getContext(),
                        MyBD.class, "myDB").build();

                MovieDAO movieDAO = myBD.movieDetailsDao();
                List<DetachMovie> detachedMovies = movieDAO.getAllDetachedMovies();
                StringBuilder series = new StringBuilder();
                for (DetachMovie c: detachedMovies) {
                    series.append(", ").append(c.getName());
                }
                return series.toString();
            }

            @Override
            protected void onPostExecute(String series) {
                binding.textView4.setText(series);
            }
        }.execute();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

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
import com.mywatchs.databinding.FragmentCompletedMoviesBinding;
import com.mywatchs.db.Dao.MovieDAO;
import com.mywatchs.db.Dao.entities.CompletedMovie;
import com.mywatchs.db.MyBD;
import java.util.List;
public class CompletedMoviesFragment extends Fragment {

    private FragmentCompletedMoviesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCompletedMoviesBinding.inflate(inflater, container, false);
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
                List<CompletedMovie> completedMovies = movieDAO.getAllCompeltedMovies();
                StringBuilder series = new StringBuilder();
                for (CompletedMovie c: completedMovies) {
                    series.append(", ").append(c.getName());
                }
                return series.toString();
            }

            @Override
            protected void onPostExecute(String series) {
                binding.textView3.setText(series);
            }
        }.execute();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

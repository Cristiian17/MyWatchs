package com.mywatchs.ui.seriesViewPager;

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

import com.mywatchs.databinding.FragmentCompletedSeriesBinding;
import com.mywatchs.db.Dao.MovieDAO;
import com.mywatchs.db.Dao.SerieDAO;
import com.mywatchs.db.Dao.entities.CompletedSerie;
import com.mywatchs.db.Dao.entities.ForWatchMovie;
import com.mywatchs.db.MyBD;

import java.util.List;


public class CompletedSeriesFragment extends Fragment {

    private FragmentCompletedSeriesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCompletedSeriesBinding.inflate(inflater, container, false);
        getAllSeries();
        return binding.getRoot();
    }
    @Override
    public void onResume() {
        super.onResume();
        getAllSeries();  // Recarga los datos cada vez que el fragmento se vuelve visible
    }

    @SuppressLint("StaticFieldLeak")
    private void getAllSeries() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                MyBD myBD = Room.databaseBuilder(getContext(),
                        MyBD.class, "myDB").build();

                SerieDAO serieDAO = myBD.serieDetailsDao();
                List<CompletedSerie> completedSeries = serieDAO.getAllCompeltedSeries();
                StringBuilder series = new StringBuilder();
                for (CompletedSerie c: completedSeries) {
                    series.append(", ").append(c.getName());
                }
                return series.toString();
            }

            @Override
            protected void onPostExecute(String series) {
                binding.textView6.setText(series);
            }
        }.execute();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

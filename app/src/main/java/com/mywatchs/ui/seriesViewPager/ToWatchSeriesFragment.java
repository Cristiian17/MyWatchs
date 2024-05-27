package com.mywatchs.ui.seriesViewPager;

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

import com.mywatchs.SerieDetailsActivity;
import com.mywatchs.adapter.ForWatchSeriesAdapter;
import com.mywatchs.databinding.FragmentToWatchSeriesBinding;
import com.mywatchs.db.Dao.SerieDAO;
import com.mywatchs.db.Dao.entities.ForWatchSerie;
import com.mywatchs.db.MyBD;

import java.util.ArrayList;
import java.util.List;


public class ToWatchSeriesFragment extends Fragment {

    private FragmentToWatchSeriesBinding binding;
    private List<ForWatchSerie> forWatchSeries;
    private ForWatchSeriesAdapter adapter;
    private RecyclerView view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentToWatchSeriesBinding.inflate(inflater, container, false);
        view = binding.toWatchSeriesView;
        forWatchSeries = new ArrayList<>();
        getAllSeries();
        createAdapter();
        return binding.getRoot();
    }
    @Override
    public void onResume() {
        super.onResume();
        getAllSeries();
    }
    private void createAdapter() {
        view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new ForWatchSeriesAdapter(forWatchSeries, getContext(), new ForWatchSeriesAdapter.SerieItemClickListener() {
            @Override
            public void onMovieItemClick(int position) {
                ForWatchSerie serie = forWatchSeries.get(position);
                Intent intent = new Intent(getContext(), SerieDetailsActivity.class);
                int id = (int) serie.getId();
                intent.putExtra("id", id);
                startActivity(intent);
            }

            @Override
            public void onDeleteButtonClick(int position) {
                ForWatchSerie serie = forWatchSeries.get(position);
                deleteSerieFromDatabase(serie);
                forWatchSeries.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });
        view.setAdapter(adapter);
    }

    @SuppressLint("StaticFieldLeak")
    private void deleteSerieFromDatabase(ForWatchSerie serie) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MyBD myBD = Room.databaseBuilder(requireContext(), MyBD.class, "myDB").build();
                SerieDAO serieDAO = myBD.serieDetailsDao();
                serieDAO.deleteForWatchSerie(serie);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
            }
        }.execute();
    }


    @SuppressLint("StaticFieldLeak")
    private void getAllSeries() {
        new AsyncTask<Void, Void, List<ForWatchSerie>>() {
            @Override
            protected List<ForWatchSerie> doInBackground(Void... voids) {
                MyBD myBD = Room.databaseBuilder(requireContext(),
                        MyBD.class, "myDB").build();

                SerieDAO serieDAO = myBD.serieDetailsDao();
                return serieDAO.getAllForWatchSerie();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(List<ForWatchSerie> series) {
                ToWatchSeriesFragment.this.forWatchSeries.clear();
                ToWatchSeriesFragment.this.forWatchSeries.addAll(series);
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

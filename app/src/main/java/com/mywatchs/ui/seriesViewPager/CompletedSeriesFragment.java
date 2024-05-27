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
import com.mywatchs.adapter.CompletedSeriesAdapter;
import com.mywatchs.databinding.FragmentCompletedSeriesBinding;
import com.mywatchs.db.Dao.SerieDAO;
import com.mywatchs.db.Dao.entities.CompletedSerie;
import com.mywatchs.db.MyBD;

import java.util.ArrayList;
import java.util.List;


public class CompletedSeriesFragment extends Fragment {

    private FragmentCompletedSeriesBinding binding;
    private List<CompletedSerie> completedSeries;
    private CompletedSeriesAdapter adapter;
    private RecyclerView view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCompletedSeriesBinding.inflate(inflater, container, false);
        completedSeries = new ArrayList<>();
        view = binding.completedSeriesView;
        getAllSeries();
        createAdapter();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllSeries();
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getAllSeries();
        }
    }
    private void createAdapter() {
        view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new CompletedSeriesAdapter(completedSeries, getContext(), new CompletedSeriesAdapter.SerieItemClickListener() {
            @Override
            public void onMovieItemClick(int position) {
                CompletedSerie serie = completedSeries.get(position);
                Intent intent = new Intent(getContext(), SerieDetailsActivity.class);
                int id = (int) serie.getId();
                intent.putExtra("id", id);
                startActivity(intent);
            }

            @Override
            public void onDeleteButtonClick(int position) {
                CompletedSerie serie = completedSeries.get(position);
                deleteSerieFromDatabase(serie);
                completedSeries.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });
        view.setAdapter(adapter);
    }

    @SuppressLint("StaticFieldLeak")
    private void deleteSerieFromDatabase(CompletedSerie serie) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MyBD myBD = Room.databaseBuilder(requireContext(), MyBD.class, "myDB").build();
                SerieDAO serieDAO = myBD.serieDetailsDao();
                serieDAO.deleteCompletedSerie(serie);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
            }
        }.execute();
    }


    @SuppressLint("StaticFieldLeak")
    private void getAllSeries() {
        new AsyncTask<Void, Void, List<CompletedSerie>>() {
            @Override
            protected List<CompletedSerie> doInBackground(Void... voids) {
                MyBD myBD = Room.databaseBuilder(requireContext(),
                        MyBD.class, "myDB").build();

                SerieDAO serieDAO = myBD.serieDetailsDao();
                return serieDAO.getAllCompeltedSeries();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(List<CompletedSerie> completedSeries) {
                CompletedSeriesFragment.this.completedSeries.clear();
                CompletedSeriesFragment.this.completedSeries.addAll(completedSeries);
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

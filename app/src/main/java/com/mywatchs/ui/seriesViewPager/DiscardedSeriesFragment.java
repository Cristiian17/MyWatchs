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
import com.mywatchs.adapter.DetatchedSeriesAdapter;
import com.mywatchs.databinding.FragmentDiscardedSeriesBinding;
import com.mywatchs.db.Dao.SerieDAO;
import com.mywatchs.db.Dao.entities.CompletedSerie;
import com.mywatchs.db.Dao.entities.DetachSerie;
import com.mywatchs.db.MyBD;

import java.util.ArrayList;
import java.util.List;


public class DiscardedSeriesFragment extends Fragment {

    private FragmentDiscardedSeriesBinding binding;
    private RecyclerView view;
    private List<DetachSerie> detachSeries;
    private DetatchedSeriesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDiscardedSeriesBinding.inflate(inflater, container, false);
        view = binding.discartedSeriesView;
        detachSeries = new ArrayList<>();
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
        adapter = new DetatchedSeriesAdapter(detachSeries, getContext(), new DetatchedSeriesAdapter.SerieItemClickListener() {
            @Override
            public void onMovieItemClick(int position) {
                DetachSerie serie = detachSeries.get(position);
                Intent intent = new Intent(getContext(), SerieDetailsActivity.class);
                int id = (int) serie.getId();
                intent.putExtra("id", id);
                startActivity(intent);
            }

            @Override
            public void onDeleteButtonClick(int position) {
                DetachSerie serie = detachSeries.get(position);
                deleteSerieFromDatabase(serie);
                detachSeries.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });
        view.setAdapter(adapter);
    }

    @SuppressLint("StaticFieldLeak")
    private void deleteSerieFromDatabase(DetachSerie serie) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MyBD myBD = Room.databaseBuilder(requireContext(), MyBD.class, "myDB").build();
                SerieDAO serieDAO = myBD.serieDetailsDao();
                serieDAO.deleteDetachSerie(serie);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

            }
        }.execute();
    }


    @SuppressLint("StaticFieldLeak")
    private void getAllSeries() {
        new AsyncTask<Void, Void, List<DetachSerie>>() {
            @Override
            protected List<DetachSerie> doInBackground(Void... voids) {
                MyBD myBD = Room.databaseBuilder(requireContext(),
                        MyBD.class, "myDB").build();

                SerieDAO serieDAO = myBD.serieDetailsDao();

                return serieDAO.getAllDetachSerie();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(List<DetachSerie> series) {
                DiscardedSeriesFragment.this.detachSeries.clear();
                DiscardedSeriesFragment.this.detachSeries.addAll(series);
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

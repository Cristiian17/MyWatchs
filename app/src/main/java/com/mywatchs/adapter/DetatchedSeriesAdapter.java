package com.mywatchs.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mywatchs.R;
import com.mywatchs.db.Dao.entities.DetachSerie;

import java.util.List;

public class DetatchedSeriesAdapter extends RecyclerView.Adapter<DetatchedSeriesAdapter.MyViewHolder> {

    private List<DetachSerie> serieList;
    private Context context;
    private DetatchedSeriesAdapter.SerieItemClickListener serieItemClickListener;

    public DetatchedSeriesAdapter(List<DetachSerie> serieList, Context context, DetatchedSeriesAdapter.SerieItemClickListener serieItemClickListener) {
        this.serieList = serieList;
        this.context = context;
        this.serieItemClickListener = serieItemClickListener;
    }
    public interface SerieItemClickListener {
        void onMovieItemClick(int position);
        void onDeleteButtonClick(int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public Button deleteButton;

        public MyViewHolder(View v) {
            super(v);
            imageView = v.findViewById(R.id.imageView);
            textView = v.findViewById(R.id.tv_name);
            deleteButton = v.findViewById(R.id.deleteButton); // Asegúrate de que el ID del botón coincida
        }
    }

    @NonNull
    @Override
    public DetatchedSeriesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.db_movie_card, parent, false);
        return new DetatchedSeriesAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DetatchedSeriesAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DetachSerie serie = serieList.get(position);
        holder.textView.setText(serie.getName());
        String imageUrl = "https://image.tmdb.org/t/p/w500" + serie.getPosterPath();
        Glide.with(context).load(imageUrl).into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            if (serieItemClickListener != null) {
                serieItemClickListener.onMovieItemClick(position);
            }
        });
        holder.deleteButton.setOnClickListener(v -> {
            if (serieItemClickListener != null) {
                serieItemClickListener.onDeleteButtonClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return serieList.size();
    }
}

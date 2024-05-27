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
import com.mywatchs.db.Dao.entities.DetachMovie;

import java.util.List;

public class DetatchedMoviesAdapter extends RecyclerView.Adapter<DetatchedMoviesAdapter.MyViewHolder> {
    private List<DetachMovie> movieList;
    private Context context;
    private DetatchedMoviesAdapter.MovieItemClickListener movieItemClickListener;

    public DetatchedMoviesAdapter(List<DetachMovie> movieList, Context context, DetatchedMoviesAdapter.MovieItemClickListener movieItemClickListener) {
        this.movieList = movieList;
        this.context = context;
        this.movieItemClickListener = movieItemClickListener;
    }

    public interface MovieItemClickListener {
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
    public DetatchedMoviesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.db_movie_card, parent, false);
        return new DetatchedMoviesAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DetatchedMoviesAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DetachMovie movie = movieList.get(position);
        holder.textView.setText(movie.getName());
        String imageUrl = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();
        Glide.with(context).load(imageUrl).into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            if (movieItemClickListener != null) {
                movieItemClickListener.onMovieItemClick(position);
            }
        });
        holder.deleteButton.setOnClickListener(v -> {
            if (movieItemClickListener != null) {
                movieItemClickListener.onDeleteButtonClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList == null ? 0 : movieList.size();
    }
}

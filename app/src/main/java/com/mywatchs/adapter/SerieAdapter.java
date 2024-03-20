package com.mywatchs.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mywatchs.R;
import com.mywatchs.model.serie.Serie;

import java.util.List;

public class SerieAdapter extends RecyclerView.Adapter<SerieAdapter.MyViewHolder> {
    private List<Serie> serieList;
    private Context context;
    private SerieItemClickListener serieItemClickListener;

    public SerieAdapter(List<Serie> serieList, Context context, SerieItemClickListener serieItemClickListener) {
        this.serieList = serieList;
        this.context = context;
        this.serieItemClickListener = serieItemClickListener;
    }
    public interface SerieItemClickListener {
        void onMovieItemClick(int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public MyViewHolder(View v) {
            super(v);
            imageView = v.findViewById(R.id.imageView);
            textView = v.findViewById(R.id.tv_name);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Serie serie = serieList.get(position);
        holder.textView.setText(serie.getName());
        String imageUrl = "https://image.tmdb.org/t/p/w500" + serie.getPosterPath();
        Glide.with(context).load(imageUrl).into(holder.imageView);

        // Configuración del clic en el elemento
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serieItemClickListener != null) {
                    serieItemClickListener.onMovieItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return serieList.size();
    }
}


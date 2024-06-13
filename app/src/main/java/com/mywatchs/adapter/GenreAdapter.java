package com.mywatchs.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mywatchs.R;
import com.mywatchs.model.genre.Genre;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.MyViewHolder> {
    private final List<Genre> genreList;
    private Context context;
    private final OnItemClickListener listener;

    private int selectedPosition = -1;

    public interface OnItemClickListener {
        void onItemClick(Genre genre);
    }

    public GenreAdapter(List<Genre> genreList, Context context, OnItemClickListener listener) {
        this.genreList = genreList;
        this.context = context;
        this.listener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        private CardView genreCard;

        public MyViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.tv_genre_name);
            genreCard = v.findViewById(R.id.genreCard);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.genre_card, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Genre genre = genreList.get(position);
        holder.textView.setText(genre.getName());

        if (position == selectedPosition) {
            holder.genreCard.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.genreCard.setBackgroundColor(Color.parseColor("#96aab7"));
        }

        holder.itemView.setOnClickListener(v -> {
            if (selectedPosition == position) {
                selectedPosition = -1;
                listener.onItemClick(null);
            } else {
                selectedPosition = position;
                listener.onItemClick(genre);
            }
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }
}

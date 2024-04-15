package com.mywatchs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mywatchs.R;
import com.mywatchs.model.genre.Genre;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.MyViewHolder> {
    private List<Genre> genreList;
    private Context context;

    public GenreAdapter(List<Genre> genreList, Context context) {
        this.genreList = genreList;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public MyViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.tv_genre_name);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.genre_card, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Genre genre = genreList.get(position);
        holder.textView.setText(genre.getName());
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }
}

package com.readify.readify.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.readify.readify.R;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {

    private List<String> genres;
    private OnGenreClickListener listener;

    public interface OnGenreClickListener {
        void onGenreClick(String genre);
    }

    public GenreAdapter(List<String> genres, OnGenreClickListener listener) {
        this.genres = genres;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_genre, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        String genre = genres.get(position);
        holder.txtGenreName.setText(genre);
        holder.itemView.setOnClickListener(v -> listener.onGenreClick(genre));
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    static class GenreViewHolder extends RecyclerView.ViewHolder {
        TextView txtGenreName;

        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            txtGenreName = itemView.findViewById(R.id.genreName);
        }
    }
}

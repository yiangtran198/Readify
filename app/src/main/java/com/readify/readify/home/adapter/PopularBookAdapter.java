package com.readify.readify.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.readify.readify.R;
import com.readify.readify.home.model.Book;

import java.util.List;

public class PopularBookAdapter extends RecyclerView.Adapter<PopularBookAdapter.PopularBookViewHolder> {
    private Context context;
    private List<Book> books;

    public PopularBookAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public PopularBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_popular_book, parent, false);
        return new PopularBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularBookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.txtIndex.setText((position + 1) + ".");
        holder.txtTitle.setText(book.title);
        holder.txtAuthor.setText(book.author);
        int resId = context.getResources().getIdentifier(book.image, "drawable", context.getPackageName());
        if (resId != 0) {
            holder.imgCover.setImageResource(resId);
        } else {
            holder.imgCover.setImageResource(R.drawable.bg_book_cover_rounded);
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class PopularBookViewHolder extends RecyclerView.ViewHolder {
        TextView txtIndex, txtTitle, txtAuthor;
        ImageView imgCover;
        public PopularBookViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIndex = itemView.findViewById(R.id.txtIndex);
            imgCover = itemView.findViewById(R.id.imgBookCover);
            txtTitle = itemView.findViewById(R.id.txtBookTitle);
            txtAuthor = itemView.findViewById(R.id.txtBookAuthor);
        }
    }
} 
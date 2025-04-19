package com.example.ltudreadify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ltudreadify.R;
import com.example.ltudreadify.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Book> bookList;
    private Context context;

    public BookAdapter(Context context, List<Book> books) {
        this.context = context;
        this.bookList = books;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.title.setText(book.getTitle());
        Glide.with(context).load(book.getImageUrl()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;

        public BookViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.book_image);
            title = itemView.findViewById(R.id.book_title);
        }
    }
}

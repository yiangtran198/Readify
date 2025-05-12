package com.readify.readify.library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.readify.readify.R;
import com.readify.readify.home.model.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private Context context;
    private List<Book> bookList;
    private boolean showDeleteButton;
    private OnDeleteClickListener deleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public BookAdapter(Context context, List<Book> bookList, boolean showDeleteButton) {
        this.context = context;
        this.bookList = bookList;
        this.showDeleteButton = showDeleteButton;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);

        holder.textBookTitle.setText(book.title);
        holder.textBookAuthor.setText("by " + book.author);

        // Load book cover with Glide
        if (book.image != null && !book.image.isEmpty()) {
            Glide.with(context)
                    .load(book.image)
//                    .placeholder(R.drawable.img_1984)
                    .into(holder.imageBookCover);
        } else {
//            holder.imageBookCover.setImageResource(R.drawable.img_1984);
        }

        // Show or hide delete button
        if (showDeleteButton) {
            holder.buttonDelete.setVisibility(View.VISIBLE);
        } else {
            holder.buttonDelete.setVisibility(View.GONE);
        }

        // Set delete button click listener
        holder.buttonDelete.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView imageBookCover;
        TextView textBookTitle, textBookAuthor;
        ImageButton buttonDelete;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            imageBookCover = itemView.findViewById(R.id.image_book_cover);
            textBookTitle = itemView.findViewById(R.id.text_book_title);
            textBookAuthor = itemView.findViewById(R.id.text_book_author);
            buttonDelete = itemView.findViewById(R.id.button_delete);
        }
    }
}
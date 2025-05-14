package com.readify.readify.home.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.readify.readify.R;
import com.readify.readify.home.model.Book;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private Context context;
    private List<Book> books;
    private OnBookClickListener listener;
    private Boolean isShow; // Optional, có thể null

    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    // Constructor mặc định (không cần isShow)
    public BookAdapter(Context context, List<Book> books, OnBookClickListener listener) {
        this(context, books, listener, null);
    }

    // Constructor có isShow
    public BookAdapter(Context context, List<Book> books, OnBookClickListener listener, Boolean isShow) {
        this.context = context;
        this.books = books;
        this.listener = listener;
        this.isShow = isShow;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book_card, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);

        if (book.image != null && !book.image.isEmpty()) {
            int radiusInPx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 12, context.getResources().getDisplayMetrics());

            RequestOptions requestOptions = new RequestOptions()
                    .transform(new RoundedCorners(radiusInPx))
                    .placeholder(R.drawable.bg_book_cover_rounded)
                    .error(R.drawable.bg_book_cover_rounded);

            Glide.with(context)
                    .load(book.image)
                    .apply(requestOptions)
                    .into(holder.imgCover);
        } else {
            holder.imgCover.setImageResource(R.drawable.bg_book_cover_rounded);
        }

        // Tùy theo isShow bạn xử lý gì đó nếu cần
        if (isShow != null && isShow) {
            holder.bookInfoLayout.setVisibility(View.VISIBLE);
            holder.tvBookTitle.setText(book.title != null ? book.title : "");
            holder.tvBookAuthor.setText(book.author != null ? book.author : "");
        } else {
            holder.bookInfoLayout.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onBookClick(book);
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCover;
        LinearLayout bookInfoLayout;
        TextView tvBookTitle, tvBookAuthor;
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCover = itemView.findViewById(R.id.imgBookCover);
            bookInfoLayout = itemView.findViewById(R.id.bookInfoLayout);
            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            tvBookAuthor = itemView.findViewById(R.id.tvBookAuthor);
        }
    }
}

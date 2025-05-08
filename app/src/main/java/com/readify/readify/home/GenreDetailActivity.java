package com.readify.readify.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.readify.readify.R;
import com.readify.readify.home.model.Book;
import com.readify.readify.home.adapter.BookAdapter;

import java.util.ArrayList;

public class GenreDetailActivity extends AppCompatActivity {

    private boolean isBookmarked = false;
    private ImageView bookmarkIcon;
    private TextView descriptionView;
    private TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_detail);

        RecyclerView recyclerView = findViewById(R.id.recyclerBooks);
        titleView = findViewById(R.id.textGenreTitle);
        descriptionView = findViewById(R.id.textGenreDescription);
        ImageView backButton = findViewById(R.id.backButton);

        // Nút Back
        backButton.setOnClickListener(v -> finish());

        // Toggle Bookmark
        bookmarkIcon.setOnClickListener(v -> {
            isBookmarked = !isBookmarked;
            updateBookmarkIcon();
            // TODO: Lưu trạng thái bookmark vào Firebase sau
        });

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        ArrayList<Book> books = (ArrayList<Book>) intent.getSerializableExtra("book_list");
        String genreTitle = intent.getStringExtra("genre_title");
        String genreDescription = intent.getStringExtra("genre_description");

        if (genreTitle != null) {
            titleView.setText(genreTitle);
        }

        if (genreDescription != null) {
            descriptionView.setText(genreDescription);
        } else {
            descriptionView.setVisibility(View.GONE);
        }

        if (books != null) {
            recyclerView.setAdapter(new BookAdapter(this, books, new BookAdapter.OnBookClickListener() {
                @Override
                public void onBookClick(Book book) {

                }
            }));
        }
    }

    private void updateBookmarkIcon() {
        if (isBookmarked) {
            bookmarkIcon.setImageResource(R.drawable.fill_bookmark);
        } else {
            bookmarkIcon.setImageResource(R.drawable.bookmarkk);
        }
    }
}

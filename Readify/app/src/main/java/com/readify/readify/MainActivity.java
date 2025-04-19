package com.readify.readify;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import  com.readify.readify.Adapter.ReviewAdapter;
import  com.readify.readify.Model.Review;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView reviewRecyclerView;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList;
    private ImageView[] ratingStars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUI();
        setupReviewList();
        setupStarRating();
    }

    private void initializeUI() {
        reviewRecyclerView = findViewById(R.id.reviewRecyclerView);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize rating stars
        ratingStars = new ImageView[5];
        ratingStars[0] = findViewById(R.id.star1);
        ratingStars[1] = findViewById(R.id.star2);
        ratingStars[2] = findViewById(R.id.star3);
        ratingStars[3] = findViewById(R.id.star4);
        ratingStars[4] = findViewById(R.id.star5);
    }

    private void setupReviewList() {
        reviewList = new ArrayList<>();

        // Add sample reviews as seen in the image
        reviewList.add(new Review("Trung Pham", "Rất hay và tuyệt vời, đã đọc lại lần thứ vẫn hay như lần đầu", 5, "17 thg 12 2024"));
        reviewList.add(new Review("Trung Anh", "Rất hay và tuyệt vời...", 5, "2 thg 12 2024"));
        reviewList.add(new Review("Elon Musk", "Very good", 5, "26 thg 10 2024"));
        reviewList.add(new Review("Aron", "ok", 5, "09 thg 05 2024"));
        reviewList.add(new Review("Quốc Cường", "Sách hay lắm", 5, "20 thg 06 2024"));
        reviewList.add(new Review("Dieu Dieu", "10 điêm", 5, "04 thg 07 2024"));

        reviewAdapter = new ReviewAdapter(reviewList);
        reviewRecyclerView.setAdapter(reviewAdapter);
    }

    private void setupStarRating() {
        for (int i = 0; i < ratingStars.length; i++) {
            final int starPosition = i;
            ratingStars[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateStarRating(starPosition);
                }
            });
        }
    }

    private void updateStarRating(int position) {
        // Update star UI based on selection
        for (int i = 0; i < ratingStars.length; i++) {
            if (i <= position) {
                ratingStars[i].setImageResource(R.drawable.ic_star_filled);
            } else {
                ratingStars[i].setImageResource(R.drawable.ic_star_outline);
            }
        }
    }
}
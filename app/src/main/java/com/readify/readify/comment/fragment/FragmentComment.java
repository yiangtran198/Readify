package com.readify.readify.comment.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.readify.readify.R;
import com.readify.readify.Model.Review;
import com.readify.readify.comment.adapter.ReviewAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentComment extends Fragment {

    private RecyclerView reviewRecyclerView;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList;
    private ImageView[] ratingStars;

    public FragmentComment() {
        // Constructor rỗng là bắt buộc cho Fragment
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate layout của bạn (đã viết bằng fragment_comment.xml)
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeUI(view);
        setupReviewList();
        setupStarRating();

    }

    private void initializeUI(View view) {
        reviewRecyclerView = view.findViewById(R.id.reviewRecyclerView);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ratingStars = new ImageView[5];
        ratingStars[0] = view.findViewById(R.id.star1);
        ratingStars[1] = view.findViewById(R.id.star2);
        ratingStars[2] = view.findViewById(R.id.star3);
        ratingStars[3] = view.findViewById(R.id.star4);
        ratingStars[4] = view.findViewById(R.id.star5);
    }

    private void setupReviewList() {
        reviewList = new ArrayList<>();

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
            ratingStars[i].setOnClickListener(v -> updateStarRating(starPosition));
        }
    }

    private void updateStarRating(int position) {
        for (int i = 0; i < ratingStars.length; i++) {
            ratingStars[i].setImageResource(i <= position ?
                    R.drawable.ic_star_filled : R.drawable.ic_star_outline);
        }
    }
}

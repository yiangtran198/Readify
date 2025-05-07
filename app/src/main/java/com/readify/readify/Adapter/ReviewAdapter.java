package com.readify.readify.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.readify.readify.R;
import com.readify.readify.Model.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviewList;

    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        private TextView txtReviewerName;
        private TextView txtReviewContent;
        private RatingBar ratingBar;
        private TextView txtReviewDate;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            txtReviewerName = itemView.findViewById(R.id.txtReviewerName);
            txtReviewContent = itemView.findViewById(R.id.txtReviewContent);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            txtReviewDate = itemView.findViewById(R.id.txtReviewDate);
        }

        public void bind(Review review) {
            txtReviewerName.setText(review.getReviewerName());
            txtReviewContent.setText(review.getContent());
            ratingBar.setRating(review.getRating());
            txtReviewDate.setText(review.getDate());
        }
    }
}
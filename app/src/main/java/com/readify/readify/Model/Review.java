package com.readify.readify.Model;
public class Review {
    private String reviewerName;
    private String content;
    private int rating;
    private String date;

    public Review(String reviewerName, String content, int rating, String date) {
        this.reviewerName = reviewerName;
        this.content = content;
        this.rating = rating;
        this.date = date;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public String getContent() {
        return content;
    }

    public int getRating() {
        return rating;
    }

    public String getDate() {
        return date;
    }
}

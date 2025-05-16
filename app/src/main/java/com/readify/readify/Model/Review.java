package com.readify.readify.Model;
public class Review {
    private String name;
    private String content;
    private int rating;

    public Review(String name, String content, int rating) {
        this.name = name;
        this.content = content;
        this.rating = rating;
    }

    public String getReviewerName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public int getRating() {
        return rating;
    }

}

package com.readify.readify.home.model;

public class ReviewModel {
    private String name;
    private String content;
    private int rating;

    public ReviewModel(String name, String content, int rating) {
        this.name = name;
        this.content = content;
        this.rating = rating;
    }

    public String getName() { return name; }
    public String getContent() { return content; }
    public int getRating() { return rating; }
}

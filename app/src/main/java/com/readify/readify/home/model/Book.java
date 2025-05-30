package com.readify.readify.home.model;

import com.readify.readify.Model.Review;

import java.io.Serializable;
import java.util.List;

public class Book implements Serializable {
    public String id;
    public String title;
    public String author;
    public String image;
    public String description;
    public List<String> categories;
    public String status;
    public String created_at;
    public List<String> pages;
    public List<Review> comments;  // Thêm trường comments

    public Book() {
        // Required for Firebase deserialization
    }

    public Book(String id, String title, String author, String description, String image, List<String> categories, String status, String created_at, List<String> pages, List<Review> comments) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.image = image;
        this.categories = categories;
        this.status = status;
        this.created_at = created_at;
        this.pages = pages;
        this.comments = comments;
    }
}

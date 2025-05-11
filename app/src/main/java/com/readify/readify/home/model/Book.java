package com.readify.readify.home.model;

import java.io.Serializable;
import java.util.List;

public class Book implements Serializable {
    public String title;
    public String author;
    public String image;
    public String description;
    public List<String> categories;
    public String status;
    public String created_at;
    public List<String> pages;

    public Book() {
        // Required for Firebase deserialization
    }

    public Book(String title,String author,String description,String image,List<String> categories,String status,String created_at,List<String> pages) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.image = image;
        this.categories = categories;
        this.status = status;
        this.created_at = created_at;
        this.pages = pages;
    }
}

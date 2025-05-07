package com.readify.readify.home;

public class Book {
    public String title;
    public String author;
    public String image;
    public boolean localImage;

    public Book(String title, String author, String image, boolean localImage) {
        this.title = title;
        this.author = author;
        this.image = image;
        this.localImage = localImage;
    }
}
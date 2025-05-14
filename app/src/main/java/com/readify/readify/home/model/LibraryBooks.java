package com.readify.readify.home.model;

import java.util.List;

public class LibraryBooks {
    private List<String> favorite_books;
    private List<String> followed_books;

    // Bắt buộc có constructor rỗng cho Firestore
    public LibraryBooks() {}

    public LibraryBooks(List<String> favorite_books, List<String> followed_books) {
        this.favorite_books = favorite_books;
        this.followed_books = followed_books;
    }

    public List<String> getFavorite_books() {
        return favorite_books;
    }

    public void setFavorite_books(List<String> favorite_books) {
        this.favorite_books = favorite_books;
    }

    public List<String> getFollowed_books() {
        return followed_books;
    }

    public void setFollowed_books(List<String> followed_books) {
        this.followed_books = followed_books;
    }
}

package com.readify.readify.repository;

import com.readify.readify.home.model.Book;
import java.util.List;

public class BookRepository {
    private static BookRepository instance;
    private List<Book> cachedBooks;
    private BookRepository() {}

    public static synchronized BookRepository getInstance() {
        if (instance == null) {
            instance = new BookRepository();
        }
        return instance;
    }

    public void setBooks(List<Book> books) {
        this.cachedBooks = books;
    }

    public List<Book> getBooks() {
        return cachedBooks;
    }

    public boolean hasBooks() {
        return cachedBooks != null && !cachedBooks.isEmpty();
    }
}

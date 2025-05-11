package com.readify.readify.repository;

import com.readify.readify.home.model.Book;
import java.util.List;

public class BookRepository {
    private static BookRepository instance;
    private List<Book> cachedBooks;

    // Private constructor (chỉ cho phép 1 instance)
    private BookRepository() {}

    // Truy cập instance duy nhất (singleton)
    public static synchronized BookRepository getInstance() {
        if (instance == null) {
            instance = new BookRepository();
        }
        return instance;
    }

    // Set danh sách Book khi load từ Firestore
    public void setBooks(List<Book> books) {
        this.cachedBooks = books;
    }

    // Lấy lại danh sách Book từ cache
    public List<Book> getBooks() {
        return cachedBooks;
    }

    // Kiểm tra đã có dữ liệu chưa
    public boolean hasBooks() {
        return cachedBooks != null && !cachedBooks.isEmpty();
    }
}

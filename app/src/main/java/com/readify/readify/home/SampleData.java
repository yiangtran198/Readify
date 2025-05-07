package com.readify.readify.home;

import com.readify.readify.R;
import java.util.ArrayList;

public class SampleData {
    public static final ArrayList<Book> books = new ArrayList<>();

    static {
        // Các sách có hình ảnh từ tài nguyên drawable (local)
        books.add(new Book("1984", "George Orwell", "book1", true));
        books.add(new Book("Pride and Prejudice", "Jane Austen", "book2", true));
        books.add(new Book("Sapiens", "Yuval Noah Harari", "book3", true));
        books.add(new Book("To Kill a Mockingbird", "Harper Lee", "book4", true));
        books.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", "book5", true));
        books.add(new Book("Atomic Habits", "James Clear", "book6", true));
        books.add(new Book("The Power of Now", "Eckhart Tolle", "book7", true));
        books.add(new Book("The Alchemist", "Paulo Coelho", "book8", true));

        // Các sách có hình ảnh từ URL (remote)
        books.add(new Book("The Catcher in the Rye", "J.D. Salinger", "https://example.com/book9.jpg", false));
        books.add(new Book("The Hobbit", "J.R.R. Tolkien", "https://example.com/book10.jpg", false));
    }
}

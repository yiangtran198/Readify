package com.readify.readify.home.data;

import com.readify.readify.home.model.Book;
import com.readify.readify.home.model.Category;
import java.util.ArrayList;
import java.util.List;

public class SampleData {
    public static List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "Bedtime Stories"));
        categories.add(new Category(2, "Feminism"));
        categories.add(new Category(3, "Motivational"));
        categories.add(new Category(4, "Mindfulness"));
        categories.add(new Category(5, "Business"));
        categories.add(new Category(6, "Habits"));
        categories.add(new Category(7, "Science"));
        categories.add(new Category(8, "Self-help"));
        categories.add(new Category(9, "Decisions"));
        categories.add(new Category(10, "Marketing"));
        categories.add(new Category(11, "Entrepreneur"));
        return categories;
    }

    public static List<Book> getBooks(String type) {
        List<Book> books = new ArrayList<>();
        if (type.equals("Picks")) {
            books.add(new Book("The Collected Regrets of Clover", "Mikki Brammer", "https://vcdn1-giaitri.vnecdn.net/2018/02/13/hachiko-4366-1518525745.jpg?w=460&h=0&q=100&dpr=2&fit=crop&s=ATj-KoNGw-o-hAmttUqrcw", true));
            books.add(new Book("Retrospective", "Juan Gabriel Vásquez", "https://newshop.vn/public/uploads/products/46404/sach-truyen-doc-bang-hinh-anh-nhung-cau-chuyen-ngu-ngon-viet-nam-dac-sac-1.jpg", true));
            books.add(new Book("The Making of Another Major Motion Picture Masterpiece", "Tom Hanks", "https://down-vn.img.susercontent.com/file/4e86954aebdc981039bbbb26fded70b8", true));
            books.add(new Book("Tomorrow, and Tomorrow, and Tomorrow", "Gabrielle Zevin", "https://i.pinimg.com/736x/78/bb/e5/78bbe51836d98d0070de77b9a75cc5ae.jpg", true));
            books.add(new Book("The Midnight Library", "Matt Haig", "https://sachcanhdieu.vn/wp-content/uploads/2024/07/Bia-truyen-doc-1-512x756.jpg", true));
        } else if (type.equals("SelfHelp")) {
            books.add(new Book("Stop Overthinking", "Nick Trenton", "https://www.thalia.de/shop/home/artikeldetails/A1060492230", true));
            books.add(new Book("The Self-Love Habit", "Fiona Brennan", "https://www.goodreads.com/book/show/56700109-the-self-love-habit", true));
            books.add(new Book("Đắc Nhân Tâm", "Dale Carnegie", "https://m.media-amazon.com/images/I/81s6DUyQCZL._SL1500_.jpg", true));
            books.add(new Book("Atomic Habits", "James Clear", "https://www.penguin.com.au/books/atomic-habits-9781473565425", true));
            books.add(new Book("The Power of Now", "Eckhart Tolle", "https://www.kobo.com/ww/en/ebook/the-power-of-now-1", true));
        } else if (type.equals("Popular")) {
            books.add(new Book("Pride & Prejudice", "Jane Austen", "https://www.simonandschuster.com.au/books/Pride-and-Prejudice/Jane-Austen/Enriched-Classics/9781471134746", true));
            books.add(new Book("The Bible", "Various Authors", "https://www.christianartgifts.com/The-Holy-Bible-Brown", true));
            books.add(new Book("David Copperfield", "Charles Dickens", "https://www.alamy.com/book-cover-david-copperfield-by-charles-dickens-image429403707.html", true));
            books.add(new Book("1984", "George Orwell", "https://www.historynet.com/history-of-1984-george-orwell/1984-by-george-orwell-book-cover/", true));
            books.add(new Book("To Kill a Mockingbird", "Harper Lee", "https://www.penguin.com.au/books/to-kill-a-mockingbird-9781785150357", true));
        }
        return books;
    }
}

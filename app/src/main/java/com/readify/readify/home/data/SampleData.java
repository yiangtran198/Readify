package com.readify.readify.home.data;

import com.readify.readify.home.model.Book;
import com.readify.readify.home.model.Category;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SampleData {
    public static List<Category> getCategories() {

        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "Classics"));
        categories.add(new Category(2, "Fiction"));
        categories.add(new Category(3, "Crime"));
        categories.add(new Category(4, "Biography"));
        categories.add(new Category(5, "Children’s"));
        categories.add(new Category(6, "Comics"));
        categories.add(new Category(7, "Fantasy"));
        categories.add(new Category(8, "Romance"));
        categories.add(new Category(9, "Contemporary"));
        categories.add(new Category(10, "Young adults"));
        categories.add(new Category(11, "Self-help"));
        return categories;
    }

    public static List<Book> getBooks(String type) {
        List<Book> books = new ArrayList<>();
        List<String> categories = Arrays.asList("Classics");
        List<String> pages = Arrays.asList(
                "Amelia stood before the towering iron gates of Everwood Manor, her breath fogging in the crisp autumn air...",
                "Inside, the manor was quiet except for the hollow sound of her footsteps...",
                "The entries began in the late 1800s, written in looping cursive...",
                "That night, Amelia dreamed of the manor in flames...",
                "The cellar was lined with crumbling brick and old barrels...",
                "As she touched the locket, a gust of air swept through the chamber...",
                "Back in the study the next morning, Amelia researched Clara’s history...",
                "The whispers grew louder each night...",
                "A sudden wind extinguished her candles..."
        );

        if (type.equals("Picks")) {
            books.add( new Book( "The Forgotten Castle",
                    "Lily Nguyen",
                    "A tale of love and betrayal set in a crumbling European castle during the 1800s.",
                    "https://loading.jpg",
                    categories,
                    "Ongoing",
                    "2025-05-08T14:25:53.905064",
                    pages));
            books.add( new Book( "The Forgotten Castle",
                    "Lily Nguyen",
                    "A tale of love and betrayal set in a crumbling European castle during the 1800s.",
                    "https://loading.jpg",
                    categories,
                    "Ongoing",
                    "2025-05-08T14:25:53.905064",
                    pages));
            books.add( new Book( "The Forgotten Castle",
                    "Lily Nguyen",
                    "A tale of love and betrayal set in a crumbling European castle during the 1800s.",
                    "https://loading.jpg",
                    categories,
                    "Ongoing",
                    "2025-05-08T14:25:53.905064",
                    pages));

        } else if (type.equals("SelfHelp")) {
            books.add( new Book( "The Forgotten Castle",
                    "Lily Nguyen",
                    "A tale of love and betrayal set in a crumbling European castle during the 1800s.",
                    "https://loading.jpg",
                    categories,
                    "Ongoing",
                    "2025-05-08T14:25:53.905064",
                    pages));
            books.add( new Book( "The Forgotten Castle",
                    "Lily Nguyen",
                    "A tale of love and betrayal set in a crumbling European castle during the 1800s.",
                    "https://loading.jpg",
                    categories,
                    "Ongoing",
                    "2025-05-08T14:25:53.905064",
                    pages));
            books.add( new Book( "The Forgotten Castle",
                    "Lily Nguyen",
                    "A tale of love and betrayal set in a crumbling European castle during the 1800s.",
                    "https://loading.jpg",
                    categories,
                    "Ongoing",
                    "2025-05-08T14:25:53.905064",
                    pages));
        } else if (type.equals("Popular")) {
            books.add( new Book( "The Forgotten Castle",
                    "Lily Nguyen",
                    "A tale of love and betrayal set in a crumbling European castle during the 1800s.",
                    "https://loading.jpg",
                    categories,
                    "Ongoing",
                    "2025-05-08T14:25:53.905064",
                    pages));
            books.add( new Book( "The Forgotten Castle",
                    "Lily Nguyen",
                    "A tale of love and betrayal set in a crumbling European castle during the 1800s.",
                    "https://loading.jpg",
                    categories,
                    "Ongoing",
                    "2025-05-08T14:25:53.905064",
                    pages));
            books.add( new Book( "The Forgotten Castle",
                    "Lily Nguyen",
                    "A tale of love and betrayal set in a crumbling European castle during the 1800s.",
                    "https://loading.jpg",
                    categories,
                    "Ongoing",
                    "2025-05-08T14:25:53.905064",
                    pages));
        }
        return books;
    }
}

package com.readify.readify.repository;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class LibraryBooksRepository {

    private static LibraryBooksRepository instance;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private List<String> followedBooks = new ArrayList<>();
    private List<String> favoriteBooks = new ArrayList<>();

    private List<String> readedBooks = new ArrayList<>();

    private boolean isLoaded = false;

    public static LibraryBooksRepository getInstance() {
        if (instance == null) {
            instance = new LibraryBooksRepository();
        }
        return instance;
    }

    public List<String> getFollowedBooks() {
        return followedBooks;
    }

    public List<String> getFavoriteBooks() {
        return favoriteBooks;
    }

    public List<String> getReadedBooks() {
        return readedBooks;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void loadLibraryBooks(@Nullable Runnable onComplete, @Nullable Runnable onError) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            if (onError != null) onError.run();
            return;
        }

        db.collection("users").document(user.getUid()).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        followedBooks = (List<String>) doc.get("followed_books");
                        favoriteBooks = (List<String>) doc.get("favorite_books");
                        readedBooks = (List<String>) doc.get("readed_books");

                        if (followedBooks == null) followedBooks = new ArrayList<>();
                        if (favoriteBooks == null) favoriteBooks = new ArrayList<>();
                        if (readedBooks == null) readedBooks = new ArrayList<>();

                        isLoaded = true;
                        if (onComplete != null) onComplete.run();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("LibraryRepo", "Lỗi tải library: " + e.getMessage());
                    if (onError != null) onError.run();
                });
    }
}

package com.readify.readify.home.model;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.readify.readify.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookViewModel extends ViewModel {
    private final MutableLiveData<List<Book>> bookList = new MutableLiveData<>();

    public LiveData<List<Book>> getBookList() {
        return bookList;
    }

    public void fetchBooks() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<Book> tempList = new ArrayList<>();
        FirebaseUser infoUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = infoUser.getUid();
        db.collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<String> followedBooks = (List<String>) documentSnapshot.get("followed_books");
                        List<String> favoriteBooks = (List<String>) documentSnapshot.get("favorite_books");

                    }
                });


        db.collection("comics").get().addOnSuccessListener(query -> {
            for (DocumentSnapshot doc : query.getDocuments()) {
                String id = doc.getString("id");
                String title = doc.getString("title");
                String author = doc.getString("author");
                String description = doc.getString("description");
                String image = doc.getString("image");
                List<String> categories = (List<String>) doc.get("categories");
                String status = doc.getString("status");
                List<String> pages = (List<String>) doc.get("pages");
                Timestamp timestamp = doc.getTimestamp("created_at");
                String created_at = timestamp != null ? timestamp.toDate().toString() : null;
                Book book = new Book(id, title, author, description, image, categories, status, created_at, pages);
                tempList.add(book);
                BookRepository.getInstance().setBooks(tempList);
            }
            bookList.setValue(tempList);
        });
    }

}


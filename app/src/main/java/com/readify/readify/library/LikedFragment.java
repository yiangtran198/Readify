package com.readify.readify.library;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
//import com.readify.readify.Model.Book;
import com.readify.readify.R;
import com.readify.readify.home.model.Book;
import com.readify.readify.profile.SettingsFragment;
import com.readify.readify.utils.DeleteConfirmDialog;

import java.util.ArrayList;
import java.util.List;

public class LikedFragment extends Fragment {
    private RecyclerView recyclerLikedBooks;
    private Button btnTabFollowing, btnTabViewed, btnTabLiked;
    private ImageButton btnBack;

    private FirebaseFirestore db;
    private String currentUserId;
    private BookAdapter bookAdapter;
    private List<Book> bookList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liked, container, false);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Initialize views
        recyclerLikedBooks = view.findViewById(R.id.recycler_liked_books);
        btnTabFollowing = view.findViewById(R.id.btn_tab_following);
        btnTabViewed = view.findViewById(R.id.btn_tab_viewed);
        btnTabLiked = view.findViewById(R.id.btn_tab_liked);
        btnBack = view.findViewById(R.id.btn_back);

        // Set up RecyclerView
        recyclerLikedBooks.setLayoutManager(new GridLayoutManager(getContext(), 2));
        bookAdapter = new BookAdapter(getContext(), bookList, true); // true for showing delete button
        recyclerLikedBooks.setAdapter(bookAdapter);

        // Load liked books
        loadLikedBooks();

        // Set click listeners
        btnBack.setOnClickListener(v -> {
            // Navigate back to SettingsActivity
            Intent intent = new Intent(getActivity(), SettingsFragment.class);
            startActivity(intent);
            getActivity().finish();
        });

        btnTabFollowing.setOnClickListener(v -> {
            // Replace with FollowingFragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new FollowingFragment())
                    .commit();
        });

        btnTabViewed.setOnClickListener(v -> {
            // Replace with ViewedFragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ViewedFragment())
                    .commit();
        });

        btnTabLiked.setOnClickListener(v -> {
            // Already on Liked tab, do nothing
        });

        // Set delete listener for adapter
        bookAdapter.setOnDeleteClickListener(position -> {
            Book book = bookList.get(position);
            showDeleteConfirmationDialog(book);
        });

        return view;
    }

    private void loadLikedBooks() {
        // For this example, we'll use the same "favorites" field as the following books
        // In a real app, you might have a separate "liked" field
        db.collection("users").document(currentUserId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<String> favorites = (List<String>) documentSnapshot.get("favorites");

                        if (favorites != null && !favorites.isEmpty()) {
                            // Clear existing list
                            bookList.clear();

                            // Fetch each comic by ID
                            for (String comicId : favorites) {
                                db.collection("comics").document(comicId).get()
                                        .addOnSuccessListener(comicDoc -> {
                                            if (comicDoc.exists()) {
                                                String title = comicDoc.getString("title");
                                                String author = comicDoc.getString("author");
                                                String coverUrl = comicDoc.getString("cover_url");

//                                                Book book = new Book(comicId, title, author, coverUrl);
//                                                bookList.add(book);
//                                                bookAdapter.notifyDataSetChanged();
                                            }
                                        });
                            }
                        }
                    }
                });
    }

    private void showDeleteConfirmationDialog(Book book) {
        DeleteConfirmDialog dialog = new DeleteConfirmDialog(getContext(),
                // Yes button click
                () -> {
                    // Remove from favorites in Firestore
                    db.collection("users").document(currentUserId).get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    List<String> favorites = (List<String>) documentSnapshot.get("favorites");

                                    if (favorites != null) {

                                        Log.d("===========", "nhớ đổi title lại thành id nhé để xóa đúng");
                                        favorites.remove(book.title);

                                        // Update Firestore
                                        db.collection("users").document(currentUserId)
                                                .update("favorites", favorites)
                                                .addOnSuccessListener(aVoid -> {
                                                    // Remove from local list and update adapter
                                                    bookList.remove(book);
                                                    bookAdapter.notifyDataSetChanged();
                                                });
                                    }
                                }
                            });
                },
                // No button click
                () -> {
                    // Do nothing, just dismiss
                }
        );

        dialog.show();
    }
}
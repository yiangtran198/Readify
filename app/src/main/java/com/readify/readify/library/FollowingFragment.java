package com.readify.readify.library;

import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.readify.readify.R;
import com.readify.readify.home.model.Book;
import com.readify.readify.profile.SettingsFragment;
import com.readify.readify.utils.DeleteConfirmDialog;

import java.util.ArrayList;
import java.util.List;

public class FollowingFragment extends Fragment {
    private RecyclerView recyclerBooks;
    private Button btnTabFollowing, btnTabViewed, btnTabLiked;
    private ImageButton btnBack;
    private FloatingActionButton fabAdd;

    private FirebaseFirestore db;
    private String currentUserId;
    private BookAdapter bookAdapter;
    private List<Book> bookList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_following, container, false);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Initialize views
        recyclerBooks = view.findViewById(R.id.recycler_books);
        btnTabFollowing = view.findViewById(R.id.btn_tab_following);
        btnTabViewed = view.findViewById(R.id.btn_tab_viewed);
        btnTabLiked = view.findViewById(R.id.btn_tab_liked);
        btnBack = view.findViewById(R.id.btn_back);
        fabAdd = view.findViewById(R.id.fab_add);

        // Set up RecyclerView
        recyclerBooks.setLayoutManager(new GridLayoutManager(getContext(), 2));
        bookAdapter = new BookAdapter(getContext(), bookList, true); // true for showing delete button
        recyclerBooks.setAdapter(bookAdapter);

        // Load following books
        loadFollowingBooks();

        // Set click listeners
        btnBack.setOnClickListener(v -> {
            // Navigate back to SettingsActivity
            Intent intent = new Intent(getActivity(), SettingsFragment.class);
            startActivity(intent);
            getActivity().finish();
        });

        btnTabFollowing.setOnClickListener(v -> {
            // Already on Following tab, do nothing
        });

        btnTabViewed.setOnClickListener(v -> {
            // Replace with ViewedFragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ViewedFragment())
                    .commit();
        });

        btnTabLiked.setOnClickListener(v -> {
            // Replace with LikedFragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new LikedFragment())
                    .commit();
        });

        fabAdd.setOnClickListener(v -> {
            // This functionality is handled by another team member
            // Just show a toast for now
            // Toast.makeText(getContext(), "Add book functionality will be implemented by another team member", Toast.LENGTH_SHORT).show();
        });

        // Set delete listener for adapter
        bookAdapter.setOnDeleteClickListener(position -> {
            Book book = bookList.get(position);
            showDeleteConfirmationDialog(book);
        });

        return view;
    }

    private void loadFollowingBooks() {
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
                                                bookAdapter.notifyDataSetChanged();
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
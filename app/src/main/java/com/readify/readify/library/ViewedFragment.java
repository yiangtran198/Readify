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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.readify.readify.R;
import com.readify.readify.home.model.Book;
import com.readify.readify.profile.SettingsFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewedFragment extends Fragment {
    private RecyclerView recyclerViewedBooks;
    private Button btnTabFollowing, btnTabViewed, btnTabLiked;
    private ImageButton btnBack;

    private FirebaseFirestore db;
    private String currentUserId;
    private BookAdapter bookAdapter;
    private List<Book> bookList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewed, container, false);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Initialize views
        recyclerViewedBooks = view.findViewById(R.id.recycler_viewed_books);
        btnTabFollowing = view.findViewById(R.id.btn_tab_following);
        btnTabViewed = view.findViewById(R.id.btn_tab_viewed);
        btnTabLiked = view.findViewById(R.id.btn_tab_liked);
        btnBack = view.findViewById(R.id.btn_back);

        // Set up RecyclerView
        recyclerViewedBooks.setLayoutManager(new GridLayoutManager(getContext(), 2));
        bookAdapter = new BookAdapter(getContext(), bookList, false); // false for not showing delete button
        recyclerViewedBooks.setAdapter(bookAdapter);

        // Load viewed books
        loadViewedBooks();

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
            // Already on Viewed tab, do nothing
        });

        btnTabLiked.setOnClickListener(v -> {
            // Replace with LikedFragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new LikedFragment())
                    .commit();
        });

        return view;
    }

    private void loadViewedBooks() {
        db.collection("users").document(currentUserId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> readingStatus = (Map<String, Object>) documentSnapshot.get("reading_status");

                        if (readingStatus != null && !readingStatus.isEmpty()) {
                            // Clear existing list
                            bookList.clear();

                            // Fetch each comic by ID
                            for (String comicId : readingStatus.keySet()) {
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
}
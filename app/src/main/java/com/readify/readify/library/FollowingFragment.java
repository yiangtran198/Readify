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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.readify.readify.R;
import com.readify.readify.home.adapter.SearchBookAdapter;
import com.readify.readify.home.data.SampleData;
import com.readify.readify.home.fragment.DetailBookFragment;
import com.readify.readify.home.model.Book;
import com.readify.readify.profile.SettingsFragment;
import com.readify.readify.repository.BookRepository;
import com.readify.readify.utils.DeleteConfirmDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FollowingFragment extends Fragment {
//    private RecyclerView recyclerBooks;
    private Button btnTabFollowing, btnTabViewed, btnTabLiked;
    private RecyclerView recyclerPicks;

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
        recyclerPicks = view.findViewById(R.id.recyclerPicks);
        List<Book> books = BookRepository.getInstance().getBooks();
        com.readify.readify.home.adapter.BookAdapter.OnBookClickListener bookClickListener = book -> openBookDetail(book);
        recyclerPicks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        FirebaseUser infoUser = FirebaseAuth.getInstance().getCurrentUser();
        if (infoUser != null) {
            String uid = infoUser.getUid();
            db.collection("users").document(uid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            List<String> followedBooks = (List<String>) documentSnapshot.get("followed_books");
                            if (followedBooks != null) {
                                List<Book> followedBookList = books.stream()
                                        .filter(book -> followedBooks.contains(book.id))
                                        .collect(Collectors.toList());
                                        recyclerPicks.setAdapter(new com.readify.readify.home.adapter.BookAdapter(getContext(), followedBookList, bookClickListener));
                                        recyclerPicks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            }
                        }
                    })
                    .addOnFailureListener(e -> Log.e("Firestore", "Lỗi lấy followed_books: " + e.getMessage()));
        }

        // Initialize views
        btnTabFollowing = view.findViewById(R.id.btn_tab_following);
        btnTabViewed = view.findViewById(R.id.btn_tab_viewed);
        btnTabLiked = view.findViewById(R.id.btn_tab_liked);
        bookAdapter = new BookAdapter(getContext(), bookList, true); // true for showing delete button
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

        // Set delete listener for adapter
        bookAdapter.setOnDeleteClickListener(position -> {
            Book book = bookList.get(position);
        });

        return view;
    }

    private void openBookDetail(Book book) {
        DetailBookFragment fragment = new DetailBookFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("book", book);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment); // fragment_container là id của FrameLayout chứa fragment
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
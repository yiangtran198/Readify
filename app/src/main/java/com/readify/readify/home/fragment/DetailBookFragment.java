package com.readify.readify.home.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.readify.readify.R;
import com.readify.readify.comment.fragment.FragmentComment;
import com.readify.readify.home.model.Book;
import com.readify.readify.reader.fragment.FragmentReader;
import com.readify.readify.repository.LibraryBooksRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailBookFragment extends Fragment {

    private Book book;
    private Button btnFollow, btnFavorite;

    public DetailBookFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_book, container, false);

        if (getArguments() != null) {
            book = (Book) getArguments().getSerializable("book");
        }

        ImageView imgBookCover = view.findViewById(R.id.imgBookCover);
        TextView txtBookTitle = view.findViewById(R.id.txtBookTitle);
        TextView txtBookAuthor = view.findViewById(R.id.txtBookAuthor);
        TextView txtBookInfo = view.findViewById(R.id.txtBookInfo);
        TextView txtBookDescription = view.findViewById(R.id.txtBookDescription);
        Button btnRead = view.findViewById(R.id.btnRead);
        btnFollow = view.findViewById(R.id.btnFollow);
        btnFavorite = view.findViewById(R.id.btnFavorite);
        TextView txtGenre = view.findViewById(R.id.txtGenre);

        if (book != null) {
            txtBookTitle.setText(book.title);
            txtBookAuthor.setText("by " + book.author);
            txtBookInfo.setText(book.status+ "\n" + book.pages.size() + " trang");
            txtBookDescription.setText(book.description);
            txtGenre.setText(String.join(", ", book.categories));

            Glide.with(requireContext())
                    .load(book.image)
                    .apply(new RequestOptions()
                            .transform(new RoundedCorners(24))
                            .placeholder(R.drawable.bg_book_cover_rounded)
                            .error(R.drawable.bg_book_cover_rounded))
                    .into(imgBookCover);
        }

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<String> followed = LibraryBooksRepository.getInstance().getFollowedBooks();
        List<String> favorites = LibraryBooksRepository.getInstance().getFavoriteBooks();


        List<String> readed = LibraryBooksRepository.getInstance().getReadedBooks(); // BẠN PHẢI THÊM GETTER NÀY
        if (readed.contains(book.id)) {
            btnRead.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.green));
            btnRead.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));
        }

        btnRead.setOnClickListener(v -> {
            if (currentUser == null) return;
            String uid = currentUser.getUid();

            List<String> readedBooks = LibraryBooksRepository.getInstance().getReadedBooks();

            if (!readedBooks.contains(book.id)) {
                db.collection("users").document(uid)
                        .update("readed_books", FieldValue.arrayUnion(book.id))
                        .addOnSuccessListener(aVoid -> {
                            readedBooks.add(book.id);
                            btnRead.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.green));
                            btnRead.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));
                        })
                        .addOnFailureListener(e -> {
                            if (e.getMessage() != null && e.getMessage().contains("no document")) {
                                Map<String, Object> data = new HashMap<>();
                                data.put("readed_books", Arrays.asList(book.id));
                                db.collection("users").document(uid).set(data, SetOptions.merge());
                                readedBooks.add(book.id);
                                btnRead.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.green));
                                btnRead.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));
                            } else {
                                Toast.makeText(getContext(), "Lỗi đánh dấu đã đọc", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            // Tiếp tục điều hướng đến màn đọc
            FragmentReader fragment = new FragmentReader();
            Bundle bundle = new Bundle();
            bundle.putSerializable("book", book);
            fragment.setArguments(bundle);
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        updateFollowButtonUI(followed.contains(book.id));
        updateFavoriteButtonUI(favorites.contains(book.id));

        btnFollow.setOnClickListener(v -> {
            if (currentUser == null) return;
            String uid = currentUser.getUid();
            boolean isFollowing = followed.contains(book.id);

            if (isFollowing) {
                db.collection("users").document(uid)
                        .update("followed_books", FieldValue.arrayRemove(book.id))
                        .addOnSuccessListener(aVoid -> {
                            followed.remove(book.id);
                            updateFollowButtonUI(false);
                        });
            } else {
                db.collection("users").document(uid)
                        .update("followed_books", FieldValue.arrayUnion(book.id))
                        .addOnSuccessListener(aVoid -> {
                            followed.add(book.id);
                            updateFollowButtonUI(true);
                        })
                        .addOnFailureListener(e -> {
                            if (e.getMessage() != null && e.getMessage().contains("no document")) {
                                Map<String, Object> data = new HashMap<>();
                                data.put("followed_books", Arrays.asList(book.id));
                                db.collection("users").document(uid).set(data, SetOptions.merge());
                                followed.add(book.id);
                                updateFollowButtonUI(true);
                            }
                        });
            }
        });

        btnFavorite.setOnClickListener(v -> {
            if (currentUser == null) return;
            String uid = currentUser.getUid();
            boolean isFavorited = favorites.contains(book.id);

            if (isFavorited) {
                db.collection("users").document(uid)
                        .update("favorite_books", FieldValue.arrayRemove(book.id))
                        .addOnSuccessListener(aVoid -> {
                            favorites.remove(book.id);
                            updateFavoriteButtonUI(false);
                        });
            } else {
                db.collection("users").document(uid)
                        .update("favorite_books", FieldValue.arrayUnion(book.id))
                        .addOnSuccessListener(aVoid -> {
                            favorites.add(book.id);
                            updateFavoriteButtonUI(true);
                        })
                        .addOnFailureListener(e -> {
                            if (e.getMessage() != null && e.getMessage().contains("no document")) {
                                Map<String, Object> data = new HashMap<>();
                                data.put("favorite_books", Arrays.asList(book.id));
                                db.collection("users").document(uid).set(data, SetOptions.merge());
                                favorites.add(book.id);
                                updateFavoriteButtonUI(true);
                            }
                        });
            }
        });

        Button btnComment = view.findViewById(R.id.txtReviewTitle);
        btnComment.setOnClickListener(v -> {
            FragmentComment fragment = new FragmentComment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void updateFollowButtonUI(boolean isFollowing) {
        if (isFollowing) {
            btnFollow.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.green));
            btnFollow.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));
        } else {
            btnFollow.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), android.R.color.white));
            btnFollow.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_mint));
        }
    }

    private void updateFavoriteButtonUI(boolean isFavorited) {
        if (isFavorited) {
            btnFavorite.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.green));
            btnFavorite.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
        } else {
            btnFavorite.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.white));
            btnFavorite.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_mint));
        }
    }
}

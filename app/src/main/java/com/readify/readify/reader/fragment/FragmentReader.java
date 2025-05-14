package com.readify.readify.reader.fragment;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.readify.readify.R;
import com.readify.readify.home.model.Book;
import com.readify.readify.reader.adapter.ReaderAdapter;
import com.readify.readify.repository.LibraryBooksRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentReader extends Fragment {

    private ViewPager2 viewPager;
    private ReaderAdapter adapter;
    private float currentFontSize = 18f;
    private boolean isNightMode = false;
    private RelativeLayout layoutReader;
    private TextView tvPageNumber;
    private Book book;

    private Integer currentPageRead;

    public FragmentReader() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reader, container, false);

        // Bind View
        viewPager = view.findViewById(R.id.viewPager);
        layoutReader = view.findViewById(R.id.layoutReader);
        ImageButton btnFontUp = view.findViewById(R.id.btnFontUp);
        ImageButton btnFontDown = view.findViewById(R.id.btnFontDown);
        ImageButton btnToggleMode = view.findViewById(R.id.btnToggleMode);
        tvPageNumber = view.findViewById(R.id.tvPageNumber);
        ImageButton btnPin = view.findViewById(R.id.pin);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Get Book from arguments
        if (getArguments() != null) {
            book = (Book) getArguments().getSerializable("book");
        }

        // Initialize adapter
        if (book != null && book.pages != null && !book.pages.isEmpty()) {
            List<String> books = new ArrayList<>(book.pages);
            books.add(0, book.image);
            adapter = new ReaderAdapter(books, currentFontSize, isNightMode);
            viewPager.setAdapter(adapter);
            updatePageNumber(0); // Hiển thị ngay khi load
        }

        // Listen for page change
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onPageSelected(int position) {
                updatePageNumber(position);
            }
        });

        // Font size buttons
        btnFontUp.setOnClickListener(v -> {
            currentFontSize += 2;
            adapter.setFontSize(currentFontSize);
        });

        btnFontDown.setOnClickListener(v -> {
            if (currentFontSize > 12) {
                currentFontSize -= 2;
                adapter.setFontSize(currentFontSize);
            }
        });

        // Dark mode toggle
        btnToggleMode.setOnClickListener(v -> {
            isNightMode = !isNightMode;
            layoutReader.setBackgroundColor(isNightMode ? 0xFF1E1E1E : 0xFFFFFFFF);
            int tintColor = ContextCompat.getColor(requireContext(), isNightMode ? R.color.text_light_gray : R.color. text_dark_gray);
            btnFontUp.setImageTintList(ColorStateList.valueOf(tintColor));
            btnFontDown.setImageTintList(ColorStateList.valueOf(tintColor));
            btnPin.setImageTintList(ColorStateList.valueOf(tintColor));
            btnToggleMode.setImageResource(isNightMode ? R.drawable.light_mode : R.drawable.dark_mode);

            adapter.setNightMode(isNightMode);
        });


        // call number of page
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (book != null && currentUser != null) {
            String uid = currentUser.getUid();
            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists() && documentSnapshot.contains("page_books")) {
                            List<Map<String, Object>> existingBooks = (List<Map<String, Object>>) documentSnapshot.get("page_books");

                            for (Map<String, Object> entry : existingBooks) {
                                if (entry.get("id").equals(book.id)) {
                                    Object pageObj = entry.get("page");
                                    if (pageObj instanceof Number) {
                                        currentPageRead = ((Number) pageObj).intValue();
                                        if (viewPager.getAdapter() != null) {
                                            viewPager.setCurrentItem(currentPageRead-1, true);
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    });
        }


        // call firebase save current page
        btnPin.setOnClickListener(v -> {
            if (currentUser == null) return;
            String uid = currentUser.getUid();
            int currentPage = currentPageRead; // số trang hiện tại
            String bookId = book.id;    // id sách để lưu số trang

            DocumentReference docRef = db.collection("users").document(uid);
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                List<Map<String, Object>> readedBooks = new ArrayList<>();

                if (documentSnapshot.exists() && documentSnapshot.contains("page_books")) {
                    List<Map<String, Object>> existingBooks = (List<Map<String, Object>>) documentSnapshot.get("page_books");

                    boolean found = false;
                    for (Map<String, Object> entry : existingBooks) {
                        if (entry.get("id").equals(bookId)) {
                            entry.put("page", currentPage); // Cập nhật trang mới
                            found = true;
                        }
                        readedBooks.add(entry);
                    }

                    if (!found) {
                        Map<String, Object> newEntry = new HashMap<>();
                        newEntry.put("id", bookId);
                        newEntry.put("page", currentPage);
                        readedBooks.add(newEntry);
                    }
                } else {
                    Map<String, Object> newEntry = new HashMap<>();
                    newEntry.put("id", bookId);
                    newEntry.put("page", currentPage);
                    readedBooks.add(newEntry);
                }

                Map<String, Object> updatedData = new HashMap<>();
                updatedData.put("page_books", readedBooks);
                docRef.set(updatedData, SetOptions.merge());
            });
        });

        return view;
    }

    private void updatePageNumber(int position) {
        if (book != null && book.pages != null && !book.pages.isEmpty()) {
            int total = book.pages.size();
            if (book.image != null && (book.image.startsWith("http://") || book.image.startsWith("https://"))) {
                total += 1;
            }
            String current = String.valueOf(position + 1);
            tvPageNumber.setText(current + "/" + total);
            currentPageRead =  Integer.parseInt(current);
        }
    }

}

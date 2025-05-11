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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.readify.readify.R;
import com.readify.readify.home.model.Book;
import com.readify.readify.reader.adapter.ReaderAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentReader extends Fragment {

    private ViewPager2 viewPager;
    private ReaderAdapter adapter;
    private float currentFontSize = 18f;
    private boolean isNightMode = false;
    private RelativeLayout layoutReader;
    private TextView tvPageNumber;
    private Book book;

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
        ImageButton btnBack = view.findViewById(R.id.btnBack);
        tvPageNumber = view.findViewById(R.id.tvPageNumber);


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
            btnBack.setImageTintList(ColorStateList.valueOf(tintColor));
            btnFontUp.setImageTintList(ColorStateList.valueOf(tintColor));
            btnFontDown.setImageTintList(ColorStateList.valueOf(tintColor));

            btnToggleMode.setImageResource(isNightMode ? R.drawable.light_mode : R.drawable.dark_mode);

            adapter.setNightMode(isNightMode);
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
        }
    }

}

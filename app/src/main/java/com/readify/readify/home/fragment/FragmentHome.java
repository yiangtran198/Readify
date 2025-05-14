package com.readify.readify.home.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;
import com.readify.readify.R;
import com.readify.readify.home.adapter.BookAdapter;
import com.readify.readify.home.adapter.CategoryAdapter;
import com.readify.readify.home.adapter.PopularBookAdapter;
import com.readify.readify.home.data.SampleData;
import com.readify.readify.home.model.Book;
import com.readify.readify.home.model.BookViewModel;
import com.readify.readify.home.model.Category;
import com.readify.readify.repository.LibraryBooksRepository;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {
    private RecyclerView recyclerCategory, recyclerPicks, recyclerSelfHelp, recyclerPopular;
    private BookViewModel bookViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);

        // Initialize RecyclerViews
        recyclerCategory = view.findViewById(R.id.recyclerCategory);
        recyclerPicks = view.findViewById(R.id.recyclerPicks);
        recyclerSelfHelp = view.findViewById(R.id.recyclerSelfHelp);
        recyclerPopular = view.findViewById(R.id.recyclerPopular);

        // Category: GridLayoutManager (3 hoặc 4 cột tùy ý)
        CategoryAdapter.OnCategoryClickListener categoryClickListener = category -> openDetailCategory(category);
        recyclerCategory.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerCategory.setAdapter(new CategoryAdapter(getContext(), SampleData.getCategories(), categoryClickListener));

        // Picks & SelfHelp: BookAdapter (dạng ảnh lớn)
        BookAdapter.OnBookClickListener bookClickListener = book -> openBookDetail(book);
        recyclerPicks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerSelfHelp.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerPopular.setLayoutManager(new LinearLayoutManager(getContext()));

        // data default
        recyclerPicks.setAdapter(new BookAdapter(getContext(), SampleData.getBooks("Picks"), bookClickListener));
        recyclerSelfHelp.setAdapter(new BookAdapter(getContext(), SampleData.getBooks("SelfHelp"), bookClickListener));
        recyclerPopular.setAdapter(new PopularBookAdapter(getContext(), SampleData.getBooks("Popular")));

        bookViewModel.getBookList().observe(getViewLifecycleOwner(), books -> {
            if (books != null && !books.isEmpty()) {
                List<Book> filteredBooks = new ArrayList<>();
                for (Book b : books)
                    if (b.categories != null && (b.categories.contains("Classics") || b.categories.contains("Comics")))
                        filteredBooks.add(b);

                recyclerPicks.setAdapter(new BookAdapter(getContext(), filteredBooks, bookClickListener));

                List<Book> filteredBooksSelfHelp = new ArrayList<>();
                for (Book b : books)
                    if (b.categories != null && ( b.categories.contains("Crime") || b.categories.contains("Fantasy")))
                        filteredBooksSelfHelp.add(b);
                recyclerSelfHelp.setAdapter(new BookAdapter(getContext(), filteredBooksSelfHelp, bookClickListener));

                List<Book> filteredBooksPopular = new ArrayList<>();
                for (Book b : books)
                    if (b.categories != null && ( b.categories.contains("Fiction") || b.categories.contains("Young adults")))
                        filteredBooksPopular.add(b);
                recyclerPopular.setAdapter(new PopularBookAdapter(getContext(), filteredBooksPopular));
            }
        });


        bookViewModel.fetchBooks();

        LibraryBooksRepository.getInstance().loadLibraryBooks(
                () -> {
                    List<String> followed = LibraryBooksRepository.getInstance().getFollowedBooks();
                },
                () -> {
                    Log.e("LIBRARY", "Không thể tải dữ liệu thư viện");
                }
        );

        // Sự kiện click search
        View searchView = view.findViewById(R.id.ivSearch);
        if (searchView != null) {
            searchView.setOnClickListener(v -> openSearchFragment());
        }

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

    private void openSearchFragment() {
        SearchFragment fragment = new SearchFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openDetailCategory(Category category) {
        DetailCategoryFragment fragment = new DetailCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("category_name", category.getName());
        fragment.setArguments(bundle);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

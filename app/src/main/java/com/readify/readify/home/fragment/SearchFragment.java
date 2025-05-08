package com.readify.readify.home.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.readify.readify.R;
import com.readify.readify.home.adapter.CategoryAdapter;
import com.readify.readify.home.adapter.SearchBookAdapter;
import com.readify.readify.home.data.SampleData;
import com.readify.readify.home.model.Book;
import com.readify.readify.home.model.Category;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText edtSearch;
    private TextView btnCancel, txtResultCount;
    private LinearLayout layoutExplore, layoutSearchResult;
    private Button tabBoth, tabTitle, tabAuthor, btnExploreAllGenres;
    private RecyclerView recyclerGenres, recyclerSearchResult;
    private SearchBookAdapter searchBookAdapter;
    private List<Book> allBooks;
    private List<Book> filteredBooks;
    private int currentTab = 0; // 0: Both, 1: Title, 2: Author
    private CategoryAdapter.OnCategoryClickListener categoryClickListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        edtSearch = view.findViewById(R.id.edtSearch);
        btnCancel = view.findViewById(R.id.btnCancel);
        layoutExplore = view.findViewById(R.id.layoutExplore);
        layoutSearchResult = view.findViewById(R.id.layoutSearchResult);
        tabBoth = view.findViewById(R.id.tabBoth);
        tabTitle = view.findViewById(R.id.tabTitle);
        tabAuthor = view.findViewById(R.id.tabAuthor);
        txtResultCount = view.findViewById(R.id.txtResultCount);
        recyclerGenres = view.findViewById(R.id.recyclerGenres);
        recyclerSearchResult = view.findViewById(R.id.recyclerSearchResult);
        btnExploreAllGenres = view.findViewById(R.id.btnExploreAllGenres);

        categoryClickListener = category -> openDetailCategory(category);
        recyclerGenres.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerGenres.setAdapter(new CategoryAdapter(getContext(), SampleData.getCategories(), categoryClickListener));

        // Setup search result list
        recyclerSearchResult.setLayoutManager(new LinearLayoutManager(getContext()));
        allBooks = SampleData.getBooks("Picks");
        allBooks.addAll(SampleData.getBooks("SelfHelp"));
        allBooks.addAll(SampleData.getBooks("Popular"));
        filteredBooks = new ArrayList<>(allBooks);
        searchBookAdapter = new SearchBookAdapter(getContext(), filteredBooks, book -> openBookDetail(book));
        recyclerSearchResult.setAdapter(searchBookAdapter);

        // Search text change
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean hasText = s.length() > 0;
                layoutExplore.setVisibility(hasText ? View.GONE : View.VISIBLE);
                layoutSearchResult.setVisibility(hasText ? View.VISIBLE : View.GONE);
                if (hasText) {
                    filterBooks(s.toString());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Cancel button
        btnCancel.setOnClickListener(v -> {
            edtSearch.setText("");
            layoutExplore.setVisibility(View.VISIBLE);
            layoutSearchResult.setVisibility(View.GONE);
        });

        // Tab click
        tabBoth.setOnClickListener(v -> selectTab(0));
        tabTitle.setOnClickListener(v -> selectTab(1));
        tabAuthor.setOnClickListener(v -> selectTab(2));
        selectTab(0);

        // Explore all genres button
        btnExploreAllGenres.setOnClickListener(v -> {
            AllCategoryFragment fragment = new AllCategoryFragment();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }

    private void selectTab(int tab) {
        currentTab = tab;
        tabBoth.setAlpha(tab == 0 ? 1f : 0.5f);
        tabTitle.setAlpha(tab == 1 ? 1f : 0.5f);
        tabAuthor.setAlpha(tab == 2 ? 1f : 0.5f);
        filterBooks(edtSearch.getText().toString());
    }

    private void filterBooks(String query) {
        filteredBooks.clear();
        String q = query.toLowerCase();
        for (Book book : allBooks) {
            if (currentTab == 0) { // Both
                if (book.title.toLowerCase().contains(q) || book.author.toLowerCase().contains(q)) {
                    filteredBooks.add(book);
                }
            } else if (currentTab == 1) { // Title
                if (book.title.toLowerCase().contains(q)) {
                    filteredBooks.add(book);
                }
            } else if (currentTab == 2) { // Author
                if (book.author.toLowerCase().contains(q)) {
                    filteredBooks.add(book);
                }
            }
        }
        searchBookAdapter.notifyDataSetChanged();
        txtResultCount.setText("Showing results 1-" + filteredBooks.size() + " of " + allBooks.size());
    }

    private void openBookDetail(Book book) {
        DetailBookFragment fragment = new DetailBookFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("book", book);
        fragment.setArguments(bundle);
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
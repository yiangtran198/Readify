package com.readify.readify.home.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;
import com.readify.readify.R;
import com.readify.readify.home.adapter.BookAdapter;
import com.readify.readify.home.adapter.CategoryAdapter;
import com.readify.readify.home.adapter.PopularBookAdapter;
import com.readify.readify.home.data.SampleData;
import com.readify.readify.home.model.Book;
import com.readify.readify.home.model.Category;

public class FragmentHome extends Fragment {
    private RecyclerView recyclerCategory, recyclerPicks, recyclerSelfHelp, recyclerPopular;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

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
        recyclerPicks.setAdapter(new BookAdapter(getContext(), SampleData.getBooks("Picks"), bookClickListener));
        recyclerSelfHelp.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerSelfHelp.setAdapter(new BookAdapter(getContext(), SampleData.getBooks("SelfHelp"), bookClickListener));

        // Popular: PopularBookAdapter (dạng dọc, số thứ tự, ảnh nhỏ)
        recyclerPopular.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerPopular.setAdapter(new PopularBookAdapter(getContext(), SampleData.getBooks("Popular")));

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

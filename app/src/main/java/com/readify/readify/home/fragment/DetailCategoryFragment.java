package com.readify.readify.home.fragment;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.readify.readify.R;
import com.readify.readify.home.adapter.SearchBookAdapter;
import com.readify.readify.home.data.SampleData;
import com.readify.readify.home.model.Book;
import com.readify.readify.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailCategoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final int MAX_DESC_LINES = 3;
    private boolean expanded = false;
    private String categoryName;
    private String fullDesc;

    public DetailCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailCategoryFragment newInstance(String param1, String param2) {
        DetailCategoryFragment fragment = new DetailCategoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_detail_category, container, false);
        ImageView btnBack = view.findViewById(R.id.btnBack);
        TextView txtCategoryTitle = view.findViewById(R.id.txtCategoryTitle);
        TextView txtCategoryDesc = view.findViewById(R.id.txtCategoryDesc);
        TextView btnReadMore = view.findViewById(R.id.btnReadMore);
        RecyclerView recyclerBooks = view.findViewById(R.id.recyclerBooksInCategory);

        // Nhận tên category
        if (getArguments() != null) {
            categoryName = getArguments().getString("category_name", "Category");
        } else {
            categoryName = "Category";
        }
        txtCategoryTitle.setText(categoryName);

        // Mô tả mẫu cho từng category (có thể lấy từ SampleData hoặc hardcode demo)
        fullDesc = getCategoryDescription(categoryName);
        txtCategoryDesc.setText(fullDesc);
        txtCategoryDesc.setMaxLines(MAX_DESC_LINES);
        btnReadMore.setVisibility(View.VISIBLE);
        btnReadMore.setText("readmore");

        btnReadMore.setOnClickListener(v -> {
            expanded = !expanded;
            if (expanded) {
                txtCategoryDesc.setMaxLines(Integer.MAX_VALUE);
                btnReadMore.setText("less");
            } else {
                txtCategoryDesc.setMaxLines(MAX_DESC_LINES);
                btnReadMore.setText("readmore");
            }
        });

        List<Book> books = BookRepository.getInstance().getBooks();
        if (books != null) {
            recyclerBooks.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerBooks.setAdapter(new SearchBookAdapter(getContext(), books, book -> openBookDetail(book)));
        }

        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        return view;
    }

    private String getCategoryDescription(String category) {
        // Demo mô tả, bạn có thể lấy từ SampleData hoặc API
        if (category.equalsIgnoreCase("Classics")) {
            return "A classic stands the test of time. The work is usually considered to be a representation of the period in which it was written; and the work merits lasting recognition. In other words, if the book was published in the recent past, the work is not a classic. A classic has a certain universal appeal. Great works of literature touch us to our very core beings--partly because they integrate themes that are understood by readers from a wide range of backgrounds and levels of experience. Themes of love, hate, death, life, and faith touch upon some of our most basic emotional responses. Although the term is often associated with the Western canon, it can be applied to works of literature from all traditions, such as the Chinese classics or the Indian Vedas.";
        }
        return "This is a description for " + category + ".";
    }

    private void openBookDetail(Book book) {
        DetailBookFragment fragment = new DetailBookFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("book", book);
        fragment.setArguments(bundle);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
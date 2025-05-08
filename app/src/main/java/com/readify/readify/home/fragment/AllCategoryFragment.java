package com.readify.readify.home.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.readify.readify.R;
import com.readify.readify.home.adapter.AllCategoryAdapter;
import com.readify.readify.home.data.SampleData;
import com.readify.readify.home.model.Category;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllCategoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllCategoryFragment newInstance(String param1, String param2) {
        AllCategoryFragment fragment = new AllCategoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_all_category, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerAllCategory);
        ImageView btnBack = view.findViewById(R.id.btnBack);

        List<Category> categories = SampleData.getCategories();
        AllCategoryAdapter adapter = new AllCategoryAdapter(getContext(), categories, category -> openDetailCategory(category));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        return view;
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
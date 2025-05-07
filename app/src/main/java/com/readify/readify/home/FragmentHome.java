package com.readify.readify.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.readify.readify.R;
import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {
    private RecyclerView recyclerPicks, recyclerSelfHelp, recyclerPopular;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerViews
        recyclerPicks = view.findViewById(R.id.recyclerPicks);
        recyclerSelfHelp = view.findViewById(R.id.recyclerSelfHelp);
        recyclerPopular = view.findViewById(R.id.recyclerPopular);

        // Set layout managers
        recyclerPicks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerSelfHelp.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerPopular.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Set adapters (assuming BookAdapter is already implemented)
        recyclerPicks.setAdapter(new BookAdapter(getContext(), getSampleBooks("Picks")));
        recyclerSelfHelp.setAdapter(new BookAdapter(getContext(), getSampleBooks("SelfHelp")));
        recyclerPopular.setAdapter(new BookAdapter(getContext(), getSampleBooks("Popular")));

        return view;
    }

    private List<Book> getSampleBooks(String type) {
        List<Book> books = new ArrayList<>();
        if (type.equals("Picks")) {
            books.add(new Book("Atomic Habits", "James Clear", "atomic_habits", true));
            books.add(new Book("Deep Work", "Cal Newport", "deep_work", true));
        } else if (type.equals("SelfHelp")) {
            books.add(new Book("The Power of Now", "Eckhart Tolle", "power_now", true));
            books.add(new Book("Think Like a Monk", "Jay Shetty", "think_monk", true));
        } else if (type.equals("Popular")) {
            books.add(new Book("Rich Dad Poor Dad", "Robert Kiyosaki", "rich_dad", true));
            books.add(new Book("Start With Why", "Simon Sinek", "start_why", true));
        }
        return books;
    }
}

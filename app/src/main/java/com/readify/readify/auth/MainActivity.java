package com.example.ltudreadify;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ltudreadify.BookAdapter;
import com.example.ltudreadify.Book;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerPicks, recyclerSelfHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerPicks = findViewById(R.id.recycler_picks);
        recyclerSelfHelp = findViewById(R.id.recycler_self_help);

        recyclerPicks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerSelfHelp.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Dữ liệu mẫu - sau này có thể thay bằng Firebase hoặc API
        List<Book> picksList = new ArrayList<>();
        picksList.add(new Book("The Collected Regrets", "Mikki Brammer", "https://...image1"));
        picksList.add(new Book("Retrospective", "Juan Gabriel Vásquez", "https://...image2"));

        List<Book> selfHelpList = new ArrayList<>();
        selfHelpList.add(new Book("Stop Overthinking", "Nick Trenton", "https://...image3"));
        selfHelpList.add(new Book("Self-Love Habit", "Fiona Brennan", "https://...image4"));

        BookAdapter picksAdapter = new BookAdapter(this, picksList);
        BookAdapter selfHelpAdapter = new BookAdapter(this, selfHelpList);

        recyclerPicks.setAdapter(picksAdapter);
        recyclerSelfHelp.setAdapter(selfHelpAdapter);
    }
}

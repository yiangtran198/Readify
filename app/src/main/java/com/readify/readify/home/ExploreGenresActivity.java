package com.readify.readify.home;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.readify.readify.R;

import java.util.List;
import java.util.Arrays;

public class ExploreGenresActivity extends AppCompatActivity implements GenreAdapter.OnGenreClickListener {

    private RecyclerView recyclerView;
    private GenreAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_genres);

        recyclerView = findViewById(R.id.recyclerGenres);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load genres from temporary method
        List<String> genres = getGenres();

        adapter = new GenreAdapter(genres, this);
        recyclerView.setAdapter(adapter);
    }

    private List<String> getGenres() {
        // Fake data - sẽ thay bằng dữ liệu từ API/Firebase
        return Arrays.asList(
                "Classics", "Fiction", "Crime", "Biography",
                "Children's", "Comics", "Fantasy", "Romance",
                "Contemporary", "Young Adults"
        );
    }

    @Override
    public void onGenreClick(String genre) {
        Intent intent = new Intent(this, GenreDetailActivity.class);
        intent.putExtra("genre", genre);
        startActivity(intent);
    }
}

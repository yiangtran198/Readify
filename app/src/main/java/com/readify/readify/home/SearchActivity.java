package com.readify.readify.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.readify.readify.R;
import com.readify.readify.home.Book;
import com.readify.readify.home.GenreDetailActivity;

import java.util.ArrayList;
public class SearchActivity extends AppCompatActivity{
    EditText edtSearch;
    RadioGroup radioGroup;
    Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        edtSearch = findViewById(R.id.edtSearch);
        radioGroup = findViewById(R.id.radioGroup);
        btnSearch = findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(v -> {
            String query = edtSearch.getText().toString().toLowerCase();
            int selectedId = radioGroup.getCheckedRadioButtonId();
            String mode = "both";
            if (selectedId == R.id.radioTitle) mode = "title";
            else if (selectedId == R.id.radioAuthor) mode = "author";

            // Giả lập danh sách (sau này thay bằng API hoặc Firebase)
            ArrayList<Book> filtered = new ArrayList<>();
            for (Book book : SampleData.books) {
                if (mode.equals("both") && (book.title.toLowerCase().contains(query) || book.author.toLowerCase().contains(query))) {
                    filtered.add(book);
                } else if (mode.equals("title") && book.title.toLowerCase().contains(query)) {
                    filtered.add(book);
                } else if (mode.equals("author") && book.author.toLowerCase().contains(query)) {
                    filtered.add(book);
                }
            }

            Intent intent = new Intent(this, GenreDetailActivity.class);
            intent.putExtra("search_result", filtered);
            intent.putExtra("search_mode", true);
            startActivity(intent);
        });
    }
}

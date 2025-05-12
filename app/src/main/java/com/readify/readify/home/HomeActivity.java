package com.readify.readify.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.readify.readify.R;
import com.readify.readify.auth.LoginActivity;
import com.readify.readify.home.fragment.AllCategoryFragment;
import com.readify.readify.home.fragment.DetailCategoryFragment;
import com.readify.readify.home.fragment.FragmentHome;  // Import FragmentHome
import com.readify.readify.home.fragment.SearchFragment;
import com.readify.readify.profile.SettingsFragment;
import com.readify.readify.reader.fragment.FragmentReader;

public class HomeActivity extends AppCompatActivity {
    private ImageView ivSearch;
    private ImageButton btnMenu;
    private LinearLayout header;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        header = findViewById(R.id.header);

        // Check if the user is logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // If logged in, display the FragmentHome
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new FragmentHome());  // Replace with your Fragment container
            transaction.commit();
        } else {
            // If not logged in, redirect to login screen
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();  // Close HomeActivity
        }

        ivSearch = findViewById(R.id.ivSearch);
        btnMenu = findViewById(R.id.ivMenu);

        btnMenu.setOnClickListener(v -> {
            SettingsFragment fragment = new SettingsFragment();
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to a search fragment or activity
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new SearchFragment());
                transaction.addToBackStack(null);  // Allow back navigation
                transaction.commit();
            }
        });

        // Ẩn/hiện header khi chuyển fragment
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment current = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (current instanceof SearchFragment) {
                header.setVisibility(View.GONE);
            } else if (current instanceof AllCategoryFragment) {
                header.setVisibility(View.GONE);
            } else if (current instanceof DetailCategoryFragment) {
                header.setVisibility(View.GONE);
            } else {
                header.setVisibility(View.VISIBLE);
            }
        });
    }
}

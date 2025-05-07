package com.readify.readify.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.readify.readify.R;
import com.readify.readify.home.FragmentHome;  // Import FragmentHome

public class HomeActivity extends AppCompatActivity {
    private Button btnLogout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

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

        btnLogout = findViewById(R.id.btnLogout);

        // Logout functionality
        btnLogout.setOnClickListener(v -> {
            // Log out user and redirect to login screen
            mAuth.signOut();

            // Go back to login screen
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();  // Close HomeActivity
        });
    }
}

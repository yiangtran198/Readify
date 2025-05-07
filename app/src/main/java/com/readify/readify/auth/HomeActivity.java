package com.readify.readify.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.readify.readify.R;
import com.readify.readify.home.FragmentHome;  // Import FragmentHome

public class HomeActivity extends AppCompatActivity {
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Check if the user is logged in (this can be customized as needed)
        boolean isLoggedIn = getSharedPreferences("AppPrefs", MODE_PRIVATE)
                .getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // If logged in, display the FragmentHome
            // Use FragmentTransaction to add FragmentHome to the container
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
            // Log out user and clear preferences
            getSharedPreferences("AppPrefs", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isLoggedIn", false)
                    .apply();

            // Go back to login screen
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();  // Close HomeActivity
        });
    }
}

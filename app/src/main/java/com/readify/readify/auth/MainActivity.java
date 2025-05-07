package com.readify.readify.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // Người dùng đã đăng nhập
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        } else {
            // Người dùng chưa đăng nhập
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        finish();
    }
}

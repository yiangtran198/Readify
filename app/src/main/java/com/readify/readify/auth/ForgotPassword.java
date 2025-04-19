package com.readify.readify.auth;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.readify.readify.R;

public class ForgotPassword extends AppCompatActivity {
    private TextInputEditText etEmail;
    private MaterialButton btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword);

        etEmail = findViewById(R.id.etEmail);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            // TODO: Gửi email đặt lại mật khẩu hoặc xử lý logic
        });
    }
}

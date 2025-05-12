package com.readify.readify.profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.readify.readify.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    private CircleImageView profileImage;
    private FrameLayout btnCamera;
    private ImageButton icCamera;
    private EditText etName, etEmail, etPassword;
    private TextView tvDate;
    private RelativeLayout datePickerContainer;
    private Button btnSaveChanges;
    private ImageButton btnBack;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private FirebaseUser currentUser;

    private Uri selectedImageUri = null;
    private Calendar calendar = Calendar.getInstance();

    // Activity result launcher for image selection
    private final ActivityResultLauncher<String> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    Glide.with(this)
                            .load(uri)
                            .into(profileImage);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Initialize views
        profileImage = findViewById(R.id.profile_image);
        btnCamera = findViewById(R.id.btn_camera);
        icCamera = findViewById(R.id.ic_camera);
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        tvDate = findViewById(R.id.tv_date);
        datePickerContainer = findViewById(R.id.date_picker_container);
        btnSaveChanges = findViewById(R.id.btn_save_changes);
        btnBack = findViewById(R.id.btn_back);

        // Load user data
        loadUserData();

        // Set click listeners
        btnBack.setOnClickListener(v -> {
            // Navigate back to SettingsActivity
            Intent intent = new Intent(EditProfileActivity.this, SettingsFragment.class);
            startActivity(intent);
            finish();
        });

        btnCamera.setOnClickListener(v -> {
            // Open image picker
            imagePickerLauncher.launch("image/*");
        });

        icCamera.setOnClickListener(v -> {
            // Open image picker (in case the inner button is clicked)
            imagePickerLauncher.launch("image/*");
        });

        datePickerContainer.setOnClickListener(v -> {
            showDatePickerDialog();
        });

        btnSaveChanges.setOnClickListener(v -> {
            saveUserData();
        });
    }

    private void loadUserData() {
        if (currentUser != null) {
            String uid = currentUser.getUid();

            db.collection("users").document(uid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");
                            String email = currentUser.getEmail();
                            String birthday = documentSnapshot.getString("birthday");
                            String avatarUrl = documentSnapshot.getString("avatar");

                            // Set user data
                            etName.setText(name);
                            etEmail.setText(email);

                            // Format and set birthday
                            if (birthday != null && !birthday.isEmpty()) {
                                // Convert from YYYY-MM-DD to DD/MM/YYYY
                                String[] parts = birthday.split("-");
                                if (parts.length == 3) {
                                    String formattedDate = parts[2] + "/" + parts[1] + "/" + parts[0];
                                    tvDate.setText(formattedDate);

                                    // Set calendar date
                                    calendar.set(Calendar.YEAR, Integer.parseInt(parts[0]));
                                    calendar.set(Calendar.MONTH, Integer.parseInt(parts[1]) - 1);
                                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parts[2]));
                                }
                            }

                            // Load avatar with Glide
                            if (avatarUrl != null && !avatarUrl.isEmpty()) {
                                Glide.with(this)
                                        .load(avatarUrl)
                                        .placeholder(R.drawable.img_daidien)
                                        .into(profileImage);
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle error
                        Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    // Update the TextView with the selected date
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    tvDate.setText(dateFormat.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void saveUserData() {
        if (currentUser == null) return;

        String uid = currentUser.getUid();
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate inputs
        if (name.isEmpty()) {
            etName.setError("Name is required");
            return;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            return;
        }

        // Create a map for Firestore update
        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("name", name);

        // Format birthday from DD/MM/YYYY to YYYY-MM-DD for storage
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            String formattedDate = outputFormat.format(inputFormat.parse(tvDate.getText().toString()));
            userUpdates.put("birthday", formattedDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Handle image upload if a new image was selected
        if (selectedImageUri != null) {
            StorageReference storageRef = storage.getReference().child("profile_images").child(uid);
            storageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            userUpdates.put("avatar", uri.toString());
                            updateUserData(uid, userUpdates, email, password);
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
        } else {
            updateUserData(uid, userUpdates, email, password);
        }
    }

    private void updateUserData(String uid, Map<String, Object> userUpdates, String email, String password) {
        // Update Firestore document
        db.collection("users").document(uid)
                .update(userUpdates)
                .addOnSuccessListener(aVoid -> {
                    // Update email if changed
                    if (!email.equals(currentUser.getEmail())) {
                        currentUser.updateEmail(email)
                                .addOnSuccessListener(aVoid1 -> {
                                    // Email updated successfully
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Failed to update email", Toast.LENGTH_SHORT).show();
                                });
                    }

                    // Update password if provided
                    if (!password.isEmpty() && !password.equals("************")) {
                        currentUser.updatePassword(password)
                                .addOnSuccessListener(aVoid1 -> {
                                    // Password updated successfully
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show();
                                });
                    }

                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                    // Navigate back to ProfileActivity
//                    Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
//                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                });
    }
}
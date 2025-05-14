package com.readify.readify.profile;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class EditProfileFragment extends Fragment {

    private CircleImageView profileImage;
    private FrameLayout btnCamera;
    private ImageButton icCamera;
    private EditText etName, etPassword, etEmail;
    private TextView tvDate;
    private RelativeLayout datePickerContainer;
    private Button btnSaveChanges;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private FirebaseUser currentUser;

    private Uri selectedImageUri = null;
    private Calendar calendar = Calendar.getInstance();

    private final ActivityResultLauncher<String> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    Glide.with(requireContext()).load(uri).into(profileImage);
                }
            }
    );

    public EditProfileFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_my_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        currentUser = mAuth.getCurrentUser();

        profileImage = view.findViewById(R.id.profile_image);
        btnCamera = view.findViewById(R.id.btn_camera);
        etName = view.findViewById(R.id.et_name);
        etEmail = view.findViewById(R.id.et_email); // Chỉ hiển thị, không update
        etPassword = view.findViewById(R.id.et_password);
        tvDate = view.findViewById(R.id.tv_date);
        datePickerContainer = view.findViewById(R.id.date_picker_container);
        btnSaveChanges = view.findViewById(R.id.btn_save_changes);

        btnCamera.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));
        datePickerContainer.setOnClickListener(v -> showDatePickerDialog());
        btnSaveChanges.setOnClickListener(v -> saveUserData());

        FirebaseUser infoUser = FirebaseAuth.getInstance().getCurrentUser();
        if (infoUser != null) {
            etName.setText(infoUser.getDisplayName());
            etEmail.setText(infoUser.getEmail());
        }

        loadUserInfoFromFirestore();


        return view;
    }

    private void loadUserInfoFromFirestore() {
        if (currentUser == null) return;
        String uid = currentUser.getUid();

        db.collection("users").document(uid).get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        String birthday = snapshot.getString("birthday");
                        if (birthday != null) tvDate.setText(formatDateToDisplay(birthday));
                        String avatar = snapshot.getString("avatar");
                        Glide.with(requireContext())
                                .load(avatar)
                                .placeholder(R.drawable.img_daidien)
                                .into(profileImage);
                    }
                });
    }

    private String formatDateToDisplay(String input) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").format(
                    new SimpleDateFormat("yyyy-MM-dd").parse(input)
            );
        } catch (Exception e) {
            return "";
        }
    }

    private String getFormattedBirthday() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            .parse(tvDate.getText().toString()));
        } catch (Exception e) {
            return null;
        }
    }

    private void saveUserData() {
        if (currentUser == null) return;

        String uid = currentUser.getUid();
        String name = etName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Name is required");
            return;
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", name);

        String birthday = getFormattedBirthday();
        if (birthday != null) updates.put("birthday", birthday);

        if (!password.isEmpty() && !password.equals("************")) {
            updatePassword(password);
        }

        if (selectedImageUri != null) {
            uploadImageAndUpdate(uid, updates);
        } else {
            updateFirestore(uid, updates);
        }

        updateFirebaseAuthDisplayName(name);
    }

    private void uploadImageAndUpdate(String uid, Map<String, Object> updates) {
        StorageReference ref = storage.getReference().child("profile_images").child(uid);
        ref.putFile(selectedImageUri)
                .addOnSuccessListener(task ->
                        ref.getDownloadUrl().addOnSuccessListener(uri -> {
                            updates.put("avatar", uri.toString());
                            updateFirestore(uid, updates);
                        })
                )
                .addOnFailureListener(e ->
                        Toast.makeText(requireContext(), "Lỗi upload ảnh", Toast.LENGTH_SHORT).show()
                );
    }

    private void updateFirestore(String uid, Map<String, Object> updates) {
        db.collection("users").document(uid).update(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(requireContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    requireActivity().onBackPressed();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(requireContext(), "Lỗi khi lưu profile", Toast.LENGTH_SHORT).show()
                );
    }

    private void updatePassword(String newPassword) {
        String email = currentUser.getEmail();
        if (email == null) return;

        currentUser.updatePassword(newPassword)
                .addOnSuccessListener(unused -> {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, newPassword)
                            .addOnSuccessListener(authResult -> {
                                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(requireContext(), "Đăng nhập lại thất bại", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Lỗi cập nhật mật khẩu", Toast.LENGTH_SHORT).show();
                });
    }


    private void updateFirebaseAuthDisplayName(String name) {
        currentUser.updateProfile(
                new com.google.firebase.auth.UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
        );
    }

    private void showDatePickerDialog() {
        DatePickerDialog picker = new DatePickerDialog(
                requireContext(),
                (view, y, m, d) -> {
                    calendar.set(y, m, d);
                    tvDate.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        picker.show();
    }
}

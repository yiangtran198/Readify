package com.readify.readify.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.readify.readify.R;
import com.readify.readify.auth.LoginActivity;
import com.readify.readify.home.HomeActivity;
import com.readify.readify.library.LibraryFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsFragment extends Fragment {

    private CircleImageView profileImage;
    private TextView userName;
    private Button btnMyProfile, btnLibraryPage, btnLogout;
//    private ImageButton btnBack;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_settings, container, false);

//        // Firebase
        mAuth = FirebaseAuth.getInstance();
//        db = FirebaseFirestore.getInstance();
//        currentUser = mAuth.getCurrentUser();
        userName = view.findViewById(R.id.user_name);
        profileImage = view.findViewById(R.id.profile_image);
        userName = view.findViewById(R.id.user_name);
        btnMyProfile = view.findViewById(R.id.btn_my_profile);
        btnLibraryPage = view.findViewById(R.id.btn_library_page);
        btnLogout = view.findViewById(R.id.btn_logout);

        FirebaseUser infoUser = FirebaseAuth.getInstance().getCurrentUser();
        if (infoUser != null) {
            userName.setText(infoUser.getDisplayName());
        }

        if (infoUser != null) {
            Log.d("INFO_USER", "UID: " + infoUser.getUid());
            Log.d("INFO_USER", "Email: " + infoUser.getEmail());
            Log.d("INFO_USER", "Phone: " + infoUser.getPhoneNumber());
            Log.d("INFO_USER", "Name: " + infoUser.getDisplayName());
            Log.d("INFO_USER", "PhotoURL: " + infoUser.getPhotoUrl());
            Log.d("INFO_USER", "Is Email Verified: " + infoUser.isEmailVerified());
            Log.d("INFO_USER", "Provider: " + infoUser.getProviderId());
        }

        btnMyProfile.setOnClickListener(v -> {
            ProfileFragment fragment = new ProfileFragment();
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        btnLibraryPage.setOnClickListener(v -> {
            LibraryFragment fragment = new LibraryFragment();
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });
        return view;
    }
}

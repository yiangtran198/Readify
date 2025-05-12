package com.readify.readify.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.readify.readify.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private CircleImageView profileImage;
    private Button btnEditProfile;
    private ImageButton btnBack;

    public ProfileFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);

        profileImage = view.findViewById(R.id.profile_image);
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        btnBack = view.findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> {
            requireActivity().onBackPressed(); // hoặc popBackStack nếu dùng Navigation
        });

        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), EditProfileActivity.class);
            startActivity(intent);
        });

        return view;
    }
}

package com.readify.readify.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.readify.readify.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private CircleImageView profileImage;
    private Button btnEditProfile;

    private TextView textName;
    private TextView textEmail;

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
        textName = view.findViewById(R.id.tv_name);
        textEmail = view.findViewById(R.id.tv_email);


        FirebaseUser infoUser = FirebaseAuth.getInstance().getCurrentUser();
        if (infoUser != null) {
            textName.setText(infoUser.getDisplayName());
            textEmail.setText(infoUser.getEmail());
        }

        btnEditProfile.setOnClickListener(v -> {
            EditProfileFragment fragment = new EditProfileFragment();
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}

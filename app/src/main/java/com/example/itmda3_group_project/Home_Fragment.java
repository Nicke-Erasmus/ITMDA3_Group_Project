package com.example.itmda3_group_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.itmda3_group_project.login_registration.LoginPage;
import com.google.firebase.auth.FirebaseAuth;

public class Home_Fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Find the logout button in the fragment layout
        Button btnLogout = view.findViewById(R.id.btnLogout);

        // Set click listener
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(requireContext(), LoginPage.class);
            startActivity(intent);
            requireActivity().finish();
        });

        return view;
    }
}

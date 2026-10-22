package com.example.itmda3_group_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.itmda3_group_project.Habit_Tracker.Habit_Tracker_Fragment;
import com.example.itmda3_group_project.AI_Helper.AI_Home_Fragment;
import com.example.itmda3_group_project.login_registration.LoginPage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class Home_Fragment extends Fragment {

    private LinearLayout cardAsk, dailyProgress;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        cardAsk = rootView.findViewById(R.id.cardAsk);
        dailyProgress = rootView.findViewById(R.id.dailyProgress);
        Button btnLogout = rootView.findViewById(R.id.btnLogout);

        // Logout button
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(requireContext(), LoginPage.class);
            startActivity(intent);
            requireActivity().finish();
        });

        // Navigation: AI Helper
        cardAsk.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_Container, new AI_Home_Fragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Navigation: Habit Tracker
        dailyProgress.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_Container, new Habit_Tracker_Fragment())
                    .addToBackStack(null)
                    .commit();
        });

        return rootView;
    }
}

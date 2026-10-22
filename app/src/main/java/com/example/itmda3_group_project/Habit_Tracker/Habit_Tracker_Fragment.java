package com.example.itmda3_group_project.Habit_Tracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.itmda3_group_project.R;

public class Habit_Tracker_Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout
        View view = inflater.inflate(R.layout.fragment_habit_tracker, container, false);

        // Find the button inside the inflated view
        Button btnAddHabit = view.findViewById(R.id.btnAddHabit);

        // Set up the click listener
        btnAddHabit.setOnClickListener(v -> {
            // Replace current fragment with CategorySelectorFragment
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_Container, new CategorySelectorFragment());
            transaction.addToBackStack(null); // allows back navigation
            transaction.commit();
        });

        return view;
    }
}



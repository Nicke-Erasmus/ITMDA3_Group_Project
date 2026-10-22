package com.example.itmda3_group_project.Habit_Tracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.itmda3_group_project.R;

public class HabitSuccessFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_habit_success, container, false);

        Button btnDone = view.findViewById(R.id.btnDone);

        btnDone.setOnClickListener(v -> {
            // Navigate back to Habit_Tracker_Fragment
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_Container, new Habit_Tracker_Fragment());
            transaction.addToBackStack(null); // allows back navigation
            transaction.commit();
        });

        return view;
    }
}

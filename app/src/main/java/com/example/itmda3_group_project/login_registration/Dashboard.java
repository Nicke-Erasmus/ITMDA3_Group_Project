package com.example.itmda3_group_project.login_registration;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.itmda3_group_project.AI_Helper_Fragment;
import com.example.itmda3_group_project.Habit_Tracker_Fragment;
import com.example.itmda3_group_project.Home_Fragment;
import com.example.itmda3_group_project.Note_Taker_Fragment;
import com.example.itmda3_group_project.R;
import com.example.itmda3_group_project.study_rooms.Study_Rooms_Fragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_nav);

        /* Top tool bar */
        MaterialToolbar top_toolbar = findViewById(R.id.topAppBar);
        top_toolbar.setTitle("Dashboard");

        /* Bottom navigation bar */
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);

        /* Sets the default fragment to the home fragment*/
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_Container, new Home_Fragment())
                .commit();

        /* Logic that allows the bottom navigation menu to
        * move between the created fragments.*/
        bottomNavigationView.setOnItemSelectedListener( item -> {
            Fragment selectedFragment = null;

            int id = item.getItemId();

            if (id == R.id.home) {
                selectedFragment = new Home_Fragment();
                top_toolbar.setTitle("Home");
            } else if (id == R.id.ai_helper) {
                selectedFragment = new AI_Helper_Fragment();
                top_toolbar.setTitle("AI Helper");
            } else if (id == R.id.study_rooms) {
                selectedFragment = new Study_Rooms_Fragment();
                top_toolbar.setTitle("Study Rooms");
            } else if (id == R.id.habit_tracker) {
                selectedFragment = new Habit_Tracker_Fragment();
                top_toolbar.setTitle("Habit Tracker");
            } else if (id == R.id.notes) {
                selectedFragment = new Note_Taker_Fragment();
                top_toolbar.setTitle("Note Taker");
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_Container, selectedFragment)
                        .commit();
            }
            return true;
        });
    }
}

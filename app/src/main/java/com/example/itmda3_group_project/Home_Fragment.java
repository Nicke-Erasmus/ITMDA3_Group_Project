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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itmda3_group_project.Habit_Tracker.Habit_Tracker_Fragment;
import com.example.itmda3_group_project.AI_Helper.AI_Home_Fragment;
import com.example.itmda3_group_project.login_registration.LoginPage;
import com.example.itmda3_group_project.note_taker.NoteAdapter;
import com.example.itmda3_group_project.note_taker.NoteDao;
import com.example.itmda3_group_project.study_rooms.More_Info_Study_Rooms;
import com.example.itmda3_group_project.study_rooms.StudyRoomsAdapter;
import com.example.itmda3_group_project.study_rooms.StudyRoomsViewModel;
import com.example.itmda3_group_project.study_rooms.StudySession;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Home_Fragment extends Fragment {

    private LinearLayout cardAsk, dailyProgress;
    private StudyRoomsAdapter adapter;
    private StudyRoomsViewModel viewModel;

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

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerStudyRooms);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // Reuse StudyRoomsAdapter
        adapter = new StudyRoomsAdapter(session -> {
            // Navigate to full info when a card is clicked
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_Container, new More_Info_Study_Rooms())
                    .addToBackStack(null)
                    .commit();
        });
        recyclerView.setAdapter(adapter);

        // Reuse StudyRoomsViewModel
        viewModel = new ViewModelProvider(this).get(StudyRoomsViewModel.class);
        viewModel.sessions.observe(getViewLifecycleOwner(), sessions -> {
            // Show only first 2 sessions as preview
            List<StudySession> preview = sessions.size() > 2 ? sessions.subList(0, 2) : sessions;
            adapter.setSessions(preview);
        });


        return rootView;
    }
}
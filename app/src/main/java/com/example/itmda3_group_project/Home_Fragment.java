package com.example.itmda3_group_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itmda3_group_project.Habit_Tracker.Habit_Tracker_Fragment;
import com.example.itmda3_group_project.AI_Helper.AI_Home_Fragment;
import com.example.itmda3_group_project.login_registration.LoginPage;
import com.example.itmda3_group_project.note_taker.AppDatabase;
import com.example.itmda3_group_project.note_taker.Note;
import com.example.itmda3_group_project.note_taker.NoteDao;
import com.example.itmda3_group_project.study_rooms.More_Info_Study_Rooms;
import com.example.itmda3_group_project.study_rooms.StudyRoomsAdapter;
import com.example.itmda3_group_project.study_rooms.StudyRoomsViewModel;
import com.example.itmda3_group_project.study_rooms.StudySession;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

            // Update bottom nav selection
            BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
            bottomNav.setSelectedItemId(R.id.ai_helper);
        });

        //Adds the correct progress percent to the textview
        TextView tvHabitProgress = rootView.findViewById(R.id.tvProgressPercent);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference habitsRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid)
                .child("habits");

        habitsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalHabits = 0;
                int completedHabits = 0;

                for (DataSnapshot habitSnap : snapshot.getChildren()) {
                    totalHabits++;
                    Boolean completed = habitSnap.child("completed").getValue(Boolean.class);
                    if (completed != null && completed) {
                        completedHabits++;
                    }
                }

                if (totalHabits > 0) {
                    int percentage = (int) ((completedHabits * 100.0f) / totalHabits);
                    tvHabitProgress.setText(percentage + "%");
                } else {
                    tvHabitProgress.setText("0%");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                tvHabitProgress.setText("Error");
            }
        });


        // Navigation: Habit Tracker
        dailyProgress.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_Container, new Habit_Tracker_Fragment())
                    .addToBackStack(null)
                    .commit();

            // Update bottom nav selection
            BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
            bottomNav.setSelectedItemId(R.id.habit_tracker);
        });

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerStudyRooms);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // Reuse StudyRoomsAdapter
        adapter = new StudyRoomsAdapter(session -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("session", session);
            DialogFragment moreInfo = new More_Info_Study_Rooms();
            moreInfo.setArguments(bundle);
            moreInfo.show(requireActivity().getSupportFragmentManager(), "MoreInfoDialog");
        });
        recyclerView.setAdapter(adapter);

        // Reuse StudyRoomsViewModel
        viewModel = new ViewModelProvider(this).get(StudyRoomsViewModel.class);
        viewModel.sessions.observe(getViewLifecycleOwner(), sessions -> {
            // Show only first 2 sessions as preview
            List<StudySession> preview = sessions.size() > 2 ? sessions.subList(0, 2) : sessions;
            adapter.setSessions(preview);
        });

        TextView noteTitle1 = rootView.findViewById(R.id.noteTitle1);
        TextView noteTitle2 = rootView.findViewById(R.id.noteTitle2);

        NoteDao noteDao = AppDatabase.getInstance(requireContext()).noteDao();
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            List<Note> recentNotes = noteDao.getTwoMostRecentNotes();

            requireActivity().runOnUiThread(() -> {
                if (recentNotes.size() >= 1) {
                    noteTitle1.setText(recentNotes.get(0).getTitle());
                } else {
                    noteTitle1.setText("No notes yet");
                }

                if (recentNotes.size() >= 2) {
                    noteTitle2.setText(recentNotes.get(1).getTitle());
                    noteTitle2.setVisibility(View.VISIBLE);
                } else {
                    noteTitle2.setText("");
                    noteTitle2.setVisibility(View.GONE);
                }
            });
        });


        return rootView;
    }
}
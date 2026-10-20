package com.example.itmda3_group_project.study_rooms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itmda3_group_project.R;
import com.google.firebase.auth.FirebaseAuth;

public class Joined_Sessions_Fragment extends Fragment {

    private JoinedSessionsViewModel viewModel;
    private StudyRoomsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_joined_sessions, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewJoined);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new StudyRoomsAdapter(session -> {
            // Show details in dialog
            Bundle bundle = new Bundle();
            bundle.putSerializable("session", session);
            More_Info_Study_Rooms dialog = new More_Info_Study_Rooms();
            dialog.setArguments(bundle);
            dialog.show(requireActivity().getSupportFragmentManager(), "MoreInfoDialog");
        });
        recyclerView.setAdapter(adapter);

        // ViewModel setup
        viewModel = new ViewModelProvider(this).get(JoinedSessionsViewModel.class);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        viewModel.getJoinedSessions(userId).observe(getViewLifecycleOwner(), sessions -> {
            adapter.setSessions(sessions);
        });

        return view;
    }
}
package com.example.itmda3_group_project.study_rooms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itmda3_group_project.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Study_Rooms_Fragment extends Fragment {

    FloatingActionButton Add_button;
    private StudyRoomsViewModel viewModel;
    private StudyRoomsAdapter adapter;
    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_study_rooms, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        searchView = view.findViewById(R.id.search);
        searchView.clearFocus();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new StudyRoomsAdapter(session -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("session", session);
            DialogFragment moreInfo = new More_Info_Study_Rooms();
            moreInfo.setArguments(bundle);
            moreInfo.show(requireActivity().getSupportFragmentManager(), "MoreInfoDialog");
        });

        recyclerView.setAdapter(adapter);

        // Setup ViewModel
        viewModel = new ViewModelProvider(this).get(StudyRoomsViewModel.class);
        viewModel.sessions.observe(getViewLifecycleOwner(), sessions -> {
            adapter.setSessions(sessions);
        });

        TextView tvJoinedSessions = view.findViewById(R.id.tvJoinedSessions);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference joinedRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("joined_sessions");

        joinedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    tvJoinedSessions.setVisibility(View.VISIBLE);

                    // Move RecyclerView down below TextView
                    ConstraintLayout.LayoutParams params =
                            (ConstraintLayout.LayoutParams) recyclerView.getLayoutParams();
                    params.topToBottom = R.id.tvJoinedSessions;
                    recyclerView.setLayoutParams(params);

                    // Optional: show count
                    long count = snapshot.getChildrenCount();
                    tvJoinedSessions.setText("View Joined Sessions (" + count + ")");
                } else {
                    tvJoinedSessions.setVisibility(View.GONE);

                    // Reset RecyclerView to sit below SearchView
                    ConstraintLayout.LayoutParams params =
                            (ConstraintLayout.LayoutParams) recyclerView.getLayoutParams();
                    params.topToBottom = R.id.search;
                    recyclerView.setLayoutParams(params);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // handle error if needed
            }
        });

        tvJoinedSessions.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_Container, new Joined_Sessions_Fragment())
                    .addToBackStack(null)
                    .commit();
        });

        //Implements searching using the searchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        //Add button logic
        Add_button = view.findViewById(R.id.btnAdd);
        Add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Replaces the current fragment with the new one
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_Container, new Add_Study_Room())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}

package com.example.itmda3_group_project.study_rooms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itmda3_group_project.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Study_Rooms_Fragment extends Fragment {

    FloatingActionButton Add_button;
    private StudyRoomsViewModel viewModel;
    private StudyRoomsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_study_rooms, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
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

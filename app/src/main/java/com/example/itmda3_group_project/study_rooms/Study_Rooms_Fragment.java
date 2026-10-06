package com.example.itmda3_group_project.study_rooms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.itmda3_group_project.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Study_Rooms_Fragment extends Fragment {

    FloatingActionButton Add_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_study_rooms, container, false);

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

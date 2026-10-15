package com.example.itmda3_group_project.study_rooms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.itmda3_group_project.R;

public class More_Info_Study_Rooms extends DialogFragment {
    private StudySession session;
    TextView tvTitle, tvLocation, tvDate, tvTime, tvDescription, tvAvailability;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_info_study_room, container, false);

        if (getArguments() != null) {
            session = (StudySession) getArguments().getSerializable("session");
        }

        tvTitle = view.findViewById(R.id.dialog_room_title);
        tvDate = view.findViewById(R.id.room_date);
        tvDescription = view.findViewById(R.id.room_description);
        tvLocation = view.findViewById(R.id.room_location);
        tvTime = view.findViewById(R.id.room_time);
        tvAvailability = view.findViewById(R.id.room_availability);

        if (session != null) {
            tvTitle.setText(session.getTitle());
            tvDate.setText(session.getDate());
            tvDescription.setText(session.getDescription());
            tvLocation.setText(session.getLocation());
            tvTime.setText(session.getTime());
            tvAvailability.setText(session.getAvailability());
        }

       return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            // Make popup full width and wrap content height
            getDialog().getWindow().setLayout(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
            );
        }
    }
}

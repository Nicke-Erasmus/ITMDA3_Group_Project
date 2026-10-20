package com.example.itmda3_group_project.study_rooms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.itmda3_group_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class More_Info_Study_Rooms extends DialogFragment {
    private StudySession session;
    TextView tvTitle, tvLocation, tvDate, tvTime, tvDescription, tvAvailability;
    Button btnJoin;

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

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference userJoinedRef = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(userId)
                    .child("joined_sessions")
                    .child(session.getId());

            // 🔎 Check if already joined when dialog opens
            userJoinedRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    btnJoin.setEnabled(false);
                    btnJoin.setText("Already Joined");
                }
            });
        }


        btnJoin = view.findViewById(R.id.dialog_join_session);
        btnJoin.setOnClickListener(v -> {
            if (session != null) {
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                //Add only the room ID under the user's joined_sessions
                rootRef.child("users")
                        .child(userId)
                        .child("joined_sessions")
                        .child(session.getId())
                        .setValue(true);

                //Decrement availability in study_rooms
                DatabaseReference sessionRef = rootRef.child("study_rooms").child(session.getId());
                int updatedAvailability = Integer.parseInt(session.getAvailability()) - 1;
                sessionRef.child("availability").setValue(String.valueOf(updatedAvailability));

                dismiss();
            }
        });

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

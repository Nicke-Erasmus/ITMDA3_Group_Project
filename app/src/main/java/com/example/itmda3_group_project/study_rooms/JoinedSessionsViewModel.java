package com.example.itmda3_group_project.study_rooms;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class JoinedSessionsViewModel extends ViewModel {
    private final MutableLiveData<List<StudySession>> joinedSessions = new MutableLiveData<>();

    public LiveData<List<StudySession>> getJoinedSessions(String userId) {
        DatabaseReference userJoinedRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("joined_sessions");

        userJoinedRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<StudySession> sessionsList = new ArrayList<>();
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    String roomId = snapshot.getKey();
                    FirebaseDatabase.getInstance().getReference("study_rooms")
                            .child(roomId)
                            .get()
                            .addOnCompleteListener(roomTask -> {
                                if (roomTask.isSuccessful() && roomTask.getResult() != null) {
                                    StudySession session = roomTask.getResult().getValue(StudySession.class);
                                    if (session != null) {
                                        sessionsList.add(session);
                                        joinedSessions.setValue(new ArrayList<>(sessionsList));
                                    }
                                }
                            });
                }
            }
        });

        return joinedSessions;
    }
}

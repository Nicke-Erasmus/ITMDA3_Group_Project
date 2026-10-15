package com.example.itmda3_group_project.study_rooms;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//This viewmodel is in charge of reading the data from the database
public class StudyRoomsViewModel extends ViewModel {
    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("study_rooms");
    private final MutableLiveData<List<StudySession>> _sessions = new MutableLiveData<>();
    public LiveData<List<StudySession>> sessions = _sessions;

    public StudyRoomsViewModel() {
        //Checks for any changes in the database
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<StudySession> list = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    StudySession session = child.getValue(StudySession.class);
                    if (session != null) {
                        list.add(session);
                    }
                }
                _sessions.setValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

package com.example.itmda3_group_project.study_rooms;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddStudySessionViewModel extends ViewModel {
    private final DatabaseReference reference;
    private final MutableLiveData<Boolean> _saveSuccess = new MutableLiveData<>();
    public LiveData<Boolean> saveSuccess = _saveSuccess;

    public AddStudySessionViewModel() {
        reference = FirebaseDatabase.getInstance().getReference("study_rooms");
    }

    public void addSession(StudySession session) {
        String id= reference.push().getKey();
        if (id != null) {
            reference.child(id).setValue(session)
                    .addOnSuccessListener(aVoid -> _saveSuccess.setValue(true))
                    .addOnFailureListener(e-> _saveSuccess.setValue(false));
        }
        else {
            _saveSuccess.setValue(false);
        }
    }
}

package com.example.itmda3_group_project.Habit_Tracker;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//ViewModel handles backend firebase logic
public class AddHabit_ViewModel extends ViewModel {
    private final DatabaseReference reference;
    private final MutableLiveData<Boolean> _saveSuccess = new MutableLiveData<>();
    public LiveData<Boolean> saveSuccess = _saveSuccess;

    public AddHabit_ViewModel() {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            reference = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(uid)
                    .child("habits");
        } else {
            reference = null; // no user logged in
        }
    }

    public void addHabit(HabitClass habit) {
        if (reference == null) {
            _saveSuccess.setValue(false);
            return; // prevent crash if uid is null
        }

        String id = reference.push().getKey();
        if (id != null) {
            reference.child(id).setValue(habit)
                    .addOnSuccessListener(aVoid -> _saveSuccess.setValue(true))
                    .addOnFailureListener(e -> _saveSuccess.setValue(false));
        } else {
            _saveSuccess.setValue(false);
        }
    }
}

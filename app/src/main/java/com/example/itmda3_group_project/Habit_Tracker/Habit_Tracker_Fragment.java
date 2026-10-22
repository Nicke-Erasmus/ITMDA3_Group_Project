package com.example.itmda3_group_project.Habit_Tracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itmda3_group_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Habit_Tracker_Fragment extends Fragment {

    private DatabaseReference reference;
    private LinearLayout emptyState;
    private RecyclerView recyclerHabits;
    private Button btnAddHabit;
    private HabitAdapter adapter;
    private List<HabitClass> habitList = new ArrayList<>();

    // Stats views
    private TextView tvOutOf;
    private TextView tvProgressPercent;

    private int completedCount = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_habit_tracker, container, false);

        emptyState = rootView.findViewById(R.id.emptyState);
        recyclerHabits = rootView.findViewById(R.id.recyclerHabits);
        btnAddHabit = rootView.findViewById(R.id.btnAddHabit);
        tvOutOf = rootView.findViewById(R.id.tvOutOf);
        tvProgressPercent = rootView.findViewById(R.id.tvProgressPercent);

        recyclerHabits.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HabitAdapter(habitList, (habit, isChecked) -> {
            if (isChecked) {
                completedCount++;
            } else {
                completedCount--;
            }
            updateStats();
        });
        recyclerHabits.setAdapter(adapter);

        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            reference = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(uid)
                    .child("habits");
        } else {
            Toast.makeText(getActivity(),
                    "User not logged in. Please sign in.",
                    Toast.LENGTH_LONG).show();
        }

        btnAddHabit.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_Container, new CategorySelectorFragment())
                    .addToBackStack(null)
                    .commit();
        });

        loadHabits();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadHabits();
    }

    private void loadHabits() {
        if (reference == null) return;

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                habitList.clear();
                completedCount = 0;

                if (!snapshot.hasChildren()) {
                    emptyState.setVisibility(View.VISIBLE);
                    recyclerHabits.setVisibility(View.GONE);
                    tvOutOf.setText("0 out of 0 completed");
                    tvProgressPercent.setText("0%");
                    return;
                }

                String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                for (DataSnapshot child : snapshot.getChildren()) {
                    HabitClass habit = child.getValue(HabitClass.class);
                    if (habit != null) {
                        habit.setID(child.getKey());

                        // Reset if lastCompletedDate is not today
                        if (habit.getLastCompletedDate() == null ||
                                !habit.getLastCompletedDate().equals(today)) {
                            habit.setCompleted(false);
                            reference.child(habit.getID()).child("completed").setValue(false);
                        }

                        habitList.add(habit);
                    }
                }

                adapter.setHabits(habitList);
                adapter.notifyDataSetChanged();
                emptyState.setVisibility(View.GONE);
                recyclerHabits.setVisibility(View.VISIBLE);
                updateStats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),
                        "Couldn't load habits. Check Firebase rules and connection.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateStats() {
        int total = habitList.size();
        int completed = 0;

        for (HabitClass habit: habitList) {
            if (habit.isCompleted()) {
                completed++;
            }
        }
        int percentage = total > 0 ? (completed * 100 / total) : 0;

        tvOutOf.setText(completed + " out of " + total + " completed");
        tvProgressPercent.setText(percentage + "%");
    }
}

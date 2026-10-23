package com.example.itmda3_group_project.Habit_Tracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.itmda3_group_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HabitDetailFragment extends Fragment {
    private HabitClass habit;
    private DatabaseReference reference;

    public HabitDetailFragment(HabitClass habit) {
        this.habit = habit;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_habit_detail, container, false);

        TextView txtBackArrow = rootView.findViewById(R.id.txtBackArrow);
        TextView tvTitle = rootView.findViewById(R.id.tvHabitTitle);
        TextView tvCategory = rootView.findViewById(R.id.tvCategory);
        TextView tvFrequency = rootView.findViewById(R.id.tvFrequency);
        TextView tvDescription = rootView.findViewById(R.id.tvDescription);
        TextView tvReminderTime = rootView.findViewById(R.id.tvHabitReminderTime);
        TextView tvNotes = rootView.findViewById(R.id.tvNotes);
        TextView tvemoji = rootView.findViewById(R.id.tvemoji);
        Button btnDeleteHabit = rootView.findViewById(R.id.btnDeleteHabit);

        //Gets the correct data from firebase to use in the details fragment
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            reference = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(uid)
                    .child("habits")
                    .child(habit.getID());
        }

        // Populate UI
        tvTitle.setText(habit.getName());
        tvCategory.setText(habit.getCategory());
        tvFrequency.setText(habit.getFrequency());
        tvDescription.setText(habit.getDescription());
        tvReminderTime.setText(habit.getReminderTime());
        tvNotes.setText("Focus on assignments and revision. You got this! 💪");

        txtBackArrow.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        String category = habit.getCategory(); // make sure HabitClass has a category field
        String emoji;
        String note;
        switch (category) {
            case "Academic":
                emoji = "🎓";
                note = "Keep learning, every step counts!";
                break;
            case "Health":
                emoji = "🏋️‍♂️";
                note = "Strong body, strong mind — you’ve got this!";
                break;
            case "Personal":
                emoji = "💗";
                note = "Take care of yourself, you deserve it.";
                break;
            case "Study Skills":
                emoji = "💡";
                note = "Smart habits build lasting success.";
                break;
            case "Social":
                emoji = "👥";
                note = "Connections make life brighter — reach out!";
                break;
            case "Finance":
                emoji = "💰";
                note = "Small savings today grow into big wins tomorrow.";
                break;
            default:
                emoji = "🔥";
                note = "Stay motivated, you’re on fire!";
        }
        tvemoji.setText(emoji);
        tvNotes.setText(note); // add a TextView for the note


        btnDeleteHabit.setOnClickListener(v -> {
            if (reference != null) {
                reference.removeValue().addOnSuccessListener(aVoid ->
                        Toast.makeText(getActivity(), "Habit deleted", Toast.LENGTH_SHORT).show()
                );
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_Container, new Habit_Tracker_Fragment())
                        .commit();

            }
        });

        return rootView;
    }
}
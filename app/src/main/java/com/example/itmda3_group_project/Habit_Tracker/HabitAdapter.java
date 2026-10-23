package com.example.itmda3_group_project.Habit_Tracker;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itmda3_group_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {

    private List<HabitClass> habits;
    private final OnHabitCheckedListener checkedListener;

    // Callback interface for checkbox changes
    public interface OnHabitCheckedListener {
        void onHabitChecked(HabitClass habit, boolean isChecked);
    }

    public HabitAdapter(List<HabitClass> habits, OnHabitCheckedListener checkedListener) {
        this.habits = habits;
        this.checkedListener = checkedListener;
    }

    public void setHabits(List<HabitClass> habits) {
        this.habits = habits;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.habit_items, parent, false);
        return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        HabitClass habit = habits.get(position);
        holder.bind(habit, checkedListener);

        // Make the whole item clickable → open detail fragment
        holder.itemView.setOnClickListener(v -> {
            HabitDetailFragment detailFragment = new HabitDetailFragment(habit);
            ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_Container, detailFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return habits != null ? habits.size() : 0;
    }

    static class HabitViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvTime, tvEmoji;
        CheckBox checkHabit;

        HabitViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvHabitTitle);
            tvTime = itemView.findViewById(R.id.tvHabitTime);
            tvEmoji = itemView.findViewById(R.id.tvProgressPercent);
            checkHabit = itemView.findViewById(R.id.checkHabit);
        }

        void bind(HabitClass habit, OnHabitCheckedListener checkedListener) {
            tvTitle.setText(habit.getName() == null || habit.getName().isEmpty()
                    ? "Untitled habit" : habit.getName());

            tvTime.setText(habit.getReminderTime() == null || habit.getReminderTime().isEmpty()
                    ? "No reminder" : habit.getReminderTime());

            // Map category → emoji
            String category = habit.getCategory(); // make sure HabitClass has a category field
            String emoji;
            if (category == null) {
                emoji = "🔥"; // fallback
            } else {
                switch (category) {
                    case "Academic":
                        emoji = "\uD83C\uDF93";
                        break;
                    case "Health and Fitness":
                        emoji = "\uD83C\uDFCB\uFE0F";
                        break;
                    case "Personal":
                        emoji = "\uD83D\uDC97";
                        break;
                    case "Study Skills":
                        emoji = "\uD83D\uDCA1";
                        break;
                    case "Social":
                        emoji = "\uD83D\uDC65";
                        break;
                    case "Finance":
                        emoji = "💰";
                        break;
                    default:
                        emoji = "🔥"; // fallback
                }
            }

            tvEmoji.setText(emoji);

            checkHabit.setOnCheckedChangeListener(null);
            checkHabit.setChecked(habit.isCompleted());

            checkHabit.setOnCheckedChangeListener((buttonView, isChecked) -> {
                habit.setCompleted(isChecked);
                checkedListener.onHabitChecked(habit, isChecked);

                String uid = FirebaseAuth.getInstance().getUid();
                if (uid != null && habit.getID() != null) {
                    DatabaseReference habitRef = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(uid)
                            .child("habits")
                            .child(habit.getID());

                    habitRef.child("completed").setValue(isChecked);

                    if (isChecked) {
                        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                        habit.setLastCompletedDate(today);
                        habitRef.child("lastCompletedDate").setValue(today);

                        // Update streak (When a habit is completed the streak updates)
                        DatabaseReference streakRef = FirebaseDatabase.getInstance()
                                .getReference("users")
                                .child(uid)
                                .child("streak");

                        streakRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String lastDate = snapshot.child("lastStreakDate").getValue(String.class);
                                Integer streakCount = snapshot.child("streakCount").getValue(Integer.class);
                                if (streakCount == null) streakCount = 0;

                                if (lastDate != null) {
                                    Calendar cal = Calendar.getInstance();
                                    cal.add(Calendar.DAY_OF_YEAR, -1);
                                    String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

                                    if (lastDate.equals(yesterday)) {
                                        streakCount++; // consecutive day
                                    } else if (!lastDate.equals(today)) {
                                        streakCount = 1; // reset streak
                                    }
                                } else {
                                    streakCount = 1; // first completion
                                }

                                streakRef.child("streakCount").setValue(streakCount);
                                streakRef.child("lastStreakDate").setValue(today);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}
                        });
                    }
                }
            });

        }
    }
}
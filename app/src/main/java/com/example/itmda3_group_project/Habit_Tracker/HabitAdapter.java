package com.example.itmda3_group_project.Habit_Tracker;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itmda3_group_project.R;

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
    }

    @Override
    public int getItemCount() {
        return habits != null ? habits.size() : 0;
    }

    static class HabitViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvTime, tvProgress;
        CheckBox checkHabit;

        HabitViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvHabitTitle);
            tvTime = itemView.findViewById(R.id.tvHabitTime);
            tvProgress = itemView.findViewById(R.id.tvProgressPercent);
            checkHabit = itemView.findViewById(R.id.checkHabit);
        }

        void bind(HabitClass habit, OnHabitCheckedListener checkedListener) {
            tvTitle.setText(habit.getName() == null || habit.getName().isEmpty()
                    ? "Untitled habit" : habit.getName());

            tvTime.setText(habit.getReminderTime() == null || habit.getReminderTime().isEmpty()
                    ? "No reminder" : habit.getReminderTime());

            tvProgress.setText("🔥"); // placeholder icon

            checkHabit.setOnCheckedChangeListener((buttonView, isChecked) -> {
                checkedListener.onHabitChecked(habit, isChecked);
            });
        }
    }
}
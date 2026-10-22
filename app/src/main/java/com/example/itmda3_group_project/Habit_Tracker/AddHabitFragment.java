package com.example.itmda3_group_project.Habit_Tracker;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.itmda3_group_project.R;

import java.util.Calendar;
import java.util.Locale;

public class AddHabitFragment extends Fragment {

    private EditText edtHabitName, edtDescription, edtReminder;
    private Spinner spinnerCategory, spinnerFrequency;
    private Button btnSaveHabit;
    private TextView successBanner;
    private AddHabit_ViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment layout
        View rootView = inflater.inflate(R.layout.fragment_add_habit, container, false);

        // ViewModel tied to fragment lifecycle
        viewModel = new ViewModelProvider(this).get(AddHabit_ViewModel.class);

        edtHabitName = rootView.findViewById(R.id.edtHabitName);
        edtDescription = rootView.findViewById(R.id.edtDescription);
        edtReminder = rootView.findViewById(R.id.edtReminder);
        spinnerCategory = rootView.findViewById(R.id.spinnerCategory);
        spinnerFrequency = rootView.findViewById(R.id.spinnerFrequency);
        btnSaveHabit = rootView.findViewById(R.id.btnSaveHabit);

        edtReminder.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePicker = new TimePickerDialog(
                    getActivity(),
                    (view, selectedHour, selectedMinute) -> {
                        String selectedTime = String.format(
                                Locale.getDefault(),
                                "%02d:%02d",
                                selectedHour,
                                selectedMinute
                        );
                        edtReminder.setText(selectedTime);
                    },
                    hour,
                    minute,
                    true
            );
            timePicker.setTitle("Set Habit Reminder");
            timePicker.show();
        });

        setupSpinners();
        setupCategorySelection();
        createSuccessBanner(rootView);

        btnSaveHabit.setOnClickListener(v -> saveHabit());

        viewModel.saveSuccess.observe(getViewLifecycleOwner(), success -> {
            if (success) {
                btnSaveHabit.setEnabled(true);
                successBanner.setVisibility(View.VISIBLE);
                successBanner.bringToFront();

                successBanner.postDelayed(() -> {
                    // Navigate to HabitSuccessFragment instead of Activity
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_Container, new HabitSuccessFragment())
                            .addToBackStack(null)
                            .commit();
                }, 900);
            } else {
                btnSaveHabit.setEnabled(true);
                Toast.makeText(
                        getActivity(),
                        "Couldn't save habit. Check Firebase access and try again.",
                        Toast.LENGTH_LONG
                ).show();
            }
        });

        return rootView;
    }

    private void setupSpinners() {
        String[] categories = {"Academic", "Health", "Personal", "Study Skills", "Social", "Finance"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, categories
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        String[] frequencies = {"Daily", "Weekdays", "Weekly", "Custom"};
        ArrayAdapter<String> frequencyAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, frequencies
        );
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrequency.setAdapter(frequencyAdapter);
    }

    private void setupCategorySelection() {
        Bundle args = getArguments();
        int selectedCategoryId = args != null ? args.getInt("selectedCategoryId", -1) : -1;

        int selectedPosition = 0;
        if (selectedCategoryId == R.id.rbHealth) selectedPosition = 1;
        else if (selectedCategoryId == R.id.rbPersonal) selectedPosition = 2;
        else if (selectedCategoryId == R.id.rbStudySkills) selectedPosition = 3;
        else if (selectedCategoryId == R.id.rbSocial) selectedPosition = 4;
        else if (selectedCategoryId == R.id.rbFinance) selectedPosition = 5;

        spinnerCategory.setSelection(selectedPosition);
    }

    private void createSuccessBanner(View rootView) {
        FrameLayout root = rootView.findViewById(R.id.bannerContainer);
        // Make sure you have a container in your layout for this banner

        successBanner = new TextView(getActivity());
        successBanner.setText("\u2713  Habit saved successfully!\nYour new habit is ready to track.");
        successBanner.setTextColor(Color.rgb(39, 91, 55));
        successBanner.setTextSize(14);
        successBanner.setGravity(Gravity.CENTER_VERTICAL);
        successBanner.setPadding(dp(18), dp(14), dp(18), dp(14));
        successBanner.setVisibility(View.GONE);
        successBanner.setElevation(dp(8));

        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.rgb(226, 241, 223));
        background.setCornerRadius(dp(14));
        successBanner.setBackground(background);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.TOP
        );
        params.setMargins(dp(16), dp(12), dp(16), 0);

        root.addView(successBanner, params);
    }

    private void saveHabit() {
        String name = edtHabitName.getText().toString();
        String description = edtDescription.getText().toString();
        String reminder = edtReminder.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String frequency = spinnerFrequency.getSelectedItem().toString();

        if (name.isEmpty()) {
            edtHabitName.setError("Please enter a habit name");
            edtHabitName.requestFocus();
            return;
        }

        HabitClass habit = new HabitClass(name, description, category, frequency, reminder);
        viewModel.addHabit(habit);
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }
}

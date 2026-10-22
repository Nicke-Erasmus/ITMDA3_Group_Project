package com.example.itmda3_group_project.Habit_Tracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.itmda3_group_project.R;

public class CategorySelectorFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_category_selector, container, false);

        RadioGroup rgCategories = rootView.findViewById(R.id.rgCategories);
        Button btnContinue = rootView.findViewById(R.id.btnContinueCategory);

        btnContinue.setOnClickListener(v -> {
            int selectedCategoryId = rgCategories.getCheckedRadioButtonId();

            if (selectedCategoryId == -1) {
                Toast.makeText(getActivity(), "Please select a category", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create the AddHabitFragment and pass the selected category
            AddHabitFragment addHabitFragment = new AddHabitFragment();
            Bundle args = new Bundle();
            args.putInt("selectedCategoryId", selectedCategoryId);
            addHabitFragment.setArguments(args);

            // Replace current fragment with AddHabitFragment
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_Container, addHabitFragment); // use the one with args
            transaction.addToBackStack(null);
            transaction.commit();

        });

        return rootView;
    }
}

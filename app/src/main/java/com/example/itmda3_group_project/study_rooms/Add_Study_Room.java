package com.example.itmda3_group_project.study_rooms;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.itmda3_group_project.R;

public class Add_Study_Room extends Fragment {
    EditText etTitle, etDate, etTime, etLocation, etAvailability, etDescription;
    Button btnAddSession;
    private AddStudySessionViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_study_room, container, false);

        //Adding the view model to the fragment
        viewModel = new ViewModelProvider(this).get(AddStudySessionViewModel.class);

        etTitle = view.findViewById(R.id.etTitle);
        etDate = view.findViewById(R.id.etDate);
        etTime = view.findViewById(R.id.etTime);
        etLocation = view.findViewById(R.id.etLocation);
        etAvailability = view.findViewById(R.id.etAvailability);
        etDescription = view.findViewById(R.id.etDescription);
        btnAddSession = view.findViewById(R.id.add_study_session);

        btnAddSession.setOnClickListener(v -> {
            if (validateInputs()) {
                StudySession session = new StudySession(
                        etTitle.getText().toString().trim(),
                        etDate.getText().toString().trim(),
                        etTime.getText().toString().trim(),
                        etLocation.getText().toString().trim(),
                        etAvailability.getText().toString().trim(),
                        etDescription.getText().toString().trim()
                );
                viewModel.addSession(session);
            }
        });

        viewModel.saveSuccess.observe(getViewLifecycleOwner(), success -> {
            if (success) {
                Toast.makeText(getContext(), "Study room added!", Toast.LENGTH_SHORT).show();
                clearFields();

                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_Container, new Study_Rooms_Fragment())
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(getContext(), "Failed to add study room", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }
    private boolean validateInputs() {
        //Checks if title is filled in
        if (TextUtils.isEmpty(etTitle.getText())) {
            etTitle.setError("Title required");
            return false;
        }

        //Checks the date input
        String date = etDate.getText().toString().trim();
        if (TextUtils.isEmpty(date)) {
            etDate.setError("Date required");
            return false;
        } else if (!date.matches("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{2}$")) {
            etDate.setError("Date must be in format dd/MM/yy (e.g. 14/10/26)");
            return false;
        }

        //Checks the time input
        String time = etTime.getText().toString().trim();
        if (TextUtils.isEmpty(time)) {
            etTime.setError("Time required");
            return false;
        } else if (!time.matches("^([01]\\d|2[0-3]):[0-5]\\d$")) {
            etTime.setError("Time must be in format HH:mm (e.g. 14:30)");
            return false;
        }

        //Checks the location input
        if (TextUtils.isEmpty(etLocation.getText())) {
            etLocation.setError("Location required");
            return false;
        }

        //Checks the availability input
        if (TextUtils.isEmpty(etAvailability.getText())) {
            etAvailability.setError("Availability required");
            return false;
        }

        return true;
    }

    private void clearFields() {
        etTitle.setText("");
        etDate.setText("");
        etTime.setText("");
        etLocation.setText("");
        etAvailability.setText("");
        etDescription.setText("");
    }
}


package com.example.itmda3_group_project.AI_Helper;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.itmda3_group_project.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class StudyPlanFragment extends Fragment {

    private static final String PREFS = "study_plan";
    private static final String KEY = "events";

    private JSONObject allEvents = new JSONObject();
    private final Calendar selectedCal = Calendar.getInstance();
    private String selectedKey;

    private TextView tvSelectedDate, tvEmpty;
    private LinearLayout container;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup parent,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_study_plan, parent, false);

        tvSelectedDate = view.findViewById(R.id.tvSelectedDate);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        container = view.findViewById(R.id.scheduleContainer);
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        ImageButton btnBack = view.findViewById(R.id.btnBack);

        loadEvents(requireContext());
        selectedKey = keyFor(selectedCal);

        calendarView.setOnDateChangeListener((calView, year, month, day) -> {
            selectedCal.set(year, month, day);
            selectedKey = keyFor(selectedCal);
            refresh();
        });

        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());
        view.findViewById(R.id.btnAdd).setOnClickListener(v -> showAddDialog());

        refresh();
        return view;
    }

    // ---------- Showing the schedule ----------

    private void refresh() {
        SimpleDateFormat fmt = new SimpleDateFormat("EEEE, d MMMM", Locale.getDefault());
        tvSelectedDate.setText(fmt.format(selectedCal.getTime()));

        container.removeAllViews();
        List<JSONObject> items = getEventsFor(selectedKey);
        tvEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);

        for (JSONObject e : items) {
            View row = LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_schedule, container, false);

            ((TextView) row.findViewById(R.id.tvTime)).setText(e.optString("time"));
            ((TextView) row.findViewById(R.id.tvEventTitle)).setText(e.optString("title"));

            TextView tvNotes = row.findViewById(R.id.tvEventNotes);
            String notes = e.optString("notes");
            if (notes.isEmpty()) {
                tvNotes.setVisibility(View.GONE);
            } else {
                tvNotes.setText(notes);
            }

            long id = e.optLong("id");
            row.findViewById(R.id.tvDelete).setOnClickListener(v -> removeEvent(selectedKey, id));
            container.addView(row);
        }
    }

    // ---------- Adding an entry ----------

    private void showAddDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_event, null);
        EditText etTitle = dialogView.findViewById(R.id.etEventTitle);
        EditText etNotes = dialogView.findViewById(R.id.etEventNotes);
        TextView tvTime = dialogView.findViewById(R.id.tvPickTime);

        final String[] time = {"09:00"};
        tvTime.setText("🕘  09:00  (tap to change)");
        tvTime.setOnClickListener(v ->
                new TimePickerDialog(requireContext(), (picker, hour, minute) -> {
                    time[0] = String.format(Locale.US, "%02d:%02d", hour, minute);
                    tvTime.setText("🕘  " + time[0]);
                }, 9, 0, true).show());

        new AlertDialog.Builder(requireContext())
                .setTitle("Add to schedule")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String title = etTitle.getText().toString().trim();
                    if (title.isEmpty()) {
                        Toast.makeText(requireContext(), "Please enter a title", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    addEvent(selectedKey, title, time[0], etNotes.getText().toString().trim());
                    refresh();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // ---------- Saving and loading ----------

    private void addEvent(String key, String title, String time, String notes) {
        try {
            JSONObject o = new JSONObject();
            o.put("id", System.currentTimeMillis());
            o.put("title", title);
            o.put("time", time);
            o.put("notes", notes);

            JSONArray arr = allEvents.optJSONArray(key);
            if (arr == null) arr = new JSONArray();
            arr.put(o);
            allEvents.put(key, arr);
            saveEvents(requireContext());
        } catch (Exception ignored) { }
    }

    private void removeEvent(String key, long id) {
        try {
            JSONArray arr = allEvents.optJSONArray(key);
            if (arr == null) return;
            JSONArray kept = new JSONArray();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                if (o.optLong("id") != id) kept.put(o);
            }
            allEvents.put(key, kept);
            saveEvents(requireContext());
            refresh();
        } catch (Exception ignored) { }
    }

    private List<JSONObject> getEventsFor(String key) {
        List<JSONObject> list = new ArrayList<>();
        JSONArray arr = allEvents.optJSONArray(key);
        if (arr != null) {
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.optJSONObject(i);
                if (o != null) list.add(o);
            }
        }
        Collections.sort(list, (a, b) -> a.optString("time").compareTo(b.optString("time")));
        return list;
    }

    private void loadEvents(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        try {
            allEvents = new JSONObject(prefs.getString(KEY, "{}"));
        } catch (Exception e) {
            allEvents = new JSONObject();
        }
    }

    private void saveEvents(Context context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit().putString(KEY, allEvents.toString()).apply();
    }

    private String keyFor(Calendar c) {
        return String.format(Locale.US, "%04d-%02d-%02d",
                c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
    }
}

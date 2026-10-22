package com.example.itmda3_group_project.Habit_Tracker;

public class HabitClass {
    private String id;
    private String name;
    private String description;
    private String category;
    private String frequency;
    private String reminderTime;
    private boolean completed;
    private String lastCompletedDate;
    public HabitClass() {}

    //Using a class simplifies adding and extracting data from firebase
    public HabitClass(String name, String description, String category, String frequency, String reminderTime) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.frequency = frequency;
        this.reminderTime = reminderTime;

    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getReminderTime() {
        return reminderTime;
    }
    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public String getLastCompletedDate() { return lastCompletedDate; }
    public void setLastCompletedDate(String lastCompletedDate) { this.lastCompletedDate = lastCompletedDate; }
}

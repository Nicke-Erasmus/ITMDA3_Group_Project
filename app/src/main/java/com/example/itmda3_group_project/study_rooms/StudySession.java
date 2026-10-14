package com.example.itmda3_group_project.study_rooms;

public class StudySession {
    String title;
    String location;
    String time;
    String date;
    String availability;
    String description;

    public StudySession() {
    }

    public StudySession(String title, String location, String time, String date, String availability, String description) {
        this.title = title;
        this.location = location;
        this.time = time;
        this.date = date;
        this.availability = availability;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

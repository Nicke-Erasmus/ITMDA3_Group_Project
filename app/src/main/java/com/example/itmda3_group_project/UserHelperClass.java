package com.example.itmda3_group_project;

public class UserHelperClass {

    String username, email, universityName, studyPath;

    public UserHelperClass() {
    }

    public UserHelperClass(String username, String email, String universityName, String studyPath) {
        this.username = username;
        this.email = email;
        this.universityName = universityName;
        this.studyPath = studyPath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public String getStudyPath() {
        return studyPath;
    }

    public void setStudyPath(String studyPath) {
        this.studyPath = studyPath;
    }
}

package com.example.tutorapp.model;

public class Files {

    private String courseID;
    private String file;
    private String file_name;

    public Files(String courseID, String file, String file_name) {
        this.courseID = courseID;
        this.file = file;
        this.file_name = file_name;
    }

    public Files() {
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}

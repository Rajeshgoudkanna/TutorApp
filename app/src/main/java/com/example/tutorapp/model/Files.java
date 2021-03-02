package com.example.tutorapp.model;

public class Files {

    private String courseID;
    private String File;
    private String file_name;

    public Files(String courseID, String File, String file_name) {
        this.courseID = courseID;
        this.File = File;
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
        return File;
    }

    public void setFile(String file) {
        this.File = file;
    }
}

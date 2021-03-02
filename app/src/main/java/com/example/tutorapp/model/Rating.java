package com.example.tutorapp.model;

public class Rating {

    private String review;
    private String rating;
    private String pid;
    private String username;

    public Rating(String review, String rating, String pid, String username) {
        this.review = review;
        this.rating = rating;
        this.pid = pid;
        this.username = username;
    }

    public Rating() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}

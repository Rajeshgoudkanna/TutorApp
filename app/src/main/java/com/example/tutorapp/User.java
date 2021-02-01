package com.example.tutorapp;

public class User {
    private String name;
    private String phone;
    private String password;
    private String username;

    public User(String name, String phone, String password, String username, String email) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.username = username;
        this.email = email;
    }
    public User() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    private String email;
}

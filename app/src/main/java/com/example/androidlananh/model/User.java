package com.example.androidlananh.model;

import android.net.Uri;

public class User {
    private String userName="";
    private String id="";
    private String email="";
    private String imageAvatar="";
    public User() {

    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getImageAvatar() {
        return imageAvatar;
    }

    public void setImageAvatar(String imageAvatar) {
        this.imageAvatar = imageAvatar;
    }

    public User(String id, String userName, String email) {
        this.userName = userName;
        this.id = id;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User(String userName, String email) {
        this.userName = userName;
        this.email=email;
    }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

}
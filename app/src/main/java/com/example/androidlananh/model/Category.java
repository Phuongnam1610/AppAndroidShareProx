package com.example.androidlananh.model;

import java.io.Serializable;

public class Category implements Serializable {
    private String id="";
    private String image="";
    private String title="";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category(String id, String image, String title) {
        this.id = id;
        this.image = image;
        this.title = title;
    }
}
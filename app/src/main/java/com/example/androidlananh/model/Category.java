package com.example.androidlananh.model;

import java.io.Serializable;

public class Category implements Serializable {
    private String id="";
    private String img="";
    private String title="";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category(){}

    public Category(String id, String img, String title) {
        this.id = id;
        this.img = img;
        this.title = title;
    }
}
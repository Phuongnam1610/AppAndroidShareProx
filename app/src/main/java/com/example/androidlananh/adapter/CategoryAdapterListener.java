package com.example.androidlananh.adapter;

import com.example.androidlananh.model.Category;

import java.util.ArrayList;

public interface CategoryAdapterListener {
    void onClickCategory(Category category);
    void displayAllCategory(ArrayList<Category> categories);
    
}

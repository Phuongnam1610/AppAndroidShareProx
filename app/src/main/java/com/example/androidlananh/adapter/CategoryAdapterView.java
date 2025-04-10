package com.example.androidlananh.adapter;

import com.example.androidlananh.model.Category;
import com.example.androidlananh.ui.base.BaseView;

import java.util.ArrayList;

public interface CategoryAdapterView extends BaseView {
    void onClickCategory(Category category);
    void displayAllCategory(ArrayList<Category> categorys);
    
}

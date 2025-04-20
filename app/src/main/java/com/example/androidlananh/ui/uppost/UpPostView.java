package com.example.androidlananh.ui.uppost;

import android.net.Uri;

import com.example.androidlananh.model.Category;
import com.example.androidlananh.ui.base.BaseView;

import java.util.ArrayList;

public interface UpPostView extends BaseView {
    void addProductSuccess();
    void displayImageUri(Uri uri);

    void getAllCategories(ArrayList<Category> categories);
}

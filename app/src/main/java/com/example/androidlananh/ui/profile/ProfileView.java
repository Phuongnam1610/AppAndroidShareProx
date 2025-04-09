package com.example.androidlananh.ui.profile;

import android.net.Uri;

import com.example.androidlananh.model.User;
import com.example.androidlananh.ui.base.BaseView;

public interface ProfileView extends BaseView {
    void displayProfileData(User user);
    void displayImageUri(Uri uri);
    void updateSuccess();
}

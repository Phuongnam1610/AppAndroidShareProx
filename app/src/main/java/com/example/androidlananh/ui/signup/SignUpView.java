package com.example.androidlananh.ui.signup;

import android.net.Uri;

import com.example.androidlananh.ui.base.BaseView;

public interface SignUpView extends BaseView {
    void onSignUpSuccess();
    void displayAvatarUri(Uri imageUri);

}

package com.example.androidlananh.ui.fragments.user;
import com.example.androidlananh.adapter.ProductAdapterListener;
import com.example.androidlananh.model.User;
import com.example.androidlananh.ui.base.BaseView;


public interface UserView extends BaseView, ProductAdapterListener {
    void navigateToSignInActivity();
    void displayUserData(User user);

}

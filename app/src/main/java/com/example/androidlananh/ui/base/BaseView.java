package com.example.androidlananh.ui.base;

import android.content.Context;

public interface BaseView {
    void showLoading();
    void hideLoading();
    void showError(String error);
    public Context getContext();
}

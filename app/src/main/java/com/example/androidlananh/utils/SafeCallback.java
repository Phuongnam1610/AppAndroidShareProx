package com.example.androidlananh.utils;

import android.util.Log;

import com.example.androidlananh.repository.ApiCallback;

public abstract class SafeCallback<T> implements ApiCallback<T> {

    @Override
    public void onSuccess(T data) {
        try {
            handleSuccess(data);
        } catch (Exception e) {
            onError(e.getMessage());
        }
    }

    @Override
    public void onError(String error) {
        try {
            handleError(error);
        } catch (Exception e) {
            Log.e("SafeCallback", "Error in error callback", e);
        }
    }
    protected abstract void handleSuccess(T result);
    protected abstract void handleError(String error);
}
package com.example.androidlananh.repository;

public interface ApiCallback<T> {
    void onSuccess(T result);
    void onError(String error);
}
package com.example.androidlananh.ui.base;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public abstract class BaseActivity<Presenter extends BasePresenter> extends AppCompatActivity implements BaseView{
    @Override
    public void showError(String error) {
        Toast.makeText(this,error,LENGTH_SHORT).show();
        hideLoading();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();

    }

    protected Presenter presenter;

    @NonNull
    protected abstract Presenter createPresenter();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }


}

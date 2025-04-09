package com.example.androidlananh.ui.base;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment<Presenter extends BasePresenter> extends Fragment implements BaseView {

    protected Presenter presenter;

    @NonNull
    protected abstract Presenter createPresenter();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
    }

    @Nullable
    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(getContext(), error, LENGTH_SHORT).show();
        hideLoading();
    }


}
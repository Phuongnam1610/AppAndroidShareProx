package com.example.androidlananh.ui.signup;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidlananh.databinding.ActivityMainBinding;
import com.example.androidlananh.databinding.ActivitySignUpBinding;
import com.example.androidlananh.ui.base.BaseActivity;

public class SignUpActivity extends BaseActivity<SignUpPresenter> implements SignUpView {
    private ActivitySignUpBinding binding;

    @NonNull
    @Override
    protected SignUpPresenter createPresenter() {
        return new SignUpPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupEdgeToEdge();
        setupWindowInsets();

    }



    private void setupEdgeToEdge() {
        EdgeToEdge.enable(this);
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    @Override
    public void showLoading() {
        
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onSignUpSuccess() {

    }
}
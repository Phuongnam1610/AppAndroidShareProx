package com.example.androidlananh.ui.signin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidlananh.databinding.ActivityMainBinding;
import com.example.androidlananh.databinding.ActivitySignInBinding;
import com.example.androidlananh.ui.base.BaseActivity;
import com.example.androidlananh.ui.main.MainActivity;
import com.example.androidlananh.ui.signup.SignUpActivity;

public class SignInActivity extends BaseActivity<SignInPresenter> implements SignInView {
    private ActivitySignInBinding binding;

    @NonNull
    @Override
    protected SignInPresenter createPresenter() {
        return new SignInPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupEdgeToEdge();
        setupWindowInsets();
        binding.btnlogin.setOnClickListener(v -> {
            String email=binding.edtEmail.getText().toString();
            String password=binding.edtPassword.getText().toString();
            presenter.signIn(email,password);
        });
        binding.btnregister.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class));
        });
        binding.btnforgot.setOnClickListener(v -> {
            
        });
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
        binding.animationView.setVisibility(View.VISIBLE);
        binding.btnlogin.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideLoading() {
        binding.animationView.setVisibility(View.GONE);
        binding.btnlogin.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSignInSuccess() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
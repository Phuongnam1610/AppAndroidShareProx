package com.example.androidlananh.ui.signup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidlananh.databinding.ActivityMainBinding;
import com.example.androidlananh.databinding.ActivitySignUpBinding;
import com.example.androidlananh.ui.base.BaseActivity;
import com.example.androidlananh.ui.main.MainActivity;

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
        binding.btnRegister.setOnClickListener(v -> {
            String email=binding.edtEmail.getText().toString();
            String password=binding.edtPassword.getText().toString();
            String userName=binding.edtUserName.getText().toString();
            presenter.signUp(email,password,userName);
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
        binding.btnRegister.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideLoading() {
        binding.animationView.setVisibility(View.GONE);
        binding.btnRegister.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSignUpSuccess() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void displayAvatarUri(Uri imageUri) {

    }
}
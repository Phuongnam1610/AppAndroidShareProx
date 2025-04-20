package com.example.androidlananh.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidlananh.R;
import com.example.androidlananh.model.User;
import com.example.androidlananh.repository.ApiCallback;
import com.example.androidlananh.repository.UserRepository;
import com.example.androidlananh.ui.main.MainActivity;
import com.example.androidlananh.ui.signin.SignInActivity;
import com.example.androidlananh.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = new UserRepository();
        setContentView(R.layout.activity_splash);

        checkLoginStatus();
    }

    private void checkLoginStatus() {
        Intent intent;

        if (SessionManager.getInstance().getAuthUser() != null) {
            userRepository.getUserById(SessionManager.getInstance().getAuthUser().getUid(), new ApiCallback<User>() {
                @Override
                public void onSuccess(User result) {
                    SessionManager.getInstance().currentUser = result;
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();

                }

                @Override
                public void onError(String error) {
                    startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                    finish();
                }
            });

        } else {
            startActivity(new Intent(this, SignInActivity.class));
            finish();

        }
    }
}
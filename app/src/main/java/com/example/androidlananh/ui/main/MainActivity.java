package com.example.androidlananh.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.androidlananh.R;
import com.example.androidlananh.databinding.ActivityMainBinding;
import com.example.androidlananh.ui.base.BaseActivity;
import com.example.androidlananh.ui.fragments.home.HomeFragment;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends BaseActivity<MainPresenter> implements MainView {
    private ActivityMainBinding binding;

    @NonNull
    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViewBinding();
        setupEdgeToEdge();
        setupWindowInsets();
        getSupportFragmentManager().beginTransaction().replace(binding.fragment.getId(), new HomeFragment()).commit();

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment fragment;
            if (item.getItemId() == R.id.tab_home) {
                getSupportFragmentManager().beginTransaction().replace(binding.fragment.getId(), new HomeFragment()).commit();

            } else if (item.getItemId() == R.id.tab_plus) {
            } else if (item.getItemId() == R.id.tab_notif) {
            } else if (item.getItemId() == R.id.tab_user) {
            } else {
            }
            return true;
        });
    }

    private void initializeViewBinding() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
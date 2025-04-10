package com.example.androidlananh.ui.main;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;

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
import com.example.androidlananh.ui.fragments.user.UserFragment;
import com.example.androidlananh.ui.uppost.UpPostActivity;
import com.example.androidlananh.utils.SafeCallback;
import com.example.androidlananh.utils.SessionManager;
import com.example.androidlananh.utils.ShakeDetector;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class MainActivity extends BaseActivity<MainPresenter> implements MainView {
    private ActivityMainBinding binding;
    private Fragment currentFragment;
    private SensorManager sensorManager;
    private ShakeDetector shakeDetector;
    private Sensor accelerometer;

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
        initSensor();

        replaceFragment(new HomeFragment());
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.tab_home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.tab_plus) {
                startActivity(new Intent(this, UpPostActivity.class));
            } else if (item.getItemId() == R.id.tab_user) {
                replaceFragment(new UserFragment());
            } else {
            }
            return true;
        });
    }

    private void initSensor(){
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeDetector = new ShakeDetector();

        shakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake() {
                showError("Gặp sự cố liên hệ: 19006750");
            }
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

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI);

        if (currentFragment instanceof HomeFragment) {
            binding.bottomNavigation.setSelectedItemId(R.id.tab_home);
        } else if (currentFragment instanceof UserFragment) {
            binding.bottomNavigation.setSelectedItemId(R.id.tab_user);
        }
    }

    private void replaceFragment(Fragment fragment) {
        currentFragment = fragment;
        getSupportFragmentManager().beginTransaction().replace(binding.fragment.getId(), currentFragment).commit();

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(shakeDetector);
    }
}
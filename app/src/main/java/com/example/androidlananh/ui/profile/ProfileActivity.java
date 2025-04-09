package com.example.androidlananh.ui.profile;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.androidlananh.databinding.ActivityProfileBinding;
import com.example.androidlananh.model.User;
import com.example.androidlananh.ui.base.BaseActivity;
import com.example.androidlananh.utils.SessionManager;

public class ProfileActivity extends BaseActivity<ProfilePresenter> implements ProfileView {
    private ActivityProfileBinding binding;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    private Uri selectedImageUri;
    private User currentUser=SessionManager.getInstance().getCurrentUser();
    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            displayImageUri(selectedImageUri);
                        }
                    }
                }
        );
    }

    public void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }
    @NonNull
    @Override
    protected ProfilePresenter createPresenter() {
        return new ProfilePresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViewBinding();
        setupEdgeToEdge();
        setupWindowInsets();
        setupImagePicker();

        displayProfileData(currentUser);
        binding.imavatar.setOnClickListener(v -> {
            openImagePicker();
        });
        binding.btnBack.setOnClickListener(v->{
            finish();
        });
        binding.btnUpdate.setOnClickListener(v -> {
            String email = binding.edtEmail.getText().toString();
            String userName = binding.edtUserName.getText().toString();

            currentUser.setId(currentUser.getId());
            currentUser.setEmail(email);
            currentUser.setUserName(userName);
            if(selectedImageUri!=null){
                currentUser.setImageAvatar(selectedImageUri.toString());
            }

            presenter.updateProfile(currentUser);
        });
    }

    private void mockTest() {
        currentUser=SessionManager.test();
    }

    private void initializeViewBinding() {
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
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
        binding.animationView.setVisibility(VISIBLE);
        binding.btnUpdate.setEnabled(false);
        binding.btnSignOut.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        binding.animationView.setVisibility(GONE);
        binding.btnUpdate.setEnabled(true);
        binding.btnSignOut.setEnabled(true);
    }

    @Override
    public void displayProfileData(User user) {

        binding.edtUserName.setText(user.getUserName());
        binding.edtEmail.setText(user.getEmail());
        if (user.getImageAvatar()!=null && user.getImageAvatar().isEmpty() == false) {
            Glide.with(this)
                    .load(user.getImageAvatar())
                    .into(binding.imavatar);
        }
    }

    @Override
    public void displayImageUri(Uri uri) {
        binding.imavatar.setImageURI(uri);
    }

    @Override
    public void updateSuccess() {

        finish();
    }



}
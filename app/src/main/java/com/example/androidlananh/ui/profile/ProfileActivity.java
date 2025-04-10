package com.example.androidlananh.ui.profile;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.androidlananh.databinding.ActivityProfileBinding;
import com.example.androidlananh.model.User;
import com.example.androidlananh.ui.base.BaseActivity;
import com.example.androidlananh.ui.dialoginputpass.DialogInputPassword;
import com.example.androidlananh.utils.BiometricHelper;
import com.example.androidlananh.utils.SafeCallback;
import com.example.androidlananh.utils.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class ProfileActivity extends BaseActivity<ProfilePresenter> implements ProfileView, DialogInputPassword.PasswordListener {
    private ActivityProfileBinding binding;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private DialogInputPassword dialogInputPassword;

    private Uri selectedImageUri;
    private User currentUser = SessionManager.getInstance().getCurrentUser();

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
        setupDialog();
        setupBiometric();

        setupImagePicker();
        setupOnClick();

        displayProfileData(currentUser);

    }

    private void setupDialog() {
        dialogInputPassword = new DialogInputPassword();
        dialogInputPassword.setCancelable(false);


    }

    private void setupOnClick() {
        binding.imavatar.setOnClickListener(v -> {
            openImagePicker();
        });
        binding.btnBack.setOnClickListener(v -> {
            finish();
        });
        binding.btnUpdate.setOnClickListener(v -> {
            String email = binding.edtEmail.getText().toString();
            String userName = binding.edtUserName.getText().toString();

            currentUser.setId(currentUser.getId());
            currentUser.setEmail(email);
            currentUser.setUserName(userName);
            if (selectedImageUri != null) {
                currentUser.setImageAvatar(selectedImageUri.toString());
            }

            presenter.updateProfile(currentUser);
        });
    }

    private void setupBiometric() {
        if (BiometricHelper.isBiometricAvailable(this)) {
            BiometricHelper.showBiometricPrompt(this, new BiometricHelper.BiometricAuthCallback() {
                @Override
                public void onAuthenticationSuccess() {
                    showError("Xác thực vân tay thành công");
                }

                @Override
                public void onAuthenticationError(int errorCode, String errorMessage) {
                    showError("Không xác thực được vân tay");
                    dialogInputPassword.show(getSupportFragmentManager(), "password_dialog");
                }
            });
        } else {
            showError("Không xác thực được vân tay");
            dialogInputPassword.show(getSupportFragmentManager(), "password_dialog");

        }

    }

    private void mockTest() {
        currentUser = SessionManager.test();
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
        if (user.getImageAvatar() != null && user.getImageAvatar().isEmpty() == false) {
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


    @Override
    public void onPasswordEntered(String password) {
        try {
            SessionManager.getInstance().signIn(currentUser.getEmail(), password, new SafeCallback<Boolean>() {
                @Override
                protected void handleSuccess(Boolean result) {
                    dialogInputPassword.dismiss();
                }

                @Override
                protected void handleError(String error) {
                    dialogInputPassword.hideLoading();
                    showError(error);
                }
            });
        }catch (Exception e){
            dialogInputPassword.hideLoading();
            showError(e.getMessage());
        }
    }

    @Override
    public void onClose() {
        finish();
    }
}
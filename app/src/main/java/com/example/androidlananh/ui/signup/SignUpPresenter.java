package com.example.androidlananh.ui.signup;

import android.net.Uri;
import android.util.Log;

import com.example.androidlananh.ui.base.BasePresenter;
import com.example.androidlananh.utils.SessionManager;

public class SignUpPresenter extends BasePresenter<SignUpView> {

    protected SignUpPresenter(SignUpView view) {
        super(view);
    }

    public void signUp(String email, String password, String userName) {
        try {
            view.showLoading();
            if (email.isEmpty() || password.isEmpty() || userName.isEmpty()) {
                view.showError("Vui lòng nhập đầy đủ thông tin");
                return;
            }
            SessionManager.getInstance().signUp(email, password, userName, task -> {
                if (task.isSuccessful()) {
                    view.onSignUpSuccess();
                } else {
                    String errorMessage = task.getException() != null ? task.getException().getMessage() : "Lỗi khi đăng ký tài khoản";
                    view.showError(errorMessage);
                }
            });
        } catch (Exception e) {
            Log.d("signup", e.toString());
            view.showError(e.getMessage() != null ? e.getMessage() : "Đã xảy ra lỗi. Vui lòng thử lại.");
            view.hideLoading();
        }
    }


}

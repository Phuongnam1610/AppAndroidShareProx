package com.example.androidlananh.ui.signup;

import android.net.Uri;
import android.util.Log;

import com.example.androidlananh.ui.base.BasePresenter;
import com.example.androidlananh.utils.SafeCallback;
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
            SessionManager.getInstance().signUp(email, password, userName, new SafeCallback<Boolean>() {
                @Override
                protected void handleSuccess(Boolean result) {
                    view.onSignUpSuccess();
                }

                @Override
                protected void handleError(String error) {
                    view.showError(error);
                }
            });

        } catch (Exception e) {
            Log.d("signup", e.toString());
            view.showError(e.getMessage() != null ? e.getMessage() : "Đã xảy ra lỗi. Vui lòng thử lại.");
            view.hideLoading();
        }
    }


}

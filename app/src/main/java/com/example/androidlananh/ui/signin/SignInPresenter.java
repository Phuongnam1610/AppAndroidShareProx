package com.example.androidlananh.ui.signin;

import android.util.Log;

import com.example.androidlananh.ui.base.BasePresenter;
import com.example.androidlananh.utils.SafeCallback;
import com.example.androidlananh.utils.SessionManager;

public class SignInPresenter extends BasePresenter<SignInView> {
    protected SignInPresenter(SignInView view) {
        super(view);
    }

    public void signIn(String email, String password) {
        try {
            view.showLoading();
            if (email.isEmpty() || password.isEmpty()) {
                view.showError("Vui lòng nhập đầy đủ thông tin");
                return;
            }
            SessionManager.getInstance().signIn(email, password, new SafeCallback<Boolean>() {
                        @Override
                        protected void handleSuccess(Boolean result) {
                            view.onSignInSuccess();
                        }

                        @Override
                        protected void handleError(String error) {
                            view.showError(error);
                        }
                    }
            );
        } catch (Exception e) {
            Log.d("signup", e.toString());
            view.showError(e.getMessage() != null ? e.getMessage() : "Đã xảy ra lỗi. Vui lòng thử lại.");
            view.hideLoading();
        }
    }

}

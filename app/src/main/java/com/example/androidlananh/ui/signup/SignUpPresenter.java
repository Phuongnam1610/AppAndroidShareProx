package com.example.androidlananh.ui.signup;

import android.util.Log;

import com.example.androidlananh.ui.base.BasePresenter;
import com.example.androidlananh.utils.SessionManager;

public class SignUpPresenter extends BasePresenter<SignUpView> {
    private SessionManager sessionManager;

    protected SignUpPresenter(SignUpView view) {
        super(view);
        this.sessionManager=new SessionManager();
    }

    private void SignUp(String email, String password){
        try{
            view.showLoading();
            sessionManager.signIn(email,password,task -> {
                if (task.isSuccessful()) {
                    view.onSignUpSuccess();
                } else {
                    view.showError("Lỗi khi đăng ký tài khoản");
                }
            });
        }
        catch (Exception e){
            Log.d("signup",e.toString());
            view.showError("Lỗi khi đăng ký tài khoản");
            view.hideLoading();
        }
    }



}

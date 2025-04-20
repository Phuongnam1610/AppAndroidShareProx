package com.example.androidlananh.ui.profile;

import android.net.Uri;
import android.util.Log;

import com.example.androidlananh.data.ImageRepository;
import com.example.androidlananh.model.User;
import com.example.androidlananh.repository.ApiCallback;
import com.example.androidlananh.repository.UserRepository;
import com.example.androidlananh.ui.base.BasePresenter;
import com.example.androidlananh.utils.FileUtils;
import com.example.androidlananh.utils.SafeCallback;
import com.example.androidlananh.utils.SessionManager;

import java.io.File;

public class ProfilePresenter extends BasePresenter<ProfileView> {
    private UserRepository userRepository;

    protected ProfilePresenter(ProfileView view) {
        super(view);
        userRepository = new UserRepository();
    }

    public void updateProfileAndPassword(User user,String password, String rePassword) {
        try {
            view.showLoading();
            Log.d("updateuser", "ok");
            updatePassword(password, rePassword, new SafeCallback<Boolean>() {
                @Override
                protected void handleSuccess(Boolean result) {
                    updateUserInfo(user);
                }

                @Override
                protected void handleError(String error) {
                    view.showError(error);
                }
            });


        } catch (Exception e) {
            Log.e("UpdateProfileError", "Error updating profile", e);

            view.showError(e.getMessage() != null ? e.getMessage() : "Đã xảy ra lỗi. Vui lòng thử lại.");

        }
    }
    private void updateUserInfo(User user){
        File file = FileUtils.uriToFile(view.getContext(), Uri.parse(user.getImageAvatar()));
        if(file!=null) {
            ImageRepository.uploadUrlFile(file, user.getId(), new SafeCallback<String>() {

                @Override
                protected void handleSuccess(String result) {
                    user.setImageAvatar(result);
                    updateUser(user);
                }

                @Override
                protected void handleError(String error) {
                    view.showError(error);

                }
            });
        }
        else{
            updateUser(user);
        }
    }
    private void updateUser(User user){
        userRepository.updateUser(user, new SafeCallback<Boolean>() {
            @Override
            protected void handleSuccess(Boolean result) {
                SessionManager.getInstance().currentUser = user;
                view.updateSuccess();
            }

            @Override
            protected void handleError(String error) {
                view.showError(error);
            }
        });
    }

    public void updatePassword(String password, String rePassword,ApiCallback<Boolean> updateCallBack) {
        if(!password.equals(rePassword)){
            view.showError("Mật khẩu và Nhập lại mật khẩu phải giống nhau!");
            return;
        }
        SessionManager.getInstance().updatePassword(password, new SafeCallback<Boolean>() {
            @Override
            protected void handleSuccess(Boolean result) {
                updateCallBack.onSuccess(result);
            }

            @Override
            protected void handleError(String error) {
                updateCallBack.onError(error);
            }
        });

    }

}




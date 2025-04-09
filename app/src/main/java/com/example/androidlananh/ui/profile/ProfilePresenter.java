package com.example.androidlananh.ui.profile;

import android.net.Uri;
import android.util.Log;

import com.example.androidlananh.data.ImageRepository;
import com.example.androidlananh.model.User;
import com.example.androidlananh.repository.ApiCallback;
import com.example.androidlananh.repository.UserRepository;
import com.example.androidlananh.ui.base.BasePresenter;
import com.example.androidlananh.utils.FileUtils;
import com.example.androidlananh.utils.SessionManager;

import java.io.File;

public class ProfilePresenter extends BasePresenter<ProfileView> {
    private UserRepository userRepository;

    protected ProfilePresenter(ProfileView view) {
        super(view);
        userRepository = new UserRepository();
    }

    public void updateProfile(User user) {
        try {
            view.showLoading();
            Log.d("updateuser", "ok");

            File file = FileUtils.uriToFile(view.getContext(), Uri.parse(user.getImageAvatar()));
            if(file!=null) {
                ImageRepository.uploadUrlFile(file, user.getId(), new ApiCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        user.setImageAvatar(result);
                        updateUser(user);
                    }

                    @Override
                    public void onError(String error) {
                        view.showError(error);

                    }
                });
            }
            else{
                updateUser(user);
            }

        } catch (Exception e) {
            Log.e("UpdateProfileError", "Error updating profile", e);

            view.showError(e.getMessage() != null ? e.getMessage() : "Đã xảy ra lỗi. Vui lòng thử lại.");

        }
    }
    private void updateUser(User user){
        userRepository.updateUser(user, new ApiCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                SessionManager.getInstance().currentUser = user;
                view.updateSuccess();
            }

            @Override
            public void onError(String error) {
                view.showError(error);
            }
        });
    }

}




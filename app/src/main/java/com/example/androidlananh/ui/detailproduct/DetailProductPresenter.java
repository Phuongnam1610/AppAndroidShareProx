package com.example.androidlananh.ui.detailproduct;
import android.util.Log;

import com.example.androidlananh.model.Product;
import com.example.androidlananh.model.User;
import com.example.androidlananh.repository.UserRepository;
import com.example.androidlananh.ui.base.BasePresenter;
import com.example.androidlananh.utils.SafeCallback;

import java.util.ArrayList;

public class DetailProductPresenter extends BasePresenter<DetailProductView> {
    private UserRepository userRepository;
    protected DetailProductPresenter(DetailProductView view) {
        super(view);
        userRepository=new UserRepository();
    }


    public void getUserInfo(String userId) {
        try {
            view.showLoading();
            userRepository.getUserById(userId,new SafeCallback<User>() {
                @Override
                public void handleSuccess(User user) {
                    view.displayUser(user);
                }

                @Override
                public void handleError(String error) {
                    view.showError(error);

                }
            });

        } catch (Exception e) {
            Log.e("UpdateProfileError", "Error updating profile", e);

            view.showError(e.getMessage() != null ? e.getMessage() : "Đã xảy ra lỗi. Vui lòng thử lại.");

        }

    }
}

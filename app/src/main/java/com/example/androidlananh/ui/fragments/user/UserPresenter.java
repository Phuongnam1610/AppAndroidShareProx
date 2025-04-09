package com.example.androidlananh.ui.fragments.user;
import android.util.Log;

import com.example.androidlananh.model.Product;
import com.example.androidlananh.repository.ProductRepository;
import com.example.androidlananh.ui.base.BasePresenter;
import com.example.androidlananh.utils.SafeCallback;
import com.example.androidlananh.utils.SessionManager;

import java.util.ArrayList;

public class UserPresenter extends BasePresenter<UserView> {
    private ProductRepository productRepository;
    protected UserPresenter(UserView view) {
        super(view);
        productRepository=new ProductRepository();
    }

    public void signOut(){
        SessionManager.getInstance().signOut();
        view.navigateToSignInActivity();
    }

    public void getAllProductByAuthorId(String authorId) {
        try {
            view.showLoading();
            productRepository.getAllProductByAuthorId(authorId,new SafeCallback<ArrayList<Product>>() {
                @Override
                public void handleSuccess(ArrayList<Product> products) {
                    view.displayAllProduct(products);
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

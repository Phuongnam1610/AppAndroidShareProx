package com.example.androidlananh.ui.fragments.home;

import android.net.Uri;
import android.util.Log;

import com.example.androidlananh.data.ImageRepository;
import com.example.androidlananh.model.Category;
import com.example.androidlananh.model.Product;
import com.example.androidlananh.repository.ApiCallback;
import com.example.androidlananh.repository.CategoryRepository;
import com.example.androidlananh.repository.ProductRepository;
import com.example.androidlananh.ui.base.BasePresenter;
import com.example.androidlananh.utils.FileUtils;
import com.example.androidlananh.utils.SafeCallback;
import com.example.androidlananh.utils.SessionManager;

import java.io.File;
import java.util.ArrayList;

public class HomePresenter extends BasePresenter<HomeView> {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    protected HomePresenter(HomeView view) {
        super(view);
        productRepository = new ProductRepository();
        categoryRepository= new CategoryRepository();

    }

    public void getAllProduct() {
        try {
            view.showLoading();
            productRepository.getAllProduct(new SafeCallback<ArrayList<Product>>() {
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
            Log.e("Home", "Error ", e);

            view.showError(e.getMessage() != null ? e.getMessage() : "Đã xảy ra lỗi. Vui lòng thử lại.");

        }
    }

    public void getAllCategory() {
        try {
            view.showLoading();
            categoryRepository.getAllCategory(new SafeCallback<ArrayList<Category>>() {
                @Override
                public void handleSuccess(ArrayList<Category> categories) {
                    view.displayAllCategory(categories);
                }

                @Override
                public void handleError(String error) {
                    view.showError(error);

                }
            });

        } catch (Exception e) {
            Log.e("Home", "Error ", e);

            view.showError(e.getMessage() != null ? e.getMessage() : "Đã xảy ra lỗi. Vui lòng thử lại.");

        }
    }
}

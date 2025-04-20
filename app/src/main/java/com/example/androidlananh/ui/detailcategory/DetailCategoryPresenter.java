package com.example.androidlananh.ui.detailcategory;
import android.util.Log;

import com.example.androidlananh.model.Product;
import com.example.androidlananh.model.User;
import com.example.androidlananh.repository.ProductRepository;
import com.example.androidlananh.repository.UserRepository;
import com.example.androidlananh.ui.base.BasePresenter;
import com.example.androidlananh.utils.SafeCallback;

import java.util.ArrayList;

public class DetailCategoryPresenter extends BasePresenter<DetailCategoryView> {
    private ProductRepository productRepository;
    protected DetailCategoryPresenter(DetailCategoryView view) {
        super(view);
        productRepository=new ProductRepository();
    }

    public void getAllProductByCategoryId(String categoryId) {
        try {
            view.showLoading();
            productRepository.getAllProductByCategoryid(categoryId,new SafeCallback<ArrayList<Product>>() {
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

}

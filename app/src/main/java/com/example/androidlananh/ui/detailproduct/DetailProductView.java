package com.example.androidlananh.ui.detailproduct;
import com.example.androidlananh.model.Product;
import com.example.androidlananh.model.User;
import com.example.androidlananh.ui.base.BaseView;


public interface DetailProductView extends BaseView {
    void displayProduct(Product product);
    void displayUser(User user);
}

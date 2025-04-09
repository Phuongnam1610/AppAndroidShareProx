package com.example.androidlananh.ui.fragments.home;
import com.example.androidlananh.adapter.ProductAdapterView;
import com.example.androidlananh.model.Product;
import com.example.androidlananh.model.User;
import com.example.androidlananh.ui.base.BaseView;


public interface HomeView extends ProductAdapterView {
    void onClickProduct(Product product);
}

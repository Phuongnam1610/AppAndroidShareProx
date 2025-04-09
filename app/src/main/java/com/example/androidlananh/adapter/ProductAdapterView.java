package com.example.androidlananh.adapter;

import com.example.androidlananh.model.Product;
import com.example.androidlananh.ui.base.BaseView;

import java.util.ArrayList;

public interface ProductAdapterView extends BaseView {
    void onClickProduct(Product product);
    void displayAllProduct(ArrayList<Product> products);
    
}

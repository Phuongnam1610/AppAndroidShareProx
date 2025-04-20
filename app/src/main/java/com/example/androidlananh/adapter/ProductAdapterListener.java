package com.example.androidlananh.adapter;

import com.example.androidlananh.model.Product;
import com.example.androidlananh.model.Product;

import java.util.ArrayList;

public interface ProductListener {
    void onClickProduct(Product product);
    void displayAllProduct(ArrayList<Product> products);

}
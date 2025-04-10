
package com.example.androidlananh.ui.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.androidlananh.ui.nearproduct.NearProductMapActivity;
import com.example.androidlananh.adapter.ProductAdapter;
import com.example.androidlananh.databinding.FragmentHomeBinding;
import com.example.androidlananh.model.Product;
import com.example.androidlananh.ui.base.BaseFragment;
import com.example.androidlananh.ui.detailproduct.DetailProductActivity;
import com.example.androidlananh.ui.uppost.UpPostActivity;
import com.example.androidlananh.utils.Constant;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment<HomePresenter> implements HomeView {
    private FragmentHomeBinding binding;
    private ProductAdapter productAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater);
        productAdapter = new ProductAdapter(this);
        binding.rcvPost.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rcvPost.setAdapter(productAdapter);
        setupOnClick();
        return binding.getRoot();
    }
    private void setupOnClick(){
        presenter.getAllProduct();
        binding.btnReceive.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), UpPostActivity.class).putExtra(Constant.TYPE_PASS_KEY, Constant.TYPE_RECEIVE));
        });
        binding.btnShare.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), UpPostActivity.class).putExtra(Constant.TYPE_PASS_KEY,Constant.TYPE_SHARE));
        });
        binding.btnRecent.setOnClickListener(v->{
            startActivity(new Intent(getContext(), NearProductMapActivity.class));

        });
        binding.edtFind.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String searchText = s.toString().toLowerCase().trim();
                productAdapter.filterList(searchText);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString().toLowerCase().trim();
                productAdapter.filterList(searchText);
            }
        });
    }
    @NonNull
    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onClickProduct(Product product) {
        Intent i = new Intent(getContext(), DetailProductActivity.class);
        i.putExtra(Constant.PRODUCT_PASS_KEY,product);
        startActivity(i);
    }

    @Override
    public void displayAllProduct(ArrayList<Product> products) {
        productAdapter.loadAllProduct(products);
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
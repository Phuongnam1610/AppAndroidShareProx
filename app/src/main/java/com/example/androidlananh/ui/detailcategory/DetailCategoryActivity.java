package com.example.androidlananh.ui.detailcategory;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidlananh.adapter.CategoryAdapter;
import com.example.androidlananh.adapter.ProductAdapter;
import com.example.androidlananh.databinding.ActivityDetailCategoryBinding;
import com.example.androidlananh.databinding.ActivityDetailProductBinding;
import com.example.androidlananh.model.Category;
import com.example.androidlananh.model.Product;
import com.example.androidlananh.model.User;
import com.example.androidlananh.ui.base.BaseActivity;
import com.example.androidlananh.ui.detailproduct.DetailProductActivity;
import com.example.androidlananh.utils.Constant;

import java.util.ArrayList;

public class DetailCategoryActivity extends BaseActivity<DetailCategoryPresenter> implements DetailCategoryView {
    private ActivityDetailCategoryBinding binding;
    private ProductAdapter productAdapter;
    private Category category;
    @NonNull
    @Override
    protected DetailCategoryPresenter createPresenter() {
        return new DetailCategoryPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViewBinding();
        setupEdgeToEdge();
        setupWindowInsets();
        category=(Category) getIntent().getSerializableExtra(Constant.CATEGORY_PASS_KEY);
        productAdapter = new ProductAdapter(this);
        binding.rcvPost.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rcvPost.setAdapter(productAdapter);
        binding.tvCategory.setText(category.getTitle());
        presenter.getAllProductByCategoryId(category.getId());
        binding.btnBack.setOnClickListener(v->finish());
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

    private void initializeViewBinding() {
        binding = ActivityDetailCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void setupEdgeToEdge() {
        EdgeToEdge.enable(this);
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
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
    public void onLongClickProduct(Product product) {

    }
}
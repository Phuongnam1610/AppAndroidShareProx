package com.example.androidlananh.ui.detailproduct;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.androidlananh.R;
import com.example.androidlananh.adapter.ProductAdapter;
import com.example.androidlananh.databinding.ActivityDetailProductBinding;
import com.example.androidlananh.databinding.ActivityDetailProductBinding;
import com.example.androidlananh.databinding.FragmentHomeBinding;
import com.example.androidlananh.model.Product;
import com.example.androidlananh.model.User;
import com.example.androidlananh.ui.base.BaseActivity;
import com.example.androidlananh.ui.base.BaseFragment;
import com.example.androidlananh.ui.fragments.home.HomeFragment;
import com.example.androidlananh.ui.fragments.user.UserFragment;
import com.example.androidlananh.utils.Constant;

public class DetailProductActivity extends BaseActivity<DetailProductPresenter> implements DetailProductView {
    private ActivityDetailProductBinding binding;
    private Product product;

    @NonNull
    @Override
    protected DetailProductPresenter createPresenter() {
        return new DetailProductPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        product=(Product) getIntent().getSerializableExtra(Constant.PRODUCT_PASS_KEY);
        initializeViewBinding();
        setupEdgeToEdge();
        setupWindowInsets();
        binding.btnBack.setOnClickListener(v->finish());
        displayProduct(product);
        presenter.getUserInfo(product.getAuthorID());
    }

    private void initializeViewBinding() {
        binding = ActivityDetailProductBinding.inflate(getLayoutInflater());
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
    public void displayProduct(Product product) {
        binding.tvName.setText(product.getName());
        binding.tvDescription.setText(product.getDescription());
        if(product.getImage()!=null||!product.getImage().isEmpty()){
            Glide.with(this).load(product.getImage()).into(binding.imvProduct);
        }

    }

    @Override
    public void displayUser(User user) {
        binding.tvNameUser.setText(user.getUserName());
        if(user.getImageAvatar()!=null||!user.getImageAvatar().isEmpty()){
            Glide.with(this).load(user.getImageAvatar()).into(binding.imvAvatarUser);
        }
    }
}
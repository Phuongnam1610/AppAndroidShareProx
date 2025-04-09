
package com.example.androidlananh.ui.fragments.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.androidlananh.adapter.ProductAdapter;
import com.example.androidlananh.databinding.FragmentUserBinding;
import com.example.androidlananh.model.Product;
import com.example.androidlananh.model.User;
import com.example.androidlananh.ui.base.BaseFragment;
import com.example.androidlananh.ui.detailproduct.DetailProductActivity;
import com.example.androidlananh.ui.profile.ProfileActivity;
import com.example.androidlananh.ui.signin.SignInActivity;
import com.example.androidlananh.utils.Constant;
import com.example.androidlananh.utils.SessionManager;

import java.util.ArrayList;

public class UserFragment extends BaseFragment<UserPresenter> implements UserView {
    private FragmentUserBinding binding;
    private ProductAdapter productAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private User currentUser = SessionManager.getInstance().getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater);
        productAdapter = new ProductAdapter(this);
        binding.rcvPost.setAdapter(productAdapter);
        binding.rcvPost.setLayoutManager(new GridLayoutManager(getContext(), 2));
        setupOnClick();
        displayUserData(currentUser);
        presenter.getAllProductByAuthorId(currentUser.getId());
        return binding.getRoot();

    }
    private void setupOnClick(){
        binding.btnlogout.setOnClickListener(v -> {
            presenter.signOut();
        });
        binding.btnsetting.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), ProfileActivity.class));
        });
    }

    @NonNull
    @Override
    protected UserPresenter createPresenter() {
        return new UserPresenter(this);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void navigateToSignInActivity() {
        getActivity().finish();
        startActivity(new Intent(getContext(), SignInActivity.class));
    }

    @Override
    public void displayUserData(User user) {
        binding.tvuser.setText(user.getUserName());
        if (user.getImageAvatar()!=null && user.getImageAvatar().isEmpty() == false) {
            Glide.with(this)
                    .load(user.getImageAvatar())
                    .into(binding.imguser);        }
    }

    @Override
    public void onResume() {
        super.onResume();
        displayUserData(currentUser);
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

}
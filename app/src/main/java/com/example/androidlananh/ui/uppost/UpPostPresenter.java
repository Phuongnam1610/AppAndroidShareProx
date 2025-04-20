package com.example.androidlananh.ui.uppost;

import android.net.Uri;
import android.util.Log;

import com.example.androidlananh.data.ImageRepository;
import com.example.androidlananh.model.Category;
import com.example.androidlananh.model.Product;
import com.example.androidlananh.repository.ApiCallback;
import com.example.androidlananh.repository.CategoryRepository;
import com.example.androidlananh.repository.ProductRepository;
import com.example.androidlananh.ui.base.BasePresenter;
import com.example.androidlananh.ui.profile.ProfileActivity;
import com.example.androidlananh.utils.Constant;
import com.example.androidlananh.utils.FileUtils;
import com.example.androidlananh.utils.SafeCallback;
import com.example.androidlananh.utils.SessionManager;

import java.io.File;
import java.util.ArrayList;

public class UpPostPresenter extends BasePresenter<UpPostView> {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    protected UpPostPresenter(UpPostView view) {
        super(view);
        productRepository = new ProductRepository();
        categoryRepository = new CategoryRepository();

    }

    public void upPost(Product product) {
    }

    public void addProduct(Product product) {
        try {
            view.showLoading();

            if (product.getDescription().isEmpty() || product.getName().isEmpty() || product.getType().isEmpty() || product.getCount() == -1 || product.getUnit().isEmpty() || product.getCategoryId().isEmpty()) {
                view.showError("Vui lòng điền đầy đủ");
                return;
            }
            if (product.getType().equals(Constant.TYPE_SHARE)) {
                if (product.getImage().isEmpty()) {
                    view.showError("Vui lòng chọn ảnh");
                    return;
                }
            } else {
                if (product.getReason().isEmpty()) {
                    view.showError("Vui lòng chọn lí do");
                    return;
                }
            }
            if (product.getLocation().getAddress().isEmpty()) {
                view.showError("Vui lòng chọn địa chỉ");
                return;
            }
            productRepository.addProduct(product.toMap(), new SafeCallback<String>() {
                @Override
                public void handleSuccess(String productId) {
                    if (product.getType().equals(Constant.TYPE_SHARE)) {
                        File file = FileUtils.uriToFile(view.getContext(), Uri.parse(product.getImage()));
                        ImageRepository.uploadUrlFile(file, productId, new SafeCallback<String>() {
                            @Override
                            public void handleSuccess(String url) {
                                product.setImage(url);
                                productRepository.updateProduct(product.toMap(), new SafeCallback<Boolean>() {
                                    @Override
                                    protected void handleSuccess(Boolean result) {
                                        view.addProductSuccess();
                                    }

                                    @Override
                                    protected void handleError(String error) {
                                        view.showError(error);
                                    }
                                });
                            }

                            @Override
                            public void handleError(String error) {
                                view.showError(error);

                            }
                        });
                    }
                    else{
                        view.addProductSuccess();

                    }
                }

                @Override
                public void handleError(String error) {
                    view.showError(error);
                }
            });


        } catch (Exception e) {
            Log.e("UpdateProfileError", "Error updating profile", e);

            view.showError(e.getMessage() != null ? e.getMessage() : "Đã xảy ra lỗi. Vui lòng thử lại.");

        }
    }


    public void getAllCategory() {
        try {
            view.showLoading();
            categoryRepository.getAllCategory(new SafeCallback<ArrayList<Category>>() {
                @Override
                public void handleSuccess(ArrayList<Category> categories) {
                    view.getAllCategories(categories);
                    view.hideLoading();
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

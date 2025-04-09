package com.example.androidlananh.ui.uppost;

import android.net.Uri;
import android.util.Log;

import com.example.androidlananh.data.ImageRepository;
import com.example.androidlananh.model.Product;
import com.example.androidlananh.repository.ApiCallback;
import com.example.androidlananh.repository.ProductRepository;
import com.example.androidlananh.ui.base.BasePresenter;
import com.example.androidlananh.ui.profile.ProfileActivity;
import com.example.androidlananh.utils.FileUtils;
import com.example.androidlananh.utils.SafeCallback;
import com.example.androidlananh.utils.SessionManager;

import java.io.File;

public class UpPostPresenter extends BasePresenter<UpPostView> {
    private ProductRepository productRepository;
    protected UpPostPresenter(UpPostView view) {
        super(view);
        productRepository = new ProductRepository();

    }
    public void upPost(Product product){}
    public void addProduct(Product product){
        try {
            view.showLoading();
            if(product.getImage().isEmpty()||product.getDescription().isEmpty()||product.getName().isEmpty()||product.getType().isEmpty()||product.getCount()==-1||product.getUnit().isEmpty())
            {
                view.showError("Vui lòng điền đầy đủ");
                return;
            }
            productRepository.addProduct(product, new SafeCallback<String>() {
                @Override
                public void handleSuccess(String productId) {
                    product.setId(productId);
                    File file = FileUtils.uriToFile(view.getContext(), Uri.parse(product.getImage()));
                    ImageRepository.uploadUrlFile(file, productId,new SafeCallback<String>() {
                        @Override
                        public void handleSuccess(String result) {
                            product.setImage(result);
                            productRepository.updateProduct(product, new SafeCallback<Boolean>() {
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


}

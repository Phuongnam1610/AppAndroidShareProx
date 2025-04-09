package com.example.androidlananh.repository;

import com.example.androidlananh.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ProductRepository {
    private FirebaseFirestore db;

    public ProductRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void getProductById(String postId, final ApiCallback<Product> listener) {
        db.collection("Product").document(postId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Product post = document.toObject(Product.class);
                                listener.onSuccess(post);
                            } else {
                                listener.onError("Không tìm thấy Product");
                            }
                        } else {
                            listener.onError(task.getException().getMessage());
                        }
                    }
                });
    }

    public void updateProduct(Product product, ApiCallback<Boolean> listener) {
        db.collection("Product").document(product.getId())
                .set(product).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        listener.onSuccess(true);
                    }else {
                        listener.onError(task.getException().getMessage());
                    }
                });
    }

    public void addProduct(Product product, final ApiCallback<String> listener) {
        db.collection("Product")
                .add(product)
                .addOnSuccessListener(documentReference -> {
                    String productId = documentReference.getId();
                    listener.onSuccess(productId);
                })
                .addOnFailureListener(e -> {
                    listener.onError(e.getMessage());
                });
    }

    public void getAllProduct(final ApiCallback<ArrayList<Product>> listener) {
        db.collection("Product")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Product> products = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Product product = document.toObject(Product.class);
                        if (product != null) {
                            product.setId(document.getId());
                            products.add(product);
                        }
                    }
                    listener.onSuccess(products);
                })
                .addOnFailureListener(e -> {
                    listener.onError(e.getMessage());
                });
    }
    public void getAllProductByAuthorId(String authorId, final ApiCallback<ArrayList<Product>> listener) {
        db.collection("Product")
                .whereEqualTo("authorID", authorId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Product> products = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Product product = document.toObject(Product.class);
                        if (product != null) {
                            product.setId(document.getId());
                            products.add(product);
                        }
                    }
                    listener.onSuccess(products);
                })
                .addOnFailureListener(e -> {
                    listener.onError(e.getMessage());
                });
    }
}
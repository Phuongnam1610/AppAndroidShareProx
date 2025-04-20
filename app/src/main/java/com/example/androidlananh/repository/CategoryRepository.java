package com.example.androidlananh.repository;

import androidx.annotation.NonNull;

import com.example.androidlananh.model.Category;
import com.example.androidlananh.model.Category;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CategoryRepository {
    private FirebaseFirestore db;

    public CategoryRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void getProductById(String postId, final ApiCallback<Category> listener) {
        db.collection("Category").document(postId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Category post = document.toObject(Category.class);
                                listener.onSuccess(post);
                            } else {
                                listener.onError("Không tìm thấy Category");
                            }
                        } else {
                            listener.onError(task.getException().getMessage());
                        }
                    }
                });
    }

    public void updateProduct(Map<String, Object> product, ApiCallback<Boolean> listener) {
        db.collection("Category").document(product.get("id").toString())
                .set(product).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess(true);
                    } else {
                        listener.onError(task.getException().getMessage());
                    }
                });
    }

    public void addProduct(Map<String, Object> product, final ApiCallback<String> listener) {
        db.collection("Category")
                .add(product)
                .addOnSuccessListener(documentReference -> {
                    String productId = documentReference.getId();
                    listener.onSuccess(productId);
                })
                .addOnFailureListener(e -> {
                    listener.onError(e.getMessage());
                });
    }

    public void getAllCategory(final ApiCallback<ArrayList<Category>> listener) {
        db.collection("Category")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Category> categories = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Category category = document.toObject(Category.class);
                        if (category != null) {
                            category.setId(document.getId());
                            categories.add(category);
                        }
                    }
                    Collections.sort(categories, (a, b) -> {
                        if (a.getId().equals("khongro") && !b.getId().equals("khongro")) {
                            return 1; // a xuống sau b
                        } else if (!a.getId().equals("khongro") && b.getId().equals("khongro")) {
                            return -1; // a lên trước b
                        } else {
                            return 0; // giữ nguyên vị trí nếu giống nhau
                        }
                    });
                    listener.onSuccess(categories);
                })
                .addOnFailureListener(e -> {
                    listener.onError(e.getMessage());
                });
    }

    public void getAllProductByAuthorId(String authorId, final ApiCallback<ArrayList<Category>> listener) {
        db.collection("Category")
                .whereEqualTo("authorID", authorId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Category> products = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Category product = document.toObject(Category.class);
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
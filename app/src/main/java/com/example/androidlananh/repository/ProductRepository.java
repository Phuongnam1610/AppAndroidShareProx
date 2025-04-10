package com.example.androidlananh.repository;

import androidx.annotation.NonNull;

import com.example.androidlananh.model.Product;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    public void updateProduct(Map<String, Object> product, ApiCallback<Boolean> listener) {
        db.collection("Product").document(product.get("id").toString())
                .set(product).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess(true);
                    } else {
                        listener.onError(task.getException().getMessage());
                    }
                });
    }

    public void addProduct(Map<String, Object> product, final ApiCallback<String> listener) {
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
                        Product product = Product.fromDocument(document);
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

    public void getNearbyProducts(double latitude, double longitude, double radiusInKm, final ApiCallback<ArrayList<Product>> listener) {
        final double radiusInM = 50 * 1000;
        final GeoLocation center = new GeoLocation(latitude, longitude);

        List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInM);
        final List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (GeoQueryBounds b : bounds) {
            Query q = db.collection("Product")
                    .orderBy("location.point")
                    .startAt(b.startHash)
                    .endAt(b.endHash);

            tasks.add(q.get());
        }
        // Collect all the query results together into a single list
        Tasks.whenAllComplete(tasks)
                .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Task<?>>> t) {
                        List<DocumentSnapshot> matchingDocs = new ArrayList<>();

                        for (Task<QuerySnapshot> task : tasks) {
                            QuerySnapshot snap = task.getResult();
                            for (DocumentSnapshot doc : snap.getDocuments()) {
                                double lat = doc.getDouble("lat");
                                double lng = doc.getDouble("lng");
                                GeoLocation docLocation = new GeoLocation(lat, lng);
                                double distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center);
                                if (distanceInM <= radiusInM) {
                                    matchingDocs.add(doc);
                                }
                            }
                        }

                    }
                });
    }


}
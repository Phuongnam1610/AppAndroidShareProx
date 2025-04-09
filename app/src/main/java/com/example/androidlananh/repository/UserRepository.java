package com.example.androidlananh.repository;

import com.example.androidlananh.model.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class UserRepository {
    private FirebaseFirestore db;

    public UserRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void getUserById(String userId, final ApiCallback<User> listener) {
        db.collection("User").document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                User user = document.toObject(User.class);
                                listener.onSuccess(user);
                            } else {
                                listener.onError("Không tìm thấy User");
                            }
                        } else {
                            listener.onError(task.getException().getMessage());
                        }
                    }
                });
    }

    public void updateUser(User user, ApiCallback<Boolean> listener) {


        db.collection("User").document(user.getId())
                .set(user).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        listener.onSuccess(true);
                    }else {
                        listener.onError(task.getException().getMessage());
                    }
                });
    }

    public void addUser(User user, final ApiCallback<Boolean> listener) {


        db.collection("User").document(user.getId())
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            listener.onSuccess(true);
                        } else {
                            listener.onError(task.getException().getMessage());
                        }
                    }
                });
    }
}
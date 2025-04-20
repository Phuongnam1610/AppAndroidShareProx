package com.example.androidlananh.utils;

import androidx.annotation.NonNull;

import com.example.androidlananh.model.User;
import com.example.androidlananh.repository.ApiCallback;
import com.example.androidlananh.repository.UserRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SessionManager {
    private static SessionManager instance;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    public User currentUser;
    private UserRepository userRepository;

    private SessionManager() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userRepository = new UserRepository();
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void signIn(String email, String password, ApiCallback<Boolean> listener) {

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userRepository.getUserById(getAuthUser().getUid(), new ApiCallback<User>() {
                            @Override
                            public void onSuccess(User result) {
                                currentUser = result;
                                listener.onSuccess(true);
                            }

                            @Override
                            public void onError(String error) {
                                listener.onError(error);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onError(e.getMessage());
                    }
                });

    }

    public void signUp(String email, String password, String userName, ApiCallback<Boolean> listener) {

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User user = new User(getAuthUser().getUid(), userName, email);
                        currentUser = user;
                        db.collection("User").document(getAuthUser().getUid())
                                .set(user);
                    }
                    listener.onSuccess(true);

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onError(e.getMessage());
                    }
                });
    }

    public void updatePassword(String newPassword,ApiCallback<Boolean> listener){
        getAuthUser().updatePassword(newPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.onSuccess(true);

            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onError(e.getMessage());
            }
        });
    }

    public void signOut() {
        auth.signOut();
        currentUser = null;
    }

    public FirebaseUser getAuthUser() {
        return auth.getCurrentUser();
    }

    public boolean isUserLoggedIn() {
        return getAuthUser() != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public static User test() {
        return new User("i2BXtnkRKcNfyisWSR13Y4vh9S93", "nam", "nam@gmail.com");
    }
}
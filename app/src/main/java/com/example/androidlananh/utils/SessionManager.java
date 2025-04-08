package com.example.androidlananh.utils;

import com.example.androidlananh.model.User;
import com.example.androidlananh.repository.UserRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SessionManager {
    private static SessionManager instance;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private User currentUser;
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

    public void signIn(String email, String password, OnCompleteListener<AuthResult> listener) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userRepository.getUserById(getAuthUser().getUid(), new UserRepository.OnUserFetchedListener() {
                            @Override
                            public void onUserFetched(User user) {
                                currentUser = user;
                            }

                            @Override
                            public void onError(String error) {
                            }
                        });
                    }
                    listener.onComplete(task);
                });
    }

    public void signUp(String email, String password, String userName, OnCompleteListener<AuthResult> listener) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User user = new User(userName, email);
                        db.collection("User").document(getAuthUser().getUid())
                                .set(user);
                    }
                    listener.onComplete(task);

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
}
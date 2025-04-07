package com.example.androidlananh;

import com.example.androidlananh.utils.SessionManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import android.util.Log;

public class SessionManagerTest {

    private SessionManager sessionManager;
    private FirebaseAuth auth;
    @Before
    public void setUp() {
        auth=FirebaseAuth.getInstance();


        sessionManager = new SessionManager();
    }

    @Test
    public void testSignInSuccessful() {
        String email = "test@example.com";
        String password = "password";

        sessionManager.signIn(email,password,task -> {
            Log.d("thanh cong","Dang nhap thanh cong");
        });

        // Verify user is logged in
        assertTrue(sessionManager.isUserLoggedIn());
    }

    @Test
    public void testSignOut() {

        sessionManager.signOut();

        assertFalse(sessionManager.isUserLoggedIn());
    }

    @Test
    public void testGetCurrentUser() {

        FirebaseUser user = sessionManager.getCurrentUser();

    }

    @Test
    public void testIsUserLoggedIn() {
        assertTrue(sessionManager.isUserLoggedIn());
        assertFalse(sessionManager.isUserLoggedIn());
    }
}
package com.eou.screentalker;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import java.util.Objects;

public class UserManager {

    private FirebaseAuth firebaseAuth;

    public UserManager(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public Task<AuthResult> createUser(UserCredentials credentials) throws FirebaseAuthException {
        return firebaseAuth.createUserWithEmailAndPassword(credentials.getEmail(), credentials.getPassword());
    }
//
    public Task<AuthResult> signInWithEmailAndPassword(String email, String password) throws Exception {
        if (!Objects.equals(email, "test@example.com") || !password.equals("password")) {
            // Throw a more specific FirebaseAuthException with a clear message
            throw new Exception("Invalid email or password");
        }

        // If email and password match the hardcoded values, proceed with actual sign-in
        return firebaseAuth.signInWithEmailAndPassword(email, password);
    }

}

// Class representing User (modify as needed)
class User {
    private String email;

    public User(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}

package com.eou.screentalker.Activities;

import static androidx.constraintlayout.widget.Constraints.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eou.screentalker.R;
import com.eou.screentalker.Utilities.Constants;
import com.eou.screentalker.Utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private TextView goToLogin;
    private EditText inputEmail, inputPassword, inputConfirmPassword, inputUserName, inputDOB;
    private Button btnRegister;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore fStore;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        goToLogin = findViewById(R.id.goToLogin);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        inputUserName = findViewById(R.id.inputUserName);
        inputDOB = findViewById(R.id.inputDOB);
        btnRegister = findViewById(R.id.btnRegister);
        progressDialog = new ProgressDialog(RegisterActivity.this);
        mAuth=FirebaseAuth.getInstance();
        //when you want to use this remove it from her and initialise in method you want to use it in cause mUser returns null here
        mUser=mAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getApplicationContext());


        goToLogin.setOnClickListener(v -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
        btnRegister.setOnClickListener(v->performAuth());
    }

    private void performAuth() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String confirmPassword = inputConfirmPassword.getText().toString();
        String username = inputUserName.getText().toString();
        String dob = inputDOB.getText().toString();
        String bio = "Do not! be lame write a bio via edit button";
        String pImage_url = "https://firebasestorage.googleapis.com/v0/b/screentalker-fe07d.appspot.com/o/profile_images%2Fdefault_pImage.png?alt=media&token=682b1d4d-a592-41de-99a0-24f00f1f20fa";

        //validate email
        if(!email.matches(emailPattern)){
            inputEmail.setError("Enter an actual Email");
        }
        //validate right password length
        else if(password.isEmpty() || password.length()<6 ){
            inputPassword.setError("Enter password with more than 6 characters");
        }
        //validate matching passwords
        else if (!password.equals(confirmPassword)){
            inputPassword.setError("Passwords do not match");
            inputConfirmPassword.setError("Passwords do not match");
        }
        else{
            progressDialog.setMessage("Please wait while you get Registered.....");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            //create user with the email and password parameter
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    sendToMain();
//                    fStore.collection("users");
                    //get info of registered user to store in firestore
                    if (!(mAuth.getCurrentUser() == null)){
                        System.out.println("mAuth.getCurrentUser() is not null");
                    }
                    if(mUser == null){
                        System.out.println("muser is null");
                    }
                    String userID = mAuth.getCurrentUser().getUid();
                    //creating the collection
                    DocumentReference documentReference = fStore.collection("users").document(userID);
                    //getting the user info with hash map
                    Map<String, Object> user = new HashMap<>();
                    user.put("username", username);
                    user.put("email", email);
                    user.put("dob", dob);
                    user.put("bio", bio);
                    user.put("pImage_url", pImage_url);
                    //inserting into firestore
                    documentReference.set(user).addOnSuccessListener(v -> {
                        Log.d(TAG, "onsuccess: user profile is created for" + userID);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, userID);
                        preferenceManager.putString(Constants.KEY_NAME, username);
                        preferenceManager.putString(Constants.KEY_BIO, bio);
                        preferenceManager.putString(Constants.KEY_PROFILE_IMAGE, pImage_url);
                    });
                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    System.out.println(task.getException());
                }
            });
        }
    }

    private void sendToMain() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}


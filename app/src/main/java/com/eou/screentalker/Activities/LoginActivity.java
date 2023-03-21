package com.eou.screentalker.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eou.screentalker.R;
import com.eou.screentalker.Utilities.Constants;
import com.eou.screentalker.Utilities.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private TextView register;
    private TextView goToLogin;
    private EditText inputEmail, inputPassword;
    private Button btnLogin;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        register = findViewById(R.id.createAccount);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressDialog = new ProgressDialog(LoginActivity.this);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        preferenceManager = new PreferenceManager(getApplicationContext());
//        keeps user in once signed in
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        btnLogin.setOnClickListener(v->performAuth());
        register.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void performAuth() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        //validate email
        if(!email.matches(emailPattern)){
            inputEmail.setError("Enter an actual Email");
        }
        //validate right password length
        else if(password.isEmpty() || password.length()<6 ){
            inputPassword.setError("Enter password with more than 6 characters");
        }
        else{
            progressDialog.setMessage("Please wait while you get Logged in.....");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            //create user with the email and password parameter
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
                    fStore.collection("users").whereEqualTo(FieldPath.documentId(), mAuth.getCurrentUser().getUid()).get()
                                    .addOnCompleteListener(getData_task -> {
                                        if (getData_task.isSuccessful() && getData_task != null && getData_task.getResult().getDocuments().size()>0){
                                            DocumentSnapshot documentSnapshot = getData_task.getResult().getDocuments().get(0);
                                            preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                                            preferenceManager.putString(Constants.KEY_USER_ID, mAuth.getCurrentUser().getUid());
                                            preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString("username"));
                                            preferenceManager.putString(Constants.KEY_BIO, documentSnapshot.getString("bio"));
                                            preferenceManager.putString(Constants.KEY_PROFILE_IMAGE, documentSnapshot.getString("pImage_url"));
                                            System.out.println("First check: " + documentSnapshot.getString("pImage_url"));
                                            sendToMain();
                                        }
                                    });

                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    System.out.println(task.getException());
                }
            });
        }
    }
    private void sendToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

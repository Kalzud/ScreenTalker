package com.eou.screentalker.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.eou.screentalker.Firebase.MessagingService;
import com.eou.screentalker.R;
import com.eou.screentalker.Utilities.Constants;
import com.eou.screentalker.Utilities.PreferenceManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.messaging.FirebaseMessaging;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.HashMap;
import java.util.Set;

/**
 * Author: Emmanuel O. Uduma
 *
 * This class and would host the main activities all the
 * other fragment would be hosted here
 */
public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String userID;
    private ImageButton logoutBtn;
    private FirebaseFirestore fStore;
    private DocumentReference documentReference;
    private ListenerRegistration listenerRegistration;
    private PreferenceManager preferenceManager;
    private TextView nameTextView;
    private RoundedImageView pImage;
    private static final int EditprofileActivity_REQUEST_CODE = 1;
    private MessagingService m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialising firebase database stuff
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(MainActivity.this);
        userID = mAuth.getCurrentUser().getUid();
        documentReference = fStore.collection("users").document(userID);
        getToken();

//        initialise menu layout and action after click on menu bars
        final DrawerLayout drawerLayout = findViewById(R.id.drawLayout);
        findViewById(R.id.imageMenu).setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        //initialise navigation view and nav controller
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);
        navigationView.setItemIconSize(100);
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navigationView, navController);

// Get a reference to the header view
        View headerView = navigationView.getHeaderView(0);
// Get a reference to the TextView in the header view
        nameTextView = headerView.findViewById(R.id.displayName);
        // Get a reference to the ImageView in the header view
        pImage = headerView.findViewById(R.id.imageProfile);

       setUserHeadingDetails();

//log out button
        logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(this::logOut);

    }

    private  void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private  void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    public void updateToken(String token){
        documentReference.update(Constants.KEY_FCM_TOKEN, token).addOnSuccessListener(unused -> showToast("Token updated successfully"))
                .addOnFailureListener(e -> showToast("Unable to update token"));
        preferenceManager.putString(Constants.KEY_FCM_TOKEN, token);
        System.out.println(preferenceManager.getString(Constants.KEY_FCM_TOKEN));
    }
     public void setUserHeadingDetails(){
         nameTextView.setText(preferenceManager.getString(Constants.KEY_NAME));
         System.out.println("Second check: " + preferenceManager.getString(Constants.KEY_NAME));
         Picasso.get().load(Uri.parse(preferenceManager.getString(Constants.KEY_PROFILE_IMAGE))).into(pImage);
         System.out.println("Third check: " + preferenceManager.getString(Constants.KEY_PROFILE_IMAGE));
     }


    public void logOut(View v){
        showToast("Logging out....");
        HashMap<String, Object> update = new HashMap<>();
        update.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(update).addOnSuccessListener(unused -> {
            preferenceManager.clear();
            if(mAuth.getCurrentUser() != null){mAuth.signOut();}
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }).addOnFailureListener(e -> showToast("unable to sign out"));
    }


}
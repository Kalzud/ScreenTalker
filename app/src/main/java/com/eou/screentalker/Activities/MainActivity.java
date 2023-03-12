package com.eou.screentalker.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.eou.screentalker.Fragments.Stream;
import com.eou.screentalker.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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
    ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialising firebase database stuff
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        documentReference = fStore.collection("users").document(userID);

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
        TextView nameTextView = headerView.findViewById(R.id.displayName);
        // Get a reference to the ImageView in the header view
        RoundedImageView pImage = headerView.findViewById(R.id.imageProfile);

        //get database info
        listenerRegistration = documentReference.addSnapshotListener((value, error) -> {
//            assert value != null;
            Picasso.get().load(Uri.parse(value.getString("pImage_url"))).into(pImage);
            nameTextView.setText(value.getString("username"));
        });
        documentReference.get().addOnCompleteListener(task -> listenerRegistration.remove());

//log out button
        logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(this::logOut);

    }




    public void logOut(View v){
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
package com.eou.screentalker.Activities;

import android.os.Bundle;

import com.eou.screentalker.Utilities.PreferenceManager;
import com.eou.screentalker.databinding.ActivityCreatecommunityBinding;
import com.eou.screentalker.Utilities.PreferenceManager;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.eou.screentalker.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateCommunityActivity extends AppCompatActivity {

    private ActivityCreatecommunityBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatecommunityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        fStore = FirebaseFirestore.getInstance();
    }

}
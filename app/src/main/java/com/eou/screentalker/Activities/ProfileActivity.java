package com.eou.screentalker.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.eou.screentalker.Models.Group_chat_messageModel;
import com.eou.screentalker.Utilities.Constants;
import com.eou.screentalker.Utilities.PreferenceManager;
import com.eou.screentalker.databinding.ActivityProfileBinding;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private FirebaseFirestore fStore;
    private  String id;
    private DocumentReference documentReference;
    private PreferenceManager preferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        fStore = FirebaseFirestore.getInstance();
        id = getIntent().getStringExtra("id");
        documentReference = fStore.collection("users").document(id);
        binding.imageBack.setOnClickListener(v-> {
            super.onBackPressed();
            finish();
        });
        binding.add.setOnClickListener(v -> add());

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                //setting this to show the current data in database on xml
                Picasso.get().load(Uri.parse(value.getString("pImage_url"))).into(binding.imageProfile);
                binding.username.setText(value.getString("username"));
                binding.bio.setText(value.getString("bio"));

            }
        });
    }

    private  void add(){
        HashMap<String, Object> request = new HashMap<>();
        request.put("sender_id", preferenceManager.getString(Constants.KEY_USER_ID));
        request.put("sender_pImage", preferenceManager.getString(Constants.KEY_PROFILE_IMAGE));
        request.put("sender_username", preferenceManager.getString(Constants.KEY_NAME));
        request.put("receiver_id", id);
        fStore.collection("requests").add(request);
        binding.add.setVisibility(View.GONE);
        binding.requested.setVisibility(View.VISIBLE);
    }



}
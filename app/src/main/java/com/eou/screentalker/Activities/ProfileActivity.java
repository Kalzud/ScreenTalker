package com.eou.screentalker.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import com.eou.screentalker.R;
import com.eou.screentalker.databinding.ActivityProfileBinding;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private FirebaseFirestore fStore;
    private  String id;
    private DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fStore = FirebaseFirestore.getInstance();
        id = getIntent().getStringExtra("id");
        documentReference = fStore.collection("users").document(id);

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                //setting this to show the current data in database on xml
                assert value != null;
                Picasso.get().load(Uri.parse(value.getString("pImage_url"))).into(binding.imageProfile);
                binding.username.setText(value.getString("username"));
                binding.bio.setText(value.getString("bio"));

            }
        });
    }

}
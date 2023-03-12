package com.eou.screentalker.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.eou.screentalker.Activities.EditprofileActivity;
import com.eou.screentalker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class PersonalProfile extends Fragment {
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore fStore;
    TextView username, bio;
    String userID;
    DocumentReference documentReference;
    Button edit;
    RoundedImageView imageProfile;
    ListenerRegistration listenerRegistration;



    public PersonalProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        documentReference = fStore.collection("users").document(userID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.personal_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //how I got info from database first time
        username = view.findViewById(R.id.username);
        bio = view.findViewById(R.id.bio);
        edit = view.findViewById(R.id.editProfile);
        imageProfile = view.findViewById(R.id.imageProfile);
        listenerRegistration = documentReference.addSnapshotListener((value, error) -> {
//                assert  value != null;
            username.setText(value.getString("username"));
            Picasso.get().load(Uri.parse(value.getString("pImage_url"))).into(imageProfile);
            bio.setText(value.getString("bio"));
        });
        documentReference.get().addOnCompleteListener(task -> listenerRegistration.remove());

        edit.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditprofileActivity.class);
            startActivity(intent);
        });

    }


}
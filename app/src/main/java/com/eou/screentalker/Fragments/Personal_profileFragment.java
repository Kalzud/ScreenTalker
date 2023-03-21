package com.eou.screentalker.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.eou.screentalker.Activities.EditprofileActivity;
import com.eou.screentalker.R;
import com.eou.screentalker.Utilities.Constants;
import com.eou.screentalker.Utilities.PreferenceManager;
import com.eou.screentalker.databinding.FragmentPersonalProfileBinding;
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
public class Personal_profileFragment extends Fragment {
    private FragmentPersonalProfileBinding binding;
    private PreferenceManager preferenceManager;



    public Personal_profileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(requireActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentPersonalProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadUserDetails();
        binding.editProfile.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditprofileActivity.class);
            startActivity(intent);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserDetails();
    }

    private void loadUserDetails(){
        binding.username.setText(preferenceManager.getString(Constants.KEY_NAME));
        binding.bio.setText(preferenceManager.getString(Constants.KEY_BIO));
        Picasso.get().load(Uri.parse(preferenceManager.getString(Constants.KEY_PROFILE_IMAGE))).into(binding.imageProfile);
        System.out.println("Fourth check: " + preferenceManager.getString(Constants.KEY_PROFILE_IMAGE));
    }
}
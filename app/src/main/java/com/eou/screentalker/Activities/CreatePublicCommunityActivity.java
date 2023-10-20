package com.eou.screentalker.Activities;

import android.os.Bundle;
import com.eou.screentalker.Utilities.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import com.eou.screentalker.databinding.ActivityCreatePublicCommunityBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreatePublicCommunityActivity extends AppCompatActivity {

    private ActivityCreatePublicCommunityBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePublicCommunityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        fStore = FirebaseFirestore.getInstance();
    }

}